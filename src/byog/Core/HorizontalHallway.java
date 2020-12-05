package byog.Core;

import byog.TileEngine.TETile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HorizontalHallway implements Room, java.io.Serializable {
    private Position position;
    private int height;
    private int startX;
    private int endX;
    private int yPos;
    private int width;
    private static Random rand;
    private int seed;

    /**
     * Creates a horizontal hallway that starts and ends at different xPos and same yPos.
     * The height of the hallway is always 1.
     * @param yPos The y integer position of the hall.
     * @param startX The x coordinate where the hall starts.
     * @param endX The x coordinate where the hall ends.
     * @param seed The seed.
     */
    public HorizontalHallway(int yPos, int startX, int endX, int seed) {
        this.yPos = yPos;
        this.startX = startX;
        this.endX = endX;
        height = 1;
        width =  Math.abs(startX - endX) + 1;
        //Set the xPos to the most leftmost point
        if (startX < endX) {
            position = new Position(startX, yPos);
        } else {
            position = new Position(endX, yPos);
        }
        this.seed = seed;
        this.rand = new Random(this.seed);
    }

    /**
     * Getter method.
     * @return X position of LEFTMOST point of hall.
     */
    @Override
    public int getX() {
        return position.getX();
    }

    /**
     * Getter method.
     * @return Y position of LEFTMOST point of hall.
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
     * Invariant: The position of the hallway is pointing at the LEFTMOST point.
     * @param world The 2D TETile array.
     * @param tile The specified floor tile to use.
     */
    @Override
    public void generate(TETile[][] world, TETile tile) {
        for (int x = position.getX(); x < position.getX() + getWidth(); x++) {
            world[x][yPos] = tile;
        }
    }

    /**
     * Returns a random point in the hallway.
     * @return A Position instance of a random point in the hallway.
     */
    @Override
    public Position randomPoint() {
        int randX = RandomUtils.uniform(rand, getX(), getX() + getWidth());
        int randY = RandomUtils.uniform(rand, getY(), getY() + getHeight());
        return new Position(randX, randY);
    }
    /**
     * Checks to see if the hallway can fit within the world array.
     * @param world The 2D TETile array.
     * @return True or false depending on if the room fits.
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
