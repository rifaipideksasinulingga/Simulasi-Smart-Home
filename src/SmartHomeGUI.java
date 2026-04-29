import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class SmartHomeGUI extends JFrame {
    private JComboBox<String> roomSelector;
    private JTextArea statusArea;
    private CCTVPanel cctvPanel;

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

        statusArea = new JTextArea();
        statusArea.setEditable(false);
        add(new JScrollPane(statusArea), BorderLayout.CENTER);

        cctvPanel = new CCTVPanel(room1.getCurrentImage());
        add(cctvPanel, BorderLayout.EAST);

        JPanel panel = new JPanel();

        JButton lampBtn = new JButton("ON/OFF Lamp");
        JButton acBtn = new JButton("ON/OFF AC");
        JButton doorBtn = new JButton("Lock/Unlock Door");
        JButton tempBtn = new JButton("Set Temperature");
        
        // Tombol Show Status dihapus dari sini

        panel.add(lampBtn);
        panel.add(acBtn);
        panel.add(doorBtn);
        panel.add(tempBtn);

        add(panel, BorderLayout.SOUTH);

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
        

        String info =
                "Ruangan: " + r.getName() + "\n" +
                "Lampu: " + (r.getLamp().isOn() ? "ON" : "OFF") + "\n" +
                "AC: " + (r.getAC().isOn() ? "ON" : "OFF") + "\n" +
                "Suhu: " + r.getAC().getTemperature() + "°C\n" +
                "Pintu: " + r.getDoor().getState();

        statusArea.setText(info);

        cctvPanel.setImage(r.getCurrentImage());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SmartHomeGUI::new);
    }
}