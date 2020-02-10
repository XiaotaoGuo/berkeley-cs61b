public class Planet{

    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    private static final double G = 6.67e-11;

    public Planet(double xP, double yP, double xV, double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }

    public Planet(Planet b) {
        xxPos = b.xxPos;
        yyPos = b.yyPos;
        xxVel = b.xxVel;
        yyVel = b.yyVel;
        mass = b.mass;
        imgFileName = b.imgFileName;
    }

    public double calcDistance(Planet other){
        return Math.pow((other.xxPos - xxPos) * (other.xxPos - xxPos) + 
                (other.yyPos - yyPos) * (other.yyPos - yyPos), 0.5);
    }

    public double calcForceExertedBy(Planet other){
        double distance = calcDistance(other);
        return G * other.mass * mass / (distance * distance);
    }

    public double calcForceExertedByX(Planet other){
        double distance = calcDistance(other);
        double dx = other.xxPos - xxPos;
        double force = calcForceExertedBy(other);
        return force * dx / distance;
    }

    public double calcForceExertedByY(Planet other){
        double distance = calcDistance(other);
        double dy = other.yyPos - yyPos;
        double force = calcForceExertedBy(other);
        return force * dy / distance;
    }

    public double calcNetForceExertedByX(Planet[] others){
        double totalForceX = 0.0;
        for(Planet other: others){
            if(calcDistance(other) == 0.0) continue;
            totalForceX += calcForceExertedByX(other);
        }
        return totalForceX;
    }

    public double calcNetForceExertedByY(Planet[] others){
        double totalForceY = 0.0;
        for(Planet other: others){
            if(calcDistance(other) == 0.0) continue;
            totalForceY += calcForceExertedByY(other);
        }
        return totalForceY;
    }

    public void update(double dt, double fX, double fY){
        double xxAcc = fX / mass;
        double yyAcc = fY / mass;
        xxVel += xxAcc * dt;
        yyVel += yyAcc * dt;
        xxPos += xxVel * dt;
        yyPos += yyVel * dt;

    }

    public void draw(){
        StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
    }

}