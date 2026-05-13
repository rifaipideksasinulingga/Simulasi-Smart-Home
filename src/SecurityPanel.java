import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class SecurityPanel extends JPanel {

    // Palet Warna untuk Konsistensi
    private Color colorBg = new Color(25, 29, 38);
    private Color colorCard = new Color(42, 48, 60);
    private Color textPrimary = Color.WHITE;
    private Color colorAlert = new Color(231, 76, 60);

    private List<Room> rooms;
    private JComboBox<String> cctvSelector;
    private ModernCCTV cctvMonitor;
    private JTextArea securityLog;

    public SecurityPanel(List<Room> rooms) {
        this.rooms = rooms;
        setLayout(new BorderLayout(20, 20));
        setBackground(colorBg);
        setBorder(new EmptyBorder(10, 0, 10, 0));

        // --- HEADER ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(colorBg);
        headerPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel title = new JLabel("Pusat Keamanan & CCTV");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(textPrimary);
        headerPanel.add(title, BorderLayout.WEST);

        cctvSelector = new JComboBox<>();
        for (Room r : rooms) cctvSelector.addItem("📹 Kamera: " + r.getName());
        cctvSelector.setFont(new Font("SansSerif", Font.BOLD, 14));
        cctvSelector.setPreferredSize(new Dimension(250, 40));
        cctvSelector.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cctvSelector.addActionListener(e -> updateCCTVFeed());
        headerPanel.add(cctvSelector, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // --- CENTER: Monitor CCTV (DIBUNGKUS KARTU ROUNDED) ---
        RoundedPanel centerCard = new RoundedPanel(20, colorCard);
        centerCard.setLayout(new BorderLayout());
        centerCard.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        cctvMonitor = new ModernCCTV();
        centerCard.add(cctvMonitor, BorderLayout.CENTER);
        add(centerCard, BorderLayout.CENTER);

        // --- EAST: Log Sensor (DIBUNGKUS KARTU ROUNDED) ---
        RoundedPanel rightCard = new RoundedPanel(20, colorCard);
        rightCard.setLayout(new BorderLayout(0, 15));
        rightCard.setBorder(new EmptyBorder(15, 15, 15, 15));
        rightCard.setPreferredSize(new Dimension(300, 0));

        JLabel logTitle = new JLabel("Log Sensor Gerak");
        logTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        logTitle.setForeground(textPrimary);
        rightCard.add(logTitle, BorderLayout.NORTH);

        securityLog = new JTextArea();
        securityLog.setEditable(false);
        securityLog.setBackground(new Color(15, 18, 24)); // Gelap ala Terminal
        securityLog.setForeground(new Color(46, 204, 113));
        securityLog.setFont(new Font("Monospaced", Font.PLAIN, 13));
        securityLog.setLineWrap(true);
        securityLog.setWrapStyleWord(true);
        securityLog.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollLog = new JScrollPane(securityLog);
        scrollLog.setBorder(null); 
        rightCard.add(scrollLog, BorderLayout.CENTER);

        JButton btnTest = new JButton("🚨 Uji Sensor Gerak");
        btnTest.setBackground(colorAlert);
        btnTest.setForeground(Color.WHITE);
        btnTest.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnTest.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTest.setFocusPainted(false);
        btnTest.setPreferredSize(new Dimension(0, 45));
        btnTest.addActionListener(e -> simulateMotion());
        rightCard.add(btnTest, BorderLayout.SOUTH);

        add(rightCard, BorderLayout.EAST);

        updateCCTVFeed();
        startAutoLog();

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                updateCCTVFeed();
            }
        });
    }

    private void updateCCTVFeed() {
        int index = cctvSelector.getSelectedIndex();
        if (index >= 0) {
            cctvMonitor.setImageFade(rooms.get(index).getCurrentImage());
        }
    }

    private void simulateMotion() {
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        securityLog.append("[" + time + "] ⚠️ PERINGATAN: Gerakan terdeteksi!\n");
        scrollToBottomIfVisible();
        cctvMonitor.triggerAlert();
    }

    private void startAutoLog() {
        Timer t = new Timer(8000, e -> {
            Random rand = new Random();
            if (rand.nextBoolean()) {
                securityLog.append("[" + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] Status: Aman. Tidak ada anomali.\n");
            } else {
                Room randomRoom = rooms.get(rand.nextInt(rooms.size()));
                securityLog.append("[" + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] 🔍 Sensor mendeteksi aktivitas di " + randomRoom.getName() + ".\n");
            }
            scrollToBottomIfVisible();
        });
        t.start();
    }

    // SOLUSI BUG FLOATING TEXT: Hanya auto-scroll jika panel sedang dilihat
    private void scrollToBottomIfVisible() {
        if (this.isShowing()) {
            securityLog.setCaretPosition(securityLog.getDocument().getLength());
        }
    }

    // =========================================================
    // INNER CLASS: CCTV Murni dengan Logika CCTVPanel Lama
    // =========================================================
    class ModernCCTV extends JPanel {
        private BufferedImage currentImage;
        private String currentImagePath;
        private float alpha = 1.0f;
        private boolean fading = false;
        private Timer fadeTimer;
        private String nextPath;
        private boolean showRec = true;
        private boolean isAlert = false;

        public ModernCCTV() {
            setOpaque(false);
            // Kedip REC hanya diproses jika panel terlihat (mencegah lag UI)
            new Timer(800, e -> { 
                if(isShowing()) { showRec = !showRec; repaint(); } 
            }).start();
        }

        // --- Logika Asli CCTVPanel Kamu (Dimodifikasi sedikit agar lebih aman) ---
        public void setImageFade(String path) {
            if (path == null || path.equals(currentImagePath)) return;
            nextPath = path;

            if (currentImage == null) {
                loadImage(nextPath);
                currentImagePath = nextPath;
                repaint();
                return;
            }

            fading = true;
            if (fadeTimer != null && fadeTimer.isRunning()) fadeTimer.stop();

            fadeTimer = new Timer(40, e -> {
                if (fading) {
                    alpha -= 0.12f;
                    if (alpha <= 0f) {
                        alpha = 0f;
                        loadImage(nextPath);
                        currentImagePath = nextPath;
                        fading = false;
                    }
                } else {
                    alpha += 0.12f;
                    if (alpha >= 1.0f) {
                        alpha = 1.0f;
                        fadeTimer.stop();
                    }
                }
                repaint();
            });
            fadeTimer.start();
        }

        private void loadImage(String path) {
            try {
                currentImage = ImageIO.read(getClass().getResource(path));
            } catch (Exception e) {
                currentImage = null;
            }
        }

        public void triggerAlert() {
            isAlert = true;
            repaint();
            Timer t = new Timer(2000, e -> { isAlert = false; repaint(); });
            t.setRepeats(false);
            t.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // Background layar CCTV
            g2.setColor(Color.BLACK);
            g2.fillRoundRect(0, 0, w, h, 20, 20);

            // Render Gambar dengan Transparansi (AlphaComposite)
            if (currentImage != null) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                g2.drawImage(currentImage, 5, 5, w - 10, h - 10, null);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            } else {
                g2.setColor(Color.DARK_GRAY);
                g2.drawString("NO SIGNAL", w / 2 - 30, h / 2);
            }

            // REC Indikator
            if (showRec) {
                g2.setColor(Color.RED);
                g2.fillOval(25, 25, 12, 12);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 14));
                g2.drawString("REC", 45, 35);
            }

            // Timestamp waktu nyata
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Monospaced", Font.BOLD, 16));
            g2.drawString(LocalTime.now().withNano(0).toString(), w - 110, h - 15);

            // Border Merah jika Alert
            if (isAlert) {
                g2.setColor(new Color(231, 76, 60, 150));
                g2.setStroke(new BasicStroke(8));
                g2.drawRoundRect(4, 4, w - 8, h - 8, 20, 20);
            }

            g2.dispose();
        }
    }

    // =========================================================
    // CLASS KUSTOM UI
    // =========================================================
    class RoundedPanel extends JPanel {
        private int radius;
        private Color bgColor;
        public RoundedPanel(int radius, Color bgColor) {
            this.radius = radius; this.bgColor = bgColor; setOpaque(false);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));
            g2.dispose(); super.paintComponent(g);
        }
    }
}