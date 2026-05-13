public abstract class Device {
    protected String name;
    protected boolean status;

    public Device(String name) {
        this.name = name;
        this.status = false; // Default mati
    }

    public abstract void turnOn();
    public abstract void turnOff();

    // Pastikan method ini PUBLIC agar terbaca di panel mana pun
    public String getName() {
        return name;
    }

    public boolean isOn() {
        return status;
    }
}