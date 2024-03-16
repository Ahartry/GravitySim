public class Apside {

    double x;
    double y;
    boolean apo;
    double distance;
    
    public Apside(double xInput, double yInput, boolean apoInput, double distanceInput){
        x = xInput;
        y = yInput;
        apo = apoInput;
        distance = distanceInput;
    }

    public Apside(Apside input){
        x = input.x;
        y = input.y;
        apo = input.apo;
        distance = input.distance;
    }

    public double getX(){
        return this.x;
    }
    public double getY(){
        return this.y;
    }
    public boolean getApo(){
        return this.apo;
    }
    public double getDistance(){
        return this.distance;
    }
}
