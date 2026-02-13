public abstract class Remote {
    protected String name;
    protected boolean status;

    public Remote(String name) {
        this.name = name;
        this.status = false;
    }

    public abstract void turnOn();
    public abstract void turnOff();

    public String info() {
        return name + " : "+ (status ? "ON" : "OFF");
    }


}
