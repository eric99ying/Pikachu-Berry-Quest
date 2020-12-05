package byog.Core;

import byog.TileEngine.TETile;
import java.util.List;

public interface Room {
    int getX();
    // X position of room.
    int getY();
    // Y position of room.
    void generate(TETile[][] world, TETile tile);
    // Adds the room into the world array
    // using the specified tile.                    .
    Position randomPoint();
    // Return a random position within the room.
    // Used in connecting rooms together using hallways.
    boolean inBound(TETile[][] world);
    // Checks to see if the room can be placed in the world array.
    boolean containsPoint(Position p);
    // Checks to see if a point is in the room.
    List<Position> allPoints();
    // Returns a list of all points in the room.
    int getHeight();
    // Returns the height of the room.
    int getWidth();
    // Returns the width of the room.


}
