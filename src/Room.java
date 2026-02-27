public class Room {

    private String name;
    private Lamp lamp;
    private AC ac;
    private Door door;

    private String imageOn;
    private String imageOff;

    public Room(String name, String imageOn, String imageOff) {
        this.name = name;
        this.imageOn = imageOn;
        this.imageOff = imageOff;

        lamp = new Lamp("Lampu");
        ac = new AC("AC");
        door = new Door("Pintu");
    }

    public String getName() { return name; }
    public Lamp getLamp() { return lamp; }
    public AC getAC() { return ac; }
    public Door getDoor() { return door; }

    public String getCurrentImage() {
        return lamp.isOn() ? imageOn : imageOff;
    }
}