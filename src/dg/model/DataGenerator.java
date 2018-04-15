package dg.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dg.util.DailyS11ToStringUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;


public class DataGenerator {

	private IntegerProperty timeSpan;
	private IntegerProperty dailyMileage;
	private int currentMileage;
	private List<Tire> tireInfoList;
	private ObjectProperty<LocalDate> startDate;
	private LocalDate currentDate;
	private BooleanProperty outlierEnabled;
	private IntegerProperty outlierInterval;
	private int dayCounter;
	private int randomOutlierInterval;
	
	public DataGenerator(ObjectProperty<LocalDate> startDate, IntegerProperty timeSpan, IntegerProperty dailyMileage,
			List<Tire> tireInfoList,BooleanProperty outlierEnabled, IntegerProperty outlierInterval) {
		super();
		this.timeSpan = timeSpan;
		this.dailyMileage = dailyMileage;
		this.tireInfoList = tireInfoList;
		this.startDate = startDate;
		this.currentDate = startDate.get();
		this.currentMileage = 0;
		this.outlierInterval = outlierInterval;
		this.outlierEnabled = outlierEnabled;
		this.dayCounter = 0;
		generateRandomOutlierInterval();
	}

    public DataGenerator(LocalDate startDate, int timeSpan, int dailyMileage,
                         List<Tire> tireInfoList, boolean outlierEnabled, int outlierInterval) {
        super();
        this.timeSpan = new SimpleIntegerProperty(timeSpan);
        this.dailyMileage = new SimpleIntegerProperty(dailyMileage);
        this.tireInfoList = tireInfoList;
        this.startDate = new SimpleObjectProperty<>(startDate);
        this.currentDate = startDate;
        this.currentMileage = 0;
        this.outlierEnabled = new SimpleBooleanProperty(outlierEnabled);
        this.outlierInterval = new SimpleIntegerProperty(outlierInterval);
        this.dayCounter = 0;
        generateRandomOutlierInterval();
    }

	public ObjectProperty<LocalDate> getStartDate() {
		return startDate;
	}

	public void setStartDate(ObjectProperty<LocalDate> startDate) {
		this.startDate = startDate;
	}
	
	private void generateRandomOutlierInterval() {
		Random rand = new Random();
		int max = (int)Math.ceil(1.3 * outlierInterval.get());
		int min = (int)Math.ceil(0.7 * outlierInterval.get());
		
		randomOutlierInterval = rand.nextInt((max-min) + 1) + min;
		if(randomOutlierInterval <= 0) {
			randomOutlierInterval = 1;
		}
		
	}
	
	private DailyS11 computeNextS11() {
		
		DailyS11 result = new DailyS11(currentDate, currentMileage);
		boolean isOutlierDay = false;
		
		if (dayCounter == randomOutlierInterval && outlierEnabled.get() == true) {
			isOutlierDay = true;
		}
		for (int i = 0; i < tireInfoList.size(); i++) {
			double x;
			if (isOutlierDay) {
				x = rollOutlier();
			} else {
				x = rollNormal();
			}
//			System.out.println("number " + x);
			double s11_m = tireInfoList.get(i).getInitS11()  + this.currentMileage * 0.08 / 5000 * x;
			
			//remove show tire location, tyrata mobile will calibrate tire location
			String tireinfo = tireInfoList.get(i).getTireID();
					//+ " " + tireInfoList.get(i).getTirePos();
			double pressure = tireInfoList.get(i).getPressure();
			result.addTireS11(tireinfo, s11_m, pressure);
		}
		dayCounter++;
		if (isOutlierDay) {
			dayCounter = 0;
			generateRandomOutlierInterval();
		}
		currentDate = currentDate.plusDays(1);
		currentMileage += dailyMileage.get();
		
		return result;
	}
	
	private double rollNormal() {
		Random randomno = new Random();
		double x = randomno.nextGaussian() * 0.1 + 1;
		while (!ifNormal(x)) {
			x = randomno.nextGaussian() * 0.1 + 1;
		}
		return x;
	}
	private double rollOutlier() {
		Random randomno = new Random();
		double x = randomno.nextGaussian() * 100 + 1;
		while (!ifOutlier(x)) {
			x = randomno.nextGaussian() * 100 + 1;
		}
		return x;
	}
	private boolean ifNormal(double x) {
		return !ifOutlier(x);
	}
	private boolean ifOutlier(double x) {
		double ucl = 2;
		double lcl = 0;
		if (x > ucl || x < lcl) {
			return true;
		}
		return false;
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
        Tire tireLF = new Tire("C-1", "LF", -2.0, 3.5);
        Tire tireRF = new Tire("C-154", "RF", -1.5, 3.5);
        List<Tire> tireList = new ArrayList<>();
        tireList.add(tireLF);
        tireList.add(tireRF);
        //startDate
        LocalDate startDate = LocalDate.of(2018,03,01);
        //timeSpan
        int timeSpan = 20;
        //dailyMileage
        int dailyMileage = 15;
        //dataGen
        DataGenerator dataGen = new DataGenerator(startDate, timeSpan, dailyMileage, tireList, true, 1);
        //day_list
        ArrayList<DailyS11> result = dataGen.generateSeries();
        String resultInString = DailyS11ToStringUtil.dailyS11ToString(result);
        System.out.println(resultInString);
        //result.forEach((dailyResult) -> dailyResult.print());
        
    }
	
}
