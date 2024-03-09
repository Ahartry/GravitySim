import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
//import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
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
    int trailLimit = 1000000;
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
    int radiusScalar = 1;
    double realTime;
    double predictedTime;
    double timeDifference;
    double rotationalOffset = 0;
    boolean lagrange = false;
    double lagrangeOffset = 0;
    boolean justPaused = false;

    private ArrayList<Trail> trailList = new ArrayList<>();
    private ArrayList<Apside> apsideList = new ArrayList<>();

    private Physics physicsSim;

    public GPanel(Physics p, JFrame f){
        physicsSim = p;
        frame = f;
    }

    @Override
    public void paintComponent(Graphics g1) {
        super.paintComponent(g1);

        Graphics2D g = (Graphics2D) g1;
        //g.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));

        if(lagrange){
            lagrangeOffset = -1 * Math.atan2((physicsSim.getPhysicsList().get(firstBody).getLocy() - physicsSim.getPhysicsList().get(secondBody).getLocy()), (physicsSim.getPhysicsList().get(firstBody).getLocx() - physicsSim.getPhysicsList().get(secondBody).getLocx()));
        }
        //System.out.println(Math.atan2((physicsSim.getPhysicsList().get(firstBody).getLocy() - physicsSim.getPhysicsList().get(secondBody).getLocy()), (physicsSim.getPhysicsList().get(firstBody).getLocx() - physicsSim.getPhysicsList().get(secondBody).getLocx())));
        //System.out.println(firstBody + ", " + secondBody);
        //iterates through all the objects to draw them
        for(int i = 0; i < physicsSim.getPhysicsList().size(); i++){

            //I need to add logarithmic scaling so that the sun doesn't need to be massive for the other planets to be visible
            //double radius = 100000000 * Math.log(physicsSim.getPhysicsList().get(i).getRadius() * radiusScalar);
            double radius = physicsSim.getPhysicsList().get(i).getRadius() * radiusScalar;
            g.setColor(physicsSim.getPhysicsList().get(i).color);

            //checks when to add new trail
            if(j > 10 && !physicsSim.getPaused() && !physicsSim.getPhysicsList().get(i).getFixed()){
                if(lagrange && i != firstBody && i != secondBody){
                    trailList.add(new Trail(rotateObjectX(physicsSim.getPhysicsList().get(i), 0), rotateObjectY(physicsSim.getPhysicsList().get(i), 0), i));
                }else if(lagrange){
                    //do nothing
                }else{
                    trailList.add(new Trail(physicsSim.getPhysicsList().get(i).getLocx(), physicsSim.getPhysicsList().get(i).getLocy(), i));
                }
                    
            //something about drawing first loop after pausing
            }else if(justPaused){
                if(lagrange && i != firstBody && i != secondBody){
                    trailList.add(new Trail(rotateObjectX(physicsSim.getPhysicsList().get(i), 0), rotateObjectY(physicsSim.getPhysicsList().get(i), 0), i));
                }else if(lagrange){
                    //do nothing
                }else{
                    trailList.add(new Trail(physicsSim.getPhysicsList().get(i).getLocx(), physicsSim.getPhysicsList().get(i).getLocy(), i));
                }

                justPaused = false; 
            }
            j++;

            //I have NO IDEA why it needs to multiply by -1, but it does
            if(focused){
                setOffsetx((int) (rotateObjectX(physicsSim.getPhysicsList().get(objectFocus), rotationalOffset) / zoom) * -1 + getXDimension() / 2);
                setOffsety((int) (rotateObjectY(physicsSim.getPhysicsList().get(objectFocus), rotationalOffset) / zoom) * -1 + getYDimension() / 2);
            }

            xdraw = (rotateX(physicsSim.getPhysicsList().get(i).getLocx(), physicsSim.getPhysicsList().get(i).getLocy(), rotationalOffset) - radius) / zoom + (double) offsetx;
            ydraw = (rotateY(physicsSim.getPhysicsList().get(i).getLocx(), physicsSim.getPhysicsList().get(i).getLocy(), rotationalOffset) - radius) / zoom + (double) offsety;

            //draws the bodies
            g.setColor(physicsSim.getPhysicsList().get(i).color);
            g.fillOval((int) xdraw, (int) ydraw, (int) ((radius * 2) /  zoom), (int) ((radius * 2) /  zoom));

            firstTrailLoop = true;
            //draws the trail
            if(trailDrawMode){
                //does the lines
                for(int index = 0; index < trailList.size(); index++){
                    if(trailList.get(index).getIndex() == i){
    
                        if(lagrange){
                            xdraw = rotateX(trailList.get(index).getxCoord(), trailList.get(index).getyCoord(), rotationalOffset - lagrangeOffset) / zoom + offsetx;
                            ydraw = rotateY(trailList.get(index).getxCoord(), trailList.get(index).getyCoord(), rotationalOffset - lagrangeOffset) / zoom + offsety;
                        }else{
                            xdraw = rotateX(trailList.get(index).getxCoord(), trailList.get(index).getyCoord(), rotationalOffset) / zoom + offsetx;
                            ydraw = rotateY(trailList.get(index).getxCoord(), trailList.get(index).getyCoord(), rotationalOffset) / zoom + offsety;
                        }
                        g.setColor(Color.RED);
                        
                        //stops first loop shenanigans 
                        if(firstTrailLoop){
                            firstTrailLoop = false;
                            xdraw2 = xdraw;
                            ydraw2 = ydraw;
                        }
    
                        g.drawLine((int) xdraw, (int) ydraw, (int) xdraw2, (int) ydraw2);
        
                        try{
                            if(lagrange){
                                xdraw2 = rotateX(trailList.get(index).getxCoord(), trailList.get(index).getyCoord(), rotationalOffset - lagrangeOffset) / zoom + offsetx;
                                ydraw2 = rotateY(trailList.get(index).getxCoord(), trailList.get(index).getyCoord(), rotationalOffset - lagrangeOffset) / zoom + offsety;
                            }else{
                                xdraw2 = rotateX(trailList.get(index).getxCoord(), trailList.get(index).getyCoord(), rotationalOffset) / zoom + offsetx;
                                ydraw2 = rotateY(trailList.get(index).getxCoord(), trailList.get(index).getyCoord(), rotationalOffset) / zoom + offsety;
                            }
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

                System.out.println(index);

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
            //twoBodyAnalysis();
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

    public void increaseSpeed(){
        physicsSim.setSpeed(physicsSim.getSpeed() * 2);
    }

    public void decreaseSpeed(){
        physicsSim.setSpeed((int) (physicsSim.getSpeed() / 2));
    }

    public double rotateX(double posx, double posy, double offset){
        double rads = offset + lagrangeOffset;
        return (posx * Math.cos(rads)) - (posy * Math.sin(rads));
    }

    public double rotateObjectX(GravBody input, double offset){
        double rads = offset + lagrangeOffset;
        return (input.getLocx() * Math.cos(rads)) - (input.getLocy() * Math.sin(rads));
    }

    public double rotateObjectY(GravBody input, double offset){
        double rads = offset + lagrangeOffset;
        return (input.getLocx() * Math.sin(rads)) + (input.getLocy() * Math.cos(rads));
    }

    public double rotateY(double posx, double posy, double offset){
        double rads = offset + lagrangeOffset;
        return (posx * Math.sin(rads)) + (posy * Math.cos(rads));
    }

    public void setRotationalOffset(double rads){
        rotationalOffset = rads;
    }

    public double getRotationalOffset(){
        return rotationalOffset;
    }

    public void setLagrange(boolean input){
        lagrange = input;
    }

    public boolean getLagrange(){
        return lagrange;
    }

    public void setLagrangeOffset(double input){
        lagrangeOffset = input;
    }

    public double getLagrangeOffset(){
        return lagrangeOffset;
    }

    public void setJustPaused(boolean x){
        justPaused = x;
    }

    // public void twoBodyAnalysis(){
    //     if(twoBodyAnalytics){
    //         if(!(firstBody == secondBody)){
    //             distance = (double) Math.sqrt(Math.pow((double) (physicsSim.getPhysicsList().get(firstBody).getLocx() - physicsSim.getPhysicsList().get(secondBody).getLocx()), 2) + Math.pow((double) (physicsSim.getPhysicsList().get(firstBody).getLocy() - physicsSim.getPhysicsList().get(secondBody).getLocy()), 2));
    //             velocity = Math.sqrt(Math.pow(physicsSim.getPhysicsList().get(firstBody).getVelx(), 2) + Math.pow(physicsSim.getPhysicsList().get(firstBody).getVely(), 2));

    //             if(distance > distance2){
    //                 ascending = true;
    //             }else if(distance < distance2){
    //                 ascending = false;
    //             }

    //             //make the first loop work right
    //             if(firstLoopThing){
    //                 ascended = ascending;
    //                 firstLoopThing = false;
    //             }

    //             //does the stuff
    //             if(ascending && !ascended){
    //                 distancePeri = distance;
    //                 apsideList.add(new Apside(physicsSim.getPhysicsList().get(firstBody).getLocx(), physicsSim.getPhysicsList().get(firstBody).getLocy(), false, distance));
    //                 //System.out.println("Periapsis Reached, altitude: " + distance);
    //                 realTime = physicsSim.getTimePassed() - lastPeri;
    //                 System.out.println("Time: " + realTime /* (60 * 60 * 24) */);

    //                 //finds the semi-major axis through black magic (This most likely does not work)
    //                 gm = 67384.1 * (physicsSim.getPhysicsList().get(firstBody).getMass() + physicsSim.getPhysicsList().get(secondBody).getMass());
    //                 orbitalEnergy = (Math.pow(velocity, 2) / 2) - (gm / distance);
    //                 semimajorAxis = -1 * gm / (2 * orbitalEnergy);
    //                 semimajorAxis = (distanceApo + distancePeri) / 2;

    //                 predictedTime = 2 * Math.PI * Math.sqrt(Math.pow(semimajorAxis, 3) / gm);

    //                 timeDifference = ((predictedTime - realTime) / ((predictedTime + realTime) / 2)) * 100;

    //                 System.out.println("Time should be: " + predictedTime);
    //                 System.out.println("Percentage difference: " + timeDifference + "%\n");
    //                 //System.out.println("Semi-major Axis: " + semimajorAxis);
    //                 //System.out.println("Semi-major Axis scuffed: " + (distanceApo + distancePeri) / 2);

    //                 lastPeri = physicsSim.getTimePassed();

    //             }else if(!ascending && ascended){
    //                 distanceApo = distance;
    //                 apsideList.add(new Apside(physicsSim.getPhysicsList().get(firstBody).getLocx(), physicsSim.getPhysicsList().get(firstBody).getLocy(), true, distance));
    //                 //System.out.println("Apoapsis Reached, altitude: " + distance);
    //             }

    //             if(ascending){
    //                 ascended = true;
    //             }else{
    //                 ascended = false;
    //             }

    //             distance2 = (double) Math.sqrt(Math.pow((double) (physicsSim.getPhysicsList().get(firstBody).getLocx() - physicsSim.getPhysicsList().get(secondBody).getLocx()), 2) + Math.pow((double) (physicsSim.getPhysicsList().get(firstBody).getLocy() - physicsSim.getPhysicsList().get(secondBody).getLocy()), 2));

    //         }
    //     }else{
    //         apsideList.clear();
    //         firstBody = 0;
    //         secondBody = 0;
    //     }
    // }


    private void drawTransparentRing(Graphics g1, GravBody b) {

        Graphics2D g = (Graphics2D) g1;
        int centerX = (int) (rotateObjectX(b, rotationalOffset) / getZoom() + getOffsetx());
        int centerY = (int) (rotateObjectY(b, rotationalOffset) / getZoom() + getOffsety());
        int ringWidth = 1; // Adjust the ring width as needed

        int outerRadius = (int) (b.getRadius() / zoom + 10);
        int innerRadius = outerRadius - ringWidth;

        // g.setColor(Color.BLUE); // Set the color of the ring

        // // Draw outer oval
        // g.drawOval(centerX - outerRadius, centerY - outerRadius, 2 * outerRadius, 2 * outerRadius);

        // // Set XOR mode to make the inner oval transparent
        // g.setXORMode(getBackground());

        // // Draw inner oval
        // g.drawOval(centerX - innerRadius, centerY - innerRadius, 2 * innerRadius, 2 * innerRadius);

        // // Reset the XOR mode
        // g.setPaintMode();

        Shape outer = new Ellipse2D.Double(centerX - (outerRadius), centerY - (outerRadius), outerRadius * 2, outerRadius * 2);
        Shape inner = new Ellipse2D.Double(centerX - (innerRadius), centerY - (innerRadius), innerRadius * 2, innerRadius * 2);

        Area circle = new Area( outer );
        circle.subtract( new Area(inner) );

        g.setColor(Color.BLUE);
        g.fill(circle);
        // g.setColor(Color.BLACK);
        // g.draw(circle);
    }
}

