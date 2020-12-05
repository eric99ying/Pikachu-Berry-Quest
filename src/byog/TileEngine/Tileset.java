package byog.TileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final TETile PLAYER = new TETile('@', Color.white, Color.black,
            "player", "./tileImages/player.png");

    // WALLS
    public static final TETile WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray,
            "wall");
    public static final TETile WALL_0 = new TETile('#', new Color(216, 128, 128), Color.darkGray,
            "wall", "./tileImages/wall_0.png");
    public static final TETile WALL_1 = new TETile('#', new Color(216, 128, 128), Color.darkGray,
            "wall", "./tileImages/wall_1.png");
    public static final TETile WALL_2 = new TETile('#', new Color(216, 128, 128), Color.darkGray,
            "wall", "./tileImages/wall_2.png");

    public static final TETile[] WALLS = new TETile[] {WALL_0, WALL_1, WALL_2};

    // FLOORS
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor");
    public static final TETile FLOOR_0 = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor", "./tileImages/floor_0.png");
    public static final TETile FLOOR_1 = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor", "./tileImages/floor_1.png");
    public static final TETile FLOOR_2 = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor", "./tileImages/floor_2.png");

    public static final TETile[] FLOORS = new TETile[] {FLOOR_0, FLOOR_1, FLOOR_2};

    // LOCKED DOORS
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile LOCKED_DOOR_0 = new TETile('█', Color.orange, Color.black,
            "locked door", "./tileImages/locked_door_0.png");
    public static final TETile LOCKED_DOOR_1 = new TETile('█', Color.orange, Color.black,
            "locked door", "./tileImages/locked_door_1.png");
    public static final TETile LOCKED_DOOR_2 = new TETile('█', Color.orange, Color.black,
            "locked door", "./tileImages/locked_door_2.png");

    public static final TETile[] LOCKED_DOORS =
            new TETile[] {LOCKED_DOOR_0, LOCKED_DOOR_1, LOCKED_DOOR_2};


    // UNLOCKED DOORS
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile UNLOCKED_DOOR_0 = new TETile('▢', Color.orange, Color.black,
            "unlocked door", "./tileImages/unlocked_door_0.png");
    public static final TETile UNLOCKED_DOOR_1 = new TETile('▢', Color.orange, Color.black,
            "unlocked door", "./tileImages/unlocked_door_1.png");
    public static final TETile UNLOCKED_DOOR_2 = new TETile('▢', Color.orange, Color.black,
            "unlocked door", "./tileImages/unlocked_door_2.png");

    public static final TETile[] UNLOCKED_DOORS =
            new TETile[] {UNLOCKED_DOOR_0, UNLOCKED_DOOR_1, UNLOCKED_DOOR_2};


    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");

    public static final TETile BERRY = new TETile('❀', Color.magenta, Color.pink, "berry",
            "./tileImages/berry.png");

    public static final TETile ENEMY_0 = new TETile('#', Color.red, Color.black, "wild pokemon",
            "./tileImages/enemy_0.png");
    public static final TETile ENEMY_1 = new TETile('#', Color.red, Color.black, "wild pokemon",
            "./tileImages/enemy_1.png");
    public static final TETile ENEMY_2 = new TETile('#', Color.red, Color.black, "wild pokemon",
            "./tileImages/enemy_2.png");
    public static final TETile[] ENEMIES = {ENEMY_0, ENEMY_1, ENEMY_2};

}


