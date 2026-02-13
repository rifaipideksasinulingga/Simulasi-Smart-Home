import java.util.ArrayList;

public class Control {
    private ArrayList<Remote> devices = new ArrayList<>();

    public void addDevice(Remote device) {
        devices.add(device);
    }

    public void turnAllOn() {
        for (Remote d : devices) {
            d.turnOn();
        }
    }

    public void turnAllOff() {
        for (Remote d : devices) {
            d.turnOff();
        }
    }

    public String getStatus() {
        StringBuilder sb = new StringBuilder();
        for (Remote d : devices) {
            sb.append(d.info()).append("\n");
        }
        return sb.toString();
    }
}
