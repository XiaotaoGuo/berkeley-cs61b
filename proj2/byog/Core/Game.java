package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
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
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        World m_World = new World(WIDTH, HEIGHT, 5654);
        int[][] worldArray = m_World.getWorldArray();
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];

        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                int content = worldArray[HEIGHT - 1 - y][x];
                if (content == 0) {
                    finalWorldFrame[x][y] = Tileset.NOTHING;
                } else if (content == 1) {
                    finalWorldFrame[x][y] = Tileset.WALL;
                } else if (content == 2) {
                    finalWorldFrame[x][y] = Tileset.FLOOR;
                } else if (content == 3) {
                    finalWorldFrame[x][y] = Tileset.PLAYER;
                } else if (content == 4) {
                    finalWorldFrame[x][y] = Tileset.LOCKED_DOOR;
                }
            }
        }

        ter.renderFrame(finalWorldFrame);
        return finalWorldFrame;
    }
}
