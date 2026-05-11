import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginGUI extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginGUI() {
        setTitle("Login untuk akses");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel userLabel = new JLabel("Username: ");
        JLabel pwLabel = new JLabel("Password: ");

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton ("Login");

        panel.add(userLabel);
        panel.add(usernameField);

        panel.add(pwLabel);
        panel.add(passwordField);

        panel.add(new JLabel());
        panel.add(loginButton);

        add(panel);


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if(username.equals("Shakilla Marsya") && password.equals("09021282530086")) {

                    JOptionPane.showMessageDialog(null, "Akses Berhasil!");
                    new SmartHomeGUI();
                }
                else if(username.equals("Chanda Putri Zahira") && password.equals("09021282530110")) {

                    JOptionPane.showMessageDialog(null, "Akses Berhasil!");
                    new SmartHomeGUI();
                }
                else if(username.equals("Rifai Pideksa Sinulingga") && password.equals("0902128253010")) {

                    JOptionPane.showMessageDialog(null, "Akses Berhasil!");
                    new SmartHomeGUI();
                    dispose();
                }
                else if(username.equals("M Farhan Hidayat") && password.equals("09021282530097")) {

                    JOptionPane.showMessageDialog(null, "Akses Berhasil!");
                    new SmartHomeGUI();
                }
                else if(username.equals("Achmad Daniel Albar") && password.equals("09021282530078")) {

                    JOptionPane.showMessageDialog(null, "Akses Berhasil!");
                    new SmartHomeGUI();
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Anda tidak memiliki akses");
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginGUI();

    }

}

