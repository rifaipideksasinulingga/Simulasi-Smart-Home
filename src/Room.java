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

    // Method getName untuk Ruangan
    public String getName() {
        return name;
    }

    public String getCurrentImage() {
        for (Device d : devices) {
            // Jika ada satu saja Lampu yang menyala, ruangan dianggap terang
            if (d instanceof Lamp && d.isOn()) {
                return imageOn;
            }
        }
        return imageOff; 
    }
}