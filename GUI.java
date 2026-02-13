import java.awt.*;
import javax.swing.*;

public class GUI extends JFrame {

    private Control home;
    private JTextArea statusArea;

    public GUI() {
        home = new Control();

        // Object
        Lamp lamp1 = new Lamp("Lampu Ruang Tamu");
        Lamp lamp2 = new Lamp("Lampu Kamar");
        AC ac1 = new AC("AC Utama", 22);

        home.addDevice(lamp1);
        home.addDevice(lamp2);
        home.addDevice(ac1);

        // GUI Setup
        setTitle("Smart Home Simulator");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Text Area
        statusArea = new JTextArea();
        statusArea.setEditable(false);
        add(new JScrollPane(statusArea), BorderLayout.CENTER);

        // Buttons
        JPanel panel = new JPanel();
        JButton onButton = new JButton("Turn All ON");
        JButton offButton = new JButton("Turn All OFF");
        JButton statusButton = new JButton("Show Status");

        panel.add(onButton);
        panel.add(offButton);
        panel.add(statusButton);

        add(panel, BorderLayout.SOUTH);

        // Action
        onButton.addActionListener(e -> {
            home.turnAllOn();
            statusArea.setText("Semua perangkat dinyalakan.\n");
        });

        offButton.addActionListener(e -> {
            home.turnAllOff();
            statusArea.setText("Semua perangkat dimatikan.\n");
        });

        statusButton.addActionListener(e -> {
            statusArea.setText(home.getStatus());
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GUI().setVisible(true);
        });
    }
}
