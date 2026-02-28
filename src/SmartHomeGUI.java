import java.awt.*;
import javax.swing.*;

public class SmartHomeGUI extends JFrame {
    private JComboBox<String> roomSelector;
    private JTextArea statusArea;
    private CCTVPanel cctvPanel;
    private Room room1, room2;

    public SmartHomeGUI() {

        room1 = new Room(
                "Ruang Tamu",
                "/images/room1_on.jpg", 
                "/images/room1_off.jpg"
        );

        room2 = new Room(
                "Kamar Tidur",
                "/images/Gemini_Generated_Image_43qujw43qujw43qu.png", 
                "/images/Gemini_Generated_Image_43qujw43qujw43qu (1).png" 
        );

        setTitle("Smart Home CCTV Monitor");
        setSize(1000, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Selector Ruangan
        roomSelector = new JComboBox<>(new String[]{"Ruang Tamu", "Kamar Tidur"});
        roomSelector.addActionListener(e -> updateDisplay());
        add(roomSelector, BorderLayout.NORTH);

        // Status Area
        statusArea = new JTextArea();
        statusArea.setEditable(false);
        statusArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(statusArea), BorderLayout.CENTER);

        // Panel CCTV
        cctvPanel = new CCTVPanel(room1.getCurrentImage());
        add(cctvPanel, BorderLayout.EAST);

        // Kontrol Panel
        JPanel controlPanel = new JPanel();
        JButton lampBtn = new JButton("Toggle Lamp");
        JButton acBtn = new JButton("Toggle AC");
        JButton doorBtn = new JButton("Lock/Unlock Door");

        controlPanel.add(lampBtn);
        controlPanel.add(acBtn);
        controlPanel.add(doorBtn);
        add(controlPanel, BorderLayout.SOUTH);

        // Event Listeners
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

        updateDisplay(); // Initial load
        setVisible(true);
    }

    private Room getSelectedRoom() {
        return roomSelector.getSelectedIndex() == 0 ? room1 : room2;
    }

    private void updateDisplay() {
        Room r = getSelectedRoom();
        String info = " === STATUS RUANGAN ===\n" +
                      " Nama    : " + r.getName() + "\n" +
                      " Lampu   : " + (r.getLamp().isOn() ? "MENYALA" : "MATI") + "\n" +
                      " AC      : " + (r.getAC().isOn() ? "HIDUP" : "MATI") + "\n" +
                      " Pintu   : " + r.getDoor().getState();

        statusArea.setText(info);
        cctvPanel.setImage(r.getCurrentImage()); // Render ulang gambar
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SmartHomeGUI::new);
    }
}