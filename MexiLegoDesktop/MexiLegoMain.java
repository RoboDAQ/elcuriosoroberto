import java.io.DataInputStream;
import java.io.DataOutputStream;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import lejos.nxt.Sound;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.robotics.Touch;
import lejos.robotics.objectdetection.Feature;
import lejos.robotics.objectdetection.FeatureDetector;
import lejos.robotics.objectdetection.FeatureListener;
import lejos.robotics.objectdetection.FusorDetector;
import lejos.robotics.objectdetection.RangeFeatureDetector;
import lejos.robotics.objectdetection.TouchFeatureDetector;
import lejos.util.Delay;

public class MexiLegoMain {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S4);
		FeatureDetector detector1 = new RangeFeatureDetector(us, 30,500);

		Touch leftBump = new TouchSensor(SensorPort.S2);
		FeatureDetector detector2 = new TouchFeatureDetector(leftBump, 10, 10); 

		Touch rightBump = new TouchSensor(SensorPort.S3);
		FeatureDetector detector3 = new TouchFeatureDetector(rightBump, -10, 10); 

		FusorDetector fusion = new FusorDetector();
		fusion.addDetector(detector1);
		fusion.addDetector(detector2);
		fusion.addDetector(detector3);

		fusion.addListener(new FeatureListener() {			
			public void featureDetected(Feature feature, FeatureDetector detector) {
				int range = (int)feature.getRangeReading().getRange();
				Sound.playTone(1200 - (range * 10), 100);
				LCD.drawString("Range:" + range,0,4);
				Delay.msDelay(1000);
			}
		});
		
		boolean continueFlag = true;
		
		String connected = "Connected";
        String waiting = "Waiting...";
        String closing = "Closing...";
        
        DataInputStream dis = null;
		DataOutputStream dos = null;
		BTConnection btc = Bluetooth.waitForConnection();
        
		while (continueFlag)
		{
			try{
			LCD.drawString(waiting,0,0);
			LCD.refresh();

	        
	        
			LCD.clear();
			LCD.drawString(connected,0,0);
			LCD.refresh();	

			dis = btc.openDataInputStream();
			dos = btc.openDataOutputStream();
			
			boolean readFlag = true;
			String cmd = "";
			while(readFlag){
				char c = dis.readChar();
				LCD.drawChar(c, 0, 1);				
				if(c == '\n'){
					readFlag = false;
				}else{
					cmd = cmd + c;
				}				
			}
			LCD.drawString(cmd,0,1);
			LCD.refresh();	
			 			
			if(cmd.equals("quit")){				
				continueFlag = false;
			}
						
			if(cmd.substring(0, 4).equals("cmd:")){				
				String params = cmd.substring(4);
				String[] values = split(params);
				LCD.clear();
				for(int i = 0; i < values.length; i++){
					LCD.drawString(values[i], 0, i);
				}
				
				boolean clockwise = true;
				int angularSpeed = Integer.parseInt(values[0]) * 180;
				if(angularSpeed < 0){
					angularSpeed = angularSpeed * -1;
					clockwise = false;
				}
				
				Motor.B.setSpeed(angularSpeed);
				Motor.C.setSpeed(angularSpeed);
				
				if(!clockwise){
					Motor.B.forward();
					Motor.C.backward();
				}else{
					Motor.B.backward();
					Motor.C.forward();
				}
				
				Delay.msDelay(Integer.parseInt(values[2]));
				Motor.B.stop();
				Motor.C.stop();
				
				boolean forward = true;
				int linearSpeed = Integer.parseInt(values[1]) * 180;
				if(linearSpeed < 0){
					linearSpeed = linearSpeed * -1;
					forward = false;
				}
				
				Motor.B.setSpeed(linearSpeed);
				Motor.C.setSpeed(linearSpeed);
				
				if(forward){
					Motor.B.forward();
					Motor.C.forward();
				}else{
					Motor.B.backward();
					Motor.C.backward();
				}
				
				Delay.msDelay(Integer.parseInt(values[3]));
				Motor.B.stop();
				Motor.C.stop();
				
			}else if(cmd.substring(0, 4).equals("cfg:")){
				
			}
			
			Thread.sleep(1000);
			
			LCD.clear();
			LCD.drawString(closing,0,0);
			LCD.refresh();			
			LCD.clear();
			}catch(Exception e){
				LCD.drawString(e.getMessage(), 0, 0);
			}
		}
		
		try{
			if(dis != null){
				dis.close();
			}
			if(dos != null){
				dos.close();
			}
			btc.close();
		}catch(Exception e){
			LCD.drawString(e.getMessage(), 0, 0);
		}
	
	}
	
	private static String[] split(String s) throws InterruptedException{		
		int count = 0;
		for(int i = 0; i < s.length(); i++){
			char c = s.charAt(i);
			if(c == '@'){
				count++;
			}
		}		
		String[] result = new String[count+1];
		int lastIndex = 0;
		for(int i = 0; i <= count; i++){
			int pos = s.indexOf("@", lastIndex);
			if(pos == -1){
				pos = s.length();
			}
			System.out.println("POS: " + pos);
			result[i] = s.substring(lastIndex,pos);
			lastIndex = pos+1;
		}		
		return result;
	}

}
