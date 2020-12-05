package byog.Core;

import byog.TileEngine.TETile;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class HexagonRoom implements Room, java.io.Serializable {
    private Position pos;
    private int seed;
    private int s;
    private Random rand;

    /**
     * Creates a hexagon room with given position and side length.
     * @param xPos The x position of the BOTTOM LEFT corner of the hexagon.
     * @param yPos The y position of the BOTTOM LEFT corner of the hexagon.
     * @param s The integer side length of the hexagon.
     * @param seed The seed.
     */
    public HexagonRoom(int xPos, int yPos, int s, int seed) {
        pos = new Position(xPos, yPos);
        this.seed = seed;
        this.s = s;
        this.rand = new Random(this.seed);
    }

    /**
     * Getter method.
     * @return The x position of the bottom left corner.
     */
    @Override
    public int getX() {
        return pos.getX();
    }

    /**
     * Getter method.
     * @return The y position of the bottom left corner.
     */
    @Override
    public int getY() {
        return pos.getY();
    }

    /**
     * Getter method.
     * @return The side length of the hexagon.
     */
    public int getSideLength() {
        return s;
    }

    /**
     * Adds the hexagon room to the world 2D array using the specified tile.
     * Assumes that the room can fit into the world.
     * @param world The TETile 2D array.
     * @param tile The specified tile to use
     */
    @Override
    public void generate(TETile[][] world, TETile tile) {
        int start = pos.getX();
        int height = getSideLength();
        int width = getSideLength();
        for (int y = pos.getY(); y < pos.getY() + height; y++) {
            for (int x = start; x < start + width; x++) {
                world[x][y] = tile;
            }
            width += 2;
            start -= 1;
        }
        width -= 2;
        start += 1;
        for (int y = pos.getY() + height; y < pos.getY() + 2 * height; y++) {
            for (int x = start; x < start + width; x++) {
                world[x][y] = tile;
            }
            width -= 2;
            start += 1;
        }
    }

    /**
     * Returns a random point in the room.
     * @return A random position in the room.
     */
    @Override
    public Position randomPoint() {
        List<Position> positionList =  allPoints();
        int randIndex = RandomUtils.uniform(rand, 0, positionList.size());
        return positionList.get(randIndex);
    }

    /**
     * Checks to see if the room can fit into the specified world 2D array.
     * @param world The TETile 2D array.
     * @return True or false if the room can fit or not.
     */
    @Override
    public boolean inBound(TETile[][] world) {
        return pos.getY() > 0
                && pos.getY() + 2 * getSideLength() < world[0].length - 1
                && pos.getX() - getSideLength() >= 0
                && pos.getX() + 2 * getSideLength() - 1 < world.length - 1;
    }

    /**
     * Checks if a point is in the room.
     * @param p The position.
     * @return True or false if the point is in the room.
     */
    @Override
    public boolean containsPoint(Position p) {
        int start = pos.getX();
        int height = getSideLength();
        int width = getSideLength();
        List<Position> positionList = new ArrayList<Position>();
        for (int y = pos.getY(); y < pos.getY() + height; y++) {
            for (int x = start; x < start + width; x++) {
                positionList.add(new Position(x, y));
            }
            width += 2;
            start -= 1;
        }
        width -= 2;
        start += 1;
        for (int y = pos.getY() + height; y < pos.getY() + 2 * height; y++) {
            for (int x = start; x < start + width; x++) {
                positionList.add(new Position(x, y));
            }
            width -= 2;
            start += 1;
        }
        for (int i = 0; i < positionList.size(); i += 1) {
            if (positionList.get(i).equals(p)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns list of all points in the room.
     * @return All points in the room.
     */
    @Override
    public List<Position> allPoints() {
        int start = pos.getX();
        int height = getSideLength();
        int width = getSideLength();
        List<Position> positionList = new ArrayList<Position>();
        for (int y = pos.getY(); y < pos.getY() + height; y++) {
            for (int x = start; x < start + width; x++) {
                positionList.add(new Position(x, y));
            }
            width += 2;
            start -= 1;
        }
        width -= 2;
        start += 1;
        for (int y = pos.getY() + height; y < pos.getY() + 2 * height; y++) {
            for (int x = start; x < start + width; x++) {
                positionList.add(new Position(x, y));
            }
            width -= 2;
            start += 1;
        }
        return positionList;
    }

    /**
     * Returns the height of the room.
     * @return The height of the room.
     */
    @Override
    public int getHeight() {
        return 2 * getSideLength();
    }

    /**
     * Returns the width of the room.
     * @return The width of the room.
     */
    @Override
    public int getWidth() {
        return getSideLength() + getSideLength() - 2 + getSideLength();
    }

}
