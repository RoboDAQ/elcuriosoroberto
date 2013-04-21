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
	
	
	DataOutputStream dos = null;
    
    public EnviaCoordenadas() {
		super();
		
		NXTConnector conn = new NXTConnector();
	       
        conn.addLogListener(new NXTCommLogListener(){

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
        boolean connected = conn.connectTo("btspp://");
   
       
        if (!connected) {
            System.err.println("Failed to connect to any NXT");
            System.exit(1);
        }
       
        dos = new DataOutputStream(conn.getOutputStream());

	
	}

	@Override
    public void mouseClicked(MouseEvent e) {
    	
        Component c = (Component) e.getSource();
        
        boolean clockwise = true;
        
       int xFactor = c.getWidth()/2;
       int yFactor = c.getHeight()/2;  
        
       int x = e.getX() - xFactor;
       int y = yFactor - e.getY();
        
        x = (int)((x / (double)xFactor) * zoom);
        y = (int)((y / (double)yFactor) * zoom);
        
        System.out.println("(" + x + "," + y + ")@" + zoom); 
        
       int angle = (int)(Math.toDegrees(Math.atan(y / (double)x)));
       System.out.println("PRE Angle: " + angle);
       int angularSpeed=0;
       
       if(x > 0 && y > 0){
    	   clockwise = true;
    	   angle = 90 - angle;    	   
       }else if (x < 0 && y > 0){
    	   clockwise = false;
    	   angle = 90 - angle;
       }else if (x < 0 && y < 0){
    	   clockwise = false;
    	   angle = 90 + angle;
       }else if (x > 0 && y < 0){
    	   clockwise = true;
    	   angle = 90 - angle;
       }       
       
       
       if(!clockwise){
    	   this.angularSpeed = this.angularSpeed * -1;
       }
       
       
       System.out.println("Angle: " + angle);
       
       int magnitude =  (int)(Math.sqrt( Math.pow(x, 2.0) + Math.pow(y, 2.0) ));
       System.out.println("Magnitude: " + magnitude);
       
       int turnTime = angle * 468 / 90;
       
       System.out.println("TurnTime: " + turnTime);
       
       int forwardTime = magnitude * 300;
       
       System.out.println("forwardTime: " + forwardTime);
       
        //double tmsLin = Math.sqrt( Math.pow(x, 2.0) + Math.pow(y, 2.0) ) * t1[speed] / d1[speed]; 
        //double tmsAng = Math.toDegrees(Math.atan( Math.abs(y / x) )) * t2[angularSpeed] / a2[angularSpeed];
        
        
        if(x < 0){
        	this.angularSpeed = -this.angularSpeed;
        }
         
        
        try {
			this.conexion(turnTime, forwardTime);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
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
    
    public void conexion(int rotationTime, int translationTime) throws Exception {
                
        String cmd="cmd:" + angularSpeed + "@" + speed + "@" + rotationTime + "@" + translationTime;  // Declare & initialize a String to hold input.  

            try {
                System.out.print("Sending ");
                for(int i=0; i < cmd.length(); i++){
                    System.out.print(cmd.charAt(i));
                    dos.writeChar(cmd.charAt(i));                   
                }
                dos.writeChar('\n');
                System.out.println();
                dos.flush();   
            } catch (Exception ioe) {
                System.out.println("IO Exception writing bytes:");
                System.out.println(ioe.getMessage());
            }
        
    }

	
    
    

}
