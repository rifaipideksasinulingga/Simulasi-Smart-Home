import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class SettingsPanel extends JPanel {
    private Color colorBg = new Color(25, 29, 38);
    private Color colorCard = new Color(42, 48, 60);
    private Color textPrimary = Color.WHITE;
    private Color colorAccent = new Color(0, 120, 215);

    private JTextField nameField;
    private SmartHomeGUI mainFrame;

    public SettingsPanel(SmartHomeGUI mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(colorBg);
        setBorder(new EmptyBorder(30, 50, 30, 50));

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(colorBg);

        JLabel title = new JLabel("Pengaturan Akun & Sistem");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(textPrimary);
        container.add(title);
        container.add(Box.createVerticalStrut(30));

        // Input Nama
        JLabel lblName = new JLabel("Nama Pengguna:");
        lblName.setForeground(Color.LIGHT_GRAY);
        container.add(lblName);
        container.add(Box.createVerticalStrut(10));

        nameField = new JTextField("Andi Prasetyo");
        nameField.setMaximumSize(new Dimension(400, 40));
        nameField.setBackground(colorCard);
        nameField.setForeground(textPrimary);
        nameField.setCaretColor(Color.WHITE);
        nameField.setBorder(BorderFactory.createLineBorder(colorAccent, 1));
        container.add(nameField);
        container.add(Box.createVerticalStrut(20));

        // Tombol Simpan
        JButton btnSave = new JButton("Simpan Perubahan");
        btnSave.setBackground(colorAccent);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setPreferredSize(new Dimension(200, 40));
        btnSave.addActionListener(e -> {
            String newName = nameField.getText();
            JOptionPane.showMessageDialog(this, "Profil berhasil diperbarui!");
        });
        container.add(btnSave);

        add(container, BorderLayout.NORTH);
    }
}