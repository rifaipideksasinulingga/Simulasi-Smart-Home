import javax.swing.*;

public class CCTV extends Remote {

    private String imagePath;

    public CCTV(String name, String imagePath) {
        super(name);
        this.imagePath = imagePath;
        this.status = true; // CCTV selalu aktif
    }

    @Override
    public void turnOn() {
        status = true;
    }

    @Override
    public void turnOff() {
        status = false;
    }

    public ImageIcon getImage() {
        return new ImageIcon(imagePath);
    }
}
