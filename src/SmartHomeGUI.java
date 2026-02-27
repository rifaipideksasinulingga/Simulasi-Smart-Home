import java.awt.*;
import javax.swing.*;

public class SmartHomeGUI extends JFrame {
    private Image image;
    private JComboBox<String> roomSelector;
    private JTextArea statusArea;
    private CCTVPanel cctvPanel;

    private Room room1;
    private Room room2;

    public SmartHomeGUI() {

        room1 = new Room(
                "Ruang Tamu",
                "/images/room1_on.jpg",
                "/images/room1_off.jpg"
        );

        room2 = new Room(
                "Kamar Tidur",
                "/src/images/Gemini_Generated_Image_43qujw43qujw43qu_(1).png",
                "/images/room2_off.jpg"
        );

        setTitle("Smart Home CCTV Monitor");
        setSize(1000, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        roomSelector = new JComboBox<>(new String[]{
                "Ruang Tamu", "Kamar Tidur"
        });
        add(roomSelector, BorderLayout.NORTH);

        statusArea = new JTextArea();
        statusArea.setEditable(false);
        add(new JScrollPane(statusArea), BorderLayout.CENTER);

        cctvPanel = new CCTVPanel(room1.getCurrentImage());
        add(cctvPanel, BorderLayout.EAST);

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

        statusBtn.addActionListener(e -> updateDisplay());

        setVisible(true);
    }

    private Room getSelectedRoom() {
        return roomSelector.getSelectedIndex() == 0 ? room1 : room2;
    }

    private void updateDisplay() {
        Room r = getSelectedRoom();

        String info =
                "Ruangan: " + r.getName() + "\n" +
                "Lampu: " + (r.getLamp().isOn() ? "ON" : "OFF") + "\n" +
                "AC: " + (r.getAC().isOn() ? "ON" : "OFF") + "\n" +
                "Pintu: " + r.getDoor().getState();

        statusArea.setText(info);

        cctvPanel.setImage(r.getCurrentImage());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SmartHomeGUI::new);
    }
}