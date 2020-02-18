package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Font;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.Set;
import java.util.HashSet;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    private static int menuWidth = 50;
    private static int menuHeight = 30;

    private static int hudHeight = 1;

    World mWorld;
    TETile[][] worldFrame;
    Set<Character> motionCommand;

    private long seed = 0;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        //pop up a menu frame
        int option = drawMenu();
        int worldHeight = HEIGHT - hudHeight;
        int worldWidth = WIDTH;
        //initialize a new game setting
        if (option == 1) {
            mWorld = new World(worldWidth, worldHeight, seed);
        } else if (option == 2) {
            mWorld = loadGame();
        }

        int[][] worldArray = mWorld.getWorldArray();
        ter.initialize(WIDTH, HEIGHT, 0, hudHeight);

        worldFrame = new TETile[worldWidth][worldHeight];
        initializeWorldTile(worldArray, worldFrame);
        ter.renderFrame(worldFrame);
        drawHUD("", "",  "World begin!");

        motionCommand = initializeMotionCommand();
        boolean saveOption = playGame("");
        if (saveOption) {
            saveGame();
            System.out.println("Game saved!");

        }


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

        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        int index = 0;


        //ter.initialize(WIDTH, HEIGHT);
        TETile[][] finalWorldFrame = null;
        int worldHeight = HEIGHT - hudHeight;
        int worldWidth = WIDTH;

        if (input.charAt(index) == 'N' || input.charAt(index) == 'n') {
            /* new game */
            index++;
            seed = 0;
            while (index < input.length()
                    && input.charAt(index) <= '9' && input.charAt(index) >= '0') {
                seed *= 10;
                seed += input.charAt(index) - '0';
                index++;
            }

            mWorld = new World(worldWidth, worldHeight, seed);
            /* after seed, it should be s to start the game */
            if (input.charAt(index) != 'S' && input.charAt(index) != 's') {
                System.out.println("Invalid command.\n");
                return worldFrame;
            }
        } else if (input.charAt(index) == 'L' || input.charAt(index) == 'l') {
            /* load game */
            mWorld = loadGame();
            index++;
        } else {
            System.out.println("Invalid command.\n");
            return worldFrame;
        }



        int[][] worldArray = mWorld.getWorldArray();
        worldFrame = new TETile[worldWidth][worldHeight];

        initializeWorldTile(worldArray, worldFrame);

        motionCommand = initializeMotionCommand();

        boolean saveOption = playGame(input.substring(index));
        if (saveOption) {
            saveGame();
            System.out.println("Game saved!");

        }

        //ter.renderFrame(finalWorldFrame);
        return worldFrame;
    }

    private Set<Character> initializeMotionCommand() {
        Set<Character> motioncommand = new HashSet<>();
        motioncommand.add('W');
        motioncommand.add('w');
        motioncommand.add('A');
        motioncommand.add('a');
        motioncommand.add('S');
        motioncommand.add('s');
        motioncommand.add('D');
        motioncommand.add('d');

        return motioncommand;
    }

    private void initializeWorldTile(int[][] worldArray, TETile[][] worldframe) {
        int worldWidth = worldframe.length;
        int worldHeight = worldframe[0].length;
        for (int x = 0; x < worldWidth; x += 1) {
            for (int y = 0; y < worldHeight; y += 1) {
                int content = worldArray[worldHeight - 1 - y][x];
                if (content == 0) {
                    worldframe[x][y] = Tileset.NOTHING;
                } else if (content == 1) {
                    worldframe[x][y] = Tileset.WALL;
                } else if (content == 2) {
                    worldframe[x][y] = Tileset.FLOOR;
                } else if (content == 3) {
                    worldframe[x][y] = Tileset.PLAYER;
                } else if (content == 4) {
                    worldframe[x][y] = Tileset.LOCKED_DOOR;
                }
            }
        }
    }

    private int drawMenu() {

        StdDraw.setCanvasSize(menuWidth * 16, menuHeight * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, menuWidth);
        StdDraw.setYscale(0, menuHeight);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        StdDraw.clear();
        StdDraw.clear(Color.black);

        // Draw the actual text
        Font bigFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(menuWidth / 2, menuHeight / 3 * 2, "CS61B The Game");
        StdDraw.text(menuWidth / 2, menuHeight / 3, "Start(N)");
        StdDraw.text(menuWidth / 2, menuHeight / 3 - 2, "Load(L)");
        StdDraw.text(menuWidth / 2, menuHeight / 3 - 4, "Quit(Q)");
        StdDraw.show();
        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            if (key == 'N' || key == 'n') {
                getSeedInput();
                return 1;
            } else if (key == 'L' || key == 'l') {
                return 2;
            } else if (key == 'Q' || key == 'q') {
                return 0;
            }

        }


    }

    private void drawHUD(String lastmove, String moveStatus, String centerMessage) {
        StdDraw.setPenColor(Color.white);
        StdDraw.textRight(WIDTH / 12, 0.5, "Last input " + lastmove);
        StdDraw.textRight(WIDTH / 5, 0.5, moveStatus);
        StdDraw.text(WIDTH / 2, 0.5, centerMessage);
        int x = Math.round((float) (StdDraw.mouseX() - 0.5));
        int y = Math.round((float) (StdDraw.mouseY() - 0.5) - hudHeight);
        int xInd = x;
        int yInd = HEIGHT - hudHeight - 1 - y;
        String mouseMessage = mWorld.getStatus(xInd, yInd);
        StdDraw.textLeft(WIDTH - 10, 0.5, mouseMessage);
        StdDraw.show();
    }

    private boolean playGame(String stringInput) {
        String lastinput = "";
        String lastStatus = "";
        String lastmessage = "Game begin!";
        int count = 0;
        int index = 0;
        while (stringInput.length() == 0
                || (index < stringInput.length())) {
            char key;
            if (stringInput.length() == 0) {
                if (!StdDraw.hasNextKeyTyped()) {
                    count++;
                    if (count == 10e5) {
                        ter.renderFrame(worldFrame);
                        drawHUD(lastinput, lastStatus, lastmessage);
                        count = 0;
                    }
                    continue;
                }
                key = StdDraw.nextKeyTyped();
            } else {
                key = stringInput.charAt(index);
                index++;
            }

            if (motionCommand.contains(key)) {
                int[] prevPos = mWorld.character.getPosition();
                boolean status = mWorld.moveCharacter(Character.toUpperCase(key));
                int[] currPos = mWorld.character.getPosition();
                if (status) {
                    worldFrame[prevPos[0]][prevPos[1]] = Tileset.FLOOR;
                    worldFrame[currPos[0]][currPos[1]] = Tileset.PLAYER;
                    lastinput = Character.toString(Character.toUpperCase(key));
                    lastStatus = "Successful move!";
                    lastmessage = "Keep going!";
                    if (reachGoal()) {
                        lastStatus = "Successful move!";
                        lastmessage = "Reached the gold door!";
                        if (stringInput.length() == 0) {
                            ter.renderFrame(worldFrame);
                            drawHUD(lastinput, lastStatus, lastmessage);
                        }


                        return false;
                    }

                } else {
                    lastinput = Character.toString(Character.toUpperCase(key));
                    lastStatus = "Invalid move!";
                    lastmessage = "Keep going!";
                }

            } else if (key == ':') {
                lastinput = ":";
                lastStatus = "";
                lastmessage = "Ready to save and quit?";
            } else if (key == 'Q' || key == 'q' && lastinput.equals(":")) {
                lastStatus = "";
                lastmessage = "Game saved, please close the window!";
                if (stringInput.length() == 0) {
                    ter.renderFrame(worldFrame);
                    drawHUD(":Q", lastStatus, lastmessage);
                }


                return true;
            } else {
                lastinput = Character.toString(Character.toUpperCase(key));
                lastStatus = "Invalid command!";
                lastmessage = "Keep going!";
            }
        }

        return false;
    }

    private boolean reachGoal() {
        int[] posCharacter = mWorld.character.getPosition();
        int[] posDoor = mWorld.goldDoor.getPosition();
        return (posCharacter[0] == posDoor[0]
                && posCharacter[1] == posDoor[1]);
    }

    private void getSeedInput() {
        StdDraw.clear();
        StdDraw.clear(Color.black);
        Font bigFont = new Font("Monaco", Font.BOLD, 30);

        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(menuWidth / 2, menuHeight / 2 + 2, "Please enter a seed, press S to finish");
        StdDraw.show();
        seed = 0;
        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            if (key == 'S' || key == 's') {
                this.seed = seed;
                break;
            } else if (key >= '0' && key <= '9') {
                seed *= 10;
                seed += key - '0';
                StdDraw.clear();
                StdDraw.clear(Color.black);
                StdDraw.text(menuWidth / 2, menuHeight / 2 + 2,
                        "Please enter a seed, press S to finish");
                StdDraw.text(menuWidth / 2, menuHeight / 2, Long.toString(seed));
                StdDraw.show();
            } else {
                StdDraw.clear();
                StdDraw.clear(Color.black);
                StdDraw.text(menuWidth / 2, menuHeight / 2 + 2,
                        "Please enter a seed, press S to finish");
                StdDraw.text(menuWidth / 2, menuHeight / 2, Long.toString(seed));
                StdDraw.text(menuWidth / 2, menuHeight / 2 - 2,
                        "Please enter a number between 0 to 9!");
                StdDraw.show();
            }
        }
    }

    private void saveGame() {
        File f = new File("./savefile.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(mWorld);
            os.close();
        }  catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    private World loadGame() {
        File f = new File("./savefile.txt");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                World lWorld = (World) os.readObject();
                os.close();
                return lWorld;
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }

        System.out.println("file not found");
        return null;
    }


}
