import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class SmartHomeGUI extends JFrame {
    private JComboBox<String> roomSelector;
    private JTextArea statusArea;
    private CCTVPanel cctvPanel;
    private JButton lampBtn, acBtn, doorBtn;
    private JSlider tempSlider;
    private JPanel sliderPanel;

    private Room room1;
    private Room room2;
    private Room room3;

    private boolean isUpdatingUI = false;

    public SmartHomeGUI() {
        // 1. Mengatur Look and Feel agar GUI terlihat lebih modern mengikuti sistem OS
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Inisialisasi Ruangan
        room1 = new Room(
                "Ruang Tamu",
                "/images/LR_LampOn_DoorCl.png",
                "/images/LR_LampOff_DoorCl.png"
        );

        room2 = new Room(
                "Kamar Tidur",
                "/images/Gemini_Generated_Image_43qujw43qujw43qu.png",
                "/images/Gemini_Generated_Image_43qujw43qujw43qu (1).png"
        );

        room3 = new Room(
                "Dapur",
                "/images/K_LampOn.png",
                "/images/K_LampOff.png"
        );

        setTitle("Smart Home CCTV Monitor");
        setSize(1050, 550); // Ukuran sedikit dilebarkan
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Menambahkan jarak antar komponen
        getContentPane().setBackground(new Color(240, 244, 248)); // Warna latar aplikasi

        // --- Panel Atas (Pemilihan Ruangan) ---
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        headerPanel.setBackground(new Color(44, 62, 80));
        
        JLabel titleLabel = new JLabel("Pilih Ruangan: ");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        roomSelector = new JComboBox<>(new String[]{
                "Ruang Tamu", "Kamar Tidur", "Dapur"
        });
        roomSelector.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        headerPanel.add(titleLabel);
        headerPanel.add(roomSelector);
        add(headerPanel, BorderLayout.NORTH);

        // --- Panel Tengah (Monitor CCTV) ---
        cctvPanel = new CCTVPanel(room1.getCurrentImage());
        cctvPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                " Live Camera Feed ",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 14),
                Color.DARK_GRAY
        ));
        add(cctvPanel, BorderLayout.CENTER);

        // --- Panel Kanan (Status Ruangan) ---
        JPanel statusContainer = new JPanel(new BorderLayout());
        statusContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        statusContainer.setOpaque(false);
        
        statusArea = new JTextArea(12, 22);
        statusArea.setEditable(false);
        // Desain ala monitor digital
        statusArea.setFont(new Font("Monospaced", Font.BOLD, 15));
        statusArea.setBackground(new Color(30, 30, 30));
        statusArea.setForeground(new Color(0, 255, 0));
        statusArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JScrollPane scrollPane = new JScrollPane(statusArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                null, " Informasi Status ", TitledBorder.LEFT, TitledBorder.TOP, 
                new Font("SansSerif", Font.BOLD, 14)
        ));
        statusContainer.add(scrollPane, BorderLayout.CENTER);
        add(statusContainer, BorderLayout.EAST);

        // --- Panel Bawah (Kontrol Perangkat) ---
        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 20, 10),
                BorderFactory.createTitledBorder(null, " Panel Kontrol Perangkat ", TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 14))
        ));
        
        lampBtn = createStyledButton("💡 ON/OFF Lampu", new Color(46, 204, 113));
        doorBtn = createStyledButton("🚪 Lock/Unlock Pintu", new Color(231, 76, 60));
        acBtn = createStyledButton("❄️ ON/OFF AC", new Color(52, 152, 219));

        // Konfigurasi Slider Suhu (Sebagai pengganti tombol Set Temp)
        tempSlider = new JSlider(JSlider.HORIZONTAL, 16, 30, 24);
        tempSlider.setMajorTickSpacing(2);
        tempSlider.setMinorTickSpacing(1);
        tempSlider.setPaintTicks(true);
        tempSlider.setPaintLabels(true);
        tempSlider.setFont(new Font("SansSerif", Font.PLAIN, 12));
        
        sliderPanel = new JPanel(new BorderLayout());
        JLabel sliderLabel = new JLabel("Pengatur Suhu AC (°C):", SwingConstants.CENTER);
        sliderLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        sliderPanel.add(sliderLabel, BorderLayout.NORTH);
        sliderPanel.add(tempSlider, BorderLayout.CENTER);

        // Mengatur Layout Tombol-tombol di Bawah
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0;
        controlPanel.add(lampBtn, gbc);
        
        gbc.gridx = 1;
        controlPanel.add(doorBtn, gbc);

        gbc.gridx = 2;
        controlPanel.add(acBtn, gbc);

        gbc.gridx = 3; gbc.weightx = 2.0; // Slider diberikan ruang lebih besar
        controlPanel.add(sliderPanel, gbc);

        add(controlPanel, BorderLayout.SOUTH);

        // --- Event Listeners ---
        roomSelector.addActionListener(e -> updateDisplay());

        lampBtn.addActionListener(e -> {
            Room r = getSelectedRoom();
            if (r.getLamp().isOn()) r.getLamp().turnOff();
            else r.getLamp().turnOn();
            updateDisplay();
        });

        acBtn.addActionListener(e -> {
            Room r = getSelectedRoom();
            if (r.getAC().isOn()) r.getAC().turnOff();
            else r.getAC().turnOn();
            updateDisplay();
        });

        doorBtn.addActionListener(e -> {
            Room r = getSelectedRoom();
            if (r.getDoor().isOn()) r.getDoor().unlock();
            else r.getDoor().lock();
            updateDisplay();
        });

        // Event listener untuk Slider layaknya volume
        tempSlider.addChangeListener(e -> {
            if (isUpdatingUI) return; // Mencegah looping event saat mengganti ruangan
            
            Room r = getSelectedRoom();
            if (r.getAC().isOn()) {
                r.getAC().setTemperature(tempSlider.getValue());
                updateStatusText(r, r.getName().equals("Dapur")); // Update text tanpa kedip
            }
        });

        // Panggil update pertama kali saat aplikasi dibuka
        updateDisplay();
        setLocationRelativeTo(null); // Tampilkan di tengah layar
        setVisible(true);
    }

    // Fungsi bantuan untuk mempercantik UI Tombol
    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 40));
        return btn;
    }

    private Room getSelectedRoom() {
        int index = roomSelector.getSelectedIndex();
        switch (index) {
            case 0: return room1;
            case 1: return room2;
            case 2: return room3;
            default: return room1;
        }
    }

    // Memisahkan update text agar bisa dipanggil oleh slider secara real-time
    private void updateStatusText(Room r, boolean isDapur) {
        String info =
                "Ruangan : " + r.getName() + "\n\n" +
                "Lampu   : " + (r.getLamp().isOn() ? "🟢 ON" : "🔴 OFF") + "\n";

        if (!isDapur) {
            info += "AC      : " + (r.getAC().isOn() ? "🟢 ON" : "🔴 OFF") + "\n" +
                    "Suhu AC : " + r.getAC().getTemperature() + "°C\n" +
                    "Pintu   : " + (r.getDoor().getState().equals("LOCKED") ? "🔒 LOCKED" : "🔓 UNLOCKED");
        }

        statusArea.setText(info);
    }

    private void updateDisplay() {
        Room r = getSelectedRoom();
        boolean isDapur = r.getName().equals("Dapur");

        updateStatusText(r, isDapur);

        // Mengatur ketersediaan tombol sesuai ruangan (Dapur tidak ada AC dan Pintu di simulasi ini)
        acBtn.setVisible(!isDapur);
        doorBtn.setVisible(!isDapur);
        sliderPanel.setVisible(!isDapur);

        // Set state slider secara diam-diam tanpa men-trigger ChangeListener
        isUpdatingUI = true;
        if (!isDapur) {
            tempSlider.setValue(r.getAC().getTemperature());
            // Jika AC mati, slider tidak bisa digeser (Disabled)
            tempSlider.setEnabled(r.getAC().isOn());
        }
        isUpdatingUI = false;

        // Update gambar panel CCTV
        cctvPanel.setImage(r.getCurrentImage());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SmartHomeGUI::new);
    }
}