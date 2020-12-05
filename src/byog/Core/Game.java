package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.awt.Font;
import java.awt.Color;
import java.util.Random;


public class Game {
    TERenderer te = new TERenderer();
    GameInstance gameInstance;
    String currentTile;
    int currentHealth;
    Random rand;
    int seed;

    /* Feel free to change these parameters. */
    public static final int WIDTH = 60;
    public static final int HEIGHT = 50;
    public static final int MIN_ROOM = 14;
    public static final int MAX_ROOM = 22;
    public static final int CANVAS_WIDTH = 80 * 10;
    public static final int CANVAS_HEIGHT = 50 * 12;
    public static final int MENU_FONT_SIZE = 30;
    public static final int UI_FONT_SIZE = 30;
    public static final Font MENU_FONT = new Font("Monaco", Font.BOLD, MENU_FONT_SIZE);
    public static final Font UI_FONT = new Font("SansSerif", Font.BOLD, UI_FONT_SIZE);
    public static final int STARTING_HEALTH = 50;
    public static final int NUM_LEVELS = 6;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        currentTile = "";
        StdDraw.setCanvasSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        StdDraw.setXscale(0, CANVAS_WIDTH);
        StdDraw.setYscale(0, CANVAS_HEIGHT);
        StdDraw.enableDoubleBuffering();

        // Create main menu.
        createMainMenu();
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        // Checks the first character.
        char firstChar = input.charAt(0);
        input = input.substring(1);

        if (Character.toLowerCase(firstChar) == 'n') {
            // Seed is defaulted at 0. Checks the seed given.
            int typedSeed = 0;
            while (Character.isDigit(input.charAt(0))) {
                typedSeed = typedSeed * 10 + Character.getNumericValue(input.charAt(0));
                input = input.substring(1);
            }
            this.seed = typedSeed;
            this.rand = new Random(this.seed);

            // Checks if S is after seed.
            if (Character.toLowerCase(input.charAt(0)) == 's') {
                World world = new World(WIDTH, HEIGHT, MIN_ROOM, MAX_ROOM, this.seed);
                input = input.substring(1);

                // Check input movement
                String[] arr = inputStringFromKeyboard(input);
                String inputString = arr[0];
                input = arr[1];

                GameInstance gameInst = new GameInstance(world, inputString,
                        this.seed, 1, STARTING_HEALTH, STARTING_HEALTH);
                this.gameInstance = gameInst;

                // Handle quit and save.
                input = handleQuitFromKeyboard(input);

                // UNCOMMENT IN ORDER TO DISPLAY THE WORLD
                //te.initialize(WIDTH, HEIGHT);
                //te.renderFrame(this.gameInstance.getWorld().getWorld());

                return this.gameInstance.getWorld().getWorld();

            }

        } else if (Character.toLowerCase(firstChar) == 'l') {
            this.gameInstance = deserializeGame();
            this.seed = gameInstance.getSeed();
            this.rand = new Random(this.seed);

            // Exit this function if the load does not actually load anything.
            if (this.gameInstance == null) {
                return null;
            }

            // Check input movement
            String[] arr = inputStringFromKeyboard(input);
            String inputString = arr[0];
            input = arr[1];

            this.gameInstance.movePlayerInputString(inputString);

            // Handle quit and save.
            input = handleQuitFromKeyboard(input);

            // UNCOMMENT IN ORDER TO DISPLAY THE WORLD
            //te.initialize(WIDTH, HEIGHT);
            //te.renderFrame(this.gameInstance.getWorld().getWorld());

            return this.gameInstance.getWorld().getWorld();

        } else if (Character.toLowerCase(firstChar) == 'q') {
            // If we quit, we just return nothing.
            return null;
        }

        return null;
    }

    // Takes an input string and returns the wasd movements and the
    // newly spliced input string in an array.
    // The 0th element is the wasd string and the 1st element is the input.
    private String[] inputStringFromKeyboard(String input) {
        // Check input movement
        String inputString = "";
        while (input.length() >= 1) {
            if (Character.toLowerCase(input.charAt(0)) == 'w'
                    || Character.toLowerCase(input.charAt(0)) == 'a'
                    || Character.toLowerCase(input.charAt(0)) == 's'
                    || Character.toLowerCase(input.charAt(0)) == 'd') {
                inputString = inputString + Character.toLowerCase(input.charAt(0));
                input = input.substring(1);
            } else {
                break;
            }
        }
        String[] arr = new String[2];
        arr[0] = inputString;
        arr[1] = input;
        return arr;
    }

    // Handles :q from the playWithInputString function.
    private String handleQuitFromKeyboard(String input) {
        // Handle quitting and saving.
        if (input.length() >= 1) {
            if (input.charAt(0) == ':') {
                input = input.substring(1);
                if (input.length() == 1) {
                    if (Character.toLowerCase(input.charAt(0)) == 'q') {
                        serializeGame();
                        input = input.substring(1);
                    }
                }
            }
        }
        return input;
    }

    // Uses STDDraw to draw a main menu on the screen.
    private void createMainMenu() {
        StdDraw.clear(Color.black);
        StdDraw.setPenColor(Color.white);
        StdDraw.setFont(MENU_FONT);
        // Displays the text
        StdDraw.text(CANVAS_WIDTH / 2, CANVAS_HEIGHT * 3 / 4, "Pokemon: Pikachu's Berry Quest");
        StdDraw.text(CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2 + MENU_FONT_SIZE + 5, "New Game (N)");
        StdDraw.text(CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2, "Load Game (L)");
        StdDraw.text(CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2 - MENU_FONT_SIZE - 5, "Quit Game (Q)");

        StdDraw.text(CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2 - MENU_FONT_SIZE - 30
                        - 2 * UI_FONT_SIZE,
                "Eat Berries!");
        StdDraw.text(CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2 - MENU_FONT_SIZE - 30
                        - 3 * UI_FONT_SIZE,
                "Avoid Wild Pokemons!");
        StdDraw.text(CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2 - MENU_FONT_SIZE - 30
                        - 4 * UI_FONT_SIZE,
                "Beat Level " + NUM_LEVELS + " to Win Game!");

        StdDraw.show();

        // Handle key input.
        handleMainMenuInput();
    }

    // Handles key presses during the seed selection screen.
    // Checks to see if this is the first time calling this function.
    private void createEnterSeedScreen(String typedSeed, boolean firstTimeCalling) {
        StdDraw.clear(Color.black);
        StdDraw.setPenColor(Color.white);
        StdDraw.setFont(MENU_FONT);
        // Displays the text
        StdDraw.text(CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2 + MENU_FONT_SIZE + 5, "Enter the Seed:");
        StdDraw.text(CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2, typedSeed);
        StdDraw.text(CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2 - MENU_FONT_SIZE - 5, "Start Game (S)");
        StdDraw.show();

        if  (firstTimeCalling) {
            handleSeedInput();
        }
    }

    // Handles key presses during the main menu. Possible keys: NLQ
    private void handleMainMenuInput() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char in = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (in == 'n') {
                    createEnterSeedScreen("", true);
                    break;
                } else if (in == 'l') {
                    this.gameInstance = deserializeGame();
                    this.seed = gameInstance.getSeed();
                    rand = new Random(this.seed);
                    if (this.gameInstance != null) {
                        te.initialize(WIDTH, HEIGHT);
                        createUpdateGameScreen("");
                        handlePlayerInput();
                    }
                    break;
                } else if (in == 'q') {
                    break;
                }
            }
        }
    }

    // Handles the seed input. Possible keys: Numbers and S
    private void handleSeedInput() {
        String typedSeed = "";
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char in = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (Character.isDigit(in)) {
                    typedSeed += in;
                    createEnterSeedScreen(typedSeed, false);
                } else if (in == 's') {
                    // If nothing is inputted, default seed is 0.
                    if (typedSeed.equals("")) {
                        typedSeed = "0";
                    }
                    this.seed = Integer.parseInt(typedSeed);
                    this.rand = new Random(this.seed);
                    World world = new World(WIDTH, HEIGHT, MIN_ROOM, MAX_ROOM,
                            this.seed);
                    GameInstance gameInst = new GameInstance(world, "",
                            this.seed, 1, STARTING_HEALTH, STARTING_HEALTH);
                    this.gameInstance = gameInst;
                    te.initialize(WIDTH, HEIGHT);
                    createUpdateGameScreen("");
                    handlePlayerInput();
                    break;
                }
            }
        }
    }

    // Renders and creates the main game screen and UI for the first time.
    private void createUpdateGameScreen(String currentMouseTile) {

        // Renders the world.
        te.renderFrame(this.gameInstance.getWorld().getWorld());

        // Draws the current hovered over tile.
        StdDraw.setFont(UI_FONT);
        StdDraw.setPenColor(Color.white);
        // Depending on if the string is one or two words, draw the UI differently.
        // This is to prevent long strings from exiting the bounds of the game.
        if (currentMouseTile.contains(" ")) {
            StdDraw.text(5, HEIGHT - 2, currentMouseTile.substring(0,
                    currentMouseTile.indexOf(' ')));
            StdDraw.text(5, HEIGHT - 4, currentMouseTile.substring(currentMouseTile.indexOf(' '),
                    currentMouseTile.length()));
        } else {
            StdDraw.text(5, HEIGHT - 2, currentMouseTile);
        }

        // Draws the current level.
        StdDraw.text(WIDTH - 6, HEIGHT - 2, "Level: " + gameInstance.getLevel());
        switch (gameInstance.getWorld().getTileSet()) {
            case 0 :
                StdDraw.text(WIDTH - 6, HEIGHT - 4, "Desert");
                break;
            case 1:
                StdDraw.text(WIDTH - 6, HEIGHT - 4, "Snow");
                break;
            case 2:
                StdDraw.text(WIDTH - 6, HEIGHT - 4, "Grassland");
                break;
            default:
                break;
        }

        // Draws health.
        if (this.currentHealth != gameInstance.getPlayer().getHealth()) {
            this.currentHealth = gameInstance.getPlayer().getHealth();
        }
        StdDraw.text(WIDTH / 2, 5, "Health: " + this.currentHealth);


        StdDraw.show();
    }

    // Handles the player movements. Possible keys: WASD
    // Also handles mouse and tile UI.
    private void handlePlayerInput() {
        boolean endGame = false;
        // Stores the current tile the mouse is on.
        while (!endGame) {
/*              System.out.println("WORLD WIDTH: " + gameInstance.getWorld().getWorld().length);
            System.out.println("WORLD HEIGHT: " + gameInstance.getWorld().getWorld()[0].length);

            System.out.println("x: " + (int) Math.floor(StdDraw.mouseX()));
            System.out.println("y: " + (int) Math.floor(StdDraw.mouseY()));*/
            if (StdDraw.hasNextKeyTyped()) {
                char in = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (in == 'w') {
                    this.gameInstance.movePlayerUp();
                } else if (in == 'a') {
                    this.gameInstance.movePlayerLeft();
                } else if (in == 's') {
                    this.gameInstance.movePlayerDown();
                } else if (in == 'd') {
                    this.gameInstance.movePlayerRight();
                } else if (in == ':') {
                    while (true) {
                        addPauseUI();
                        if (StdDraw.hasNextKeyTyped()) {
                            if (Character.toLowerCase(StdDraw.nextKeyTyped()) == 'q') {
                                endGame = true;
                                serializeGame();
                            }
                            break;
                        }
                    }
                }
                createUpdateGameScreen(currentTile);
            }

            // Checks if game is lost.
            if (this.gameInstance.gameLost()) {
                endGame = true;
                createGameOver();
                break;
            }

            // Check nextlevel status.
            if (gameInstance.playerReachExit()) {
                System.out.println("Proceed to next level.");
                if (this.gameInstance.getLevel() == NUM_LEVELS) {
                    createGameWon();
                } else {
                    createNextLevel();
                }
                break;
            }

            // Checks what tile the mosue is currently hovering over and shows it in the UI.
            int mouseX = (int) Math.floor(StdDraw.mouseX());
            int mouseY = (int) Math.floor(StdDraw.mouseY());
            if (mouseX >= 0 && mouseX < gameInstance.getWorld().getWidth()
                    && mouseY >= 0 && mouseY < gameInstance.getWorld().getHeight()) {
                String nextTile = gameInstance.getWorld().
                        getTile(new Position(mouseX, mouseY)).description();
                if (!nextTile.equals(currentTile)) {
                    currentTile = nextTile;
                    createUpdateGameScreen(currentTile);
                }
            }
        }
    }

    // Creates the next level.
    private void createNextLevel() {
        int nextSeed = RandomUtils.uniform(rand, 0, Integer.MAX_VALUE / 2);
        this.seed = nextSeed;
        this.rand = new Random(this.seed);
        World world = new World(WIDTH, HEIGHT, MIN_ROOM, MAX_ROOM,
                this.seed);
        GameInstance gameInst = new GameInstance(world, "",
                this.seed, this.gameInstance.getLevel() + 1,
                gameInstance.getPlayer().getHealth(), STARTING_HEALTH);
        this.gameInstance = gameInst;
        createUpdateGameScreen("");
        handlePlayerInput();
    }

    private void addPauseUI() {
        // Adds a pause icon to the top.
        StdDraw.setFont(UI_FONT);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2, HEIGHT - 2, "Paused");
        StdDraw.text(WIDTH / 2, HEIGHT - 4, "Press Q to Quit");
        StdDraw.text(WIDTH / 2, HEIGHT - 6, "Press Anything Else to Unpause");
        StdDraw.show();
    }

    // Creates game over screen.
    private void createGameOver() {
        StdDraw.clear(Color.black);
        StdDraw.setFont(UI_FONT);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 5,
                "Pikachu fainted :( ");
        StdDraw.text(WIDTH / 2, HEIGHT / 2,
                "Game Over");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 5,
                "Press any key to exit");
        StdDraw.show();

        // Waits for the player to input a key.
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                break;
            }
        }
    }

    // Creates game over screen.
    private void createGameWon() {
        StdDraw.clear(Color.black);
        StdDraw.setFont(UI_FONT);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 5,
                "Pikachu is satisfied!");
        StdDraw.text(WIDTH / 2, HEIGHT / 2,
                "You beat the game!");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 5,
                "Press any key to exit");
        StdDraw.show();

        // Waits for the player to input a key.
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                break;
            }
        }
    }



    // Saves the current gameInstance object into another file.
    // @Source https://www.tutorialspoint.com/java/java_serialization.html
    private void serializeGame() {
        try {
            FileOutputStream fileOut =
                    new FileOutputStream("./savedGame.txt");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this.gameInstance);
            out.close();
            fileOut.close();
            System.out.println("Game Object is saved in ./savedGame.txt");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    // Loads the game from a given .txt file.
    // Returns the gameInstance object loaded or null if nothing gets loaded.
    // @Source https://www.tutorialspoint.com/java/java_serialization.html
    private GameInstance deserializeGame() {
        GameInstance e = null;
        try {
            FileInputStream fileIn = new FileInputStream("./savedGame.txt");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            e = (GameInstance) in.readObject();
            System.out.println("Game Object is loaded from ./savedGame.txt");
            in.close();
            fileIn.close();
            return e;
        } catch (IOException i) {
            System.out.println("Could not find save file.");
            return null;
        } catch (ClassNotFoundException c) {
            System.out.println("GameInstance class not found.");
            c.printStackTrace();
            return null;
        }
    }

    // TEST
    public static void main(String[] args) {
        Game g = new Game();
        //g.playWithKeyboard();
        g.playWithInputString("n25s:q");
        g.playWithInputString("lddd:q");
        //g.playWithInputString("q");
        //g.playWithInputString("ldDDdd:Q");
        //g.playWithInputString("LwwW:Q");
        //g.playWithInputString("lddd");
    }


}
