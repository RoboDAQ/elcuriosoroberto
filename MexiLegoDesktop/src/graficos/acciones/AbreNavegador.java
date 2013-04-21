package graficos.acciones;

import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abre un navegador con la URL solicitada.
 * 
 * @author Diego Hernández
 */
public class AbreNavegador extends MouseAdapter {
    private URL url;
    
    public AbreNavegador(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException ex) {
            Logger.getLogger(AbreNavegador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        Desktop desktop = Desktop.isDesktopSupported() ?
                Desktop.getDesktop() : null;
        
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(url.toURI());
            }
            catch(IOException | URISyntaxException exc) {
                Logger.getLogger(AbreNavegador.class.getName()).log(Level.SEVERE, null, exc);
            }
        }
    }
}
