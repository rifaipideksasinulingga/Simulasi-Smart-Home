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
                "/images/LR_LampOn_DoorCl.png",
                "/images/LR_LampOff_DoorCl.png"
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
        JButton tempBtn = new JButton("Set Temperature");
        JButton statusBtn = new JButton("Show Status");

        panel.add(lampBtn);
        panel.add(acBtn);
        panel.add(doorBtn);
        panel.add(tempBtn);
        panel.add(statusBtn);

        add(panel, BorderLayout.SOUTH);

        // ActionListener tetap sama, updateDisplay akan memicu pembaruan gambar
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

        tempBtn.addActionListener(e -> {
            Room r = getSelectedRoom();
            if (!r.getAC().isOn()) {
                JOptionPane.showMessageDialog(this, "Turn on the AC before setting temperature.", "Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                String input = JOptionPane.showInputDialog(this, "Enter temperature (16-30 °C):", r.getAC().getTemperature());
                if (input != null) {
                    try {
                        int temp = Integer.parseInt(input.trim());
                        r.getAC().setTemperature(temp);
                        updateDisplay();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid temperature value.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
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
                "Suhu: " + r.getAC().getTemperature() + "°C\n" +
                "Pintu: " + r.getDoor().getState();

        statusArea.setText(info);

        // Ini akan memanggil loadImage() di CCTVPanel berdasarkan status lampu
        cctvPanel.setImage(r.getCurrentImage());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SmartHomeGUI::new);
    }
}