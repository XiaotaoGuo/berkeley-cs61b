package byog.Test;

import byog.Core.World;

public class WorldCreatorTest {


    public static void main(String[] args) {
        World mWorld = new World(30, 20, 2213);

        mWorld.printWorld();

        mWorld.moveCharacter('W');

        mWorld.printWorld();

        mWorld.moveCharacter('D');

        mWorld.printWorld();

    }

    public static void runTestRandomGenerate(World mWorld) {

        int count0 = 0, count1 = 0, count2 = 0, count3 = 0;
        System.out.println("Run random generation between 0 and 3 for 1000 times");
        for (int i = 0; i < 1000; i++) {
            int choice = mWorld.generateRandom(0, 4);
            if (choice == 0) {
                count0++;
            } else if (choice == 1) {
                count1++;
            } else if (choice == 2) {
                count2++;
            } else if (choice == 3) {
                count3++;
            }
        }
        System.out.println("0: " + count0 + ", 1: "
                + count1 + ", 2: " + count2 + ", 3: " + count3 + ".");
    }

}
