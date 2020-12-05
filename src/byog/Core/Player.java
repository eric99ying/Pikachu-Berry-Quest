package byog.Core;

public class Player implements java.io.Serializable {
    private Position pos;
    private int health;
    private int maxHealth;

    /**
     * Player object.
     * @param pos Starting position of player.
     * @param health Starting health of player.
     * @param maxHealth Max health of the player.
     */
    public Player(Position pos, int health, int maxHealth) {
        this.pos = pos;
        this.health = health;
        this.maxHealth = maxHealth;
    }


    // Getter and Setter
    public Position getPos() {
        return this.pos;
    }
    public int getHealth() {
        return this.health;
    }
    public void setPos(Position pos) {
        this.pos = pos;
    }
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Loses health. Can't go below 0 health.
     * @param amount Amount of health lost.
     */
    public void loseHealth(int amount) {
        setHealth(getHealth() - amount);
        if (getHealth() < 0) {
            setHealth(0);
        }
    }

    /**
     * Gains health. Can't go above max health.
     * @param amount Amount of health gained.
     */
    public void gainHealth(int amount) {
        setHealth(getHealth() + amount);
        if (getHealth() > maxHealth) {
            setHealth(maxHealth);
        }
    }

}
