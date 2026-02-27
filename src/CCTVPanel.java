import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalTime;
import javax.imageio.ImageIO;
import javax.swing.*;

public class CCTVPanel extends JPanel {

    private BufferedImage image;
    private String imagePath;

    public CCTVPanel(String imagePath) {
        this.imagePath = imagePath;
        loadImage();

        setPreferredSize(new Dimension(400, 300));

        Timer timer = new Timer(1000, e -> repaint());
        timer.start();
    }

    private void loadImage() {
        try {
            image = ImageIO.read(getClass().getResource(imagePath));
            if (image == null) {
                System.out.println("Gambar tidak ditemukan: " + imagePath);
            }
        } catch (Exception e) {
            System.out.println("Error load: " + imagePath);
            e.printStackTrace();
        }
    }

    public void setImage(String path) {
        this.imagePath = path;
        loadImage();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // Background monitor
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        if (image != null) {
            g2.drawImage(image, 20, 20,
                    getWidth() - 40,
                    getHeight() - 40,
                    null);
        }

        // REC
        g2.setColor(Color.RED);
        g2.fillOval(25, 25, 12, 12);
        g2.setColor(Color.WHITE);
        g2.drawString("REC", 45, 35);

        // Timestamp
        g2.drawString(
                LocalTime.now().withNano(0).toString(),
                getWidth() - 110,
                getHeight() - 15
        );
    }
}