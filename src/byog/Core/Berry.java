package byog.Core;

public class Berry implements java.io.Serializable {
    private Position pos;
    /**
     * Berry object.
     * @param pos Starting position of berry.
     */
    public Berry(Position pos) {
        this.pos = pos;
    }


    // Getter and Setter
    public Position getPos() {
        return this.pos;
    }
    public void setPos(Position pos) {
        this.pos = pos;
    }


}
