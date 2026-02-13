public class AC extends Remote {
    private int temperature;

    public AC(String name, int temperature) {
        super(name);
        this.temperature = temperature;
    }

    @Override
    public void turnOn() {
        status = true;
    }

    @Override
    public void turnOff() {
        status = false;
    }

    @Override
    public String info() {
        return name + " : " + (status ? "ON" : "OFF") +
               " | Suhu: " + temperature + "°C";
    }
}
