import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class LoginGUI extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Smart Home Login");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        //bg
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(24, 28, 33));
        mainPanel.setLayout(new GridBagLayout());

        //log
        JPanel cardPanel = new JPanel();
        cardPanel.setPreferredSize(new Dimension(340, 350));
        cardPanel.setBackground(new Color(40, 44, 52));
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(new EmptyBorder(25, 50, 25, 50));

        JLabel title = new JLabel("SMART HOME");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Login untuk akses SmartHome");
        subtitle.setFont(new Font("Montserrat", Font.PLAIN, 13));
        subtitle.setForeground(Color.LIGHT_GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Username
        JLabel userLabel = new JLabel("Username");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Montserrat", Font.PLAIN, 14));
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Judul
        JLabel title = new JLabel("SMART HOME");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitle = new JLabel("Saran teks");
        subtitle.setForeground(Color.LIGHT_GRAY);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);

        // Label
        JLabel userLabel = new JLabel("👤 Username");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("SansSerif", Font.BOLD, 13));

        JLabel pwLabel = new JLabel("🔒 Password");
        pwLabel.setForeground(Color.WHITE);
        pwLabel.setFont(new Font("SansSerif", Font.BOLD, 13));

        // Textfield
        usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Password
        JLabel passLabel = new JLabel("Password");
        passLabel.setForeground(Color.WHITE);
        passLabel.setFont(new Font("Montserrat", Font.PLAIN, 14));
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Tombol login
        loginButton = new JButton("LOGIN");
        loginButton.setFocusPainted(false);
        loginButton.setBackground(new Color(0, 120, 215));
        loginButton.setForeground(new Color(24, 28, 33));
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Hover effect
        loginButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(30, 144, 255));
            }

            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(new Color(0, 120, 215));
            }
        });

        // Tambah komponen ke card
        cardPanel.add(title);
        cardPanel.add(Box.createVerticalStrut(5));
        cardPanel.add(subtitle);
        cardPanel.add(Box.createVerticalStrut(20));

        cardPanel.add(userLabel);
        cardPanel.add(Box.createVerticalStrut(5));
        cardPanel.add(usernameField);
        cardPanel.add(Box.createVerticalStrut(10));

        cardPanel.add(passLabel);
        cardPanel.add(Box.createVerticalStrut(5));
        cardPanel.add(passwordField);
        cardPanel.add(Box.createVerticalStrut(20));

        cardPanel.add(loginButton);

        mainPanel.add(cardPanel);
        add(mainPanel);

        //login
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                SmartHomeGUI homeGUI = null;

                if ((username.equals("Shakilla Marsya") && password.equals("09021282530086"))
                        || (username.equals("Chanda Putri Zahira") && password.equals("09021282530110"))
                        || (username.equals("Rifai Pideksa Sinulingga") && password.equals("0902128253010"))
                        || (username.equals("M Farhan Hidayat") && password.equals("09021282530097"))
                        || (username.equals("Achmad Daniel Albar") && password.equals("09021282530078"))) {
                    JOptionPane.showMessageDialog(null, "Akses Berhasil!");
                    new SmartHomeGUI();
                    dispose();

                } else if(username.equals("Chanda Putri Zahira") && password.equals("09021282530110")) {

                    JOptionPane.showMessageDialog(null, "Akses Berhasil!");
                    new SmartHomeGUI();
                    dispose();

                } else if(username.equals("Rifai Pideksa Sinulingga") && password.equals("0902128253010")) {

                    JOptionPane.showMessageDialog(null, "Akses Berhasil!");
                    new SmartHomeGUI();
                    dispose();
                } else if(username.equals("M Farhan Hidayat") && password.equals("09021282530097")) {

                    JOptionPane.showMessageDialog(null, "Akses Berhasil!");
                    new SmartHomeGUI();
                    dispose();

                } else if(username.equals("Achmad Daniel Albar") && password.equals("09021282530078")) {

                    JOptionPane.showMessageDialog(null, "Akses Berhasil!");
                    new SmartHomeGUI();
                    dispose();

                } else {

                    JOptionPane.showMessageDialog(
                            null,
                            "Anda tidak memiliki akses",
                            "Login Gagal",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        setVisible(true);
    }

    private void styleTextField(JTextField field) {
        field.setPreferredSize(new Dimension(420, 65));
        field.setFont(new Font("SansSerif", Font.PLAIN, 20));
        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(100, 116, 139), 1),
                        BorderFactory.createEmptyBorder(
                                5, 10, 5, 10)));

        field.setBackground(new Color(51, 65, 85));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
    }

    public static void main(String[] args) {
        new LoginGUI();
    }
}
