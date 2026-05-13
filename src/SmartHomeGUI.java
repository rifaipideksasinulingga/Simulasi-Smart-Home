import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class SmartHomeGUI extends JFrame {
    private List<Room> rooms;
    // --- Palet Warna Dark Theme ---
    private Color colorBackground = new Color(25, 29, 38);      // Background utama
    private Color colorSidebar = new Color(33, 38, 48);         // Warna sidebar
    private Color colorPanel = new Color(42, 48, 60);           // Warna panel/kartu
    private Color colorAccent = new Color(0, 120, 215);         // Biru aksen (tombol aktif)
    private Color textPrimary = Color.WHITE;
    private Color textSecondary = new Color(160, 170, 185);
    private JLabel lblProfileName; // Tambahkan ini
    private JTextField searchField;
    private JButton btnAdd;

    // Komponen Navigasi
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JLabel lblPageTitle;
    private JLabel lblPageSubtitle;

    public SmartHomeGUI() {
        rooms = new ArrayList<>();
        Room r1 = new Room("Ruang Tamu", "/images/LR_LampOn_DoorCl.png", "/images/LR_LampOff_DoorCl.png");
        r1.addDevice(new Lamp("Lampu Utama"));
        r1.addDevice(new AC("AC Ruangan"));
        r1.addDevice(new Door("Pintu Utama"));
        
        Room r2 = new Room("Kamar Tidur", "/images/Kamar2_On.png", "/images/Kamar2_Off.png");
        r2.addDevice(new Lamp("Lampu Tidur"));
        r2.addDevice(new GenericDevice("PC Desktop"));

        Room r3 = new Room("Dapur", "/images/K_LampOn.png", "/images/K_LampOff.png");
        r3.addDevice(new Lamp("Lampu Dapur"));
        r3.addDevice(new GenericDevice("Kulkas Pintar"));

        rooms.add(r1);
        rooms.add(r2);
        rooms.add(r3);

        lblProfileName = new JLabel("👦 Andi Prasetyo");
        
        /*btnAdd.addActionListener(e -> {
            String query = searchField.getText().toLowerCase().replace(" 🔍 cari perangkat...", "").trim();
            JOptionPane.showMessageDialog(this, "Mencari: " + query + "\n(Tips: Gunakan menu 'Devices' untuk melihat hasil filter)");
        });*/

        // Setup Window Dasar
        setTitle("HOME SYNC Pro - Smart Home Simulator");
        setSize(1280, 720); // Resolusi HD
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(colorBackground);

        // 1. Tambahkan Sidebar di sisi kiri (WEST)
        add(createSidebar(), BorderLayout.WEST);

        // 2. Setup Area Utama di sisi tengah (CENTER)
        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.setBackground(colorBackground);
        
        mainArea.add(createTopBar(), BorderLayout.NORTH);

        // 3. Setup Content Panel (Tempat halaman berganti-ganti)
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(colorBackground);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Margin keliling

        // Tambahkan halaman-halaman (sementara berupa halaman kosong/placeholder)
        contentPanel.add(new DashboardPanel(), "Dashboard");
        contentPanel.add(new DevicesPanel(rooms), "Devices");
        contentPanel.add(new RoomsPanel(rooms), "Rooms");
        contentPanel.add(new AutomationsPanel(rooms), "Automations");
        contentPanel.add(new SecurityPanel(rooms), "Security");
        contentPanel.add(new SettingsPanel(this), "Settings");  

        mainArea.add(contentPanel, BorderLayout.CENTER);
        add(mainArea, BorderLayout.CENTER);

        setVisible(true);
    }

    // --- FUNGSI PEMBUAT SIDEBAR ---
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(colorSidebar);
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBorder(new EmptyBorder(20, 10, 20, 10));

        // Logo / Title App
        JLabel logo = new JLabel("🏠 HOME SYNC");
        logo.setFont(new Font("SansSerif", Font.BOLD, 22));
        logo.setForeground(textPrimary);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(40));

        // Menu Buttons
        String[] menus = {"Dashboard", "Devices", "Rooms", "Automations", "Security", "Settings"};
        String[] icons = {"🏠", "🖥️", "🪟", "⚙️", "🛡️", "🔧"};

        for (int i = 0; i < menus.length; i++) {
            JPanel menuBtn = createMenuButton(icons[i] + "  " + menus[i], menus[i]);
            sidebar.add(menuBtn);
            sidebar.add(Box.createVerticalStrut(10));
        }

        // Profil User di bawah (Mendorong komponen ke bawah menggunakan lem vertikal)
        sidebar.add(Box.createVerticalGlue());
        
        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        profilePanel.setBackground(colorSidebar);
        profilePanel.setMaximumSize(new Dimension(230, 50));
        JLabel lblProfile = new JLabel("👦 Andi Prasetyo");
        lblProfile.setForeground(textPrimary);
        lblProfile.setFont(new Font("SansSerif", Font.BOLD, 14));
        profilePanel.add(lblProfile);
        
        sidebar.add(profilePanel);

        return sidebar;
    }

    // --- FUNGSI PEMBUAT TOMBOL MENU SIDEBAR ---
    private JPanel createMenuButton(String text, String cardName) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panel.setBackground(colorSidebar);
        panel.setMaximumSize(new Dimension(230, 45));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel label = new JLabel(text);
        label.setForeground(textSecondary);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        panel.add(label);

        // Efek Hover & Klik
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(colorPanel);
                label.setForeground(textPrimary);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(colorSidebar);
                label.setForeground(textSecondary);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Ganti halaman di CardLayout
                cardLayout.show(contentPanel, cardName);
                // Ubah judul Top Bar sesuai menu yang diklik
                lblPageTitle.setText(cardName.equals("Dashboard") ? "Dashboard Utama" : cardName);
                lblPageSubtitle.setText("Menampilkan halaman " + cardName + ".");
            }
        });

        // Modifikasi khusus tombol lengkung (Opsional untuk mempercantik)
        return new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(panel.getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
            {
                setOpaque(false);
                add(panel);
            }
        };
    }

    // --- FUNGSI PEMBUAT TOP BAR ---
    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(colorBackground);
        topBar.setBorder(new EmptyBorder(20, 20, 10, 20));

        // Kiri: Judul Halaman
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setBackground(colorBackground);
        
        lblPageTitle = new JLabel("Dashboard Utama");
        lblPageTitle.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblPageTitle.setForeground(textPrimary);
        
        lblPageSubtitle = new JLabel("Halo, Andi! Semuanya terlihat normal.");
        lblPageSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblPageSubtitle.setForeground(textSecondary);
        
        titlePanel.add(lblPageTitle);
        titlePanel.add(lblPageSubtitle);
        topBar.add(titlePanel, BorderLayout.WEST);

        // Kanan: Search & Tambah Perangkat
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionPanel.setBackground(colorBackground);

        searchField = new JTextField(" 🔍 Cari Perangkat...");
        searchField.setPreferredSize(new Dimension(200, 35));
        searchField.setBackground(colorSidebar);
        searchField.setForeground(textSecondary);
        searchField.setBorder(BorderFactory.createLineBorder(colorPanel, 1));
        searchField.setCaretColor(Color.WHITE);

        searchField.addActionListener(e -> {
    String query = searchField.getText().toLowerCase().replace(" 🔍 cari perangkat...", "").trim();
    JOptionPane.showMessageDialog(this, "Mencari: " + query + "\n(Tips: Gunakan menu 'Devices' untuk melihat hasil filter)");
});

        btnAdd = new JButton("+ Tambah Perangkat");
        btnAdd.setBackground(colorAccent);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnAdd.setBorder(new EmptyBorder(8, 15, 8, 15));
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnAdd.addActionListener(e -> showAddDeviceDialog());

        actionPanel.add(searchField);
        actionPanel.add(btnAdd);
        
        topBar.add(actionPanel, BorderLayout.EAST);

        return topBar;
    }

    private void showAddDeviceDialog() {
        String deviceName = JOptionPane.showInputDialog(this, "Masukkan nama perangkat baru:", "Tambah Perangkat", JOptionPane.PLAIN_MESSAGE);
        if (deviceName != null && !deviceName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Perangkat '" + deviceName.trim() + "' berhasil ditambahkan!");
        }
    }

    // --- FUNGSI PEMBUAT HALAMAN PLACEHOLDER ---
    private JPanel createPlaceholderPage(String text) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(colorBackground);
        
        JLabel label = new JLabel("Berada di Halaman: " + text);
        label.setFont(new Font("SansSerif", Font.BOLD, 24));
        label.setForeground(textSecondary);
        
        panel.add(label);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SmartHomeGUI::new);
    }
}