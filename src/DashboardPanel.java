import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DashboardPanel extends JPanel {

    // Palet Warna
    private Color colorBg = new Color(25, 29, 38);
    private Color colorCard = new Color(42, 48, 60);
    private Color textPrimary = Color.WHITE;
    private Color textSecondary = new Color(160, 170, 185);
    private Color colorOn = new Color(46, 204, 113); // Hijau
    private Color colorOff = new Color(99, 110, 114); // Abu-abu

    public DashboardPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(colorBg);
        setBorder(new EmptyBorder(10, 0, 10, 0));

        // --- BAGIAN ATAS: Ringkasan Perangkat & Map 2D ---
        JPanel topSection = new JPanel(new GridLayout(1, 2, 20, 0));
        topSection.setBackground(colorBg);

        // Kiri: Ringkasan Perangkat
        JPanel deviceSummaryPanel = new JPanel(new BorderLayout(0, 10));
        deviceSummaryPanel.setBackground(colorBg);
        
        JLabel lblSummary = new JLabel("Ringkasan Perangkat Aktif");
        lblSummary.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblSummary.setForeground(textPrimary);
        deviceSummaryPanel.add(lblSummary, BorderLayout.NORTH);

        JPanel cardsGrid = new JPanel(new GridLayout(2, 2, 15, 15));
        cardsGrid.setBackground(colorBg);
        
        // Tambahkan Card Perangkat (Simulasi)
        cardsGrid.add(createDeviceCard("💡 Lampu R. Tamu", "75% Kecerahan", true, true));
        cardsGrid.add(createDeviceCard("❄️ AC Kamar Tidur", "22°C Sejuk", true, false));
        cardsGrid.add(createDeviceCard("🔒 Kunci Pintu", "Terkunci", false, false)); // False = Merah/Abu
        cardsGrid.add(createDeviceCard("📹 CCTV Teras", "Gerakan Terdeteksi", true, false));
        
        deviceSummaryPanel.add(cardsGrid, BorderLayout.CENTER);
        topSection.add(deviceSummaryPanel);

        // Kanan: Map 2D
        JPanel mapPanel = new JPanel(new BorderLayout(0, 10));
        mapPanel.setBackground(colorBg);
        
        JLabel lblMap = new JLabel("Visualisasi Rumah (2D Layout)");
        lblMap.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblMap.setForeground(textPrimary);
        mapPanel.add(lblMap, BorderLayout.NORTH);

        RoundedPanel mapContainer = new RoundedPanel(20, colorCard);
        mapContainer.setLayout(new BorderLayout());
        JLabel lblMapPlaceholder = new JLabel("🗺️ [ Area Gambar Denah 2D Nanti Disini ]", SwingConstants.CENTER);
        lblMapPlaceholder.setForeground(textSecondary);
        mapContainer.add(lblMapPlaceholder, BorderLayout.CENTER);
        
        mapPanel.add(mapContainer, BorderLayout.CENTER);
        topSection.add(mapPanel);

        add(topSection, BorderLayout.CENTER);

        // --- BAGIAN BAWAH: Automasi, Konsumsi, Modus ---
        JPanel bottomSection = new JPanel(new GridLayout(1, 3, 20, 0));
        bottomSection.setBackground(colorBg);
        bottomSection.setPreferredSize(new Dimension(0, 200));

        bottomSection.add(createBottomWidget("Automasi & Jadwal", "⏰ Lampu Teras (18:00)"));
        bottomSection.add(createBottomWidget("Konsumsi Energi", "⚡ Total 7,2 kWh"));
        bottomSection.add(createBottomWidget("Modus Cepat", "🌙 Waktu Tidur"));

        add(bottomSection, BorderLayout.SOUTH);
    }

    // --- FUNGSI PEMBUAT CARD PERANGKAT ---
    private JPanel createDeviceCard(String title, String subtitle, boolean isOn, boolean hasSlider) {
        RoundedPanel card = new RoundedPanel(20, colorCard);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Header Card (Judul & Toggle)
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTitle.setForeground(textPrimary);
        header.add(lblTitle, BorderLayout.WEST);

        // Toggle Switch Kustom
        ToggleSwitch toggle = new ToggleSwitch(isOn);
        header.add(toggle, BorderLayout.EAST);
        
        card.add(header);
        card.add(Box.createVerticalStrut(10));

        // Subtitle / Status
        JLabel lblSub = new JLabel(subtitle);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSub.setForeground(textSecondary);
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblSub);

        // Jika butuh slider (seperti kecerahan / suhu)
        if (hasSlider) {
            card.add(Box.createVerticalGlue());
            JSlider slider = new JSlider(0, 100, 75);
            slider.setOpaque(false);
            slider.setPreferredSize(new Dimension(150, 20));
            card.add(slider);
        }

        return card;
    }

    // --- FUNGSI PEMBUAT WIDGET BAWAH ---
    private JPanel createBottomWidget(String title, String info) {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitle.setForeground(textPrimary);
        panel.add(lblTitle, BorderLayout.NORTH);

        RoundedPanel content = new RoundedPanel(20, colorCard);
        content.setLayout(new GridBagLayout());
        JLabel lblInfo = new JLabel(info);
        lblInfo.setForeground(textSecondary);
        content.add(lblInfo);

        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    // =========================================================
    // KELAS KUSTOM: Panel Sudut Melengkung (Rounded)
    // =========================================================
    class RoundedPanel extends JPanel {
        private int radius;
        private Color bgColor;

        public RoundedPanel(int radius, Color bgColor) {
            this.radius = radius;
            this.bgColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // =========================================================
    // CLASS ToggleSwitch (ANTI-MELAR)
    // =========================================================
    class ToggleSwitch extends JComponent {
        private boolean activated;
        private Color colorOn = new Color(46, 204, 113); // Hijau
        private Color colorOff = new Color(160, 170, 185); // Abu-abu
        
        public ToggleSwitch(boolean activated) {
            this.activated = activated; 
            setPreferredSize(new Dimension(50, 26)); 
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseReleased(java.awt.event.MouseEvent e) {
                    setActivated(!isActivated());
                }
            });
        }
        
        public boolean isActivated() { return activated; }
        public void setActivated(boolean activated) { this.activated = activated; repaint(); }
        
        @Override 
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // KUNCI UKURAN: Gunakan ukuran pasti, bukan ukuran dari layout
            int drawWidth = 50;
            int drawHeight = 26;
            
            // Pusatkan sakelar jika container-nya dipaksa membesar oleh layout
            int xPos = (getWidth() - drawWidth) / 2;
            int yPos = (getHeight() - drawHeight) / 2;
            
            // 1. Background Sakelar
            if (activated) g2.setColor(colorOn); 
            else g2.setColor(colorOff);
            g2.fillRoundRect(xPos, yPos, drawWidth, drawHeight, drawHeight, drawHeight);
            
            // 2. Bulatan Putih (Thumb)
            int thumbSize = drawHeight - 6; 
            int thumbY = yPos + 3;
            int thumbX = activated ? xPos + drawWidth - thumbSize - 3 : xPos + 3;
            
            // 3. Bayangan Tipis
            g2.setColor(new Color(0, 0, 0, 40));
            g2.fillOval(thumbX, thumbY + 1, thumbSize, thumbSize);
            
            // 4. Bulatan Putih
            g2.setColor(Color.WHITE);
            g2.fillOval(thumbX, thumbY, thumbSize, thumbSize);
            
            g2.dispose();
        }
    }
}