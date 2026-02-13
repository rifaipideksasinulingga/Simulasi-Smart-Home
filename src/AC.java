public class AC extends Remote {

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
