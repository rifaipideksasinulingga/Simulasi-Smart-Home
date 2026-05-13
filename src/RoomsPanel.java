import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class RoomsPanel extends JPanel {

    // Palet Warna
    private Color colorBg = new Color(25, 29, 38);
    private Color colorCard = new Color(42, 48, 60);
    private Color textPrimary = Color.WHITE;
    private Color textSecondary = new Color(160, 170, 185);
    private Color colorOn = new Color(46, 204, 113); 
    private Color colorOff = new Color(99, 110, 114);

    private List<Room> rooms;
    private JPanel devicesGrid;
    private JComboBox<String> roomSelector;

    public RoomsPanel(List<Room> rooms) {
        this.rooms = rooms;
        setLayout(new BorderLayout(20, 20));
        setBackground(colorBg);
        setBorder(new EmptyBorder(10, 0, 10, 0));

        // --- HEADER: Judul & Pemilih Ruangan ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(colorBg);
        headerPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel title = new JLabel("Filter Berdasarkan Ruangan");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(textPrimary);
        headerPanel.add(title, BorderLayout.WEST);

        // Dropdown untuk memilih ruangan
        roomSelector = new JComboBox<>();
        for (Room r : rooms) {
            roomSelector.addItem("🚪 " + r.getName());
        }
        roomSelector.setFont(new Font("SansSerif", Font.BOLD, 14));
        roomSelector.setPreferredSize(new Dimension(250, 40));
        roomSelector.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Event listener saat ruangan diganti
        roomSelector.addActionListener(e -> updateRoomDisplay());
        
        headerPanel.add(roomSelector, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // --- TENGAH: Grid Perangkat ---
        devicesGrid = new JPanel(new GridLayout(0, 3, 20, 20));
        devicesGrid.setBackground(colorBg);

        JScrollPane scrollPane = new JScrollPane(devicesGrid);
        scrollPane.setBorder(null);
        scrollPane.setBackground(colorBg);
        scrollPane.getViewport().setBackground(colorBg);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0)); 
        
        add(scrollPane, BorderLayout.CENTER);

        // Panggil fungsi ini agar saat pertama kali dibuka, data langsung muncul
        updateRoomDisplay();
    }

    // Fungsi untuk me-refresh perangkat sesuai ruangan yang dipilih
    private void updateRoomDisplay() {
        devicesGrid.removeAll(); // Bersihkan layar dulu
        
        int selectedIndex = roomSelector.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < rooms.size()) {
            Room selectedRoom = rooms.get(selectedIndex);
            
            // Masukkan perangkat yang HANYA ada di ruangan ini
            for (Device device : selectedRoom.getDevices()) {
                devicesGrid.add(createDeviceCard(device, selectedRoom.getName()));
            }
        }
        
        devicesGrid.revalidate(); // Perbarui layout GUI
        devicesGrid.repaint();    // Gambar ulang GUI
    }

    // --- FUNGSI PEMBUAT KARTU PERANGKAT (Sama seperti DevicesPanel) ---
    private JPanel createDeviceCard(Device device, String roomName) {
        RoundedPanel card = new RoundedPanel(20, colorCard);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        
        String icon = "🔌";
        if (device instanceof Lamp) icon = "💡";
        else if (device instanceof AC) icon = "❄️";
        else if (device instanceof Door) icon = "🔒";

        JLabel lblTitle = new JLabel(icon + " " + getDeviceLabel(device));
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTitle.setForeground(textPrimary);
        header.add(lblTitle, BorderLayout.WEST);

        ToggleSwitch toggle = new ToggleSwitch(device.isOn());
        header.add(toggle, BorderLayout.EAST);
        
        card.add(header);
        card.add(Box.createVerticalStrut(10));

        JLabel lblSub = new JLabel("Status: " + (device.isOn() ? "Aktif" : "Mati"));
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSub.setForeground(textSecondary);
        card.add(lblSub);

        toggle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (device.isOn()) device.turnOff();
                else device.turnOn();
                
                toggle.setActivated(device.isOn()); 
                lblSub.setText("Status: " + (device.isOn() ? "Aktif" : "Mati")); // Update teks dinamis
            }
        });

        if (device instanceof AC) {
            card.add(Box.createVerticalStrut(15));
            AC ac = (AC) device;
            JSlider slider = new JSlider(16, 30, ac.getTemperature());
            slider.setOpaque(false);
            slider.setForeground(textPrimary);
            
            slider.addChangeListener(e -> {
                if (device.isOn()) {
                    ac.setTemperature(slider.getValue());
                } else {
                    slider.setValue(ac.getTemperature());
                }
            });
            card.add(slider);
        }

        return card;
    }

    private String getDeviceLabel(Device device) {
        if (device instanceof Lamp) return "Lampu";
        if (device instanceof AC) return "AC";
        if (device instanceof Door) return "Pintu";
        return device.getClass().getSimpleName();
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

    class ToggleSwitch extends JComponent {
        private boolean activated;
        public ToggleSwitch(boolean activated) {
            this.activated = activated; setPreferredSize(new Dimension(46, 24)); setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        public boolean isActivated() { return activated; }
        public void setActivated(boolean activated) { this.activated = activated; repaint(); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int width = getWidth(); int height = getHeight();
            if (activated) g2.setColor(colorOn); else g2.setColor(colorOff);
            g2.fillRoundRect(0, 0, width, height, height, height);
            g2.setColor(Color.WHITE);
            int thumbSize = height - 4; int x = activated ? width - thumbSize - 2 : 2;
            g2.fillOval(x, 2, thumbSize, thumbSize); g2.dispose();
        }
    }
}