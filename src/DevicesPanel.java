import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DevicesPanel extends JPanel {
    private Color colorBg = new Color(25, 29, 38);
    private Color colorCard = new Color(42, 48, 60);
    private Color textPrimary = Color.WHITE;
    private List<Room> rooms;
    private JPanel gridPanel;

    public DevicesPanel(List<Room> rooms) {
        this.rooms = rooms;
        setLayout(new BorderLayout(20, 20));
        setBackground(colorBg);
        setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel title = new JLabel("Manajemen Semua Perangkat");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(textPrimary);
        add(title, BorderLayout.NORTH);

        gridPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        gridPanel.setBackground(colorBg);

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(colorBg);
        add(scrollPane, BorderLayout.CENTER);

        filterDevices(""); 
    }

    public void filterDevices(String query) {
        gridPanel.removeAll();
        for (Room room : rooms) {
            for (Device device : room.getDevices()) {
                // PANGGILAN getName() TERJADI DI SINI
                if (query.isEmpty() || 
                    device.getName().toLowerCase().contains(query.toLowerCase()) || 
                    room.getName().toLowerCase().contains(query.toLowerCase())) {
                    gridPanel.add(createDeviceCard(device, room.getName()));
                }
            }
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JPanel createDeviceCard(Device device, String roomName) {
        RoundedPanel card = new RoundedPanel(20, colorCard);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        
        JLabel lblTitle = new JLabel(device.getName()); // Mengambil nama perangkat
        lblTitle.setForeground(textPrimary);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        
        ToggleSwitch toggle = new ToggleSwitch(device.isOn());
        header.add(lblTitle, BorderLayout.WEST);
        header.add(toggle, BorderLayout.EAST);
        
        card.add(header);
        JLabel lblRoom = new JLabel("Lokasi: " + roomName);
        lblRoom.setForeground(new Color(160, 170, 185));
        card.add(lblRoom);

        toggle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (device.isOn()) device.turnOff();
                else device.turnOn();
                toggle.setActivated(device.isOn());
            }
        });

        return card;
    }

    // Class UI Kustom (RoundedPanel & ToggleSwitch tetap disertakan di bawah)
    class RoundedPanel extends JPanel { 
        private int r; Color c; 
        RoundedPanel(int r, Color c) { this.r=r; this.c=c; setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c); g2.fill(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),r,r));
            g2.dispose(); super.paintComponent(g);
        }
    }

    class ToggleSwitch extends JComponent {
        private boolean activated;
        ToggleSwitch(boolean act) { this.activated = act; setPreferredSize(new Dimension(50, 26)); }
        public boolean isActivated() { return activated; }
        public void setActivated(boolean act) { this.activated = act; repaint(); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int x = (getWidth()-50)/2; int y = (getHeight()-26)/2;
            g2.setColor(activated ? new Color(46, 204, 113) : new Color(160, 170, 185));
            g2.fillRoundRect(x, y, 50, 26, 26, 26);
            g2.setColor(Color.WHITE);
            int tx = activated ? x + 27 : x + 3;
            g2.fillOval(tx, y + 3, 20, 20);
            g2.dispose();
        }
    }
}