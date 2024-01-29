import java.util.ArrayList;

public class Physics {

    private double velx;
    private double vely;
    private double locx1;
    private double locy1;
    private double locx2;
    private double locy2;
    private double mass2;
    private double hypSquared;
    private double accelx;
    private double accely;
    private double theta;
    private int signx = 1;
    private int signy = 1;
    private boolean paused = false;
    private double timePassed = 0;
    private double orbitalEnergy;

    ArrayList<GravBody> physicsList = new ArrayList<>();
    private GPanel gpanel;
    private long ticksPerFrame;
    private double leftover = 0;
    private int physicsSpeed;

    //contruscts or somehting
    public Physics(int speed, int ticks){
        physicsSpeed = speed;
        ticksPerFrame = ticks;
    }


    public void tick(long delta){
        //acounts for the leftover time
        double f = (delta / (double) 16666666) + leftover;
        int loopTimes = (int) f;
        leftover = f - loopTimes;

        if(!paused){

            for(int i = 0; i < (loopTimes * ticksPerFrame); i++){
                //actualy physics part of loop

                //increments time
                timePassed = timePassed + (physicsSpeed / ticksPerFrame);
    
                //iterates through each object of the list
                for(int i1 = 0; i1 < physicsList.size(); i1++){
    
    
                    //each object interacts with each other object
                    for(int i2 = 0; i2 < physicsList.size(); i2++){
    
                        //makes sure that it does not check itself and give silly /0 error
                        if(i1 != i2){
    
                            velx = physicsList.get(i1).getVelx();
                            vely = physicsList.get(i1).getVely();
                            locx1 = physicsList.get(i1).getLocx();
                            locy1 = physicsList.get(i1).getLocy();
                            //double mass1 = physicsList.get(i1).getMass();
    
                            locx2 = physicsList.get(i2).getLocx();
                            locy2 = physicsList.get(i2).getLocy();
                            mass2 = physicsList.get(i2).getMass();
    
                            hypSquared = (double) (locx1 - locx2) * (locx1 - locx2) + (double) (locy1 - locy2) * (locy1 - locy2);
                            theta = Math.atan(Math.abs(locy1 - locy2) / Math.abs(locx1 - locx2));
    
                            //applies the sign for the direction
                            if(locx1 > locx2){
                                signx = -1;
                            } else{
                                signx = 1;
                            }
                            if(locy1 > locy2){
                                signy = -1;
                            } else{
                                signy = 1;
                            }
                            
                            //checks if they have the same position
                            if(hypSquared == 0){
                                System.out.println("Same location");
                            } else{
    
                                //removed 15 orders of magnitude from G to account for lower mass values
                                //does the actual physics acceleration math
                                accelx = (double) (Math.cos(theta) * (67384.1 * mass2 / (hypSquared))) * signx;
                                accely = (double) (Math.sin(theta) * (67384.1 * mass2 / (hypSquared))) * signy;
    
                            }
    
                            //applies the acceleration to the speed
                            physicsList.get(i1).setVelx(velx + (accelx * (physicsSpeed / ticksPerFrame)));
                            physicsList.get(i1).setVely(vely + (accely * (physicsSpeed / ticksPerFrame)));
    
                            velx = physicsList.get(i1).getVelx();
                            vely = physicsList.get(i1).getVely();
    
    
                        }
    
                    }
                }
                //iterates and applies the new velocity to the loc
                for(int i1 = 0; i1 < physicsList.size(); i1++){
                    if(physicsList.get(i1).getFixed()){
    
                    }else{
                        physicsList.get(i1).setLocx(physicsList.get(i1).getLocx() + physicsList.get(i1).getVelx() * (double) (physicsSpeed / ticksPerFrame));
                        physicsList.get(i1).setLocy(physicsList.get(i1).getLocy() + physicsList.get(i1).getVely() * (double) (physicsSpeed / ticksPerFrame));
                    }
    
                }

                gpanel.twoBodyAnalysis();
    
            }
        }

    }

    public ArrayList<GravBody> getPhysicsList(){
        return this.physicsList;
    }

    public boolean getPaused(){
        return this.paused;
    }

    public void setPaused(boolean x){
        this.paused = x;
    }

    public double getTimePassed(){
        return timePassed;
    }

    public void setOrbitalEnergy(double x){
        this.orbitalEnergy = x;
    }

    public double getOrbitalEnergy(){
        return this.orbitalEnergy;
    }

    public void setGPanel(GPanel panel){
        this.gpanel = panel;
    }
}
