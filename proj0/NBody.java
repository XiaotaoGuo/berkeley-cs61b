public class NBody{
    
    public static void main(String[] args){
        if(args.length != 3){
            System.out.println("You must enter 3 arguement.");
            return;
        }
        
        /** read lines arguments */
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        
        /** read Radius and planets from target file */
        double Radius = readRadius(filename);
        Planet[] AllPlanets = readPlanets(filename);

        /** draw background */
        StdDraw.setScale(-1 * Radius, Radius);
        StdDraw.clear();
        StdDraw.picture(0, 0, "images/starfield.jpg");

        /** draw planets */
        for(Planet planet: AllPlanets){
            planet.draw();
        }

        /** create animation */
        StdDraw.enableDoubleBuffering();
        double time = 0;
        int N = AllPlanets.length;
        while(time != T){
            double[] xForces = new double[N];
            double[] yForces = new double[N];
            for(int i = 0; i < N; i++){
                xForces[i] = AllPlanets[i].calcNetForceExertedByX(AllPlanets);
                yForces[i] = AllPlanets[i].calcNetForceExertedByY(AllPlanets);
            }

            for(int i = 0; i < N; i++){
                AllPlanets[i].update(dt, xForces[i], yForces[i]);
            }

            StdDraw.picture(0, 0, "images/starfield.jpg");
            /** draw planets */
            for(Planet planet: AllPlanets){
                planet.draw();
            }

            StdDraw.show();
            StdDraw.pause(10);

            time += dt;
        }

        StdOut.printf("%d\n", AllPlanets.length);
        StdOut.printf("%.2e\n", Radius);
        for (int i = 0; i < AllPlanets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                        AllPlanets[i].xxPos, AllPlanets[i].yyPos, AllPlanets[i].xxVel,
                        AllPlanets[i].yyVel, AllPlanets[i].mass, AllPlanets[i].imgFileName);   
        }
    }

    public static double readRadius(String path){
        In in = new In(path);
        in.readInt();
        double R = in.readDouble();
        return R;
    }

    public static Planet[] readPlanets(String path){
        In in = new In(path);
        int N = in.readInt();
        Planet[] AllPlanets = new Planet[N];
        in.readDouble();
        double xP, yP, xV, yV, m;
        String img;
        for(int i = 0; i < N; i++){
            xP = in.readDouble();
            yP = in.readDouble();
            xV = in.readDouble();
            yV = in.readDouble();
            m = in.readDouble();
            img = in.readString();
            AllPlanets[i] = new Planet(xP, yP, xV, yV, m, img);
        }
        return AllPlanets;
    }
}