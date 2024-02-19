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
    boolean ascending;
    boolean ascended;
    double distance;
    double distance2;
    double distanceApo;
    double distancePeri;
    double lastPeri = 0;
    double velocity;
    double semimajorAxis;
    double gm;
    int radiusScalar = 1;
    double realTime;
    double predictedTime;
    double timeDifference;
    ArrayList<Apside> apsideList = new ArrayList<>();

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
                timePassed = timePassed + (double) ((double) physicsSpeed / (double) ticksPerFrame);
    
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
                            physicsList.get(i1).setVelx(velx + (accelx * (double) ((double) physicsSpeed / (double) ticksPerFrame)));
                            physicsList.get(i1).setVely(vely + (accely * (double) ((double) physicsSpeed / (double) ticksPerFrame)));
    
                            velx = physicsList.get(i1).getVelx();
                            vely = physicsList.get(i1).getVely();
    
    
                        }
    
                    }
                }
                //iterates and applies the new velocity to the loc
                for(int i1 = 0; i1 < physicsList.size(); i1++){
                    if(physicsList.get(i1).getFixed()){
    
                    }else{
                        physicsList.get(i1).setLocx(physicsList.get(i1).getLocx() + physicsList.get(i1).getVelx() * (double) ((double) physicsSpeed / (double) ticksPerFrame));
                        physicsList.get(i1).setLocy(physicsList.get(i1).getLocy() + physicsList.get(i1).getVely() * (double) ((double) physicsSpeed / (double) ticksPerFrame));
                    }
    
                }

                //two body analysis
                distance = (double) Math.sqrt(Math.pow((double) (physicsList.get(0).getLocx() - physicsList.get(1).getLocx()), 2) + Math.pow((double) (physicsList.get(0).getLocy() - physicsList.get(1).getLocy()), 2));
                velocity = Math.sqrt(Math.pow(physicsList.get(0).getVelx(), 2) + Math.pow(physicsList.get(0).getVely(), 2));

                if(distance > distance2){
                    ascending = true;
                }else if(distance < distance2){
                    ascending = false;
                }

                //make the first loop work right
                // if(firstLoopThing){
                //     ascended = ascending;
                //     firstLoopThing = false;
                // }

                //does the stuff
                if(ascending && !ascended){
                    distancePeri = distance;
                    apsideList.add(new Apside(physicsList.get(0).getLocx(), physicsList.get(0).getLocy(), false, distance));
                    //System.out.println("Periapsis Reached, altitude: " + distance);
                    realTime = getTimePassed() - lastPeri;
                    System.out.println("Time: " + realTime /* (60 * 60 * 24) */);

                    //finds the semi-major axis through black magic (This most likely does not work)
                    gm = 67384.1 * (physicsList.get(0).getMass() + physicsList.get(1).getMass());
                    orbitalEnergy = (Math.pow(velocity, 2) / 2) - (gm / distance);
                    semimajorAxis = -1 * gm / (2 * orbitalEnergy);
                    semimajorAxis = (distanceApo + distancePeri) / 2;

                    predictedTime = 2 * Math.PI * Math.sqrt(Math.pow(semimajorAxis, 3) / gm);

                    timeDifference = ((predictedTime - realTime) / ((predictedTime + realTime) / 2)) * 100;

                    System.out.println("Time should be: " + predictedTime);
                    System.out.println("Percentage difference: " + timeDifference + "%\n");
                    //System.out.println("Semi-major Axis: " + semimajorAxis);
                    //System.out.println("Semi-major Axis scuffed: " + (distanceApo + distancePeri) / 2);

                    lastPeri = getTimePassed();

                }else if(!ascending && ascended){
                    distanceApo = distance;
                    apsideList.add(new Apside(physicsList.get(0).getLocx(), physicsList.get(0).getLocy(), true, distance));
                    //System.out.println("Apoapsis Reached, altitude: " + distance);
                }

                if(ascending){
                    ascended = true;
                }else{
                    ascended = false;
                }

                distance2 = (double) Math.sqrt(Math.pow((double) (physicsList.get(0).getLocx() - physicsList.get(1).getLocx()), 2) + Math.pow((double) (physicsList.get(0).getLocy() - physicsList.get(1).getLocy()), 2));
    
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

    public void setSpeed(int x){
        physicsSpeed = x;
    }

    public int getSpeed(){
        return physicsSpeed;
    }
}

