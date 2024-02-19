// import javax.swing.JFrame;
// import java.awt.event.MouseWheelListener;
// import java.awt.BorderLayout;
import java.awt.Color;
// import java.awt.Dimension;
// import java.awt.Font;
// import java.awt.GridBagConstraints;
// import java.awt.GridBagLayout;
// import java.awt.event.MouseEvent;
// import java.awt.event.MouseListener;
// import java.awt.event.MouseMotionListener;
// import java.awt.event.MouseWheelEvent;
// import javax.swing.JLabel;

public class App {

    //silly gameloop physics stuff that needed to be static
    double leftover = 0;
    int ticksPerFrame = 10;
    //JFrame frame = new JFrame();
    Physics physicsSim = new Physics(4, 32000);
    //GPanel gamePanel = new GPanel(physicsSim, frame);
    //MPanel menuPanel = new MPanel(physicsSim, gamePanel);
    //JLabel controlText = new JLabel("<html>Press '1' to hide controls<BR><BR>Pause/Play: SPACE<BR>New object: 'n'<BR>Edit object: 'e'<BR>Clear trails: 'c'<BR>Delete bodies: 'd'<BR>Refocus view: 'v'<BR>Focus on body: 'f'<BR>Save state: 's'<BR>Revert state: 'r'<BR>Cycle selection backwards: LEFT<BR>Cycle selection forwards: RIGHT<BR>Enable two-body analytics: 'a'</html>");
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

    public void run(){

        //physicsSim.setGPanel(gamePanel);
        
        //makes the basic variables and stuff

        //game loop variables
        long startNanoTime = 0;
        long newNanoTime = 0;
        long timePerFrame = 1000000000/60;
        long deltaTime;
        long startOfLastFrame;
        int delay;

        //frame setup
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setSize(1200, 760);
        // frame.setLocationRelativeTo(null);
        // frame.setTitle("GravitySim");
        // frame.setVisible(true);
        // frame.add(gamePanel, BorderLayout.CENTER);
        // frame.add(menuPanel, BorderLayout.NORTH);

        // controlText.setFont(new Font("Sans Serif", Font.BOLD, 18));
        // //controlText.setBounds(5, -480, 500, 1000);

        // GridBagConstraints gbc = new GridBagConstraints();
        // gbc.anchor = GridBagConstraints.NORTHWEST;
        // gbc.weightx = 1;
        // gbc.weighty =1;

        // //game panel setup
        // gamePanel.setDimensions(1200, 700);
        // gamePanel.setLayout(new GridBagLayout());
        // gamePanel.setZoom(50000);
        // gamePanel.setFocusable(true);
        // gamePanel.requestFocus();
        // gamePanel.addMouseWheelListener(new WheelThing());
        // gamePanel.addMouseListener(new MiddleClickPan());
        // gamePanel.addMouseMotionListener(new MouseDragged());
        // gamePanel.add(controlText, gbc);

        // //menu panel setup
        // menuPanel.setPreferredSize(new Dimension(1200, 60));
        // menuPanel.setBackground(new Color(150, 150, 150));

        //mass is in e21kg, radius is km, velocity is m/s, loc is in km

        physicsSim.getPhysicsList().add(new GravBody(0,0, 0, 0, 100, 1000, false, Color.BLACK));
        physicsSim.getPhysicsList().add(new GravBody(500,0, 0, 20000, 1, 500, false, Color.RED));

        //here is the solar system
        // physicsSim.getPhysicsList().add(new GravBody(0, 0, 0, 0, 1988500000, 695700, true, Color.YELLOW)); //sun
        // physicsSim.getPhysicsList().add(new GravBody(-58980, 0, 0, -46000000, 330, 2440, false, Color.LIGHT_GRAY)); //mercury
        // physicsSim.getPhysicsList().add(new GravBody(34790, 0, 0, 108940000, 4868, 6052, false, Color.ORANGE)); //venus
        // physicsSim.getPhysicsList().add(new GravBody(0, 30290, -147100000, 0, 5972, 6372, false, Color.BLUE)); //earth
        // physicsSim.getPhysicsList().add(new GravBody(1022, 30290, -147100000, 405000, 73,1737, false, Color.GRAY)); //moon
        // physicsSim.getPhysicsList().add(new GravBody(0, -26500, 206650000, 0, 642,3389, false, Color.RED)); //mars
        // physicsSim.getPhysicsList().add(new GravBody(13720, 0, 0, 740959000, 1898130, 69911, false, Color.ORANGE)); //jupiter
        // physicsSim.getPhysicsList().add(new GravBody(-9140, 0, 0, -1506527000, 568320, 58232, false, Color.YELLOW)); //saturn
        // physicsSim.getPhysicsList().add(new GravBody(0, 7130, -2732696000d, 0, 86811, 25362, false, Color.CYAN)); //uranus
        // physicsSim.getPhysicsList().add(new GravBody(0, -5370, 4558857000d, 0, 102409, 24622, false, Color.BLUE)); // neptune

        // physicsSim.getPhysicsList().add(new GravBody(0, 0, 0, 0, 1000, 500, false, Color.BLACK));
        // physicsSim.getPhysicsList().add(new GravBody(0, 1000, 50000, 0, 1, 100, false, Color.BLUE));

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
            // gamePanel.repaint();
            // if(!gamePanel.getShowControls() == showControls){
            //     if(gamePanel.getShowControls()){
            //         controlText.setText("<html>Press '1' to hide controls<BR><BR>Pause/Play: SPACE<BR>New object: 'n'<BR>Edit object: 'e'<BR>Clear trails: 'c'<BR>Delete bodies: 'd'<BR>Refocus view: 'v'<BR>Focus on body: 'f'<BR>Save state: 's'<BR>Revert state: 'r'<BR>Cycle selection backwards: LEFT<BR>Cycle selection forwards: RIGHT<BR>Enable two-body analytics: 'a'<BR>Switch trail draw mode: 't'<BR>Increase speed: UP<BR>Decrease speed: DOWN</html>");
            //     }else{
            //         controlText.setText("Press '1' to show controls");
            //     }
            //     showControls = gamePanel.getShowControls();
            // }

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

//     class WheelThing implements MouseWheelListener{

//         @Override
//         public void mouseWheelMoved(MouseWheelEvent e) {
//             if (e.getWheelRotation() < 0) {

//                 realx = getRealX(e);
//                 realy = getRealY(e);

//                 gamePanel.setZoom(gamePanel.getZoom() * (double) (1 / 1.5));

//                 diffx = (getRealX(e) - realx);
//                 diffy = (getRealY(e) - realy);

//                 gamePanel.setOffsetx(gamePanel.getOffsetx() + (int) (diffx / gamePanel.getZoom()));
//                 gamePanel.setOffsety(gamePanel.getOffsety() + (int) (diffy / gamePanel.getZoom()));

//             } else {
                
//                 realx = getRealX(e);
//                 realy = getRealY(e);

//                 gamePanel.setZoom(gamePanel.getZoom() *(1.5));

//                 diffx = (getRealX(e) - realx);
//                 diffy = (getRealY(e) - realy);

//                 gamePanel.setOffsetx(gamePanel.getOffsetx() + (int) (diffx / gamePanel.getZoom()));
//                 gamePanel.setOffsety(gamePanel.getOffsety() + (int) (diffy / gamePanel.getZoom()));
                
//             }
//         }

//     }

//     class MiddleClickPan implements MouseListener{

//         @Override
//         public void mouseClicked(MouseEvent e) {

//             //add something to focus and stay on certain object, don't know how to do the stay part rn so leaving it for later
//             if(e.getButton() == MouseEvent.BUTTON1){
//                 selectedSoFar = false;
//                 for(int i = 0; i < physicsSim.getPhysicsList().size(); i++){
//                     double locx = physicsSim.getPhysicsList().get(i).getLocx() / gamePanel.getZoom() + gamePanel.getOffsetx();
//                     double locy = physicsSim.getPhysicsList().get(i).getLocy() / gamePanel.getZoom() + gamePanel.getOffsety();
//                     int radius = (int) Math.sqrt(Math.pow(locx - e.getX(), 2) + Math.pow(locy - e.getY(), 2));

//                     if(radius < physicsSim.getPhysicsList().get(i).getRadius() / gamePanel.getZoom()){

//                         if(gamePanel.getTwoBodyAnalytics() && gamePanel.getSelected()){
//                             //honestly don't know what this is
//                             if(gamePanel.getObjectSelected() == i){

//                             }else{
//                                 //gamePanel.setTwoBodyAnalytics(false);
//                                 gamePanel.setFirstBody(gamePanel.getObjectSelected());
//                                 gamePanel.setSecondBody(i);
//                                 System.out.println(gamePanel.getFirstBody()+ ", " + gamePanel.getSecondBody());
//                             }

//                             selectedSoFar = true;
//                         }else{
                            
//                             gamePanel.setObjectSelected(i);
//                             selectedSoFar = true;
    
//                             if(gamePanel.getFocused()){
//                                 gamePanel.setObjectFocus(i);
//                             }
    
//                         }

//                     }else{
//                         //gamePanel.setSelected(false);
//                     }

//                 }
//                 if(selectedSoFar){
//                     gamePanel.setSelected(true);
//                     menuPanel.getEditButton().setEnabled(true);
//                     menuPanel.getFocusButton().setEnabled(true);
//                 }else{
//                     gamePanel.setSelected(false);
//                     menuPanel.getEditButton().setEnabled(false);

//                     //make sure button isn't grayed out if focused
//                     if(!gamePanel.getFocused()){
//                         menuPanel.getFocusButton().setEnabled(false);
//                     }
//                 }
//             }  
            
//         }  
//         public void mouseEntered(MouseEvent e) {  
              
//         }  
//         public void mouseExited(MouseEvent e) {  
              
//         }  
//         public void mousePressed(MouseEvent e) {  

//         }  
//         public void mouseReleased(MouseEvent e) {  
//             movementCounter = 0;

//         }  

//     }

//     class MouseDragged implements MouseMotionListener{

//         @Override
//         public void mouseDragged(MouseEvent arg0) {
//             if(movementCounter == 0){
//                 mouseLocx = arg0.getX();
//                 mouseLocy = arg0.getY();
//             }

//             panDeltax = arg0.getX() - mouseLocx;
//             panDeltay = arg0.getY() - mouseLocy;

//             mouseLocx = arg0.getX();
//             mouseLocy = arg0.getY();

//             gamePanel.setOffsetx(gamePanel.getOffsetx() + panDeltax);
//             gamePanel.setOffsety(gamePanel.getOffsety() + panDeltay);

//             menuPanel.focusButton.setText("Focus on Body");
//             gamePanel.setFocused(false);

//             movementCounter++;
//         }

//         @Override
//         public void mouseMoved(MouseEvent arg0) {
            
//         }
        
//     }

//     public double getRealX(MouseEvent e){
//         return (e.getX() - gamePanel.getOffsetx()) * gamePanel.getZoom();
//     }

//     public double getRealY(MouseEvent e){
//         return (e.getY() - gamePanel.getOffsety()) * gamePanel.getZoom();
//     }
     }





