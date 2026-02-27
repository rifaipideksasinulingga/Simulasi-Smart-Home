public class Lamp extends Device {

    public Lamp(String name) {
        super(name);
    }

    @Override
    public void turnOn() {
        status = true;
    }

    @Override
    public void turnOff() {
        status = false;
    }
}