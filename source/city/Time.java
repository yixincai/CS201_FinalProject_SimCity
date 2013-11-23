package city;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Time {
	public static enum Day { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY }
	private static int todayNumber = 0;
	private static void incrementDay() { todayNumber++; if(todayNumber >= 7) todayNumber = 0; }
	public static Day today() {
		switch(todayNumber) {
		case 0:
			return Day.MONDAY;
		case 1:
			return Day.TUESDAY;
		case 2:
			return Day.WEDNESDAY;
		case 3:
			return Day.THURSDAY;
		case 4:
			return Day.FRIDAY;
		case 5:
			return Day.SATURDAY;
		case 6:
			return Day.SUNDAY;
		default:
			return Day.MONDAY;
		}
	}
	
	private static double time = 8.0; //0.0;
	private static double counter = 0.25;
	private static double timeFactor = 4.0;
	
	static Timer timer;
	
	static {
		timer = new Timer((int)(12000/timeFactor), new ActionListener() { 
			public void actionPerformed(ActionEvent e){
				incrementTime();}});
		timer.setRepeats(true);
	}
	
	public static void startTimer(){
		timer.setInitialDelay((int)(12000/timeFactor));
		timer.restart();
	}
	
	private static void incrementTime(){ //fires every 12 seconds for now, incrementing 1/10th of an hour
		time+= counter;
		if(time >= 24.0) {
			time = 0;
			incrementDay();
		}
		System.out.printf("Time- %.0f:%.0f", (double)((int)(time)), 60.0*(time - ((int)time)));
		if(60.0*(time - ((int)time)) == 0){
			System.out.print("0");
		}
		System.out.println();
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
		return time;
	}
}
