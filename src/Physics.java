import java.util.ArrayList;

public class Physics {

    private double velx1;
    private double vely1;
    private double velx2;
    private double vely2;
    private double locx1;
    private double locy1;
    private double locx2;
    private double locy2;
    private double mass1;
    private double mass2;
    private double hypSquared;
    private double accelx1;
    private double accely1;
    private double accelx2;
    private double accely2;
    private double forcex;
    private double forcey;
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
    double lastApo = 0;
    double velocity;
    double semimajorAxis;
    double gm;
    int radiusScalar = 1;
    double realTime;
    double predictedTime;
    double timeDifference;
    int apsideLoopCount = 0;
    boolean reached = false;
    private int selected;
    private int selectedHost;

    ArrayList<GravBody> physicsList = new ArrayList<>();
    ArrayList<GravBody> startingPhysicsList = new ArrayList<>();
    ArrayList<Byte> byteList = new ArrayList<>();
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
        //long start = System.nanoTime();

        //accounts for the leftover time
        double f = (delta / (double) 16666666) + leftover;
        int loopTimes = (int) f;
        leftover = f - loopTimes;

        //stuff for indexing objects
        byteList.clear();
        for(byte j = 0; j < physicsList.size(); j++){
            byteList.add(j);
            //System.out.println(j);
        }

        if(!paused){

            //maybe optimizations
            if(gpanel.getSelected()){

                selected = gpanel.getObjectSelected();
                selectedHost = guessHost(selected);
        
            }

            for(int i = 0; i < (loopTimes * ticksPerFrame); i++){
                //actualy physics part of loop

                //increments time
                timePassed = timePassed + (double) ((double) physicsSpeed / (double) ticksPerFrame);
    
                int listSize = byteList.size();
                //iterates through each object of the list
                for(int i1 = 0; i1 < listSize; i1++){
    
                    //each object interacts with each other object
                    for(int i2 = 0; i2 < byteList.size(); i2++){

                        int i2value = byteList.get(i2);
    
                        //makes sure that it does not check itself and give silly /0 error
                        if(i1 != i2value){

                            //System.out.println(i1 + " " + i2value);

                            velx1 = physicsList.get(i1).getVelx();
                            vely1 = physicsList.get(i1).getVely();
                            locx1 = physicsList.get(i1).getLocx();
                            locy1 = physicsList.get(i1).getLocy();
                            mass1 = physicsList.get(i1).getMass();
    
                            velx2 = physicsList.get(i2value).getVelx();
                            vely2 = physicsList.get(i2value).getVely();
                            locx2 = physicsList.get(i2value).getLocx();
                            locy2 = physicsList.get(i2value).getLocy();
                            mass2 = physicsList.get(i2value).getMass();
    
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
                                if(mass1 == 0 && mass2 == 0){
                                    accelx1 = 0;
                                    accely1 = 0;
                                    accelx2 = 0;
                                    accely2 = 0;
                                }else if(mass1 == 0){
                                    accelx1 = (double) (Math.cos(theta) * (66743 * mass2 / (hypSquared))) * signx * -1;
                                    accely1 = (double) (Math.sin(theta) * (66743 * mass2 / (hypSquared))) * signy * -1;
                                    accelx2 = 0;
                                    accely2 = 0;
                                }else if(mass2 == 0){
                                    accelx1 = 0;
                                    accely1 = 0;
                                    accelx2 = (double) (Math.cos(theta) * (66743 * mass1 / (hypSquared))) * signx * -1;
                                    accely2 = (double) (Math.sin(theta) * (66743 * mass1 / (hypSquared))) * signy * -1;
                                }else{
                                    forcex = (double) (Math.cos(theta) * (66743 * mass2 * mass1 / (hypSquared))) * signx;
                                    forcey = (double) (Math.sin(theta) * (66743 * mass2 * mass1 / (hypSquared))) * signy;

                                    accelx1 = forcex / mass1;
                                    accely1 = forcey / mass1;
                                    accelx2 = -1 * forcex / mass2;
                                    accely2 = -1 * forcey / mass2;
                                }

                                // System.out.println("\n" + getHyp(forcex, forcey));
                                // System.out.println(66743 * physicsList.get(i2value).getMass() * physicsList.get(i1).getMass() / (Math.pow(getDistance(i1,i2value), 2)));
                                if(!physicsList.get(i1).getFixed()){
                                    physicsList.get(i1).setVelx(velx1 + (accelx1 * physicsSpeed));
                                    physicsList.get(i1).setVely(vely1 + (accely1 * physicsSpeed));
                                }
                                if(!physicsList.get(i2value).getFixed()){
                                    physicsList.get(i2value).setVelx(velx2 + (accelx2 * physicsSpeed));
                                    physicsList.get(i2value).setVely(vely2 + (accely2 * physicsSpeed));
                                }
    
                            }
    
                        }
    
                    }
                    if(!physicsList.get(i1).getFixed()){
                        physicsList.get(i1).setLocx(physicsList.get(i1).getLocx() + (physicsList.get(i1).getVelx() * physicsSpeed));
                        physicsList.get(i1).setLocy(physicsList.get(i1).getLocy() + (physicsList.get(i1).getVely() * physicsSpeed));
                    }

                    byteList.remove(0);
                    //System.out.println(i1);
                }

                //two body analysis
                //important: First body is the one you want to get stats about. Second is the host. Should be reversed. Yes, I know
                if(gpanel.getSelected() && !gpanel.getLagrange()){
                    distance = getDistance(selected, selectedHost);
    
                    if(distance > distance2){
                        ascending = true;
                    }else if(distance < distance2){
                        ascending = false;
                    }

                    //silly thing
                    if(apsideLoopCount < ticksPerFrame + 1){
                        ascended = ascending;
                    }

                    //overflow catch
                    if(apsideLoopCount < 0){
                        apsideLoopCount = (int) ticksPerFrame + 1;
                    }
    
                    //does the stuff
                    // && apsideLoopCount > ticksPerFrame
                    if(ascending && !ascended){

                        System.out.println(apsideLoopCount);
                        reached = true;
                        distancePeri = distance;
                        gpanel.newApside(false, distance);
                        realTime = getTimePassed() - lastPeri;
                        lastPeri = getTimePassed();

                    // && apsideLoopCount > ticksPerFrame
                    }else if(!ascending && ascended){

                        reached = true;
                        distanceApo = distance;
                        gpanel.newApside(true, distance);
                        realTime = getTimePassed() - lastApo;
                        lastApo = getTimePassed();

                    }
    
                    if(ascending){
                        ascended = true;
                    }else{
                        ascended = false;
                    }
    
                    distance2 = distance;

                    apsideLoopCount++;
                    
                    reached = false;


                }else{
                    gpanel.getApsideList().clear();
                    apsideLoopCount = 0;
                }
                
                
            }
        }

        //System.out.println(ascending);

        //optimization benchmark thing
        //System.out.println("Time: " + (System.nanoTime() - start) / 1000 + " μs");

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

    public void setTickSpeed(long x){
        ticksPerFrame = x;
    }

    public long getTickSpeed(){
        return ticksPerFrame;
    }

    public void enableFirstApside(){
        apsideLoopCount = 0;
    }

    public int getApsideLoopCount(){
        return apsideLoopCount;
    }

    public double getRealTime(){
        return realTime;
    }

    public double getDistance(int i1, int i2){
        return Math.sqrt((Math.pow(physicsList.get(i1).getLocx() - physicsList.get(i2).getLocx(), 2) + Math.pow(physicsList.get(i1).getLocy() - physicsList.get(i2).getLocy(), 2)));
    }

    public double getVelocity(int i1){
        return Math.sqrt((Math.pow(physicsList.get(i1).getVelx(), 2) + Math.pow(physicsList.get(i1).getVely(), 2)));
    }

    public double getCentripedal(int i1){
        return 66743 * physicsList.get(guessHost(i1)).getMass() * physicsList.get(i1).getMass() / (Math.pow(getDistance(i1, guessHost(i1)), 2));   
    }

    public double getHyp(double i1, double i2){
        return Math.sqrt(Math.pow(i1, 2) + Math.pow(i2, 2));
    }

    public void updateStartingPhysicsList(){
        for(int i = 0; i < physicsList.size(); i++){
            startingPhysicsList.add(new GravBody(physicsList.get(i)));
        }
    }

    public ArrayList<GravBody> getStartingPhysicsList(){
        return this.startingPhysicsList;
    }

    public int guessHost(int i1){
        int guess = 0;
        double gravityThing = 0;
        for(int i = 0; i < physicsList.size(); i++){
            if(i1 != i){
                if((physicsList.get(i).getMass() / Math.pow(getDistance(i1, i), 2)) > gravityThing){
                    guess = i;
                    gravityThing = physicsList.get(i).getMass() / Math.pow(getDistance(i1, i), 2);
                }
            }
        }
        return guess;
    }
}

