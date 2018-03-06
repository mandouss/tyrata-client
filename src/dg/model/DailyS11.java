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
		this.tireInfoAndS11_list = new ArrayList<>();
	}
	
	public void addTireS11(String tireinfo, double s11) {
		tireInfoAndS11_list.add(new Pair<String, Double>(tireinfo, s11));
	}
	
	public void print() {
		System.out.print(convertToString());
	}

	public String convertToString() {
		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
		String result = "";
		result += "Timestamp:" + timestamp.format(formatter) + "\n";
		result += "Mileage: " + mileage + "\n";

		for (int i = 0; i < tireInfoAndS11_list.size(); i++) {
			result += "Tire: "+ tireInfoAndS11_list.get(i).getKey() + "\n";
			result += "S11: " + tireInfoAndS11_list.get(i).getValue() + "\n";
		}

		return result;
	}

}
