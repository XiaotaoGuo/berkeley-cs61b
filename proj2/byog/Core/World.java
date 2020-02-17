package byog.Core;

import java.util.ArrayList;
import java.util.Random;

public class World {

    protected int xSize;
    protected int ySize;
    protected int roomWidthLowerBound;
    protected int roomWidthUpperBound;
    protected int roomHeightLowerBound;
    protected int roomHeightUpperBound;
    protected int totalRoomCount;
    protected Position character;
    protected Position goldDoor;
    private int[][] worldArray;
    private ArrayList<Room> roomList;
    private ArrayList<Hallway> hallwayList;
    private static long SEED;
    private static Random RANDOM;

    public World(int x, int y, long seed) {
        xSize = x;
        ySize = y;
        SEED = seed;
        RANDOM = new Random(SEED);
        totalRoomCount = (int) (x * y / 2500.0 * 25);

        roomWidthLowerBound = Math.max(x / 12, 5);
        roomWidthUpperBound = 2 * roomWidthLowerBound;
        roomHeightLowerBound = Math.max(y / 12, 5);
        roomHeightUpperBound = 2 * roomHeightLowerBound;

        worldArray = new int[y][x];
        for (int i = 0; i < ySize; i++) {
            for (int j = 0; j < xSize; j++) {
                worldArray[i][j] = 0;
            }
        }
        roomList = new ArrayList<>();
        hallwayList = new ArrayList<>();

        fillWorld();
        putCharacterAndDoor();
        printWorld();
        System.out.println("Initialized an empty world with width : "
                            + xSize + ", height: " + ySize + ".");
    }

    public World(String path) {

    }

    public void fillWorld() {
        while (roomList.size() < totalRoomCount) {
            if (roomList.size() == 0) {
                int x = generateRandom((int) (0.4 * xSize), (int) (0.6 * xSize));
                int y = generateRandom((int) (0.4 * ySize), (int) (0.6 * ySize));
                int width = generateRandom(roomWidthLowerBound, roomWidthUpperBound);
                int height = generateRandom(roomHeightLowerBound, roomHeightUpperBound);
                Position center = new Position(x, y);
                Room firstRoom = new Room(center, height, width);
                updateRoomToWorld(firstRoom);
            } else {
                Room currRoom, nextRoom;
                int dir;
                boolean buildHallway = false;
                while (true) {
                    int choice = generateRandom(0, roomList.size());
                    currRoom = roomList.get(choice);
                    dir = generateRandom(0, 4);
                    buildHallway = generateRandom(0, 5) == 0;
                    nextRoom = createRoom(currRoom, dir, buildHallway);

                    if (checkRoom(nextRoom)) {
                        break;
                    }
                }
                updateRoomToWorld(nextRoom);
                Hallway hallway = createHallway(currRoom, nextRoom, dir);
                updateHallwayToWorld(hallway);
                //printWorld();
            }
        }
    }

    /* Add a new room to the room lists and update the world array for visualization */
    private void updateRoomToWorld(Room newRoom) {
        roomList.add(newRoom);

        Position leftBottom = newRoom.leftBottom;
        Position rightTop = newRoom.rightTop;

        for (int i = rightTop.yInd; i <= leftBottom.yInd; i++) {
            for (int j = leftBottom.xInd; j <= rightTop.xInd; j++) {
                worldArray[i][j] = 2;
            }
        }

        for (int i = leftBottom.xInd; i < rightTop.xInd; i++) {
            worldArray[rightTop.yInd][i] = 1;
        }

        for (int i = rightTop.yInd; i < leftBottom.yInd; i++) {
            worldArray[i][rightTop.xInd] = 1;
        }

        for (int i = rightTop.xInd; i > leftBottom.xInd; i--) {
            worldArray[leftBottom.yInd][i] = 1;
        }

        for (int i = leftBottom.yInd; i > rightTop.yInd; i--) {
            worldArray[i][leftBottom.xInd] = 1;
        }
    }

    /* Add a new room to the room lists and update the world array for visualization */
    private void updateHallwayToWorld(Hallway hallway) {
        hallwayList.add(hallway);

        Position leftBottom = hallway.leftBottom;
        Position rightTop = hallway.rightTop;
        int dir = hallway.dir;

        for (int i = rightTop.yInd; i <= leftBottom.yInd; i++) {
            for (int j = leftBottom.xInd; j <= rightTop.xInd; j++) {
                worldArray[i][j] = 1;
            }
        }

        /* if the hallway is vertical */
        if (dir == 0 || dir == 2) {
            for (int i = rightTop.yInd; i <= leftBottom.yInd; i++) {
                worldArray[i][leftBottom.xInd + 1] = 2;
            }
        } else {
            for (int i = leftBottom.xInd; i <= rightTop.xInd; i++) {
                worldArray[leftBottom.yInd - 1][i] = 2;
            }
        }
    }

    /* Create a new Room randomly */
    public Room createRoom(Room currRoom, int dir, boolean buildHallway) {
        int distance = generateRandom(2, 5);
        int width = generateRandom(roomWidthLowerBound, roomWidthUpperBound);
        int height = generateRandom(roomHeightLowerBound, roomHeightUpperBound);

        /* if build a hallway set width or height to 3 to simulate a hallway */
        if (buildHallway) {
            if (dir == 0 || dir == 2) {
                height = 3;
            } else {
                width = 3;
            }
        }

        Position nextCenter = new Position(currRoom.center);
        if (dir == 0) {
            nextCenter.x = generateRandom(currRoom.leftBottom.x, currRoom.rightTop.x + 1);
            nextCenter.y += (currRoom.height / 2 + distance + height / 2);
        } else if (dir == 1) {
            nextCenter.y = generateRandom(currRoom.leftBottom.y, currRoom.rightTop.y + 1);
            nextCenter.x += (currRoom.width / 2 + distance + width / 2);
        } else if (dir == 2) {
            nextCenter.x = generateRandom(currRoom.leftBottom.x, currRoom.rightTop.x + 1);
            nextCenter.y -= (currRoom.height / 2 + distance + height / 2);
        } else if (dir == 3) {
            nextCenter.y = generateRandom(currRoom.leftBottom.y, currRoom.rightTop.y + 1);
            nextCenter.x -= (currRoom.width / 2 + distance + width / 2);
        }

        Room newRoom = new Room(nextCenter, height, width);
        return newRoom;
    }

    /* Create a hallway between two rooms */
    private Hallway createHallway(Room roomA, Room roomB, int dir) {

        int x1, y1, x2, y2;

        int lowerBoundX = Math.max(roomA.leftBottom.x, roomB.leftBottom.x);
        int upperBoundX = Math.min(roomA.rightTop.x, roomB.rightTop.x);
        int lowerBoundY = Math.max(roomA.leftBottom.y, roomB.leftBottom.y);
        int upperBoundY = Math.min(roomA.rightTop.y, roomB.rightTop.y);

        if (dir == 0) {

            x1 = generateRandom(lowerBoundX, upperBoundX - 1);
            y1 = roomA.rightTop.y;
            x2 = x1 + 2;
            y2 = roomB.leftBottom.y;

        } else if (dir == 1) {

            x1 = roomA.rightTop.x;
            y1 = generateRandom(lowerBoundY, upperBoundY - 1);
            x2 = roomB.leftBottom.x;
            y2 = y1 + 2;

        } else if (dir == 2) {

            x1 = generateRandom(lowerBoundX, upperBoundX - 1);
            y1 = roomB.rightTop.y;
            x2 = x1 + 2;
            y2 = roomA.leftBottom.y;

        } else {

            x1 = roomB.rightTop.x;
            y1 = generateRandom(lowerBoundY, upperBoundY - 1);
            x2 = roomA.leftBottom.x;
            y2 = y1 + 2;

        }
        Position leftBottom = new Position(x1, y1);
        Position rightTop = new Position(x2, y2);

        Hallway hallway = new Hallway(leftBottom, rightTop, dir);
        return hallway;
    }

    private class Room {
        private Position leftBottom;
        private Position rightTop;
        private Position center;
        private int width;
        private int height;

        Room(Position center, int height, int width) {
            this.center = new Position(center);
            this.width = width % 2 == 0 ? width + 1 : width;
            this.height = height == 0 ? height + 1 : height;

            int xOffset = (int) (0.5 * width);
            int yOffset = (int) (0.5 * height);
            leftBottom = new Position(center, -xOffset, -yOffset);
            rightTop = new Position(center, xOffset, yOffset);
            //System.out.println("Created a room with leftbottom cord: " + LeftBottom.x + ", "
            // + LeftBottom.y + " and righttop cord: " + RightTop.x + ", " + RightTop.y + ".\n");
        }

        public boolean checkOverLap(Room other) {
            int xOffset = Math.abs(center.x - other.center.x);
            int yOffset = Math.abs(center.y - other.center.y);
            return (xOffset < (width + other.width) / 2 && yOffset < (height + other.height) / 2);
        }
    }

    private class Hallway {
        private Position leftBottom;
        private Position rightTop;
        int dir;

        Hallway(Position leftBottom, Position rightTop, int dir) {
            this.leftBottom = leftBottom;
            this.rightTop = rightTop;
            this.dir = dir;
        }
    }

    /* A Utility function for checking room validation (for array and other existing rooms) */
    private boolean checkRoom(Room currentRoom) {

        if (!(currentRoom.leftBottom.checkValid() && currentRoom.rightTop.checkValid())) {
            return false;
        }

        for (Room other : roomList) {
            if (currentRoom.checkOverLap(other)) {
                return false;
            }
        }

        return true;
    }

    /* A Position class for storing and calculating position information*/
    protected class Position {
        private int x;
        private int y;
        private int xInd;
        private int yInd;

        /* initialize position with given x and y coordinates*/
        public Position(int x, int y) {
            this.x = x;
            this.y = y;
            cordToindex();
        }

        /* initialize position with another position(copy) */
        public Position(Position other) {
            x = other.x;
            y = other.y;
            cordToindex();
        }

        /* initialize position with another position and offset */
        public Position(Position center, int xOffset, int yOffset) {
            x = center.x + xOffset;
            y = center.y + yOffset;
            cordToindex();
        }

        public void cordToindex() {
            xInd = x;
            yInd = ySize - y;
        }

        public boolean checkValid() {
            return !(xInd < 0 || xInd >= xSize || yInd < 0 || yInd >= ySize);
        }
    }

    /* A Utility function for printing the world to console*/
    public void printWorld() {
        for (int i = 0; i < ySize; i++) {
            for (int j = 0; j < xSize; j++) {
                String content;
                if (worldArray[i][j] == 0) {
                    content = ".";
                } else if (worldArray[i][j] == 1) {
                    content = "#";
                } else if (worldArray[i][j] == 2) {
                    content = "/";
                } else if (worldArray[i][j] == 3) {
                    content = "*";
                } else if (worldArray[i][j] == 4) {
                    content = "$";
                } else {
                    System.out.println("Invalid content in map.\n");
                    return;
                }

                System.out.print(content + " ");
            }
            System.out.print("\n");
        }
        System.out.print("World printed\n");
    }

    public void putCharacterAndDoor() {
        int choiceForCharacter = generateRandom(0, roomList.size());
        Room roomForCharacter = roomList.get(choiceForCharacter);
        character = new Position(roomForCharacter.center.x, roomForCharacter.center.y);
        worldArray[character.yInd][character.xInd] = 3;

        while (true) {
            int choiceForDoor = generateRandom(0, roomList.size());
            Room roomForDoor = roomList.get(choiceForDoor);
            goldDoor = new Position(roomForDoor.leftBottom.x + 1, roomForDoor.leftBottom.y);
            Position up, down, left, right;
            up = new Position(goldDoor, 0, 1);
            down = new Position(goldDoor, 0, -1);
            left = new Position(goldDoor, -1, 0);
            right = new Position(goldDoor, 1, 0);
            if (up.checkValid()
                    && (worldArray[up.yInd][up.xInd] == 0
                    || worldArray[up.yInd][up.xInd] == 2)) {
                if (down.checkValid()
                        && Math.abs(worldArray[down.yInd][down.xInd]
                        - worldArray[up.yInd][up.xInd]) == 2) {
                    worldArray[goldDoor.yInd][goldDoor.xInd] = 4;
                    break;
                }
            } else if (left.checkValid()
                    && (worldArray[left.yInd][left.xInd] == 0
                    || worldArray[left.yInd][left.xInd] == 2)) {
                if (right.checkValid() && Math.abs(worldArray[right.yInd][right.xInd]
                        - worldArray[left.yInd][left.xInd]) == 2) {
                    worldArray[goldDoor.yInd][goldDoor.xInd] = 4;
                    break;
                }
            }
        }

    }

    public void moveCharacter(char dir) {
        Position nextCharacter;
        if (dir == 'W') {
            nextCharacter = new Position(character, 0, 1);
        } else if (dir == 'A') {
            nextCharacter = new Position(character, -1, 0);
        } else if (dir == 'S') {
            nextCharacter = new Position(character, 0, -1);
        } else if (dir == 'D') {
            nextCharacter = new Position(character, 1, 0);
        } else {
            System.out.println("Invalid move command!");
            return;
        }

        if (!nextCharacter.checkValid()
                || worldArray[nextCharacter.yInd][nextCharacter.xInd] == 1) {
            return;
        }

        worldArray[character.yInd][character.xInd] = 2;
        worldArray[nextCharacter.yInd][nextCharacter.xInd] = 3;
        character = new Position(nextCharacter);
    }

    /* A Utility function for generating random integer between [lower, upper) */
    public int generateRandom(int lower, int upper) {
        double ratio = RANDOM.nextDouble();
        return (int) (lower + (upper - lower) * ratio);
    }

    /* getter function for world array */
    public int[][] getWorldArray() {
        return worldArray;
    }
}
