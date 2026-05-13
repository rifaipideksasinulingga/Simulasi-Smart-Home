public class GenericDevice extends Device {

    public GenericDevice(String name) {
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