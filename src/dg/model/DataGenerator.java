package dg.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;


public class DataGenerator {

	private IntegerProperty timeSpan;
	private IntegerProperty dailyMileage;
	private int currentMileage;
	private List<Tire> tireInfoList;
	private ObjectProperty<LocalDate> startDate;
	private LocalDate currentDate;
	

	public DataGenerator(ObjectProperty<LocalDate> startDate, IntegerProperty timeSpan, IntegerProperty dailyMileage,
			List<Tire> tireInfoList) {
		super();
		this.timeSpan = timeSpan;
		this.dailyMileage = dailyMileage;
		this.tireInfoList = tireInfoList;
		this.startDate = startDate;
		this.currentDate = startDate.get();
		this.currentMileage = 0;
	}

    public DataGenerator(LocalDate startDate, int timeSpan, int dailyMileage,
                         List<Tire> tireInfoList) {
        super();
        this.timeSpan = new SimpleIntegerProperty(timeSpan);
        this.dailyMileage = new SimpleIntegerProperty(dailyMileage);
        this.tireInfoList = tireInfoList;
        this.startDate = new SimpleObjectProperty<>(startDate);
        this.currentDate = startDate;
        this.currentMileage = 0;
    }

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
	
	public ArrayList<DailyS11> generateSeries() {
		ArrayList<DailyS11> result = new ArrayList<>();
		for (int i = 0; i < timeSpan.get(); i++) {
			result.add(computeNextS11());
		}
		return result;
	}

    public ArrayList<DailyS11> generateSeries(int timeSpan) {
        ArrayList<DailyS11> result = new ArrayList<>();
        for (int i = 0; i < timeSpan; i++) {
            result.add(computeNextS11());
        }
        return result;
    }

    // will be implemented by OTHERS
//    public String convertToString() {
//        String result = "";
//        for (int i = 0; i < timeSpan.get(); i++) {
//            result += computeNextS11().convertToString();
//        }
//
//        return result;
//    }
	
    public static void main(String[] args) {
        //tireInfo_list
        Tire tireLF = new Tire("C-1", "LF", -2.0);
        Tire tireRF = new Tire("C-154", "RF", -1.5);
        List<Tire> tireList = new ArrayList<>();
        tireList.add(tireLF);
        tireList.add(tireRF);
        //startDate
        LocalDate startDate = LocalDate.of(2018,03,01);
        //timeSpan
        int timeSpan = 10;
        //dailyMileage
        int dailyMileage = 15;
        //dataGen
        DataGenerator dataGen = new DataGenerator(startDate, timeSpan, dailyMileage, tireList);
        //day_list
        ArrayList<DailyS11> result = dataGen.generateSeries(3);
        result.forEach((dailyResult) -> dailyResult.print());
        //
    }
	
}