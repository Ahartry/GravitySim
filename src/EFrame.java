import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class EFrame extends JFrame{

    Physics p;
    GravBody b;
    GPanel g;
    ArrayList<EFrame> e;
    //int index;
    int red;
    int green;
    int blue;

    Action okAction;

    JTextField LocXField;
    JTextField LocYField;
    JTextField VelXField;
    JTextField VelYField;
    JTextField MassField;
    JTextField RadiusField;
    JTextField NameField;
    JTextField rField;
    JTextField gField;
    JTextField bField;

    JComboBox<String> RelativeBox;

    JCheckBox FixedBox;

    public EFrame(GravBody b1, GPanel g1, Physics p1, ArrayList<EFrame> e1){
        b = b1;
        g = g1;
        p = p1;
        e = e1;

        this.okAction = new okAction();

        //index = e.size();
        //System.out.println(index);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setTitle("Edit Menu");
        setVisible(true);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
              CloseEFrame();
            }
        });

        String[] objectList = new String[1 + p.getPhysicsList().size()];
        objectList[0] = "None";

        for(int i = 0; i < p.getPhysicsList().size(); i++){
            objectList[i + 1] = p.getPhysicsList().get(i).getName();
        }

        JPanel epanel = new JPanel();
        JPanel cpanel = new JPanel();
        JPanel fpanel = new JPanel();
        epanel.setLayout(new GridBagLayout());;
        add(epanel);

        epanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "okAction");
        epanel.getActionMap().put("okAction", okAction);

        JLabel LocXLabel = new JLabel("X loc (km): ");
        JLabel LocYLabel = new JLabel("Y loc (km): ");
        JLabel VelXLabel = new JLabel("X vel (m/s): ");
        JLabel VelYLabel = new JLabel("Y vel (m/s): ");
        JLabel MassLabel = new JLabel("Mass (e21kg): ");
        JLabel RadiusLabel = new JLabel("Radius (km): ");
        JLabel NameLabel = new JLabel("Name: ");
        JLabel ColorLabel = new JLabel("Color (r g b): ");
        JLabel FixedLabel = new JLabel("Fixed: ");
        JLabel RelativeLabel = new JLabel("Relative: ");

        LocXField = new JTextField("", 14);
        LocYField = new JTextField("", 14);
        VelXField = new JTextField("", 14);
        VelYField = new JTextField("", 14);
        MassField = new JTextField("", 10);
        RadiusField = new JTextField("", 10);
        NameField = new JTextField("", 10);
        rField = new JTextField("", 3);
        gField = new JTextField("", 3);
        bField = new JTextField("", 3);
        //JTextField 

        RelativeBox = new JComboBox<>(objectList);

        FixedBox = new JCheckBox();

        JButton okButton = new JButton("Ok");

        okButton.addActionListener(new okAction());
		okButton.setFocusable(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        //gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        
        //adds all the stuff (First Row)
        epanel.add(LocXLabel, gbc);
        
        gbc.gridx = 1;

        epanel.add(LocXField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        
        epanel.add(LocYLabel, gbc);
        
        gbc.gridx = 1;

        epanel.add(LocYField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        
        epanel.add(VelXLabel, gbc);
        
        gbc.gridx = 1;

        epanel.add(VelXField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        
        epanel.add(VelYLabel, gbc);
        
        gbc.gridx = 1;

        epanel.add(VelYField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        
        epanel.add(fpanel, gbc);
        fpanel.add(FixedLabel);
        fpanel.add(FixedBox);

        //adds all the 2nd row
        gbc.gridx = 3;
        gbc.gridy = 0;
        
        epanel.add(MassLabel, gbc);
        
        gbc.gridx = 4;

        epanel.add(MassField, gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        
        epanel.add(RadiusLabel, gbc);
        
        gbc.gridx = 4;

        epanel.add(RadiusField, gbc);

        gbc.gridx = 3;
        gbc.gridy = 2;
        
        epanel.add(NameLabel, gbc);
        
        gbc.gridx = 4;

        epanel.add(NameField, gbc);

        gbc.gridx = 3;
        gbc.gridy = 3;
        
        epanel.add(ColorLabel, gbc);
        
        gbc.gridx = 4;

        epanel.add(cpanel, gbc);
        cpanel.add(rField);
        cpanel.add(gField);
        cpanel.add(bField);

        gbc.gridx = 3;
        gbc.gridy = 4;
        
        epanel.add(RelativeLabel, gbc);
        
        gbc.gridx = 4;

        epanel.add(RelativeBox, gbc);

        //adds button
        gbc.gridx = 2;
        gbc.gridy = 5;
        
        epanel.add(okButton, gbc);

        //grabs the values
        UpdateBoxes();
        //LocXField, LocYField, VelXField, VelYField, MassField, RadiusField,  NameField, rField, gField,  bField, FixedBox
    }

    //JTextField LocXField, JTextField LocYField, JTextField VelXField, JTextField VelYField, JTextField MassField, JTextField RadiusField, JTextField NameField, JTextField rField, JTextField gField, JTextField bField, JCheckBox FixedBox
    public void UpdateBoxes(){
        if(b.getRadius() != 0){
            System.out.println("Loading selected data");
            //loads all the stuff
            LocXField.setText(String.valueOf(b.getLocx()));
            LocYField.setText(String.valueOf(b.getLocy()));
            VelXField.setText(String.valueOf(b.getVelx()));
            VelYField.setText(String.valueOf(b.getVely()));

            MassField.setText(String.valueOf(b.getMass()));
            RadiusField.setText(String.valueOf(b.getRadius()));
            NameField.setText(String.valueOf(b.getName()));
            FixedBox.setSelected(b.getFixed());
            rField.setText(String.valueOf(b.getColor().getRed()));
            gField.setText(String.valueOf(b.getColor().getGreen()));
            bField.setText(String.valueOf(b.getColor().getBlue()));

        }else{
            System.out.println("Not loading data because it does not exist");
        }
    }

    //JTextField LocXField, JTextField LocYField, JTextField VelXField, JTextField VelYField,
    //JTextField MassField, JTextField RadiusField, JTextField NameField, JTextField rField, JTextField gField, JTextField bField, JCheckBox FixedBox, JComboBox RelativeBox
    public void ExtractData(){

        //tries each thing, error bool checks if any errors. If there are, don't close window
        boolean error = false;
        if(b.getRadius() == 0){
            p.getPhysicsList().add(b);
        }

        try{
            b.setLocx(Double.parseDouble(LocXField.getText()));
            LocXField.setForeground(Color.BLACK);
        }catch (Exception e){
            error = true;
            LocXField.setForeground(Color.RED);
        }
        try{
            b.setLocy(Double.parseDouble(LocYField.getText()));
            LocYField.setForeground(Color.BLACK);
        }catch (Exception e){
            error = true;
            LocYField.setForeground(Color.RED);
        }
        try{
            b.setVelx(Double.parseDouble(VelXField.getText()));
            VelXField.setForeground(Color.BLACK);
        }catch (Exception e){
            error = true;
            VelXField.setForeground(Color.RED);
        }
        try{
            b.setVely(Double.parseDouble(VelYField.getText()));
            VelYField.setForeground(Color.BLACK);
        }catch (Exception e){
            error = true;
            VelYField.setForeground(Color.RED);
        }
        try{
            b.setMass(Double.parseDouble(MassField.getText()));
            MassField.setForeground(Color.BLACK);
        }catch (Exception e){
            error = true;
            MassField.setForeground(Color.RED);
        }
        try{
            b.setRadius(Double.parseDouble(RadiusField.getText()));
            RadiusField.setForeground(Color.BLACK);
        }catch (Exception e){
            error = true;
            RadiusField.setForeground(Color.RED);
        }
        try{
            b.setName(NameField.getText());
            NameField.setForeground(Color.BLACK);
        }catch (Exception e){
            error = true;
            NameField.setForeground(Color.RED);
        }
        try{
            red = Integer.parseInt(rField.getText());
            rField.setForeground(Color.BLACK);
        }catch (Exception e){
            error = true;
            rField.setForeground(Color.RED);
            red = 0;
        }
        try{
            green = Integer.parseInt(gField.getText());
            gField.setForeground(Color.BLACK);
        }catch (Exception e){
            error = true;
            gField.setForeground(Color.RED);
            green = 0;
        }
        try{
            blue = Integer.parseInt(bField.getText());
            bField.setForeground(Color.BLACK);
        }catch (Exception e){
            error = true;
            bField.setForeground(Color.RED);
            blue = 0;
        }
        b.setColor(new Color(red, green, blue));

        if(FixedBox.isSelected()){
            b.setFixed(true);
        }else{
            b.setFixed(false);
        }

        if(RelativeBox.getSelectedItem().equals("None")){

        }else{
            int index = RelativeBox.getSelectedIndex() - 1;
            b.setLocx(b.getLocx() + p.getPhysicsList().get(index).getLocx());
            b.setLocy(b.getLocy() + p.getPhysicsList().get(index).getLocy());
            b.setVelx(b.getVelx() + p.getPhysicsList().get(index).getVelx());
            b.setVely(b.getVely() + p.getPhysicsList().get(index).getVely());
        }

        if(error){
            System.out.println("Syntax error in input");
        }else{
            CloseEFrame();
        }

    }
    


    public void CloseEFrame(){
        
        if(e.size() == 1){
            p.setPaused(false);
            g.setJustPaused(true);
        }
        e.remove(e.indexOf(this));
        dispose();
        System.out.println(e.size());

    }

    public class okAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent arg0) {
            ExtractData();
            //LocXField, LocYField, VelXField, VelYField, MassField, RadiusField,  NameField, rField, gField,  bField, FixedBox, RelativeBox
        }
        
    }
}
