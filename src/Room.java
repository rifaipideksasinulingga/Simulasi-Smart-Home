public class Room {
    private String name;
    private Lamp lamp;
    private AC ac;
    private Door door;
    private CCTV cctv;

    public Room(String name, String imagePath) {
        this.name = name;
        lamp = new Lamp("Lampu");
        ac = new AC("AC");
        door = new Door("Pintu");
        cctv = new CCTV("CCTV", imagePath);
    }

    public String getName() { return name; }
    public Lamp getLamp() { return lamp; }
    public AC getAC() { return ac; }
    public Door getDoor() { return door; }
    public CCTV getCCTV() { return cctv; }
}
