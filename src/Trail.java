public class Trail {
    
    private double xCoord;
    private double yCoord;
    private int index;

    public Trail(double x, double y, int indexInput){
        xCoord = x;
        yCoord = y;
        index = indexInput;
    }

    public Trail(Trail trail){
        xCoord = trail.getxCoord();
        yCoord = trail.getyCoord();
        index = trail.getIndex();
    }

    public double getxCoord(){
        return this.xCoord;
    }
    public double getyCoord(){
        return this.yCoord;
    }
    public int getIndex(){
        return this.index;
    }
}
