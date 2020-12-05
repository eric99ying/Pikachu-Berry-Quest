package byog.Core;

import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class GameInstance implements java.io.Serializable {

    // Entities in the world.
    private Player player;
    private List<Berry> berries;
    private List<Enemy> enemies;
    private Exit exit;
    private World world;

    private Random rand;
    private int seed;
    private int level;
    private int timeElapse;
    private int playerStartingHealth;
    private int playerMaxHealth;
    private boolean gameLost;

    /**
     * Creates an instance of the game. This object keeps track of the state of
     * the current game, including the position of the player and other variables.
     * On contruction, "spawns" all of the objects in the game.
     * @param world The world.
     * @param inputMovement The starting input movements of the player.
     *                      (ex. "wwssdd")
     * @param seed The seed of the game.
     * @param level The current integer level of this game instance.
     * @param playerHealth The current health of the player.
     * @param maxHealth The max health of the player.
     */
    public GameInstance(World world, String inputMovement, int seed, int level,
                        int playerHealth, int maxHealth) {
        this.level = level;
        this.world = world;
        this.rand = new Random(seed);
        this.playerStartingHealth = playerHealth;
        this.playerMaxHealth = maxHealth;
        this.berries = new ArrayList<Berry>();
        this.enemies = new ArrayList<Enemy>();
        spawnPlayer();
        spawnExit();
        spawnBerries();
        spawnEnemies();
        movePlayerInputString(inputMovement);
        generateGameInstance();
        this.seed = seed;
        this.timeElapse = 0;
        this.gameLost = false;
    }

    /**
     *  Moves the player based on the string of wasd characters.
     *  INVARIANT: The input string ONLY contains wasd characters.
     *  @param input The input string.
     */
    public void movePlayerInputString(String input) {
        for (int i = 0; i < input.length(); i += 1) {
            char direction = input.charAt(i);
            switch (Character.toLowerCase(direction)) {
                case 'w':
                    movePlayerUp();
                    break;
                case 'a':
                    movePlayerLeft();
                    break;
                case 's':
                    movePlayerDown();
                    break;
                case 'd':
                    movePlayerRight();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Moves the player up.
     * INVARIANT: The player will never go out of bounds due to walls.
     */
    public void movePlayerUp() {
        // If tile is floor, move. Otherwise, do nothing.
        if (!world.getTile(new Position(player.getPos().getX(),
                player.getPos().getY() + 1)).description().equals("wall")) {
            player.setPos(new Position(player.getPos().getX(), player.getPos().getY() + 1));
            generateGameInstance();

        }
    }
    /**
     * Moves the player left.
     * INVARIANT: The player will never go out of bounds due to walls.
     */
    public void movePlayerLeft() {
        // If tile is floor, move. Otherwise, do nothing.
        if (!world.getTile(new Position(player.getPos().getX() - 1,
                player.getPos().getY())).description().equals("wall")) {
            player.setPos(new Position(player.getPos().getX() - 1, player.getPos().getY()));
            generateGameInstance();

        }
    }
    /**
     * Moves the player down.
     * INVARIANT: The player will never go out of bounds due to walls.
     */
    public void movePlayerDown() {
        // If tile is floor, move. Otherwise, do nothing.
        if (!world.getTile(new Position(player.getPos().getX(),
                player.getPos().getY() - 1)).description().equals("wall")) {
            player.setPos(new Position(player.getPos().getX(), player.getPos().getY() - 1));
            generateGameInstance();

        }
    }
    /**
     * Moves the player right.
     * INVARIANT: The player will never go out of bounds due to walls.
     */
    public void movePlayerRight() {
        // If tile is floor, move. Otherwise, do nothing.
        if (!world.getTile(new Position(player.getPos().getX() + 1,
                player.getPos().getY())).description().equals("wall")) {
            player.setPos(new Position(player.getPos().getX() + 1, player.getPos().getY()));
            generateGameInstance();

        }
    }

    // Moves every enemy in a random direction.
    private void enemiesMove() {
        for (Enemy e : this.enemies) {
            boolean moved = false;
            while (!moved) {
                int dir;
                if (e.getDir() == -1) {
                    dir = RandomUtils.uniform(rand, 0, 4);
                } else if (e.getDir() == 0) {
                    dir = RandomUtils.discrete(rand, new double[]{0.35, 0.3, 0.05, 0.3});
                } else if (e.getDir() == 1) {
                    dir = RandomUtils.discrete(rand, new double[]{0.3, 0.35, 0.3, 0.05});
                } else if (e.getDir() == 2) {
                    dir = RandomUtils.discrete(rand, new double[]{0.05, 0.3, 0.35, 0.3});
                } else {
                    dir = RandomUtils.discrete(rand, new double[]{0.3, 0.05, 0.3, 0.35});
                }
                switch (dir) {
                    case 0:
                        // If tile is floor, move. Otherwise, do nothing.
                        if (!world.getTile(new Position(e.getPos().getX(),
                                e.getPos().getY() + 1)).description().equals("wall")) {
                            e.setPos(new Position(e.getPos().getX(), e.getPos().getY() + 1));
                            moved = true;
                            e.setDir(dir);
                        }
                        break;
                    case 1:
                        // If tile is floor, move. Otherwise, do nothing.
                        if (!world.getTile(new Position(e.getPos().getX() - 1,
                                e.getPos().getY())).description().equals("wall")) {
                            e.setPos(new Position(e.getPos().getX() - 1, e.getPos().getY()));
                            moved = true;
                            e.setDir(dir);
                        }
                        break;
                    case 2:
                        // If tile is floor, move. Otherwise, do nothing.
                        if (!world.getTile(new Position(e.getPos().getX(),
                                e.getPos().getY() - 1)).description().equals("wall")) {
                            e.setPos(new Position(e.getPos().getX(), e.getPos().getY() - 1));
                            moved = true;
                            e.setDir(dir);
                        }
                        break;
                    case 3:
                        // If tile is floor, move. Otherwise, do nothing.
                        if (!world.getTile(new Position(e.getPos().getX() + 1,
                                e.getPos().getY())).description().equals("wall")) {
                            e.setPos(new Position(e.getPos().getX() + 1, e.getPos().getY()));
                            moved = true;
                            e.setDir(dir);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    // Spawns the player at a random location in the world.
    // Spawns the player in a random position in a room.
    private void spawnPlayer() {
        Position startingPos = world.getRandomPositionInRoom();
        this.player = new Player(startingPos, this.playerStartingHealth,
                this.playerMaxHealth);
    }

    // Spawns the exit at a random location in the world.
    // Spawns the exit in a random position in a room.
    // Requires key to unlock.
    private void spawnExit() {
        Position randPos = world.getRandomPositionInRoom();
        while (this.player != null && randPos.equals(this.player.getPos())) {
            randPos = world.getRandomPositionInRoom();
        }
        this.exit = new Exit(randPos);
    }

    // Spawns berries in random rooms in the world.
    // Collect berries to satisfy your pokemon.
    private void spawnBerries() {
        int numBerries = RandomUtils.uniform(rand, 3, 5);
        List<Room> rooms = new ArrayList<Room>(getWorld().getRoomList());
        for (int i = 0; i < numBerries; i += 1) {
            int randRoom = RandomUtils.uniform(rand, 0, rooms.size());
            Room room = rooms.get(randRoom);
            Berry berry = new Berry(room.randomPoint());
            while ((this.player != null
                    && berry.getPos().equals(this.player.getPos()))
                    || (this.exit != null
                    && berry.getPos().equals(this.exit.getPos()))) {
                berry = new Berry(room.randomPoint());

            }
            rooms.remove(room);
            berries.add(berry);
        }
    }

    // Spawns enemies in random rooms in the world.
    private void spawnEnemies() {
        int numEnemies = RandomUtils.uniform(rand, 4, 6);
        List<Room> rooms = new ArrayList<Room>(getWorld().getRoomList());
        for (int i = 0; i < numEnemies; i += 1) {
            int randRoom = RandomUtils.uniform(rand, 0, rooms.size());
            Room room = rooms.get(randRoom);
            Enemy enemy = new Enemy(room.randomPoint());
            while (this.player != null
                && enemy.getPos().equals(this.player.getPos())
                || (this.exit != null
                && enemy.getPos().equals(this.exit.getPos()))) {
                enemy = new Enemy(room.randomPoint());
            }

            rooms.remove(room);
            enemies.add(enemy);
        }
    }



    /**
     * First reverts the world. Then we set different tiles to the
     * player, keys, etc. MAKE SURE TO CALL THIS METHOD EVERYTIME
     * WE CHANGE THE GAME IN SOME WAY(IE. move the player).
     * Also handles the player health interactions.
     */
    public void generateGameInstance() {
        // If player is on an enemy, lose health.
        if (playeronEnemy()) {
            this.player.loseHealth(10);
        }

        // First move all of the enemies at once.
        enemiesMove();

        // Decreases the player's health every few turns.
        if (this.timeElapse % 2 == 0) {
            this.player.loseHealth(1);
        }

        // If player is on an enemy, lose health.
        if (playeronEnemy()) {
            this.player.loseHealth(15);
        }

        // Increase the time elapsed in the current game instance.
        this.timeElapse += 1;

        // If player is on top of a berry, the player eats it and gains health.
        if (playeronBerry()) {
            this.player.gainHealth(10);
        }

        // If player health is 0, lose game.
        if (player.getHealth() == 0) {
            this.gameLost = true;
        }

        // Reverts the world to the original world first.
        getWorld().revertOriginalWorld();

        // ADD THE EXITS AND OTHER ITEMS FIRST, THEN ADD THE PLAYER.

        // Add the exit, depending on if locked or unlocked.
        if (this.exit != null) {
            if (exit.getLockedStatus()) {
                getWorld().setTile(exit.getPos(),
                        Tileset.LOCKED_DOORS[getWorld().getTileSet()]);
            } else {
                getWorld().setTile(exit.getPos(),
                        Tileset.UNLOCKED_DOORS[getWorld().getTileSet()]);
            }
        }

        // Add berries.
        if (this.berries != null) {
            for (Berry b : berries) {
                getWorld().setTile(b.getPos(), Tileset.BERRY);
            }
        }

        // Add enemies.
        if (this.enemies != null) {
            for (Enemy e : this.enemies) {
                getWorld().setTile(e.getPos(), Tileset.ENEMIES[getWorld().getTileSet()]);
            }
        }

        // Add player.
        if (this.player != null) {
            getWorld().setTile(player.getPos(), Tileset.PLAYER);
        }

    }

    /**
     * Checks if the player reached an UNLOCKED exit and proceeds to the next level.
     * @return The boolean whether the player reached the exit.
     */
    public boolean playerReachExit() {
        if (player.getPos().equals(exit.getPos())) {
            if (!exit.getLockedStatus()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the player is on top of a berry.
     * Removes the berry in the process.
     * @return The boolean whether the player is on top of a berry.
     */
    public boolean playeronBerry() {
        for (Berry b : this.berries) {
            if (player.getPos().equals(b.getPos())) {
                this.berries.remove(b);
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the player is on top of a enemy.
     * Removes the enemy in the process.
     * @return The boolean whether the player is on top of a enemy.
     */
    public boolean playeronEnemy() {
        for (Enemy e : this.enemies) {
            if (player.getPos().equals(e.getPos())) {
                this.enemies.remove(e);
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the game world in a 2D array.
     * @return The World object.
     */
    public World getWorld() {
        return world;
    }

    /**
     * Returns the game instance seed.
     * @return Integer seed.
     */
    public int getSeed() {
        return this.seed;
    }

    /**
     * Returns the game instance level.
     * @return Integer level.
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * Returns the player.
     * @return Player player.
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Returns if the game is lost or not.
     * @return Boolean value.
     */
    public boolean gameLost() {
        return this.gameLost;
    }

}
