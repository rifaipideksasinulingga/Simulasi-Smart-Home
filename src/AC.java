public class AC extends Device {

    public AC(String name) {
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