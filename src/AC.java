public class AC extends Device {

    private int temperature; 

    public AC(String name) {
        super(name);
        this.temperature = 24;
    }

    @Override
    public void turnOn() {
        status = true;
    }

    @Override
    public void turnOff() {
        status = false;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        if (temperature < 16) {
            this.temperature = 16;
        } else if (temperature > 30) {
            this.temperature = 30;
        } else {
            this.temperature = temperature;
        }
    }
}