package city;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Time {
	
	// ------------------------------------- DATA ----------------------------------------
	
	// Day:
	public static enum Day { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY }
	private static Day _today = Day.MONDAY;
	public static Day today() { return _today; }
	private static void incrementDay() {
		switch(_today) {
		case MONDAY:
			_today = Day.TUESDAY;
			return;
		case TUESDAY:
			_today = Day.WEDNESDAY;
			return;
		case WEDNESDAY:
			_today = Day.THURSDAY;
			return;
		case THURSDAY:
			_today = Day.FRIDAY;
			return;
		case FRIDAY:
			_today = Day.SATURDAY;
			return;
		case SATURDAY:
			_today = Day.SUNDAY;
			return;
		case SUNDAY:
			_today = Day.MONDAY;
			return;
		}
	}
	
	private static double _currentTime = 8.0; //0.0;
	private static double _counter = 0.25;
	private static double _timeFactor = 4.0; // this is in units of .25-hour-per-second??
	
	private static Timer _timer;
	
	
	
	// ---------------------------------- CONSTRUCTOR & PROPERTIES ----------------------------------
	static {
		_timer = new Timer((int)(12000/_timeFactor), new ActionListener() { 
			public void actionPerformed(ActionEvent e){
				incrementTime();}});
		_timer.setRepeats(true);
	}
	
	public static double currentTime(){
		return _currentTime;
	}
	
	public static void setTimeFactor(int newTimeFactor){
		if(newTimeFactor <= 12000 && newTimeFactor > 0){
			_timeFactor = newTimeFactor;
			_timer.setInitialDelay((int)(12000/_timeFactor));
		} else{
			System.out.println("Setting time factor failed, you tried to set it to " + newTimeFactor);
		}
	}
	
	/** returns the number of real-life milliseconds that will cause the passed-in number of hours to pass in the simulation */
	public static int getRealTime(double hours) {
		return (int)((12000/_timeFactor)*4*hours);
	}
	
	
	
	// --------------------------------------- METHODS -----------------------------------------
	public static void startTimer(){
		_timer.setInitialDelay((int)(12000/_timeFactor));
		_timer.restart();
	}
	
	private static void incrementTime(){ //fires every 12 seconds for now, incrementing 1/10th of an hour
		_currentTime+= _counter;
		if(_currentTime >= 24.0) {
			_currentTime = 0;
			incrementDay();
		}
		System.out.printf("Day: " + today() + "; Time- %.0f:%.0f", (double)((int)(_currentTime)), 60.0*(_currentTime - ((int)_currentTime)));
		if(60.0*(_currentTime - ((int)_currentTime)) == 0){
			System.out.print("0");
		}
		System.out.println();
	}
}
