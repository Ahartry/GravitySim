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
        xCoord = trail.getX();
        yCoord = trail.getY();
        index = trail.getIndex();
    }

    public double getX(){
        return this.xCoord;
    }
    public double getY(){
        return this.yCoord;
    }
    public int getIndex(){
        return this.index;
    }
    public void setIndex(int x){
        this.index = x;
    }
}
