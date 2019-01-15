import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Etch_A_Sketch implements MouseListener, MouseMotionListener, ActionListener  // NOTE multiple interfaces
{
	JFrame window;
	Container contentPane;
	int mouseX,mouseY,oldX,oldY;
	JButton changeColor;
	Color currDrawingColor = Color.BLACK; // ADD A BUTTON THAT WHEN CLICKED, CHANGES THE CURR COLOR
	Graphics g;
	int index = 0;
	
	public Etch_A_Sketch()
	{
		JFrame window = new JFrame("Classic Etch a Sketch");
		contentPane = window.getContentPane();
		contentPane.setLayout( new FlowLayout() );
		changeColor = new JButton("Click here to change the color of the drawing");
		changeColor.addActionListener(this);
		contentPane.add(changeColor); 
		contentPane.addMouseListener(this);        // "this" is the class that implements that listener
		contentPane.addMouseMotionListener(this);  // "this" is the class that implements that listener
		window.setSize(640,480);
		window.setVisible(true);
	}
	// ..............................................................
	// IMPLEMENTING MOUSELISTENER REQUIRES YOU TO WRITE (OVER-RIDE) THESE METHODS 

	//when you press & release with NO MOVEMENT while pressed
	public void mouseClicked( MouseEvent me)
	{
		mouseX = me.getX();
		mouseY = me.getY();
		reportEventCoords("Mouse clicked at: " + mouseX + "," + mouseY );
		
	}
	
	// when you press 
	public void mousePressed( MouseEvent me)
	{
		mouseX = me.getX();
		mouseY = me.getY();
		reportEventCoords("Mouse Pressed at: " + mouseX + "," + mouseY );
		//repaint();
	}

	//when you let release after dragging
	public void mouseReleased( MouseEvent me)
	{
		mouseX = me.getX();
		mouseY = me.getY();
		reportEventCoords("Mouse released at: " + mouseX + "," + mouseY );
		//repaint();
	}

	// the mouse just moved off of the JFrame
	public void mouseExited( MouseEvent me)
	{
		mouseX = me.getX();
		mouseY = me.getY();
		reportEventCoords("Mouse exited at: " + mouseX + "," + mouseY );
		//repaint();
	}
	
	// the mouse just moved onto the JFrame
	public void mouseEntered( MouseEvent me)
	{
		mouseX = me.getX();
		mouseY = me.getY();
		reportEventCoords("Mouse Entered at: " + mouseX + "," + mouseY );
		//repaint();
	}
	// ...............................................................
	// IMPLEMENTING MOUSEMOTIONLISTENER REQUIRES YOU WRITE (OVER-RIDE) THESE METHODS 

	// mouse is moving while pressed
	public void mouseDragged( MouseEvent me)
	{
		mouseX = me.getX();
		mouseY = me.getY();

		if (oldX ==0 )
		{
			oldX=mouseX;
			oldY=mouseY;
			return;
		}
		
		// draw  dot (actually small line segment) between old (x,y) and current (x,y)
		
		g = contentPane.getGraphics(); // use g to draw onto the pane
		g.setColor( currDrawingColor );
		g.drawLine( oldX,oldY, mouseX, mouseY );
		oldX = mouseX;
		oldY = mouseY;
		reportEventCoords("Mouse Dragged at: " + mouseX + "," + mouseY );
		//repaint();
	}
	
	// moved mouse but not pressed
	public void mouseMoved( MouseEvent me)
	{
		mouseX = me.getX();
		mouseY = me.getY();
		reportEventCoords("Mouse Moved at: " + mouseX + "," + mouseY );
		//repaint();
	}

	// ..............................................................

	public static void main( String[] args)
	{
		new Etch_A_Sketch();
	}
	// a helper utility
	private void reportEventCoords( String msg )
	{
		//Do nothingggg
	}
	
	
	public void actionPerformed(ActionEvent e){
		Color[] colorList = {Color.RED, Color.BLUE, Color.CYAN, Color.YELLOW, Color.ORANGE, Color.MAGENTA, Color.GREEN, Color.BLACK};
		Component source = (Component) e.getSource();
		
		if (source == changeColor){
			currDrawingColor = colorList[index];
			g.setColor(currDrawingColor);
			index = (index + 1) % colorList.length;
		}
		
	}
}//EOF