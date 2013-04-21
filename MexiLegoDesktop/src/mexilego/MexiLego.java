package mexilego;

import graficos.grafico;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JRootPane;

/**
 *
 * @author Omar / Diego Hernández
 */
public class MexiLego {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	JFrame f = new JFrame();
        f.setTitle("MexiLego");
    	f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    	f.setPreferredSize(new Dimension(750,550));
    	f.setBounds(40, 40, 750, 550);
    	
    	MexiLego mlogo = new MexiLego();
    	Fondo fondo = mlogo.getFondo();
    	f.setContentPane(fondo);
    	
        grafico g = new grafico();
        g.setOpaque(false);
        g.setPreferredSize(new Dimension(750,550));
        g.setBounds(0, 0, 750, 550);
    	f.getContentPane().add(g);
    	f.setVisible(true);
    } 
    
    public Fondo getFondo() {
    	Fondo f = null;
    	f = new Fondo();
    	return f;
    }
    
    private class Fondo extends JPanel {
    	private BufferedImage img = null;
    	
    	public Fondo() {
    		try{
                img = ImageIO.read(new File("recursos/imagenes/Desktop/DesktopWindow750x589.png"));
            }
            catch(IOException e) {
                System.err.println(e.getMessage());
            }
    	}
    	
    	@Override
        public void paintComponent(Graphics g) {
    		g.setColor(java.awt.Color.BLACK);
    		g.fillRect(0, 0, 750, 589);
            g.drawImage(img, 0, 0, null);
        }
        
        @Override
        public Dimension getPreferredSize() {
            if (img == null) {
                return new Dimension(100,100);
            }
            
            return new Dimension(img.getWidth(null), img.getHeight(null));
        }
    }
}
