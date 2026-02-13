import java.awt.*;
import javax.swing.*;

public class SmartHomeGUI extends JFrame {

    private JComboBox<String> roomSelector;
    private JTextArea statusArea;
    private JLabel cctvLabel;
    

    private Room room1;
    private Room room2;

    public SmartHomeGUI() {
        cctvLabel = new JLabel();
        cctvLabel.setHorizontalAlignment(JLabel.CENTER);
        add(cctvLabel, BorderLayout.EAST);

        room1 = new Room("Ruang Tamu", "images/room1.jpg");
        room2 = new Room("Kamar Tidur", "\"C:\\Users\\Rifai\\Downloads\\Gemini_Generated_Image_43qujw43qujw43qu (1).png\"");

        setTitle("Smart Home CCTV Simulator");
        setSize(500, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        roomSelector = new JComboBox<>(new String[]{"Ruang Tamu", "Kamar Tidur"});
        add(roomSelector, BorderLayout.NORTH);

        statusArea = new JTextArea();
        statusArea.setEditable(false);
        add(new JScrollPane(statusArea), BorderLayout.CENTER);

        JPanel panel = new JPanel();

        JButton lampBtn = new JButton("Toggle Lamp");
        JButton acBtn = new JButton("Toggle AC");
        JButton doorBtn = new JButton("Lock/Unlock Door");
        JButton statusBtn = new JButton("Show Status");

        panel.add(lampBtn);
        panel.add(acBtn);
        panel.add(doorBtn);
        panel.add(statusBtn);

        add(panel, BorderLayout.SOUTH);

        lampBtn.addActionListener(e -> {
            Room r = getSelectedRoom();
            if (r.getLamp().isOn()) r.getLamp().turnOff();
            else r.getLamp().turnOn();
        });

        acBtn.addActionListener(e -> {
            Room r = getSelectedRoom();
            if (r.getAC().isOn()) r.getAC().turnOff();
            else r.getAC().turnOn();
        });

        doorBtn.addActionListener(e -> {
            Room r = getSelectedRoom();
            if (r.getDoor().isOn()) r.getDoor().unlock();
            else r.getDoor().lock();
        });

        statusBtn.addActionListener(e -> showStatus());

        setVisible(true);
    }

    private Room getSelectedRoom() {
        return roomSelector.getSelectedIndex() == 0 ? room1 : room2;
    }

    private void showStatus() {
    Room r = getSelectedRoom();

    String info =
            "Ruangan: " + r.getName() + "\n" +
            "Lampu: " + (r.getLamp().isOn() ? "ON" : "OFF") + "\n" +
            "AC: " + (r.getAC().isOn() ? "ON" : "OFF") + "\n" +
            "Pintu: " + r.getDoor().getState();

    statusArea.setText(info);

    // tampilkan gambar CCTV
    cctvLabel.setIcon(r.getCCTV().getImage());
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(SmartHomeGUI::new);
    }
}
