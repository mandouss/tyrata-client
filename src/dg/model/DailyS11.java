package dg.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
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
		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
		System.out.println("Timestamp:" + timestamp.format(formatter));
		System.out.println("Mileage: " + mileage);
		
		for (int i = 0; i < tireInfoAndS11_list.size(); i++) {
			System.out.println("String: "+ tireInfoAndS11_list.get(i).getKey());
			System.out.println("S11: " + tireInfoAndS11_list.get(i).getValue());
		}
	}
    public static void main(String[] args) {
        //tireInfo_list
        Tire tireLF = new Tire("C-1", "LF", -2.0);
        Tire tireRF = new Tire("C-154", "RF", -1.5);
        List<Tire> tireList = new ArrayList<>();
        tireList.add(tireLF);
        tireList.add(tireRF);
        //startDate
        ObjectProperty<LocalDate> startDate = new SimpleObjectProperty<>(LocalDate.of(2018,03,01));
        //timeLength
        IntegerProperty timeLength = new SimpleIntegerProperty(10);
        //dailyMileage
        IntegerProperty dailyMileage = new SimpleIntegerProperty(15);
        //dataGen
        DataGenerator dataGen = new DataGenerator(startDate, timeLength, dailyMileage, tireList);
        //day_list
        ArrayList<DailyS11> result = dataGen.GenerateSeries();
        result.forEach((dailyResult) -> dailyResult.print());
        //
    }
}
