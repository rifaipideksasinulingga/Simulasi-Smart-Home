import java.util.ArrayList;
import java.util.List;

public class Room {
    private String name;
    private String imageOn;
    private String imageOff;
    private List<Device> devices;

    public Room(String name, String imageOn, String imageOff) {
        this.name = name;
        this.imageOn = imageOn;
        this.imageOff = imageOff;
        this.devices = new ArrayList<>();
    }

    public void addDevice(Device device) {
        this.devices.add(device);
    }

    public List<Device> getDevices() {
        return devices;
    }

    public String getName() {
        return name;
    }

    // Gambar ruangan akan "ON" jika minimal ada satu lampu yang menyala
    public String getCurrentImage() {
        for (Device d : devices) {
            if (d instanceof Lamp && d.isOn()) {
                return imageOn;
            }
        }
        return imageOff; 
    }
}