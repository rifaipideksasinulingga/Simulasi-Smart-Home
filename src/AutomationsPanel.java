import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AutomationsPanel extends JPanel {

    // Palet Warna
    private Color colorBg = new Color(25, 29, 38);
    private Color colorCard = new Color(42, 48, 60);
    private Color textPrimary = Color.WHITE;
    private Color textSecondary = new Color(160, 170, 185);
    private Color colorAccent = new Color(0, 120, 215);

    private List<Room> rooms;
    private JComboBox<String> roomCombo;
    private JComboBox<String> deviceCombo;
    private JComboBox<String> actionCombo;
    private JSpinner timeSpinner;
    private JPanel scheduleListPanel;

    public AutomationsPanel(List<Room> rooms) {
        this.rooms = rooms;
        setLayout(new BorderLayout(20, 20));
        setBackground(colorBg);
        setBorder(new EmptyBorder(10, 0, 10, 0));

        // --- HEADER ---
        JLabel title = new JLabel("Automasi & Penjadwalan Perangkat");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(textPrimary);
        title.setBorder(new EmptyBorder(0, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // --- BAGIAN TENGAH: Form Pembuatan Jadwal ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(colorBg);
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(colorCard, 2),
                " Buat Automasi Baru ", 0, 0,
                new Font("SansSerif", Font.BOLD, 14), textPrimary));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 1. Pilih Ruangan
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblRoom = new JLabel("Pilih Ruangan:");
        lblRoom.setForeground(textSecondary);
        formPanel.add(lblRoom, gbc);

        gbc.gridx = 1;
        roomCombo = new JComboBox<>();
        for (Room r : rooms) roomCombo.addItem(r.getName());
        formPanel.add(roomCombo, gbc);

        // 2. Pilih Perangkat
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblDevice = new JLabel("Pilih Perangkat:");
        lblDevice.setForeground(textSecondary);
        formPanel.add(lblDevice, gbc);

        gbc.gridx = 1;
        deviceCombo = new JComboBox<>();
        updateDeviceCombo(); // Isi awal
        formPanel.add(deviceCombo, gbc);

        // Update perangkat saat ruangan diganti
        roomCombo.addActionListener(e -> updateDeviceCombo());

        // 3. Aksi (ON/OFF)
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblAction = new JLabel("Aksi:");
        lblAction.setForeground(textSecondary);
        formPanel.add(lblAction, gbc);

        gbc.gridx = 1;
        actionCombo = new JComboBox<>(new String[]{"Nyalakan (ON)", "Matikan (OFF)"});
        formPanel.add(actionCombo, gbc);

        // 4. Waktu Tunda (Detik)
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblTime = new JLabel("Jalankan dalam (detik):");
        lblTime.setForeground(textSecondary);
        formPanel.add(lblTime, gbc);

        gbc.gridx = 1;
        timeSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 3600, 1));
        formPanel.add(timeSpinner, gbc);

        // 5. Tombol Simpan
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        JButton btnAdd = new JButton("Tambahkan Jadwal");
        btnAdd.setBackground(colorAccent);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.addActionListener(e -> addAutomation());
        formPanel.add(btnAdd, gbc);

        add(formPanel, BorderLayout.WEST);

        // --- BAGIAN KANAN: Daftar Jadwal Aktif ---
        scheduleListPanel = new JPanel();
        scheduleListPanel.setLayout(new BoxLayout(scheduleListPanel, BoxLayout.Y_AXIS));
        scheduleListPanel.setBackground(colorBg);

        JScrollPane scrollPane = new JScrollPane(scheduleListPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(colorCard, 2),
                " Jadwal Berjalan ", 0, 0,
                new Font("SansSerif", Font.BOLD, 14), textPrimary));
        scrollPane.setBackground(colorBg);
        scrollPane.getViewport().setBackground(colorBg);
        
        add(scrollPane, BorderLayout.CENTER);
    }

    private void updateDeviceCombo() {
        deviceCombo.removeAllItems();
        int roomIndex = roomCombo.getSelectedIndex();
        if (roomIndex >= 0) {
            Room r = rooms.get(roomIndex);
            for (Device d : r.getDevices()) {
                deviceCombo.addItem(d.name);
            }
        }
    }

    private void addAutomation() {
        int roomIndex = roomCombo.getSelectedIndex();
        int deviceIndex = deviceCombo.getSelectedIndex();
        
        if (roomIndex < 0 || deviceIndex < 0) return;

        Room room = rooms.get(roomIndex);
        Device device = room.getDevices().get(deviceIndex);
        boolean turnOn = actionCombo.getSelectedIndex() == 0;
        int delaySeconds = (int) timeSpinner.getValue();

        // Hitung waktu eksekusi (hanya untuk tampilan teks)
        LocalTime executeTime = LocalTime.now().plusSeconds(delaySeconds);
        String timeStr = executeTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        // Buat Card UI untuk jadwal ini
        RoundedPanel taskCard = new RoundedPanel(15, colorCard);
        taskCard.setLayout(new BorderLayout());
        taskCard.setBorder(new EmptyBorder(15, 15, 15, 15));
        taskCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel infoLabel = new JLabel("<html><b>" + device.name + "</b> (" + room.getName() + ")<br>Status: Menunggu...</html>");
        infoLabel.setForeground(textPrimary);
        
        JLabel timeLabel = new JLabel((turnOn ? "🟢 ON" : "🔴 OFF") + " pada " + timeStr);
        timeLabel.setForeground(turnOn ? new Color(46, 204, 113) : new Color(231, 76, 60));
        timeLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        taskCard.add(infoLabel, BorderLayout.WEST);
        taskCard.add(timeLabel, BorderLayout.EAST);

        scheduleListPanel.add(taskCard);
        scheduleListPanel.add(Box.createVerticalStrut(10));
        scheduleListPanel.revalidate();
        scheduleListPanel.repaint();

        // Buat Timer sungguhan untuk mengeksekusi
        Timer timer = new Timer(delaySeconds * 1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (turnOn) device.turnOn();
                else device.turnOff();

                infoLabel.setText("<html><b>" + device.name + "</b> (" + room.getName() + ")<br>Status: Selesai dijalankan.</html>");
                infoLabel.setForeground(textSecondary);
                timeLabel.setForeground(textSecondary);
                
                // Hentikan timer agar tidak looping
                ((Timer)e.getSource()).stop();
            }
        });
        timer.setRepeats(false); // Hanya jalan sekali
        timer.start();
    }

    // --- CLASS KUSTOM UI ---
    class RoundedPanel extends JPanel {
        private int radius;
        private Color bgColor;
        public RoundedPanel(int radius, Color bgColor) {
            this.radius = radius; this.bgColor = bgColor; setOpaque(false);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));
            g2.dispose(); super.paintComponent(g);
        }
    }
}