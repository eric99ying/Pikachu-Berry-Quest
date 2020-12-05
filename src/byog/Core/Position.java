package byog.Core;

public class Position implements java.io.Serializable {
    private final int x;
    private final int y;

    /**
     * Takes integer x and y and set the instance variables to it.
     * @param x x position.
     * @param y y position.
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter method.
     * @return x position.
     */
    public int getX() {
        return x;
    }

    /**
     * Getter method.
     * @return y position.
     */
    public int getY() {
        return y;
    }

    /**
     * Checks equality of two positions.
     * @param p The other position to check.
     * @return True or false.
     */
    public boolean equals(Position p) {
        return p.getY() == getY() && p.getX() == getX();
    }

}
