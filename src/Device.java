public abstract class Device {

    protected String name;
    protected boolean status;

    public Device(String name) {
        this.name = name;
        this.status = false;
    }

    public abstract void turnOn();
    public abstract void turnOff();

    public boolean isOn() {
        return status;
    }
}