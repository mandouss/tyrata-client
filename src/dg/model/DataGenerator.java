package dg.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;


public class DataGenerator {

	private IntegerProperty timeLength;
	private IntegerProperty dailyMileage;
	private int currentMileage;
	private List<Tire> tireInfoList;
	private LocalDate currentDate;
	

	public DataGenerator(ObjectProperty<LocalDate> startDate, IntegerProperty timeLength, IntegerProperty dailyMileage,
			List<Tire> tireInfoList) {
		super();
		this.timeLength = timeLength;
		this.dailyMileage = dailyMileage;
		this.tireInfoList = tireInfoList;
		this.currentDate = startDate.get();
		this.currentMileage = 0;
	}

	//this is a try

	private DailyS11 computeNextS11() {
		
		DailyS11 result = new DailyS11(currentDate, currentMileage);
		for (int i = 0; i < tireInfoList.size(); i++) {
			Random randomno = new Random();
			double x = randomno.nextGaussian() * 0.1 + 1;
			double s11_m = tireInfoList.get(i).getInitS11()  + this.currentMileage * 0.08 / 5000 * x;
			String tireinfo = tireInfoList.get(i).getTireID() + " " + tireInfoList.get(i).getTirePos();
			result.addTireS11(tireinfo, s11_m);
		}
		
		currentDate = currentDate.plusDays(1);
		currentMileage += dailyMileage.get();
		return result;
	}
	
	public ArrayList<DailyS11> GenerateSeries() {
		ArrayList<DailyS11> result = new ArrayList<>();
		for (int i = 0; i < timeLength.get(); i++) {
			result.add(computeNextS11());
		}
		return result;
	}
	
}
