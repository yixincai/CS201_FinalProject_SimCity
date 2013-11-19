package city;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Time {
	private static double Time = 0.0;
	private static double counter = 0.1;
	private static double timeFactor = 1.0;
	
	static Timer timer;
	
	static {
		timer = new Timer((int)(12000/timeFactor), new ActionListener() { 
			public void actionPerformed(ActionEvent e){
				incrementTime();}});
		timer.setRepeats(true);
		startTimer();
	}
	
	private static void startTimer(){
		timer.setInitialDelay((int)(12000/timeFactor));
		timer.restart();
	}
	
	private static void incrementTime(){ //fires every 12 seconds for now, incrementing 1/10th of an hour
		Time+= counter;
	}
	
	public static void setTimeFactor(int newTimeFactor){
		if(newTimeFactor <= 12000 && newTimeFactor > 0){
			timeFactor = newTimeFactor;
			timer.setInitialDelay((int)(12000/timeFactor));
		} else{
			System.out.println("Setting time factor failed, you tried to set it to " + newTimeFactor);
		}
	}
	
	public static double getTime(){
		return Time;
	}
}
