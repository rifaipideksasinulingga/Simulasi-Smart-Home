import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginGUI extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginGUI() {

        setTitle("Smart Home Login");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Background utama
        JPanel background = new JPanel(new GridBagLayout());
        background.setBackground(new Color(15, 23, 42));

        // Card login
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(600, 380));
        card.setBackground(new Color(30, 41, 59));
        card.setLayout(new GridBagLayout());
        card.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20));

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
        passwordField = new JPasswordField();

        styleTextField(usernameField);
        styleTextField(passwordField);

        // Button login
        loginButton = new JButton("LOGIN");

        loginButton.setBackground(new Color(59, 130, 246));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setPreferredSize(new Dimension(150, 50));

        // Hover effect
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(
                        new Color(96, 165, 250));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(
                        new Color(59, 130, 246));
            }
        });

        // Layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        card.add(title, gbc);
        gbc.gridy++;
        card.add(subtitle, gbc);
        gbc.gridy++;
        card.add(userLabel, gbc);
        gbc.gridy++;
        card.add(usernameField, gbc);
        gbc.gridy++;
        card.add(pwLabel, gbc);
        gbc.gridy++;
        card.add(passwordField, gbc);
        gbc.gridy++;
        gbc.insets = new Insets(25, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(loginButton, gbc);
        background.add(card);
        add(background);

        // Enter untuk login
        getRootPane().setDefaultButton(loginButton);

        // Action login
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.equals("Shakilla Marsya") && password.equals("09021282530086")) {
                    JOptionPane.showMessageDialog(null, "Akses Berhasil!");
                    new SmartHomeGUI();
                }
                else if(username.equals("Chanda Putri Zahira") && password.equals("09021282530110")) {

                } else if (username.equals("Chanda Putri Zahira") && password.equals("09021282530110")) {
                    JOptionPane.showMessageDialog(null, "Akses Berhasil!");
                    new SmartHomeGUI();
                }
                else if(username.equals("Rifai Pideksa Sinulingga") && password.equals("0902128253010")) {

                } else if (username.equals("Rifai Pideksa Sinulingga") && password.equals("0902128253010")) {
                    JOptionPane.showMessageDialog(null, "Akses Berhasil!");
                    SmartHomeGUI homeGUI = new SmartHomeGUI();
                    dispose();

                } else if (username.equals("M Farhan Hidayat") && password.equals("09021282530097")) {
                    JOptionPane.showMessageDialog(null, "Akses Berhasil!");
                    new SmartHomeGUI();
                }
                else if(username.equals("Achmad Daniel Albar") && password.equals("09021282530078")) {

                } else if (username.equals("Achmad Daniel Albar") && password.equals("09021282530078")) {
                    JOptionPane.showMessageDialog(null, "Akses Berhasil!");
                    new SmartHomeGUI();
                } else {
                    JOptionPane.showMessageDialog(null, "Anda tidak memiliki akses");
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
