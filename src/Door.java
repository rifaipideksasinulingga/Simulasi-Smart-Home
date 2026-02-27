public class Door extends Device {

    public Door(String name) {
        super(name);
    }

    public void lock() {
        status = true;
    }

    public void unlock() {
        status = false;
    }

    @Override
    public void turnOn() {
        lock();
    }

    @Override
    public void turnOff() {
        unlock();
    }

    public String getState() {
        return status ? "LOCKED" : "UNLOCKED";
    }
}