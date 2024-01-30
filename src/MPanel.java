import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
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
    Action twoBodyAnalyticsAction;
    Action removeApsisAction;
    Action trailDrawAction;
    Action increaseSpeedAction;
    Action decreaseSpeedAction;

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

    int buttonWidth = 190;
    
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
        this.twoBodyAnalyticsAction = new twoBodyAnalyticsAction();
        this.removeApsisAction = new removeApsisAction();
        this.trailDrawAction = new trailDrawAction();
        this.increaseSpeedAction = new increaseSpeedAction();
        this.decreaseSpeedAction = new decreaseSpeedAction();

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

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('r'), "revertAction");
        this.getActionMap().put("revertAction", revertAction);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"), "backwardCycleAction");
        this.getActionMap().put("backwardCycleAction", backwardCycleAction);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "forwardCycleAction");
        this.getActionMap().put("forwardCycleAction", forwardCycleAction);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('a'), "twoBodyAnalyticsAction");
        this.getActionMap().put("twoBodyAnalyticsAction", twoBodyAnalyticsAction);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('q'), "removeApsisAction");
        this.getActionMap().put("removeApsisAction", removeApsisAction);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('t'), "trailDrawAction");
        this.getActionMap().put("trailDrawAction", trailDrawAction);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"), "increaseSpeedAction");
        this.getActionMap().put("increaseSpeedAction", increaseSpeedAction);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "decreaseSpeedAction");
        this.getActionMap().put("decreaseSpeedAction", decreaseSpeedAction);


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
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
        }
        
    }

    public class pauseAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent arg0) {
            physics.setPaused(!physics.getPaused());
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

            revertButton.setEnabled(true);
        }
        
    }

    public class revertAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent arg0) {

            //Collections.copy(savedList, physicsList);
            //System.out.println(savedList.get(0).getLocx());
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
        }
        
    }

    public class focusAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent arg0) {
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
          
        }
        
    }

    public class twoBodyAnalyticsAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent arg0){
            gpanel.setTwoBodyAnalytics(!gpanel.getTwoBodyAnalytics());
            gpanel.setFirstLoopThing(true);
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

}


