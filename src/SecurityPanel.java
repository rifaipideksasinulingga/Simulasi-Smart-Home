import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;

public class SecurityPanel extends JPanel {

    // Palet Warna
    private Color colorBg = new Color(25, 29, 38);
    private Color colorCard = new Color(42, 48, 60);
    private Color textPrimary = Color.WHITE;
    private Color textSecondary = new Color(160, 170, 185);
    private Color colorAlert = new Color(231, 76, 60); // Merah untuk peringatan

    private List<Room> rooms;
    private JComboBox<String> cctvSelector;
    private ModernCCTV cctvMonitor;
    private JTextArea securityLog;
    private Timer motionSensorTimer;

    public SecurityPanel(List<Room> rooms) {
        this.rooms = rooms;
        setLayout(new BorderLayout(20, 20));
        setBackground(colorBg);
        setBorder(new EmptyBorder(10, 0, 10, 0));

        // --- HEADER: Judul & Pilihan Kamera ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(colorBg);
        headerPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel title = new JLabel("Pusat Keamanan & CCTV");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(textPrimary);
        headerPanel.add(title, BorderLayout.WEST);

        cctvSelector = new JComboBox<>();
        for (Room r : rooms) {
            cctvSelector.addItem("📹 Kamera: " + r.getName());
        }
        cctvSelector.setFont(new Font("SansSerif", Font.BOLD, 14));
        cctvSelector.setPreferredSize(new Dimension(250, 40));
        cctvSelector.setCursor(new Cursor(Cursor.HAND_CURSOR));

        cctvSelector.addActionListener(e -> updateCCTVFeed());
        headerPanel.add(cctvSelector, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // --- BAGIAN TENGAH: Layar Monitor CCTV ---
        cctvMonitor = new ModernCCTV();
        add(cctvMonitor, BorderLayout.CENTER);

        // --- BAGIAN KANAN: Simulasi Sensor Gerak & Log ---
        JPanel rightPanel = new JPanel(new BorderLayout(0, 15));
        rightPanel.setBackground(colorBg);
        rightPanel.setPreferredSize(new Dimension(300, 0));

        JLabel logTitle = new JLabel("Log Sensor Gerak");
        logTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        logTitle.setForeground(textPrimary);
        rightPanel.add(logTitle, BorderLayout.NORTH);

        securityLog = new JTextArea();
        securityLog.setEditable(false);
        securityLog.setBackground(new Color(15, 18, 24)); // Lebih gelap ala terminal
        securityLog.setForeground(new Color(46, 204, 113)); // Hijau terminal
        securityLog.setFont(new Font("Monospaced", Font.PLAIN, 13));
        securityLog.setLineWrap(true);
        securityLog.setWrapStyleWord(true);
        securityLog.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollLog = new JScrollPane(securityLog);
        scrollLog.setBorder(BorderFactory.createLineBorder(colorCard, 2));
        rightPanel.add(scrollLog, BorderLayout.CENTER);

        // Tombol Kontrol Sensor
        JButton btnTriggerMotion = new JButton("🚨 Uji Sensor Gerak");
        btnTriggerMotion.setBackground(colorAlert);
        btnTriggerMotion.setForeground(Color.WHITE);
        btnTriggerMotion.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnTriggerMotion.setFocusPainted(false);
        btnTriggerMotion.setPreferredSize(new Dimension(0, 45));
        btnTriggerMotion.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnTriggerMotion.addActionListener(e -> simulateMotionDetected());
        rightPanel.add(btnTriggerMotion, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.EAST);

        // --- MULAI SISTEM ---
        updateCCTVFeed();
        startAutomatedSensor();
    }

    private void updateCCTVFeed() {
        int index = cctvSelector.getSelectedIndex();
        if (index >= 0 && index < rooms.size()) {
            Room r = rooms.get(index);
            cctvMonitor.setImage(r.getCurrentImage());
        }
    }

    private void addLog(String message) {
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        securityLog.append("[" + time + "] " + message + "\n");
        // Auto-scroll ke bawah
        securityLog.setCaretPosition(securityLog.getDocument().getLength());
    }

    private void simulateMotionDetected() {
        int index = cctvSelector.getSelectedIndex();
        if (index >= 0 && index < rooms.size()) {
            Room r = rooms.get(index);
            addLog("⚠️ PERINGATAN: Gerakan terdeteksi di " + r.getName() + "!");
            cctvMonitor.triggerAlert(); // Mengubah border CCTV jadi merah sejenak
        }
    }

    // Simulasi sensor gerak otomatis yang berjalan acak setiap beberapa detik
    private void startAutomatedSensor() {
        addLog("Sistem Sensor Gerak Aktif...");
        Random rand = new Random();
        motionSensorTimer = new Timer(8000, e -> {
            if (rand.nextBoolean()) {
                // 50% kemungkinan aman
                addLog("Status: Aman. Tidak ada anomali.");
            } else {
                // 50% kemungkinan ada gerakan di ruangan acak
                Room randomRoom = rooms.get(rand.nextInt(rooms.size()));
                addLog("🔍 Sensor mendeteksi aktivitas di " + randomRoom.getName() + ".");
            }
        });
        motionSensorTimer.start();
    }

    // =========================================================
    // KELAS KUSTOM: Monitor CCTV Modern
    // =========================================================
    class ModernCCTV extends JPanel {
        private BufferedImage image;
        private boolean showRec = true;
        private boolean isAlert = false;
        private Timer blinkTimer;
        private Timer alertTimer;

        public ModernCCTV() {
            setOpaque(false);
            
            // Timer untuk efek kedip tombol REC
            blinkTimer = new Timer(800, e -> {
                showRec = !showRec;
                repaint();
            });
            blinkTimer.start();
        }

        public void setImage(String imagePath) {
            try {
                // Mengambil gambar dari resource
                image = ImageIO.read(getClass().getResource(imagePath));
            } catch (Exception e) {
                System.out.println("Gagal memuat gambar CCTV: " + imagePath);
                image = null;
            }
            repaint();
        }

        public void triggerAlert() {
            isAlert = true;
            repaint();
            // Kembalikan ke normal setelah 2 detik
            if (alertTimer != null && alertTimer.isRunning()) alertTimer.stop();
            alertTimer = new Timer(2000, e -> {
                isAlert = false;
                repaint();
            });
            alertTimer.setRepeats(false);
            alertTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // Background Hitam Layar
            g2.setColor(Color.BLACK);
            g2.fillRoundRect(0, 0, w, h, 20, 20);

            // Gambar Ruangan (Jika ada)
            if (image != null) {
                g2.drawImage(image, 5, 5, w - 10, h - 10, null);
            } else {
                g2.setColor(Color.DARK_GRAY);
                g2.drawString("TIDAK ADA SINYAL", w / 2 - 50, h / 2);
            }

            // Indikator REC berkedip
            if (showRec) {
                g2.setColor(Color.RED);
                g2.fillOval(20, 20, 15, 15);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 14));
                g2.drawString("REC", 45, 33);
            }

            // Timestamp CCTV
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Monospaced", Font.BOLD, 16));
            String time = LocalTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            g2.drawString(time, w - 210, h - 20);

            // Efek Border Merah jika sedang Alert
            if (isAlert) {
                g2.setColor(new Color(231, 76, 60, 150)); // Merah semi transparan
                g2.setStroke(new BasicStroke(8));
                g2.drawRoundRect(4, 4, w - 8, h - 8, 15, 15);
            } else {
                // Border normal
                g2.setColor(colorCard);
                g2.setStroke(new BasicStroke(4));
                g2.drawRoundRect(2, 2, w - 4, h - 4, 18, 18);
            }

            g2.dispose();
        }
    }
}