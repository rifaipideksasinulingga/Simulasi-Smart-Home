import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class SmartHomeGUI extends JFrame {

    // Palet Warna Modern
    private Color colorBackground = new Color(25, 29, 38);
    private Color colorSidebar = new Color(33, 38, 48);
    private Color colorPanel = new Color(42, 48, 60);
    private Color colorAccent = new Color(0, 120, 215);
    private Color textPrimary = Color.WHITE;
    private Color textSecondary = new Color(160, 170, 185);

    // Data & Komponen Utama
    private List<Room> rooms;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JLabel lblPageTitle, lblPageSubtitle, lblProfileName;
    private JTextField searchField;
    private JButton btnAdd;
    private DevicesPanel devicesPanel;

    public SmartHomeGUI() {
        // 1. Inisialisasi Data Ruangan (Gunakan versi Room dengan List<Device>)
        initData();

        // 2. Setup Frame Utama
        setTitle("HOME SYNC Pro - Smart Home Simulator");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(colorBackground);

        // Inisialisasi label profil lebih awal agar tidak null saat dipanggil sidebar
        lblProfileName = new JLabel("👦 Rifai"); 

        // 3. Tambahkan Sidebar & Top Bar
        add(createSidebar(), BorderLayout.WEST);

        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.setBackground(colorBackground);
        mainArea.add(createTopBar(), BorderLayout.NORTH);

        // 4. Setup Area Konten (CardLayout)
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(colorBackground);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Daftarkan Halaman-Halaman
        contentPanel.add(new DashboardPanel(), "Dashboard");
        contentPanel.add(new DevicesPanel(rooms), "Devices");
        contentPanel.add(new RoomsPanel(rooms), "Rooms");
        contentPanel.add(new AutomationsPanel(rooms), "Automations");
        contentPanel.add(new SecurityPanel(rooms), "Security");
        contentPanel.add(new SettingsPanel(this), "Settings");

        mainArea.add(contentPanel, BorderLayout.CENTER);
        add(mainArea, BorderLayout.CENTER);

        // 5. Fix Layout saat Minimize/Maximize
        this.addWindowStateListener(e -> {
            if ((e.getOldState() & Frame.ICONIFIED) != 0 && (e.getNewState() & Frame.ICONIFIED) == 0) {
                revalidate();
                repaint();
            }
        });

        setVisible(true);
    }

    private void initData() {
        rooms = new ArrayList<>();
        Room r1 = new Room("Ruang Tamu", "/images/LR_LampOn_DoorCl.png", "/images/LR_LampOff_DoorCl.png");
        r1.addDevice(new Lamp("Lampu Utama"));
        r1.addDevice(new AC("AC Ruangan"));
        r1.addDevice(new Door("Pintu Utama"));
        
        Room r2 = new Room("Kamar Tidur", "/images/Gemini_Generated_Image_43qujw43qujw43qu.png", "/images/Gemini_Generated_Image_43qujw43qujw43qu (1).png");
        r2.addDevice(new Lamp("Lampu Tidur"));
        r2.addDevice(new GenericDevice("PC Desktop"));

        rooms.add(r1);
        rooms.add(r2);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(colorSidebar);
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBorder(new EmptyBorder(20, 10, 20, 10));

        JLabel logo = new JLabel("🏠 HOME SYNC");
        logo.setFont(new Font("SansSerif", Font.BOLD, 22));
        logo.setForeground(textPrimary);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(40));

        String[] menus = {"Dashboard", "Devices", "Rooms", "Automations", "Security", "Settings"};
        String[] icons = {"🏠", "🖥️", "🪟", "⏰", "🛡️", "⚙️"};

        for (int i = 0; i < menus.length; i++) {
            sidebar.add(createMenuButton(icons[i] + "  " + menus[i], menus[i]));
            sidebar.add(Box.createVerticalStrut(10));
        }

        sidebar.add(Box.createVerticalGlue());
        
        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        profilePanel.setOpaque(false);
        lblProfileName.setForeground(textPrimary);
        lblProfileName.setFont(new Font("SansSerif", Font.BOLD, 14));
        profilePanel.add(lblProfileName);
        sidebar.add(profilePanel);

        return sidebar;
    }

    private JPanel createMenuButton(String text, String cardName) {
        JPanel btn = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
            }
        };
        btn.setOpaque(false);
        btn.setBackground(colorSidebar);
        btn.setMaximumSize(new Dimension(230, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel label = new JLabel(text);
        label.setForeground(textSecondary);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.add(label);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(colorPanel); label.setForeground(textPrimary); btn.repaint(); }
            public void mouseExited(MouseEvent e) { btn.setBackground(colorSidebar); label.setForeground(textSecondary); btn.repaint(); }
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(contentPanel, cardName);
                lblPageTitle.setText(cardName.equals("Dashboard") ? "Dashboard Utama" : cardName);
            }
        });
        return btn;
    }

    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(20, 20, 10, 20));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        lblPageTitle = new JLabel("Dashboard Utama");
        lblPageTitle.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblPageTitle.setForeground(textPrimary);
        lblPageSubtitle = new JLabel("Halo, Rifai! Semuanya terlihat normal.");
        lblPageSubtitle.setForeground(textSecondary);
        titlePanel.add(lblPageTitle);
        titlePanel.add(lblPageSubtitle);
        topBar.add(titlePanel, BorderLayout.WEST);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionPanel.setOpaque(false);

        searchField = new JTextField(" 🔍 Cari Perangkat...");
        searchField.setPreferredSize(new Dimension(200, 35));
        searchField.setBackground(colorSidebar);
        searchField.setForeground(textSecondary);
        searchField.setCaretColor(Color.WHITE);
        searchField.setBorder(BorderFactory.createLineBorder(colorPanel));

        btnAdd = new JButton("+ Tambah Perangkat");
        btnAdd.setBackground(colorAccent);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.addActionListener(e -> showAddDeviceDialog());

        actionPanel.add(searchField);
        actionPanel.add(btnAdd);
        topBar.add(actionPanel, BorderLayout.EAST);

        return topBar;
    }

    public void setUserName(String name) {
        lblProfileName.setText("👦 " + name);
    }

    private void showAddDeviceDialog() {
        // Logika tambah perangkat seperti sebelumnya
        JOptionPane.showMessageDialog(this, "Fitur tambah perangkat terbuka.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SmartHomeGUI::new);
    }
}