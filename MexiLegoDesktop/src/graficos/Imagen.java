package graficos;


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Liga a una de las páginas.
 * @author Diego Hernández.
 */
public class Imagen extends JPanel {
    
    public Imagen(String imgPath) {
        try{
            img = ImageIO.read(new File(imgPath));
        }
        catch(IOException e) {
            System.err.println(e.getMessage());
        }
    }
    
    @Override
    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }
    
    @Override
    public Dimension getPreferredSize() {
        if (img == null) {
            return new Dimension(100,100);
        }
        
        return new Dimension(img.getWidth(null), img.getHeight(null));
    }
    
    private BufferedImage img = null;
    
    public void setBounds(int x, int y) {
        this.setBounds(x,y,img.getWidth(null), img.getHeight(null));
    }
}
