package dg.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.util.Pair;


public class DailyS11 {
	private LocalDate timestamp;
	private int mileage;
	private ArrayList<Pair<String, Double> > tireInfoAndS11_list; 
	
	public DailyS11(LocalDate timestamp, int mileage) {
		super();
		this.timestamp = timestamp;
		this.mileage = mileage;
	}
	
	public void addTireS11(String tireinfo, double s11) {
		tireInfoAndS11_list.add(new Pair<String, Double>(tireinfo, s11));
	}
	
	public void print() {
		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
		System.out.println("Timestamp:" + timestamp.format(formatter));
		System.out.println("Mileage: " + mileage);
		
		for (int i = 0; i < tireInfoAndS11_list.size(); i++) {
			System.out.println("String: "+ tireInfoAndS11_list.get(i).getKey());
			System.out.println("S11: " + tireInfoAndS11_list.get(i).getValue());
		}
	}
	
}
