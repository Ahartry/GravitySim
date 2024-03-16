import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class MPanel extends JPanel{

    Action newAction;
    Action editAction;
    Action pauseAction;
    Action controlsAction;
    Action clearTrailAction;
    Action clearBodiesAction;
    Action refocusAction;
    Action focusAction;
    Action saveAction;
    Action revertAction;
    Action backwardCycleAction;
    Action forwardCycleAction;
    Action removeApsisAction;
    Action trailDrawAction;
    Action increaseSpeedAction;
    Action decreaseSpeedAction;
    Action zToggleAction;
    Action lagrangeAction;
    Action saveSystemAction;

    GButton newButton;
    GButton editButton;
    GButton saveButton;
    GButton revertButton;
    GButton refocusButton;
    GButton focusButton;

    KeyListener listener;

    Physics physics;
    GPanel gpanel;

    ArrayList<GravBody> savedList = new ArrayList<>();
    ArrayList<Trail> trailList = new ArrayList<>();
    ArrayList<Apside> apsideList = new ArrayList<>();
    int savedFirstBody;
    int savedSecondBody;
    boolean savedLagrange;
    boolean savedAnalysis;

    int buttonWidth = 190;
    boolean zToggle = false;

    public MPanel(Physics p, GPanel g){
        //adds the actions
        this.setLayout(new GridBagLayout());

        this.newAction = new newAction();
        this.editAction = new editAction();
        this.pauseAction = new pauseAction();
        this.controlsAction = new controlsAction();
        this.clearTrailAction = new clearTrailAction();
        this.clearBodiesAction = new clearBodiesAction();
        this.refocusAction = new refocusAction();
        this.focusAction = new focusAction();
        this.saveAction = new saveAction();
        this.revertAction = new revertAction();
        this.backwardCycleAction = new backwardCycleAction();
        this.forwardCycleAction = new forwardCycleAction();
        this.removeApsisAction = new removeApsisAction();
        this.trailDrawAction = new trailDrawAction();
        this.increaseSpeedAction = new increaseSpeedAction();
        this.decreaseSpeedAction = new decreaseSpeedAction();
        this.zToggleAction = new zToggleAction();
        this.lagrangeAction = new lagrangeAction();
        this.saveSystemAction = new saveSystemAction();

        //adds the buttons
        this.newButton = new GButton("New");
        this.editButton = new GButton("Edit");
        this.saveButton = new GButton("Save State");
        this.revertButton = new GButton("Revert State");
        this.refocusButton = new GButton("Reset to Center");
        this.focusButton = new GButton("Focus on Body");

        //puts the action listeners to the thing or something (I don't know how these work)
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('n'), "newAction");
        this.getActionMap().put("newAction", newAction);
        
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"), "pauseAction");
        this.getActionMap().put("pauseAction", pauseAction);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('c'), "clearTrailAction");
        this.getActionMap().put("clearTrailAction", clearTrailAction);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('d'), "clearBodiesAction");
        this.getActionMap().put("clearBodiesAction", clearBodiesAction);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('v'), "refocusAction");
        this.getActionMap().put("refocusAction", refocusAction);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('f'), "focusAction");
        this.getActionMap().put("focusAction", focusAction);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('e'), "editAction");
        this.getActionMap().put("editAction", editAction);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('1'), "controlsAction");
        this.getActionMap().put("controlsAction", controlsAction);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('s'), "saveAction");
        this.getActionMap().put("saveAction", saveAction);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('r'), "revertAction");
        this.getActionMap().put("revertAction", revertAction);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"), "backwardCycleAction");
        this.getActionMap().put("backwardCycleAction", backwardCycleAction);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "forwardCycleAction");
        this.getActionMap().put("forwardCycleAction", forwardCycleAction);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('q'), "removeApsisAction");
        this.getActionMap().put("removeApsisAction", removeApsisAction);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('t'), "trailDrawAction");
        this.getActionMap().put("trailDrawAction", trailDrawAction);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"), "increaseSpeedAction");
        this.getActionMap().put("increaseSpeedAction", increaseSpeedAction);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "decreaseSpeedAction");
        this.getActionMap().put("decreaseSpeedAction", decreaseSpeedAction);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('z'), "zToggleAction");
        this.getActionMap().put("zToggleAction", zToggleAction);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('l'), "lagrangeAction");
        this.getActionMap().put("lagrangeAction", lagrangeAction);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('w'), "saveSystemAction");
        this.getActionMap().put("saveSystemAction", saveSystemAction);

        //sets up the buttons
        newButton.addActionListener(new newAction());
		newButton.setPreferredSize(new Dimension(buttonWidth, 50));
		newButton.setFocusable(false);

        editButton.addActionListener(new editAction());
		editButton.setPreferredSize(new Dimension(buttonWidth, 50));
		editButton.setFocusable(false);
        editButton.setEnabled(false);

        refocusButton.addActionListener(new refocusAction());
		refocusButton.setPreferredSize(new Dimension(buttonWidth, 50));
		refocusButton.setFocusable(false);

        focusButton.addActionListener(new focusAction());
		focusButton.setPreferredSize(new Dimension(buttonWidth, 50));
		focusButton.setFocusable(false);
        focusButton.setEnabled(false);

        saveButton.addActionListener(new saveAction());
		saveButton.setPreferredSize(new Dimension(buttonWidth, 50));
		saveButton.setFocusable(false);

        revertButton.addActionListener(new revertAction());
		revertButton.setPreferredSize(new Dimension(buttonWidth, 50));
		revertButton.setFocusable(false);
        revertButton.setEnabled(false);

        //adds the buttons to the panel
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5, 5, 5, 5);
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;

        c.gridx = 0;
        this.add(newButton, c);

        c.gridx = 1;
        this.add(editButton, c);

        c.gridx = 2;
        this.add(saveButton, c);

        c.gridx = 3;
        this.add(revertButton, c);

        c.gridx = 4;
        this.add(refocusButton, c);

        c.gridx = 5;
        this.add(focusButton, c);

        this.physics = p;
        this.gpanel = g;

        setFocusable(true);
        requestFocusInWindow();
    }

    public void setFocusText(String string){
        focusButton.setText(string);
    }

    public class newAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent arg0) {
            //EFrame frame = new EFrame(new GravBody(0, 0, 0, 0, 0, 0, false));
        }
        
    }

    public class editAction extends AbstractAction{

        public void actionPerformed(ActionEvent arg0) {
            
            throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
        }
        
    }

    public class pauseAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent arg0) {
            physics.setPaused(!physics.getPaused());
            if(physics.getPaused()){
                gpanel.setJustPaused(true);
            }
        }
        
    }

    public class saveAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent arg0) {

            savedList = new ArrayList<GravBody>();
            trailList = new ArrayList<Trail>();
            apsideList = new ArrayList<Apside>();
            
            //does the adding to backup
            for(int i = 0; i < physics.getPhysicsList().size(); i++){
                savedList.add(new GravBody(physics.getPhysicsList().get(i)));
            }
            for(int i = 0; i < gpanel.getTrailList().size(); i++){
                trailList.add(new Trail(gpanel.getTrailList().get(i)));
            }
            for(int i = 0; i < gpanel.getApsideList().size(); i++){
                apsideList.add(new Apside(gpanel.getApsideList().get(i)));
            }

            savedAnalysis = gpanel.getTwoBodyAnalytics();
            savedLagrange = gpanel.getLagrange();
            savedFirstBody = gpanel.getFirstBody();
            savedSecondBody = gpanel.getSecondBody();

            revertButton.setEnabled(true);
        }
        
    }

    public class revertAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent arg0) {
            if(savedList.size() > 0){
                physics.getPhysicsList().clear();
                gpanel.getTrailList().clear();
                gpanel.getApsideList().clear();
    
                for(int i = 0; i < savedList.size(); i++){
                    physics.getPhysicsList().add(new GravBody(savedList.get(i)));
                }
                for(int i = 0; i < trailList.size(); i++){
                    gpanel.getTrailList().add(new Trail(trailList.get(i)));
                }
                for(int i = 0; i < apsideList.size(); i++){
                    gpanel.getApsideList().add(new Apside(apsideList.get(i)));
                }

                gpanel.setTwoBodyAnalytics(savedAnalysis);
                gpanel.setLagrange(savedLagrange);
                gpanel.setFirstBody(savedFirstBody);
                gpanel.setSecondBody(savedSecondBody);
            }

            //Collections.copy(savedList, physicsList);
            //System.out.println(savedList.get(0).getLocx());
        }
        
    }

    public class clearTrailAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent arg0) {
            gpanel.clearTrail();
        }
        
    }

    public class clearBodiesAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent arg0) {
            physics.getPhysicsList().clear();
        }
        
    }

    public class refocusAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent arg0) {
            gpanel.setOffsetx(gpanel.getXDimension() / 2);
            gpanel.setOffsety(gpanel.getYDimension() / 2);
            gpanel.setFocused(false);
            focusButton.setText("Focus on Body");
            if(gpanel.getLagrange()){
                gpanel.setLagrange(false);
                gpanel.clearTrail();
            }
        }
        
    }

    public class focusAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent arg0) {
            if(gpanel.getSelected()){
                gpanel.setFocused(!gpanel.getFocused());
                gpanel.setObjectFocus(gpanel.getObjectSelected());
                
                //applies new text
                if(gpanel.getFocused()){
                    focusButton.setText("Unfocus");
                    
                }else{
                    focusButton.setText("Focus on Body");
                    if(!gpanel.getSelected()){
                        focusButton.setEnabled(false);
                    }
                }
            }

        }
        
    }

    public class controlsAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent arg0) {
            gpanel.setShowControls(!gpanel.getShowControls());
        }
        
    }

    public class backwardCycleAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent arg0) {

            if(gpanel.getObjectSelected() < 1){
                gpanel.setObjectSelected(physics.getPhysicsList().size() - 1);
            }else{
                gpanel.setObjectSelected(gpanel.getObjectSelected() - 1);
            }

            gpanel.setSelected(true);
            editButton.setEnabled(true);
            focusButton.setEnabled(true);
            gpanel.clearApside();
            physics.enableFirstApside();

        }
        
    }

    public class forwardCycleAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent arg0) {

            if(gpanel.getObjectSelected() + 2 > physics.getPhysicsList().size()){
                gpanel.setObjectSelected(0);
            }else{
                gpanel.setObjectSelected(gpanel.getObjectSelected() + 1);
            }

            gpanel.setSelected(true);
            editButton.setEnabled(true);
            focusButton.setEnabled(true);
            gpanel.clearApside();
            physics.enableFirstApside();
        }
        
    }

    public class removeApsisAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent arg0){
            gpanel.getApsideList().remove(0);
        }
    }

    public class trailDrawAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent arg0){
            gpanel.setTrailDrawMode(!gpanel.getTrailDrawMode());
        }
    }

    public class increaseSpeedAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent arg0){
            gpanel.increaseSpeed();
        }
    }

    public class decreaseSpeedAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent arg0){
            gpanel.decreaseSpeed();
        }
    }

    public class zToggleAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent arg0){
            zToggle = !zToggle;
            System.out.println(zToggle);
        }
    }

    public class lagrangeAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent arg0){
            gpanel.setLagrange(!gpanel.getLagrange());
            gpanel.setFirstLoopThing(true);
        }
    }

    public class saveSystemAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent arg0){
            Path path = Path.of("config.txt");
            String output = new String("#Tick speed:\n" + physics.getTickSpeed() + "\n\n#Simulation speed:\n" + physics.getSpeed() + "\n\n#Starting zoom:\n" + gpanel.getZoom() + 
            "\n\n#Celestial bodies Format: Velx (m/s) Vely (m/s) Posx (km) Posy (km) Mass (e21kg) Radius (km) Fixed(true/false) Color (r g b)");
            for(int i = 0; i < physics.getPhysicsList().size(); i++){
                output = output + "\n" + physics.getPhysicsList().get(i).getVelx() + " " + physics.getPhysicsList().get(i).getVely() + " " + physics.getPhysicsList().get(i).getLocx() / 1000
                + " " + physics.getPhysicsList().get(i).getLocy() / 1000 + " " + physics.getPhysicsList().get(i).getMass() / 1000000 + " " + physics.getPhysicsList().get(i).getRadius() / 1000
                + " " + physics.getPhysicsList().get(i).getFixed() + " " + physics.getPhysicsList().get(i).getColor().getRed() + " " + physics.getPhysicsList().get(i).getColor().getGreen()
                + " " + physics.getPhysicsList().get(i).getColor().getBlue();
            }
            //System.out.println(output);
            try {
                Files.write(path, output.getBytes(), StandardOpenOption.CREATE);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void setButtonWidth(int x){
        this.buttonWidth = (x / 6) - 10;
    }

    //button getters for access from other classes
    public GButton getEditButton(){
        return editButton;
    }

    public GButton getFocusButton(){
        return focusButton;
    }

    public Boolean getZToggle(){
        return zToggle;
    }

}


