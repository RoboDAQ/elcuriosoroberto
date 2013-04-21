package graficos.botones;

import javax.swing.JToggleButton;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

public class BotonEncendido extends JToggleButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1622568136760014706L;

	protected void paintBorder(Graphics g) {
		g.setColor(getForeground());
		g.drawOval(0, 0, getSize().width-1, getSize().height-1);
	}
	
	protected void paintComponent(Graphics g) {
		if(getModel().isArmed()) {
			g.setColor(Color.lightGray);
		}
		else {
			g.setColor(getBackground());
		}
		g.fillOval(0, 0, getSize().width - 1, getSize().height - 1);
		super.paintComponent(g);
	}
	
	Shape shape;
	public boolean contains(int x, int y) {
	    if (shape == null || !shape.getBounds().equals(getBounds())) {
	    	shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
	    }
	    return shape.contains(x, y);
	}
}
