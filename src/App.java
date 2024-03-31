import javax.swing.JFrame;
import java.awt.event.MouseWheelListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import javax.swing.JLabel;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.NumberFormat;
import java.util.Scanner;

public class App {

    //silly gameloop physics stuff that needed to be static
    double leftover = 0;
    int ticksPerFrame = 10;
    JFrame frame = new JFrame();
    Physics physicsSim = new Physics(1000, 2000);
    GPanel gamePanel = new GPanel(physicsSim, frame);
    MPanel menuPanel = new MPanel(physicsSim, gamePanel);
    JLabel controlText = new JLabel("<html>Press '1' to hide controls<BR><BR>Pause/Play: SPACE<BR>New object: 'n'<BR>Edit object: 'e'<BR>Clear trails: 'c'<BR>Delete bodies: 'd'<BR>Refocus view: 'v'<BR>Focus on body: 'f'<BR>Save state: 's'<BR>Revert state: 'r'<BR>Cycle selection backwards: LEFT<BR>Cycle selection forwards: RIGHT</html>");
    JLabel infoText = new JLabel("");
    int zoomMulti = 10;
    int panDeltax = 0;
    int panDeltay = 0;
    int mouseLocx;
    int mouseLocy;
    int movementCounter = 0;
    double realx;
    double realy;
    double diffx;
    double diffy;
    int arrayIterator = 0;
    boolean showControls;
    boolean selectedSoFar = false;
    boolean wasSelectedLastLoop = false;
    

    public void run() throws IOException{
        checkFiles();

        physicsSim.setGPanel(gamePanel);
        
        //makes the basic variables and stuff

        //game loop variables
        long startNanoTime = 0;
        long newNanoTime = 0;
        long timePerFrame = 1000000000/60;
        long deltaTime;
        long startOfLastFrame;
        int delay;

        //frame setup
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 760);
        frame.setLocationRelativeTo(null);
        frame.setTitle("GravitySim");
        frame.setVisible(true);
        frame.add(gamePanel, BorderLayout.CENTER);
        frame.add(menuPanel, BorderLayout.NORTH);

        controlText.setFont(new Font("Sans Serif", Font.BOLD, 18));
        infoText.setFont(new Font("Sans Serif", Font.BOLD, 16));
        //controlText.setBounds(5, -480, 500, 1000);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1;
        gbc.weighty = 1;

        //game panel setup
        gamePanel.setDimensions(1200, 700);
        gamePanel.setLayout(new GridBagLayout());
        //gamePanel.setZoom(50000);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();
        gamePanel.addMouseWheelListener(new WheelThing());
        gamePanel.addMouseListener(new MiddleClickPan());
        gamePanel.addMouseMotionListener(new MouseDragged());
        gamePanel.add(controlText, gbc);

        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gamePanel.add(infoText, gbc);

        //menu panel setup
        menuPanel.setPreferredSize(new Dimension(1200, 60));
        menuPanel.setBackground(new Color(150, 150, 150));

        //physics loop
        startNanoTime = System.nanoTime();
        while(true){
            //makes the gameloop
            startOfLastFrame = startNanoTime;
            startNanoTime = System.nanoTime();
            deltaTime = startNanoTime - startOfLastFrame;

            //System.out.println(deltaTime);
            //does the gameloop
            physicsSim.tick(deltaTime);
            gamePanel.repaint();

            //text display checks
            if(!gamePanel.getShowControls() == showControls){
                if(gamePanel.getShowControls()){
                    controlText.setText("<html>Press '1' to hide controls<BR><BR>" + 
                                        "Pause/Play: SPACE<BR>" +
                                        "New object: 'n'<BR>" + 
                                        "Edit object: 'e'<BR>" + 
                                        "Clear trails: 'c'<BR>" + 
                                        "Clear apsides: 'q'<BR>" + 
                                        "Delete bodies: 'd'<BR>" + 
                                        "Refocus view: 'v'<BR>" + 
                                        "Focus on body: 'f'<BR>" + 
                                        "Save state: 's'<BR>" + 
                                        "Revert state: 'r'<BR>" + 
                                        "Cycle selection backwards: LEFT<BR>" + 
                                        "Cycle selection forwards: RIGHT<BR>" + 
                                        "Switch trail draw mode: 't'<BR>" + 
                                        "Increase speed: UP<BR>" + 
                                        "Decrease speed: DOWN<BR>" + 
                                        "Enable relative reference frame: 'l'<BR>" + 
                                        "Write system to file: 'w'<BR>" + 
                                        "Reset system to start: 'k'</html>");
                }else{
                    controlText.setText("Press '1' to show controls");
                }
                showControls = gamePanel.getShowControls();
            }
            if(gamePanel.getSelected()){
                int i = gamePanel.getObjectSelected();
                int infoVelocity = (int) physicsSim.getVelocity(i);
                long infoDistance = (long) (physicsSim.getDistance(i, physicsSim.guessHost(i)) / 1000);
                long infoCentripedal = (long) (physicsSim.getCentripedal(i));

                //stuff about apsides
                String infoApo;
                String infoPeri;
                String infoPeriod = "Undetermined";
                if(gamePanel.getApsideList().size() == 0){
                    infoApo = "Undetermined";
                    infoPeri = "Undetermined";
                }else if(gamePanel.getApsideList().size() == 1){
                    if(gamePanel.getApsideList().get(gamePanel.getApsideList().size() - 1).getApo()){
                        infoApo = NumberFormat.getIntegerInstance().format((gamePanel.getApsideList().get(gamePanel.getApsideList().size() - 1).getDistance())) + " km";
                        infoPeri = "Undetermined";
                    }else{
                        infoApo = "Undetermined";
                        infoPeri = NumberFormat.getIntegerInstance().format((gamePanel.getApsideList().get(gamePanel.getApsideList().size() - 1).getDistance())) + " km";
                    }
                }else{
                    if(gamePanel.getApsideList().get(gamePanel.getApsideList().size() - 1).getApo()){
                        infoApo = NumberFormat.getIntegerInstance().format((gamePanel.getApsideList().get(gamePanel.getApsideList().size() - 1).getDistance())) + " km";
                        infoPeri = NumberFormat.getIntegerInstance().format((gamePanel.getApsideList().get(gamePanel.getApsideList().size() - 2).getDistance())) + " km";
                    }else{
                        infoApo = NumberFormat.getIntegerInstance().format((gamePanel.getApsideList().get(gamePanel.getApsideList().size() - 2).getDistance())) + " km";
                        infoPeri = NumberFormat.getIntegerInstance().format((gamePanel.getApsideList().get(gamePanel.getApsideList().size() - 1).getDistance())) + " km";
                    }
                }
                if(gamePanel.getApsideList().size() > 2){
                    //infoPeriod = NumberFormat.getIntegerInstance().format(physicsSim.getRealTime()) + " s";
                    infoPeriod = formatPeriod(physicsSim.getRealTime());
                }
                String c = NumberFormat.getIntegerInstance().format(infoCentripedal);
                String d = NumberFormat.getIntegerInstance().format(infoDistance);
                infoText.setText("<html>............................................................<BR><html>" + physicsSim.getPhysicsList().get(i).getName() + "<html> information:<BR>Velocity: <html>" + infoVelocity + " m/s" + "<html><BR>Distance: <html>" + d + " km" + "<html><BR>Centripedal force: <html>" + c + " N" + "<html><BR>Apoapsis: <html>" + infoApo + "<html><BR>Periapsis: <html>" + infoPeri + "<html><BR>Period: <html>" + infoPeriod);
                wasSelectedLastLoop = true;

            }else if(wasSelectedLastLoop){
                wasSelectedLastLoop = false;
                infoText.setText("");
            }

            //self-explanatory
            if(!physicsSim.getPaused() && (menuPanel.getEFrameList().size() > 0)){
                for(int i = 0; i < menuPanel.getEFrameList().size(); i++){
                    menuPanel.getEFrameList().get(i).UpdateBoxes();
                }
            }

            newNanoTime = System.nanoTime();
            delay = (int) (timePerFrame - (newNanoTime - startNanoTime));

            if(delay > 0){
                wait(delay);
            } else{
                System.out.println("Simulation running behind");
            }

        }
        
        
    }

    

    public static void wait(int nanos)
	{
	    try
	    {
	        Thread.sleep(nanos / 1000000);
	    }
	    catch(InterruptedException ex)
	    {
	        Thread.currentThread().interrupt();
	    }
	}

    class WheelThing implements MouseWheelListener{

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {

            //zToggle is for rotational offset
            if(menuPanel.getZToggle()){
                //gamePanel.repaint();
                if (e.getWheelRotation() < 0) {
                    gamePanel.setRotationalOffset(gamePanel.getRotationalOffset() - (Math.PI / 20));
    
                } else {
                    gamePanel.setRotationalOffset(gamePanel.getRotationalOffset() + (Math.PI / 20));
                    
                }
            }else{
                if (e.getWheelRotation() < 0) {

                    realx = getRealX(e);
                    realy = getRealY(e);
    
                    gamePanel.setZoom(gamePanel.getZoom() * (double) (1 / 1.5));
    
                    diffx = (getRealX(e) - realx);
                    diffy = (getRealY(e) - realy);
    
                    gamePanel.setOffsetx(gamePanel.getOffsetx() + (int) (diffx / gamePanel.getZoom()));
                    gamePanel.setOffsety(gamePanel.getOffsety() + (int) (diffy / gamePanel.getZoom()));
    
                } else {
                    
                    realx = getRealX(e);
                    realy = getRealY(e);
    
                    gamePanel.setZoom(gamePanel.getZoom() *(1.5));
    
                    diffx = (getRealX(e) - realx);
                    diffy = (getRealY(e) - realy);
    
                    gamePanel.setOffsetx(gamePanel.getOffsetx() + (int) (diffx / gamePanel.getZoom()));
                    gamePanel.setOffsety(gamePanel.getOffsety() + (int) (diffy / gamePanel.getZoom()));
                    
                }
            }
            
        }

    }

    class MiddleClickPan implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {

            //add something to focus and stay on certain object, don't know how to do the stay part rn so leaving it for later
            //this has been completed but the above comment will be left for the sake of historicity
            if(e.getButton() == MouseEvent.BUTTON1){
                selectedSoFar = false;
                for(int i = 0; i < physicsSim.getPhysicsList().size(); i++){
                    double locx = gamePanel.rotateObjectX(physicsSim.getPhysicsList().get(i), gamePanel.getRotationalOffset()) / gamePanel.getZoom() + gamePanel.getOffsetx();
                    double locy = gamePanel.rotateObjectY(physicsSim.getPhysicsList().get(i), gamePanel.getRotationalOffset()) / gamePanel.getZoom() + gamePanel.getOffsety();
                    int radius = (int) Math.sqrt(Math.pow(locx - e.getX(), 2) + Math.pow(locy - e.getY(), 2));

                    if(radius < physicsSim.getPhysicsList().get(i).getRadius() / gamePanel.getZoom()){
                            
                        gamePanel.setObjectSelected(i);
                        System.out.println(physicsSim.getPhysicsList().get(i).getName());
                        physicsSim.enableFirstApside();
                        selectedSoFar = true;
    
                        if(gamePanel.getFocused()){
                            gamePanel.setObjectFocus(i);
                        }
    
                    }

                }
                if(selectedSoFar){
                    gamePanel.setSelected(true);
                    menuPanel.getEditButton().setEnabled(true);
                    menuPanel.getFocusButton().setEnabled(true);
                }else{
                    gamePanel.setSelected(false);
                    menuPanel.getEditButton().setEnabled(false);
                    menuPanel.getFocusButton().setEnabled(false);
                    gamePanel.clearApside();

                    //make sure button isn't grayed out if focused
                    if(!gamePanel.getFocused()){
                        menuPanel.getFocusButton().setEnabled(false);
                    }
                }
            }  
            
        }  
        public void mouseEntered(MouseEvent e) {  
              
        }  
        public void mouseExited(MouseEvent e) {  
              
        }  
        public void mousePressed(MouseEvent e) {  

        }  
        public void mouseReleased(MouseEvent e) {  
            movementCounter = 0;

        }  

    }

    class MouseDragged implements MouseMotionListener{

        @Override
        public void mouseDragged(MouseEvent arg0) {
            if(movementCounter == 0){
                mouseLocx = arg0.getX();
                mouseLocy = arg0.getY();
            }

            panDeltax = arg0.getX() - mouseLocx;
            panDeltay = arg0.getY() - mouseLocy;

            mouseLocx = arg0.getX();
            mouseLocy = arg0.getY();

            gamePanel.setOffsetx(gamePanel.getOffsetx() + panDeltax);
            gamePanel.setOffsety(gamePanel.getOffsety() + panDeltay);

            menuPanel.focusButton.setText("Focus on Body");
            gamePanel.setFocused(false);

            movementCounter++;
        }

        @Override
        public void mouseMoved(MouseEvent arg0) {
            
        }
        
    }

    public double getRealX(MouseEvent e){
        return (e.getX() - gamePanel.getOffsetx()) * gamePanel.getZoom();
    }

    public double getRealY(MouseEvent e){
        return (e.getY() - gamePanel.getOffsety()) * gamePanel.getZoom();
    }

    public void checkFiles(){
        Scanner scan;

        int lineReached = 0;

        boolean firstCheckLoop = true;

        //starting config:
        String configDefault = 
        "#Tick speed:\n1000\n\n" + 
        "#Simulation speed:\n50000\n\n" + 
        "#Starting zoom:\n1000000000\n\n" + 
        "#Celestial bodies Format: Velx (m/s) Vely (m/s) Posx (km) Posy (km) Mass (e21kg) Radius (km) Fixed(true/false) Color (r g b) Name" +
        "\n0 0 0 0 1988500000 695700 true 255 255 0 Sun" +
        "\n-58980 0 0 -46000000 330 2440 false 192 192 192 Mercury" +
        "\n34790 0 0 108940000 4868 6052 false 255 165 0 Venus" +
        "\n0 30290 -147100000 0 5972 6372 false 0 0 255 Earth" +
        "\n1022 30290 -147100000 405000 73 1737 false 128 128 128 Moon" +
        "\n0 -26500 206650000 0 642 3389 false 255 0 0 Mars" +
        "\n13720 0 0 740959000 1898130 69911 false 255 165 0 Jupiter" +
        "\n-9140 0 0 -1506527000 568320 58232 false 255 255 0 Saturn" +
        "\n0 7130 -2732696000 0 86811 25362 false 0 255 255 Uranus" +
        "\n0 -5370 4558857000 0 102409 24622 false 0 0 255 Neptune";

        String currentDirectory = System.getProperty("user.dir");

        // Create a File object for the current directory
        File directory = new File(currentDirectory);

        // Get a list of files in the directory
        File[] files = directory.listFiles();

        Path path = Path.of("config.txt");

        boolean configFound = false;
        // Display the list of files
        if (files != null) {
            for (File file : files) {
                if(file.getName().equals("config.txt")){
                    configFound = true;
                }
            }
        }
        if(!configFound){
            try {
                // Create the file and write the content to it
                Files.write(path, configDefault.getBytes(), StandardOpenOption.CREATE);
    
                System.out.println("Config file recreated successfully.");
            } catch (IOException e) {
                // Handle file creation error
                System.err.println("Error creating the file: " + e.getMessage());
            }
        }

        File config = new File(currentDirectory + "/config.txt");
        System.out.println(config);

        try {
            scan = new Scanner(config);
            //System.out.println("ok");
            
            //gets tick speed
            while (scan.hasNext()) {
                String lineOfText = scan.nextLine();
                lineReached++;
            
                if(lineOfText.startsWith("#")) {
                    continue;
                }
                if(lineOfText.equals("")){
                    break;
                }
                physicsSim.setTickSpeed(Integer.parseInt(lineOfText));
            }

            //gets speed
            while (scan.hasNext()) {
                String lineOfText = scan.nextLine();
                lineReached++;
            
                if(lineOfText.startsWith("#")) {
                    continue;
                }
                if(lineOfText.equals("") && !firstCheckLoop){
                    break;
                }
                physicsSim.setSpeed(Integer.parseInt(lineOfText));
                firstCheckLoop = false;
            }
            System.out.println("Loaded speeds");

            //gets zoom
            while (scan.hasNext()) {
                String lineOfText = scan.nextLine();
                lineReached++;
            
                if(lineOfText.startsWith("#")) {
                    continue;
                }
                if(lineOfText.equals("") && !firstCheckLoop){
                    break;
                }
                gamePanel.setZoom(Double.parseDouble(lineOfText));
                gamePanel.setDefaultZoom(Double.parseDouble(lineOfText));
                firstCheckLoop = false;
            }
            System.out.println("Loaded zoom");

            @SuppressWarnings("unused")
            String lineOfText = scan.nextLine();
            lineReached++;

            //adds the bodies
            while (scan.hasNext()) {
                lineReached++;
                physicsSim.getPhysicsList().add(new GravBody(scan.nextDouble(), scan.nextDouble(), scan.nextDouble(), scan.nextDouble(), scan.nextDouble(), scan.nextDouble(), scan.nextBoolean(), new Color(scan.nextInt(), scan.nextInt(), scan.nextInt()), scan.next()));
            }
            System.out.println("Loaded all celestial bodies (" + physicsSim.getPhysicsList().size() + ")");

            physicsSim.updateStartingPhysicsList();
    
        } catch (Exception e1) {

            //helpful messages hopefully
            System.out.println("Error at config line " + lineReached);
            e1.printStackTrace();
            System.exit(0);
        }
    }

    public String formatPeriod(double inputTime){

        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        int remainder = (int) Math.floor(inputTime);

        //looks at the size and stuff
        if(inputTime > 86400){
            days = (int) Math.floor(inputTime / 86400);
            remainder = (int) Math.floor(inputTime % 86400);
        }

        if(remainder > 3600){
            hours = (int) Math.floor(remainder / 3600);
            remainder = (int) Math.floor(remainder % 3600);
        }

        if(remainder > 60){
            minutes = (int) Math.floor(remainder / 60);
            remainder = (int) Math.floor(remainder % 60);
        }

        seconds = remainder;

        return days + "d, " + hours + "h, " + minutes + "m, " + seconds + "s";
    }
}





