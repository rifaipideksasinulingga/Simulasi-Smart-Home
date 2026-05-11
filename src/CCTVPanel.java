import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalTime;
import javax.imageio.ImageIO;
import javax.swing.*;

public class CCTVPanel extends JPanel {

    private BufferedImage image;
    private String imagePath;
    private float alpha =1f;
    private boolean fading = false;
    private Timer fadeTime;
    private String nextImagePath;

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

    public void setImage(String path){
        this.imagePath = path;
        loadImage();
        repaint();
    }

    // edit sedikit, tambah animasi fade
    public void setImageFade(String path){
        if(path.equals(imagePath))
            return;

        nextImagePath = path;
        fading = true;
        
        if(fadeTime != null&&fadeTime.isRunning()){
            fadeTime.stop();
        }

        fadeTime = new Timer(40, e ->{
            if(fading){
                alpha -= 0.12f;

                if (alpha<= 0f){
                    alpha = 0f;
                    imagePath = nextImagePath;
                    loadImage();
                    fading = false;
                }
            } else {
                alpha += 0.12f;
                if(alpha>= 1f){
                    alpha = 1f;
                    fadeTime.stop();
                }
            }
            repaint();
        });
        fadeTime.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // Background monitor
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        if (image != null) {
            g2.setComposite(
                    AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER,
                        alpha));

            g2.drawImage(image, 20, 20,
                    getWidth() - 40,
                    getHeight() - 40,
                    null);

            g2.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 
                1f));
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