package byog.Core;

public class Enemy implements java.io.Serializable {
    private Position pos;
    // The direction the enemy is currently moving in.
    // O up, 1 left, 2 down, 3 right
    private int dir = -1;

    /**
     * Creates an enemy object.
     * @param pos Starting position of the enemy.
     */
    public Enemy(Position pos) {
        this.pos = pos;
    }

    /**
     * Returns the position of the enemy.
     * @return Position.
     */
    public Position getPos() {
        return this.pos;
    }

    /**
     * Returns the direction of the enemy.
     * @return Integer dir.
     */
    public int getDir() {
        return this.dir;
    }

    /**
     * Sets the direction of the enemy.
     * @param dir The integer direction.
     */
    public void setDir(int dir) {
        this.dir = dir;
    }

    /**
     * Sets the position of the enemy to a new position.
     * @param p The new position.
     */
    public void setPos(Position p) {
        this.pos = p;
    }
}
