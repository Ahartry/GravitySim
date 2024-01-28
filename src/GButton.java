import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class GButton extends JButton{
	private static final long serialVersionUID = 1L;
	
	private JLabel text = new JLabel();
	
	private enum Image{MAIN, PRESSED, HOVER}
	private Image image = Image.MAIN;
	
	private int paddingX = 0;
	private int paddingY = 0;
	
	private Color mainColor = new Color(0,100,200,255);
//	private Color mainColor2 = new Color(0,100,200,127);
	private Color clickedColor = new Color(0,70,170,255);
//	private Color clickedColor2 = new Color(0,70,170,127);
	private Color hoverColor = new Color(0,150,250,255);
//	private Color hoverColor2 = new Color(0,150,250,127);
	private Color disabledColor = new Color(180, 180, 180, 255);
	
	private static boolean e = false;
	
	private JComponent parent = null;

	public void setText(String t) {
		text.setText(t);
	}
	
	public void setParent(JComponent j) {
		parent = j;
	}


	public GButton(String t) {
//		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("buttonPressed.wav");
//		pressedSound = new Sound(in);
//		releasedSound = new Sound(in);
		addActionListener(new mainActionListener());
		setLayout(new FlowLayout());
		text.setText(t);
		text.setForeground(Color.WHITE);
		text.setHorizontalAlignment(JLabel.CENTER);
        this.size(22);
		this.add(text);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		setContentAreaFilled(false);
		setFocusable(false);
		setBorderPainted(false);
		
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		addMouseListener(new java.awt.event.MouseAdapter() {
			boolean p = false;
//			boolean i = false;
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	if(isEnabled()) {
		    	if(p == false) {
		    	image = Image.HOVER;
		    	}else {
		    		image = Image.PRESSED;
		    	}
		    	setCursor(new Cursor(Cursor.HAND_CURSOR));
		    	e = true;
//		    	i = true;
		    	repaint();
//		    	setCursor(new Cursor(Cursor.HAND_CURSOR));
	    		if(parent != null) {
	    			parent.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    		}
		    	}
		    	setCursor(new Cursor(Cursor.HAND_CURSOR));
		    }
		    
		    public void mouseReleased(java.awt.event.MouseEvent evt) {
//		    	if(allEnabled) {
	//		    	if(i == true) {
	//		    	image = Image.HOVER;
	//		    	}else {
			    		image = Image.MAIN;
	//		    	}
			    	p = false;
			    	if(parent != null && !e) {
			    		parent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			    	}
			    	if(!e) {
			    		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			    	}
			    	repaint();
//		    	}
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	if(isEnabled()) {
//		    	i = false;
		    	e = false;
		    	image = Image.MAIN;
		    	repaint();
//		    	setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		    	repaint();
		    	if(parent != null) {
		    		parent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		    	}
		    	
		    	}
		    	setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		    }
		    
		    public void mousePressed(java.awt.event.MouseEvent evt) {
		    	if(evt.getButton() == MouseEvent.BUTTON1) {
		    	image = Image.PRESSED;
		    	p = true;
		    	repaint();
		    	}
		    }
		});
	}
	
	public void size(int si) {
		text.setFont(new Font("Noto Sans", Font.PLAIN, si));
//		this.setPreferredSize(new Dimension(text.getWidth(), text.getHeight()));
//		this.setPreferredSize(new Dimension(getWidth()*2, getHeight()*2));
	}
	
	public void padding(int x, int y) {
		paddingX = x;
		paddingY = y;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHints(rh);
			//note, Colors go in the order - red, green, blue, alpha
		switch(image) {
		case MAIN:
//			g2d.setColor(mainColor2);
//			g2d.fillRoundRect(paddingX,paddingY,getWidth()-paddingX*2,getHeight()-paddingY*2,30,30);
			if(isEnabled()){
				g2d.setColor(mainColor);//main
				g2d.fillRoundRect(1+paddingX,1+paddingY,getWidth()-paddingX*2-2,getHeight()-paddingY*2-2,30,30);
			}
			break;
		case PRESSED:
//			g2d.setColor(clickedColor2);
//			g2d.fillRoundRect(paddingX,paddingY,getWidth()-paddingX*2,getHeight()-paddingY*2,30,30);
			if(isEnabled()){
				g2d.setColor(clickedColor);//clicked
				g2d.fillRoundRect(1+paddingX,1+paddingY,getWidth()-paddingX*2-2,getHeight()-paddingY*2-2,30,30);
			}

			break;
		default:
//			g2d.setColor(hoverColor2);
//			g2d.fillRoundRect(paddingX,paddingY,getWidth()-paddingX*2,getHeight()-paddingY*2,30,30);
			if(isEnabled()){
				g2d.setColor(hoverColor);//hover
				g2d.fillRoundRect(1+paddingX,1+paddingY,getWidth()-paddingX*2-2,getHeight()-paddingY*2-2,30,30);
			}

			break;
		}
		
		if(isEnabled() == false) {
//			g2d.setColor(Color.BLUE);
//			g2d.setColor(mainColor2);
//			g2d.fillRoundRect(paddingX,paddingY,getWidth()-paddingX*2,getHeight()-paddingY*2,30,30);
			g2d.setColor(disabledColor);
			g2d.fillRoundRect(1+paddingX,1+paddingY,getWidth()-paddingX*2-2,getHeight()-paddingY*2-2,30,30);
		}
		
		

//        g.fillRect(paddingX, paddingY, getWidth()-paddingX*2, getHeight()-paddingY*2); 

	}
	
	private class mainActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
	    	//new Sound().play("ping.wav");
		}
		
	}
}
