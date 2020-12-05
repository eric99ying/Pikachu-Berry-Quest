package byog.Core;

import byog.TileEngine.TETile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RectangleRoom implements Room, java.io.Serializable {

    private Position position;
    private int height;
    private int width;
    private int seed;
    private Random rand;

    /**
     * The constructor of the wall-less room.
     * @param xPos X position of the BOTTOM LEFT corner of the room.
     * @param yPos Y position of the BOTTOM LEFT corner of the room.
     * @param height Height of the room.
     * @param width Width of the room.
     * @param seed The seed.
     */
    public RectangleRoom(int xPos, int yPos, int height, int width, int seed) {
        position = new Position(xPos, yPos);
        this.height = height;
        this.width = width;
        this.seed = seed;
        this.rand = new Random(this.seed);
    }

    /**
     * Getter method.
     * @return X position of BOTTOM LEFT corner of room.
     */
    @Override
    public int getX() {
        return position.getX();
    }

    /**
     * Getter method.
     * @return Y position of BOTTOM LEFT corner of room.
     */
    @Override
    public int getY() {
        return position.getY();
    }

    /**
     * Getter method.
     * @return Integer height of room.
     */
    @Override
    public int getHeight() {
        return height;
    }

    /**
     * Getter method.
     * @return Integer width of room.
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * Adds the room to the 2D world array using the specified tile.
     * The room initially starts wall-less.
     * Invariant: We assume that the room CAN fit into the world array.
     * Invariant: The position of the room is pointing at the BOTTOM LEFT corner of the room.
     * @param world The 2D TETile array.
     * @param tile The specified floor tile to use.
     */
    @Override
    public void generate(TETile[][] world, TETile tile) {
        for (int x = getX(); x < getX() + getWidth(); x++) {
            for (int y = getY(); y < getY() + getHeight(); y++) {
                world[x][y] = tile;
            }
        }
    }

    /**
     * Returns a random point in the room.
     * @return A Position instance of a random point in the room.
     */
    @Override
    public Position randomPoint() {
        int randX = RandomUtils.uniform(rand, getX(), getX() + getWidth());
        int randY = RandomUtils.uniform(rand, getY(), getY() + getHeight());
        return new Position(randX, randY);
    }

    /**
     * Checks to see if the room can fit within the world array.
     * @param world The 2D TETile array.
     * @return True or false depending on if the room fits.
     */
    @Override
    public boolean inBound(TETile[][] world) {
        return getX() > 0 && getX() + getWidth() < world.length - 1
                && getY() > 0 && getY() + getHeight() < world[0].length - 1;
    }

    /**
     * Checks if a point is in the room.
     * @param p The position.
     * @return True or false if the point is in the room.
     */
    @Override
    public boolean containsPoint(Position p) {
        return p.getX() >= position.getX() && p.getX() < position.getX() + getWidth()
                && p.getY() >= position.getY() && p.getY() < position.getY() + getHeight();
    }

    /**
     * Returns list of all points in the room.
     * @return All points in the room.
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

    public static void main(String[] args) {
        // Sample random room generation.

    }


}
