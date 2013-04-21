package graficos.acciones;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Component;
import java.io.DataOutputStream;
import java.io.IOException;
import lejos.pc.comm.*;
import graficos.Constants;


/**
 * Envía las coordenadas del radar.
 * @author Diego Hernández.
 */
public class EnviaCoordenadas extends MouseAdapter {
	private int zoom = Constants.ZOOM_DEFAULT;
	private int speed = Constants.SPEED_DEFAULT;
	private int angularSpeed = Constants.SPEED_DEFAULT;
	
	private static final double[] d1 = {8, 16, 24, 32, 40};
	private static final double[] t1 = {2600, 3900, 5200, 6500, 7800};
	
	private static final double[] t2 = {650.0, 1300.0, 1950.0, 2600.0, 3250.0};
	private static final double[] a2 = {45, 90, 135, 180, 225};
	
	private NXTConnector connector = null;
    
    @Override
    public void mouseClicked(MouseEvent e) {
    	
    	System.out.println("Zoom: " + zoom);
    	System.out.println("Distancia: " + d1[speed]);
    	System.out.println("Tiempo linear: " + t1[speed]);
    	System.out.println("Angulo: " + a2[angularSpeed]);
    	System.out.println("Tiempo angular: " + t2[angularSpeed]);
    	
        Component c = (Component) e.getSource();
        
        double x = e.getX() - (c.getWidth()/2);
        double y = (c.getHeight()/2) - e.getY();
        
        x = (x / c.getWidth()) * zoom;
        y = (y / c.getHeight()) * zoom;
        
        double tmsLin = Math.sqrt( Math.pow(y, 2.0) + Math.pow(x, 2.0) ) * t1[speed] / d1[speed];
        double tmsAng = 0;
        
        System.out.println("X: " + x);
        System.out.println("Y: " + y);
        System.out.println("tms Linear: " + tmsLin);
        
        if(y < 0) {
        	System.out.println("Giro 180");
        	this.conexion(x, y, tmsLin, 2600);
        }
        
        tmsAng = Math.atan( y / x ) * 180 * t2[angularSpeed] / a2[angularSpeed];
        System.out.println("tms Angular: " + tmsAng);
        
        this.conexion(x, y, tmsLin, tmsAng);
    }
    
    public void setZoom(int zoom) {
    	this.zoom = zoom;
    }
    
    public void setSpeed(int speed) {
    	this.speed = speed;
    }
    
    public void setAngularSpeed(int angularSpeed) {
    	this.angularSpeed = angularSpeed;
    }
    
    public void initConnector() {
    	connector = new NXTConnector();
	       
        connector.addLogListener(new NXTCommLogListener(){

            public void logEvent(String message) {
                System.out.println("BTSend Log.listener: "+message);
            }

            public void logEvent(Throwable throwable) {
                System.out.println("BTSend Log.listener - stack trace: ");
                 throwable.printStackTrace();
            }
           
        }
        );
        // Connect to any NXT over Bluetooth
        boolean connected = connector.connectTo("btspp://");
   
       
        if (!connected) {
            System.err.println("Failed to connect to any NXT");
            System.exit(1);
        }
    }
    
    public void releaseConnector() {
    	try {
			connector.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void conexion(double x, double y, double tmsLin, double tmsAng) {
    	try {
    		DataOutputStream dos = new DataOutputStream(connector.getOutputStream());
            
            String cmd="cmd:" + x + "@" + y + "@" + tmsLin + "@" + tmsAng;  // Declare & initialize a String to hold input.  

                try {
                    System.out.print("Sending ");
                    for(int i=0; i < cmd.length(); i++){
                        System.out.print(cmd.charAt(i));
                        dos.writeChar(cmd.charAt(i));                   
                    }
                    dos.writeChar('\n');
                    System.out.println();
                    dos.flush();   
                } catch (IOException ioe) {
                    System.out.println("IO Exception writing bytes:");
                    System.out.println(ioe.getMessage());
                }
    	}
    	catch(Exception e1) {
    		e1.printStackTrace();
    	}
    }
}
