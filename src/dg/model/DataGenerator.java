package dg.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dg.util.DailyS11ToStringUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * This DataGenerator class is responsible for generating s11 values (including ourliers)
 * for a given time span.
 */
public class DataGenerator {

	private int timeSpan;
	private int dailyMileage;
	private int currentMileage;
	private List<Tire> tireInfoList;
	private LocalDate startDate;
	private LocalDate currentDate;
	private boolean outlierEnabled;
	private int outlierInterval;
	private int dayCounter;
	private int randomOutlierInterval;
	
	public DataGenerator(ObjectProperty<LocalDate> startDate, IntegerProperty timeSpan, IntegerProperty dailyMileage,
			List<Tire> tireInfoList,BooleanProperty outlierEnabled, IntegerProperty outlierInterval) {
		super();
		this.timeSpan = timeSpan.get();
		this.dailyMileage = dailyMileage.get();
		this.tireInfoList = tireInfoList;
		this.startDate = startDate.get();
		this.currentDate = startDate.get();
		this.currentMileage = dailyMileage.get();
		this.outlierInterval = outlierInterval.get();
		this.outlierEnabled = outlierEnabled.get();
		this.dayCounter = 0;
		generateRandomOutlierInterval();
	}

    public DataGenerator(LocalDate startDate, int timeSpan, int dailyMileage,
                         List<Tire> tireInfoList, boolean outlierEnabled, int outlierInterval) {
        super();
        this.timeSpan = timeSpan;
        this.dailyMileage = dailyMileage;
        this.tireInfoList = tireInfoList;
        this.startDate = startDate;
        this.currentDate = startDate;
        this.currentMileage = dailyMileage;
        this.outlierEnabled = outlierEnabled;
        this.outlierInterval = outlierInterval;
        this.dayCounter = 0;
        generateRandomOutlierInterval();
    }

	public ObjectProperty<LocalDate> getStartDate() {
		return new SimpleObjectProperty<LocalDate>(startDate);
	}

	public void setStartDate(ObjectProperty<LocalDate> startDate) {
		this.startDate = startDate.get();
	}
	
	/*
	 * This method adds the functionality of generating outliers with varying intervals in days
	 * instead of a fixed number of days. This adds more randomness to our algorithms and can 
	 * be further used to test the outlier removal algorithms of the mobile app.
	 */
	private void generateRandomOutlierInterval() {
		if(outlierEnabled) {
			Random rand = new Random();
			int max = (int)Math.ceil(1.3 * outlierInterval);
			int min = (int)Math.ceil(0.7 * outlierInterval);

			randomOutlierInterval = rand.nextInt((max-min) + 1) + min;
			if(randomOutlierInterval <= 0) {
				randomOutlierInterval = 1;
			}
		}
	}
	
	/*
	 * This method is the main algorithm for generating s11 data points. 
	 * Depending on the day counter, the method will either generate a normal s11 value 
	 * or a outlier value. 
	 */
	private DailyS11 computeNextS11() {
		
		DailyS11 result = new DailyS11(currentDate, currentMileage);
		boolean isOutlierDay = false;
		
		if (dayCounter == randomOutlierInterval && outlierEnabled == true) {
			isOutlierDay = true;
		}
		for (int i = 0; i < tireInfoList.size(); i++) {
			double x;
			if (isOutlierDay) {
				x = rollOutlier();
			} else {
				x = rollNormal();
			}

			double s11_m = tireInfoList.get(i).getInitS11()  + this.currentMileage * 0.08 / 5000 * x;
			
			String tireinfo = tireInfoList.get(i).getTireID();
			double pressure = tireInfoList.get(i).getPressure();
			result.addTireS11(tireinfo, s11_m, pressure);
		}
		dayCounter++;
		if (isOutlierDay) {
			dayCounter = 0;
			generateRandomOutlierInterval();
		}
		currentDate = currentDate.plusDays(1);
		currentMileage += dailyMileage;
		
		return result;
	}
	
	/*
	 * Generate a normal s11 value
	 */
	private double rollNormal() {
		Random randomno = new Random();
		double x = randomno.nextGaussian() * 0.1 + 1;
		while (isOutlier(x)) {
			// Guarantee is normal value
			x = randomno.nextGaussian() * 0.1 + 1;
		}
		return x;
	}
	
	/*
	 * Generate an outlier s11 value
	 */
	private double rollOutlier() {
		Random randomno = new Random();
		double x = randomno.nextGaussian() * 10 + 1;
		while (isNormal(x)) {
			// Guarantee is outlier value
			x = randomno.nextGaussian() * 10 + 1;
		}
		return x;
	}
	
	/*
	 * Check if the generated x value is a normal value or an outlier
	 */
	private boolean isNormal(double x) {
		return !isOutlier(x);
	}
	
	/*
	 * Normal mean=1 std=0.1. Consider [0,2] as the normal range for x
	 */
	private boolean isOutlier(double x) {
		double ucl = 2;
		double lcl = 0;
		if (x > ucl || x < lcl) {
			return true;
		}
		return false;
	}
	
	/*
	 * Generate a series of data points for the given time span.
	 */
	public ArrayList<DailyS11> generateSeries() {
		ArrayList<DailyS11> result = new ArrayList<>();
		for (int i = 0; i < timeSpan; i++) {
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
    
	/*
	 * The ancillary main function used to test the data generator 
	 */
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
        
    }
	
}
