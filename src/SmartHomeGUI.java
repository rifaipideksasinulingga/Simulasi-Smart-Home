import java.awt.*;
import javax.swing.*;

public class SmartHomeGUI extends JFrame {
    private Image image;
    private JComboBox<String> roomSelector;
    private JTextArea statusArea;
    private CCTVPanel cctvPanel;
    private JButton lampBtn, acBtn, doorBtn, tempBtn;

    private Room room1;
    private Room room2;
    private Room room3;

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

        room3 = new Room(
                "Dapur",
                "/images/K_LampOn.png",
                "/images/K_LampOff.png"
        );

        setTitle("Smart Home CCTV Monitor");
        setSize(1000, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        roomSelector = new JComboBox<>(new String[]{
                "Ruang Tamu", "Kamar Tidur", "Dapur"
        });
        add(roomSelector, BorderLayout.NORTH);

        statusArea = new JTextArea();
        statusArea.setEditable(false);
        add(new JScrollPane(statusArea), BorderLayout.CENTER);

        cctvPanel = new CCTVPanel(room1.getCurrentImage());
        add(cctvPanel, BorderLayout.EAST);

        JPanel panel = new JPanel();

        lampBtn = new JButton("ON/OFF Lamp");
        acBtn = new JButton("ON/OFF AC");
        doorBtn = new JButton("Lock/Unlock Door");
        tempBtn = new JButton("Set Temperature");
        
        // Tombol Show Status dihapus dari sini

        panel.add(lampBtn);
        panel.add(acBtn);
        panel.add(doorBtn);
        panel.add(tempBtn);

        add(panel, BorderLayout.SOUTH);

        // Tambahkan listener untuk roomSelector agar langsung update saat ruangan diganti
        roomSelector.addActionListener(e -> updateDisplay());

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

        // Panggil updateDisplay() sekali di awal agar teks UI tidak kosong saat pertama kali dibuka
        updateDisplay();

        setVisible(true);
    }

    private Room getSelectedRoom() {
        int index = roomSelector.getSelectedIndex();
        switch (index) {
            case 0:
                return room1;
            case 1:
                return room2;
            case 2:
                return room3;
            default:
                return room1;
        }
    }

    private void updateDisplay() {
        Room r = getSelectedRoom();
        boolean isDapur = r.getName().equals("Dapur");

        String info =
                "Ruangan: " + r.getName() + "\n" +
                "Lampu: " + (r.getLamp().isOn() ? "ON" : "OFF") + "\n";

        if (!isDapur) {
        info += "AC: " + (r.getAC().isOn() ? "ON" : "OFF") + "\n" +
                "Suhu: " + r.getAC().getTemperature() + "°C\n" +
                "Pintu: " + r.getDoor().getState();
        }

        statusArea.setText(info);

        acBtn.setVisible(!isDapur);
        doorBtn.setVisible(!isDapur);
        tempBtn.setVisible(!isDapur);

        cctvPanel.setImage(r.getCurrentImage());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SmartHomeGUI::new);
    }
}