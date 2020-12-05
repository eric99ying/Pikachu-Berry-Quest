package byog.Core;

public class Exit implements java.io.Serializable {
    private Position pos;
    private boolean locked;

    /**
     * Exit object. Should start of locked. Needs key to unlock.
     * @param pos Starting position of the exit.
     */
    public Exit(Position pos) {
        this.pos = pos;
        // Change to true once keys are implemented.
        this.locked = false;
    }


    // Getter and Setter
    public Position getPos() {
        return this.pos;
    }
    public boolean getLockedStatus() {
        return this.locked;
    }
    public void setPos(Position pos) {
        this.pos = pos;
    }
    public void unlock(int health) {
        this.locked = false;
    }

}
