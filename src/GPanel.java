import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.Action;
import javax.swing.JFrame;

public class GPanel extends JPanel{

    static Action scrollAction;

    //super.setPreferredSize(new Dimension(800, 500));

@Override
public void scrollRectToVisible(Rectangle arg0) {
    super.scrollRectToVisible(arg0);
}

    int sizey;
    int sizex;
    double xdraw;
    double ydraw;
    double xdraw2;
    double ydraw2;
    int offsetx;
    int offsety;
    double zoom = 1000;
    int j = 0;
    int trailLimit = 100000;
    JFrame frame;
    int objectFocus = 0;
    int objectSelected = 0;
    boolean focused = false;
    boolean showControls = true;
    boolean selected = false;
    boolean twoBodyAnalytics = false;
    boolean firstLoopThing = true;
    int firstBody;
    int secondBody;
    boolean ascending;
    boolean ascended;
    double distance;
    double distance2;
    double distanceApo;
    double distancePeri;
    boolean firstTrailLoop = true;
    boolean trailDrawMode = true;
    double lastPeri = 0;
    double orbitalEnergy;
    double velocity;
    double semimajorAxis;
    double gm;

    private ArrayList<Trail> trailList = new ArrayList<>();
    private ArrayList<Apside> apsideList = new ArrayList<>();

    private Physics physicsSim;

    public GPanel(Physics p, JFrame f){
        physicsSim = p;
        frame = f;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //iterates through all the objects to draw them
        for(int i = 0; i < physicsSim.getPhysicsList().size(); i++){
            double radius = physicsSim.getPhysicsList().get(i).getRadius();

            //case for colour 'randomization'
            g.setColor(physicsSim.getPhysicsList().get(i).color);

            //checks when to add new trail
            if(j > 10 && !physicsSim.getPaused()){
                trailList.add(new Trail(physicsSim.getPhysicsList().get(i).getLocx(), physicsSim.getPhysicsList().get(i).getLocy(), i));
            }
            j++;

            //I have NO IDEA why it needs to multiply by -1, but it does
            if(focused){
                setOffsetx((int) (physicsSim.getPhysicsList().get(objectFocus).getLocx() / zoom) * -1 + getXDimension() / 2);
                setOffsety((int) (physicsSim.getPhysicsList().get(objectFocus).getLocy() / zoom) * -1 + getYDimension() / 2);
            }

            xdraw = (physicsSim.getPhysicsList().get(i).getLocx() - radius) / zoom + (double) offsetx;
            ydraw = (physicsSim.getPhysicsList().get(i).getLocy() - radius) / zoom + (double) offsety;

            //draws the bodies
            g.fillOval((int) xdraw, (int) ydraw, (int) (radius * 2) / (int) zoom, (int) (radius * 2) / (int) zoom);

            firstTrailLoop = true;
            //draws the trail
            if(trailDrawMode){
                //does the lines
                for(int index = 0; index < trailList.size(); index++){
                    if(trailList.get(index).getIndex() == i){
    
                        g.setColor(Color.RED);
                        xdraw = trailList.get(index).getxCoord() / zoom + offsetx;
                        ydraw = trailList.get(index).getyCoord() / zoom + offsety;
    
                        //stops first loop shenanigans 
                        if(firstTrailLoop){
                            firstTrailLoop = false;
                            xdraw2 = xdraw;
                            ydraw2 = ydraw;
                        }
    
                        g.drawLine((int) xdraw, (int) ydraw, (int) xdraw2, (int) ydraw2);
        
                        try{
                            xdraw2 = trailList.get(index).getxCoord() / zoom + offsetx;
                            ydraw2 = trailList.get(index).getyCoord() / zoom + offsety;
                        }catch (Exception e){
                            xdraw2 = xdraw;
                            ydraw2 = ydraw;
                        }
    
                        
                    }
    
                }
            }else{
                //does the points
                for(int index = 0; index < trailList.size(); index++){
                    g.setColor(Color.RED);
                    xdraw = trailList.get(index).getxCoord() / zoom + offsetx;
                    ydraw = trailList.get(index).getyCoord() / zoom + offsety;

                    g.drawLine((int) xdraw, (int) ydraw, (int) xdraw, (int) ydraw);
    
                }
            }
            

            //draws the apsides
            for(int index = 0; index < apsideList.size(); index++){
                xdraw = apsideList.get(index).getX() / zoom + offsetx;
                ydraw = apsideList.get(index).getY() / zoom + offsety;

                g.setColor(Color.BLUE);
                g.fillOval((int) (xdraw - 2.5), (int) (ydraw - 2.5), 5, 5);
            }
            
            //removes old points for trail
            if(trailList.size() > trailLimit){
                for(int index = 0; index < (trailList.size() - trailLimit); index++){
                    trailList.remove(0);
                }
            }

            if(selected){
                drawTransparentRing(g, physicsSim.getPhysicsList().get(objectSelected));
                //System.out.println("Selected");
            }

            //Two body analytics
            if(twoBodyAnalytics){
                if(!(firstBody == secondBody)){
                    distance = (double) Math.sqrt(Math.pow((double) (physicsSim.getPhysicsList().get(firstBody).getLocx() - physicsSim.getPhysicsList().get(secondBody).getLocx()), 2) + Math.pow((double) (physicsSim.getPhysicsList().get(firstBody).getLocy() - physicsSim.getPhysicsList().get(secondBody).getLocy()), 2));
                    velocity = Math.sqrt(Math.pow(physicsSim.getPhysicsList().get(firstBody).getVelx(), 2) + Math.pow(physicsSim.getPhysicsList().get(firstBody).getVely(), 2));

                    if(distance > distance2){
                        ascending = true;
                    }else if(distance < distance2){
                        ascending = false;
                    }

                    //make the first loop work right
                    if(firstLoopThing){
                        ascended = ascending;
                        firstLoopThing = false;
                    }

                    //does the stuff
                    if(ascending && !ascended){
                        distancePeri = distance;
                        apsideList.add(new Apside(physicsSim.getPhysicsList().get(firstBody).getLocx(), physicsSim.getPhysicsList().get(firstBody).getLocy(), false, distance));
                        System.out.println("Periapsis Reached, altitude: " + distance);
                        System.out.println("Time: " + (physicsSim.getTimePassed() - lastPeri) /* (60 * 60 * 24) */);

                        //finds the semi-major axis through black magic (This most likely does not work)
                        gm = 67384.1 * (physicsSim.getPhysicsList().get(firstBody).getMass() + physicsSim.getPhysicsList().get(secondBody).getMass());
                        orbitalEnergy = (Math.pow(velocity, 2) / 2) - (gm / distance);
                        semimajorAxis = -1 * gm / (2 * orbitalEnergy);
                        semimajorAxis = (distanceApo + distancePeri) / 2;

                        //semimajorAxis = 40536772.03;

                        System.out.println("Time should be: " + (2 * Math.PI * Math.sqrt(Math.pow(semimajorAxis, 3) / gm)));
                        //System.out.println("Semi-major Axis: " + semimajorAxis);
                        //System.out.println("Semi-major Axis scuffed: " + (distanceApo + distancePeri) / 2);

                        lastPeri = physicsSim.getTimePassed();

                    }else if(!ascending && ascended){
                        distanceApo = distance;
                        apsideList.add(new Apside(physicsSim.getPhysicsList().get(firstBody).getLocx(), physicsSim.getPhysicsList().get(firstBody).getLocy(), true, distance));
                        //System.out.println("Apoapsis Reached, altitude: " + distance);
                    }

                    if(ascending){
                        ascended = true;
                    }else{
                        ascended = false;
                    }

                    distance2 = (double) Math.sqrt(Math.pow((double) (physicsSim.getPhysicsList().get(firstBody).getLocx() - physicsSim.getPhysicsList().get(secondBody).getLocx()), 2) + Math.pow((double) (physicsSim.getPhysicsList().get(firstBody).getLocy() - physicsSim.getPhysicsList().get(secondBody).getLocy()), 2));

                }
            }else{
                apsideList.clear();
                firstBody = 0;
                secondBody = 0;
            }
        }

    }

    public void setZoom(double x){
        this.zoom = x;
        if(this.zoom < 1){
            this.zoom = 1;
        }
    }

    public double getZoom(){
        return this.zoom;
    }

    public void setOffsetx(int x){
        this.offsetx = x;
    }

    public int getOffsetx(){
        return this.offsetx;
    }

    public void setOffsety(int x){
        this.offsety = x;
    }

    public int getOffsety(){
        return this.offsety;
    }

    public void setDimensions(int x, int y){
        offsetx = x / 2;
        offsety = y / 2;
        sizex = x;
        sizey = y;
    }

    public int getXDimension(){
        return frame.getWidth();
    }

    public int getYDimension(){
        return frame.getHeight();
    }

    public int getTrailLimit(){
        return trailLimit;
    }
    
    public void setTrailLimit(int x){
        this.trailLimit = x;
    }

    public void clearTrail(){
        trailList.clear();
    }
    
    public void setObjectFocus(int x){
        this.objectFocus = x;
    }

    public int getObjectFocus(){
        return objectFocus;
    }

    public void setObjectSelected(int x){
        this.objectSelected = x;
    }

    public int getObjectSelected(){
        return objectSelected;
    }

    public void setFocused(boolean x){
        this.focused = x;
    }

    public boolean getFocused(){
        return focused;
    }

    public void setSelected(boolean x){
        this.selected = x;
    }

    public boolean getSelected(){
        return selected;
    }

    public void setShowControls(boolean x){
        this.showControls = x;
    }

    public boolean getShowControls(){
        return showControls;
    }

    public ArrayList<Trail> getTrailList(){
        return this.trailList;
    }

    public ArrayList<Apside> getApsideList(){
        return this.apsideList;
    }

    public boolean getTwoBodyAnalytics(){
        return twoBodyAnalytics;
    }

    public void setTwoBodyAnalytics(boolean x){
        this.twoBodyAnalytics = x;
    }

    public int getFirstBody(){
        return firstBody;
    }

    public void setFirstBody(int x){
        this.firstBody = x;
    }

    public int getSecondBody(){
        return secondBody;
    }

    public void setSecondBody(int x){
        this.secondBody = x;
    }

    public void setFirstLoopThing(boolean x){
        this.firstLoopThing = x;
    }

    public boolean getTrailDrawMode(){
        return trailDrawMode;
    }

    public void setTrailDrawMode(boolean x){
        this.trailDrawMode = x;
    }


    //I do not know how this works, it was written by chatGPT
    private void drawTransparentRing(Graphics g, GravBody b) {
        int centerX = (int) (b.getLocx() / getZoom() + getOffsetx());
        int centerY = (int) (b.getLocy() / getZoom() + getOffsety());
        int ringWidth = 2; // Adjust the ring width as needed

        int outerRadius = (int) (b.getRadius() / zoom + 10);
        int innerRadius = outerRadius - ringWidth;

        g.setColor(Color.BLUE); // Set the color of the ring

        // Draw outer oval
        g.drawOval(centerX - outerRadius, centerY - outerRadius, 2 * outerRadius, 2 * outerRadius);

        // Set XOR mode to make the inner oval transparent
        g.setXORMode(getBackground());

        // Draw inner oval
        g.drawOval(centerX - innerRadius, centerY - innerRadius, 2 * innerRadius, 2 * innerRadius);

        // Reset the XOR mode
        g.setPaintMode();
    }
}

