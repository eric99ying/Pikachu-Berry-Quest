package byog.Core;

import byog.TileEngine.TETile;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class VerticalHallway implements Room, java.io.Serializable {
    private Position position;
    private int height;
    private int xPos;
    private int startY;
    private int endY;
    private int width;
    private int seed;
    private static Random rand;

    /**
     * Creates a vertical hallway that starts and ends at different xPos and same yPos.
     * The height of the hallway is always 1.
     * @param xPos The x integer position of the hall.
     * @param startY The y coordinate where the hall starts.
     * @param endY The y coordinate where the hall ends.
     * @param seed The seed.
     */
    public VerticalHallway(int xPos, int startY, int endY, int seed) {
        this.xPos = xPos;
        this.startY = startY;
        this.endY = endY;
        height = Math.abs(startY - endY) + 1;
        width = 1;
        //Set the yPos to the bottom
        if (startY < endY) {
            position = new Position(xPos, startY);
        } else {
            position = new Position(xPos, endY);
        }
        this.seed = seed;
        this.rand = new Random(this.seed);
    }

    /**
     * Getter method.
     * @return X position of BOTTOM point of hall.
     */
    @Override
    public int getX() {
        return position.getX();
    }

    /**
     * Getter method.
     * @return Y position of BOTTOM point of hall.
     */
    @Override
    public int getY() {
        return position.getY();
    }

    /**
     * Getter method.
     * @return Integer height of hall.
     */
    @Override
    public int getHeight() {
        return height;
    }

    /**
     * Getter method.
     * @return Integer width of hall.
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * Adds the hallway to the 2D world array using the specified tile.
     * The hallway initially starts wall-less.
     * Invariant: We assume that the hallway CAN fit into the world array.
     * Invariant: The position of the hallway is pointing at the BOTTOM point of the hallway.
     * @param world The 2D TETile array.
     * @param tile The specified floor tile to use.
     */
    @Override
    public void generate(TETile[][] world, TETile tile) {
        for (int y = position.getY(); y < position.getY() + getHeight(); y++) {
            world[xPos][y] = tile;
        }
    }

    /**
     * Returns a random point in the hall.
     * @return A Position instance of a random point in the hall.
     */
    @Override
    public Position randomPoint() {
        int randX = RandomUtils.uniform(rand, getX(), getX() + getWidth());
        int randY = RandomUtils.uniform(rand, getY(), getY() + getHeight());
        return new Position(randX, randY);
    }

    /**
     * Checks to see if the hall can fit within the world array.
     * @param world The 2D TETile array.
     * @return True or false depending on if the hall fits.
     */
    @Override
    public boolean inBound(TETile[][] world) {
        return getX() > 0 && getX() + getWidth() < world.length
                && getY() > 0 && getY() + getHeight() < world[0].length;
    }

    /**
     * Checks if a point is in the hall.
     * @param p The position.
     * @return True or false if the point is in the hall.
     */
    @Override
    public boolean containsPoint(Position p) {
        return p.getX() >= position.getX() && p.getX() < position.getX() + getWidth()
                && p.getY() >= position.getY() && p.getY() < position.getY() + getHeight();
    }

    /**
     * Returns list of all points in the hall.
     * @return All points in the hall.
     */
    @Override
    public List<Position> allPoints() {
        List<Position> outList = new ArrayList<Position>();
        for (int x = getX(); x < getX() + getWidth(); x += 1) {
            for (int y = getY(); y < getY() + getHeight(); y += 1) {
                outList.add(new Position(x, y));
            }
        }
        return outList;
    }

}
