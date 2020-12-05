package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World implements java.io.Serializable {

    private TETile[][] world;
    private TETile[][] origWorld;
    private int height;
    private int width;
    private int numRoom;
    private Random rand;
    private int seed;
    private List<Room> roomList;
    private int tileSet;

    // Adjustable values.
    private final int RECT_ROOM_MIN_SIZE = 3;
    private final int RECT_ROOM_MAX_SIZE = 7;
    private final int HEX_ROOM_MIN_SIZE = 2;
    private final int HEX_ROOM_MAX_SIZE = 5;
    private final int OFFSET_X = 5;
    private final int OFFSET_Y_TOP = 11;
    private final int OFFSET_Y_BOT = 9;
    // Adjust the quadrant bounds.
    private final int OVERLAP_NUM = 1;
    private final int OVERLAP_DEN = 2;

    /**
     * Constructor for the world. The world is essentially an starting layout of game.
     * Whenever we create a new level, we create a new World object.
     * @param width Integer width.
     * @param height Integer height.
     * @param minNumRoom Minimum number of rooms.
     * @param maxNumRoom Maximum number of room.
     * @param seed The input seed.
     */
    public World(int width, int height, int minNumRoom, int maxNumRoom, int seed) {
        this.world = new TETile[width][height];
        this.height = height;
        this.width = width;
        this.seed = seed;
        this.rand = new Random(this.seed);
        this.tileSet = RandomUtils.uniform(rand, 0, 3);
        this.numRoom = RandomUtils.uniform(rand, minNumRoom, maxNumRoom);
        this.roomList = new ArrayList<Room>();
        generateWorld();
        // Stores the original world for later use.
        origWorld = new TETile[width][height];
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                origWorld[x][y] = world[x][y];
            }
        }
    }

    // Generates the world 2D array using TERenderer.
    // First fills the world empty, then generates rooms and halls.
    // Finally, adds walls next to all floor tiles.
    private void generateWorld() {
        fillEmpty();
        generateRandomRoomsAndHalls();
        addWalls();
    }

    // Generates a list of random rooms that fit in the 2D world array.
    // Also generates hallways between every room.
    // Then adds every room into the world 2D array.
    private void generateRandomRoomsAndHalls() {
        int numRoomPerQuad = getNumRoom() / 4;
        int numRoomExtra = getNumRoom() % 4;
        int quad = RandomUtils.uniform(rand, 0, 4);
        int numRoomTracker = 0;
        // We split the window into 4 quadrants and generate rooms in each quadrant.
        // This creates a more even distribution of rooms.
        for (int i = 0; i < 4; i += 1) {
            while (numRoomTracker < numRoomPerQuad) {
                Room r = randomRoomInQuad(quad);
                if (roomList.size() >= 1) {
                    // Decreases chances of having overlapping rooms.
                    while (overlap(r) && RandomUtils.uniform(rand) > 0.7) {
                        r = randomRoomInQuad(quad);
                    }
                    Room prev = roomList.get(roomList.size() - 1);
                    connectRooms(r, prev);
                }
                r.generate(world, Tileset.FLOORS[getTileSet()]);
                roomList.add(r);
                numRoomTracker += 1;
                if (RandomUtils.uniform(rand) < 0.15) {
                    int numTimes = RandomUtils.uniform(rand, 1, 3);
                    for (int j = 0; j < numTimes; j += 1) {
                        quad = incrementModulo(quad, 4);
                    }
                }
            }
            numRoomTracker = 0;
            quad = incrementModulo(quad, 4);
        }

        // Adds the extra remaining rooms into each quadrant
        for (int i = 0; i < numRoomExtra; i += 1) {
            Room r = randomRoomInQuad(quad);
            if (roomList.size() >= 1) {
                Room prev = roomList.get(roomList.size() - 1);
                connectRooms(r, prev);
            }
            r.generate(world, Tileset.FLOORS[getTileSet()]);
            roomList.add(r);
            quad = incrementModulo(quad, 4);
        }

    }

    // Returns a random room in quad 0, 1, 2, or 3.
    // There is 25% chance of spawning a hexagon room instead.
    private Room randomRoomInQuad(int quad) {
        int startX = OFFSET_X;
        int startY = OFFSET_Y_BOT;
        int endX = getWidth() - OFFSET_X;
        int endY = getHeight() - OFFSET_Y_TOP;
        if (quad == 0) {
            endX = getWidth() * OVERLAP_NUM / OVERLAP_DEN;
            endY = getHeight() * OVERLAP_NUM / OVERLAP_DEN;
        } else if (quad == 1) {
            startX = getWidth() * (OVERLAP_DEN - OVERLAP_NUM) / OVERLAP_DEN;
            endY = getHeight() * OVERLAP_NUM / OVERLAP_DEN;
        } else if (quad == 2) {
            startY = getHeight() * (OVERLAP_DEN - OVERLAP_NUM) / OVERLAP_DEN;
            endX = getWidth() * OVERLAP_NUM / OVERLAP_DEN;
        } else if (quad == 3) {
            startX = getWidth() * (OVERLAP_DEN - OVERLAP_NUM) / OVERLAP_DEN;
            startY = getHeight() * (OVERLAP_DEN - OVERLAP_NUM) / OVERLAP_DEN;
        }
        Room r = null;

        // Room is either a hexagon or rectangle.
        if (RandomUtils.uniform(rand) < 0.1) {
            r = new HexagonRoom(RandomUtils.uniform(rand, startX, endX),
                    RandomUtils.uniform(rand, startY, endY),
                    RandomUtils.uniform(rand, HEX_ROOM_MIN_SIZE, HEX_ROOM_MAX_SIZE),
                    this.seed);
            while (!r.inBound(world)) {
                r = new HexagonRoom(RandomUtils.uniform(rand, startX, endX),
                        RandomUtils.uniform(rand, startY, endY),
                        RandomUtils.uniform(rand, HEX_ROOM_MIN_SIZE, HEX_ROOM_MAX_SIZE),
                        this.seed);
            }
        } else {
            r = new RectangleRoom(RandomUtils.uniform(rand, startX, endX),
                    RandomUtils.uniform(rand, startY, endY),
                    RandomUtils.uniform(rand, RECT_ROOM_MIN_SIZE, RECT_ROOM_MAX_SIZE),
                    RandomUtils.uniform(rand, RECT_ROOM_MIN_SIZE, RECT_ROOM_MAX_SIZE),
                    this.seed);
            while (!r.inBound(world)) {
                r = new RectangleRoom(RandomUtils.uniform(rand, startX, endX),
                        RandomUtils.uniform(rand, startY, endY),
                        RandomUtils.uniform(rand, RECT_ROOM_MIN_SIZE, RECT_ROOM_MAX_SIZE),
                        RandomUtils.uniform(rand, RECT_ROOM_MIN_SIZE, RECT_ROOM_MAX_SIZE),
                        this.seed);
            }
        }
        return r;
    }

    // Generates the hallway between two rooms along random points.
    private void connectRooms(Room a, Room b) {
        Position randA = a.randomPoint();
        Position randB = b.randomPoint();
        generateHallway(randA, randB);
    }

    // Generate the hallway from point start to point end.
    private void generateHallway(Position start, Position end) {
        double randHall = RandomUtils.uniform(rand);
        HorizontalHallway horHall;
        VerticalHallway verHall;
        // There are two possibilities of halls, either hor then ver, or ver then hor.
        if (randHall > 0.5) {
            horHall = new HorizontalHallway(start.getY(),
                    start.getX(),
                    end.getX(),
                    this.seed);
            verHall = new VerticalHallway(end.getX(),
                    start.getY(),
                    end.getY(),
                    this.seed);

        } else {
            verHall = new VerticalHallway(start.getX(),
                    start.getY(),
                    end.getY(),
                    this.seed);
            horHall = new HorizontalHallway(end.getY(),
                    start.getX(),
                    end.getX(),
                    this.seed);
        }
        horHall.generate(world, Tileset.FLOORS[getTileSet()]);
        verHall.generate(world, Tileset.FLOORS[getTileSet()]);
    }

    // Fills the world array with empty tiles.
    private void fillEmpty() {
        for (int x = 0; x < world.length; x++) {
            for (int y = 0; y < world[0].length; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    // Adds wall tiles to the world surrounding every floor tile.
    // Called after all floor tiles are generated.
    private void addWalls() {
        for (int x = 0; x < world.length; x++) {
            for (int y = 0; y < world[0].length; y++) {
                TETile curTile = world[x][y];
                if (curTile.description().equals("nothing")) {
                    boolean adjacentFloor = false;
                    // Checks all 8 corners to see if there is a surrounding floor tile.
                    if (x - 1 >= 0) {
                        if (world[x - 1][y].description().equals("floor")) {
                            adjacentFloor = true;
                        }
                    }
                    if (x - 1 >= 0 && y + 1 < world[0].length) {
                        if (world[x - 1][y + 1].description().equals("floor")) {
                            adjacentFloor = true;
                        }
                    }
                    if (y + 1 < world[0].length) {
                        if (world[x][y + 1].description().equals("floor")) {
                            adjacentFloor = true;
                        }
                    }
                    if (x + 1 < world.length && y + 1 < world[0].length) {
                        if (world[x + 1][y + 1].description().equals("floor")) {
                            adjacentFloor = true;
                        }
                    }
                    if (x + 1 < world.length) {
                        if (world[x + 1][y].description().equals("floor")) {
                            adjacentFloor = true;
                        }
                    }
                    if (x + 1 < world.length && y - 1 >= 0) {
                        if (world[x + 1][y - 1].description().equals("floor")) {
                            adjacentFloor = true;
                        }
                    }
                    if (y - 1 >= 0) {
                        if (world[x][y - 1].description().equals("floor")) {
                            adjacentFloor = true;
                        }
                    }
                    if (x - 1 >= 0 && y - 1 >= 0) {
                        if (world[x - 1][y - 1].description().equals("floor")) {
                            adjacentFloor = true;
                        }
                    }
                    if (adjacentFloor) {
                        world[x][y] = Tileset.WALLS[getTileSet()];
                    }
                }
            }
        }
    }

    // Checks to see if room r1 overlaps any rooms in roomList.
    // We assume that r1 IS NOT in roomList.
    private boolean overlap(Room r1) {
        List<Position> r1Pos = r1.allPoints();
        for (Room r : roomList) {
            for (Position p : r1Pos) {
                if (r.containsPoint(p)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Increments and returns and integer with given modulo.
    private int incrementModulo(int a, int mod) {
        return (a + 1) % mod;
    }

    /**
     * Getters and setter for various variables.
     * @return Height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Getters and setter for various variables.
     * @return Width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Getters and setter for various variables.
     * @return NumRoom.
     */
    public int getNumRoom() {
        return numRoom;
    }

    /**
     * Getters and setter for various variables.
     * @return World.
     */
    public TETile[][] getWorld() {
        return world;
    }

    /**
     * Getters and setter for various variables.
     * @return RoomList.
     */
    public List<Room> getRoomList() {
        return roomList;
    }

    /**
     * Returns a tile on the specified position.
     * @param pos The specified position.
     * @return The TETile.
     */
    public TETile getTile(Position pos) {
        return world[pos.getX()][pos.getY()];
    }

    /**
     * Sets a tile on the specified position.
     * @param pos The specified position.
     * @param tile The specified tile.
     */
    public void setTile(Position pos, TETile tile) {
        world[pos.getX()][pos.getY()] = tile;
    }

    /**
     * Returns a random position that is guaranteed to be in a room in roomList.
     * @return The position.
     */
    public Position getRandomPositionInRoom() {
        Room randRoom = roomList.get(RandomUtils.uniform(rand, 0, roomList.size()));
        return randRoom.randomPoint();
    }

    /**
     * Reverts the world array to original form.
     */
    public void revertOriginalWorld() {
        for (int x = 0; x < getWidth(); x += 1) {
            for (int y = 0; y < getHeight(); y += 1) {
                world[x][y] = origWorld[x][y];
            }
        }
    }

    /**
     * Returns the tile set integer.
     */
    public int getTileSet() {
        return this.tileSet;
    }

}
