package dg.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import dg.util.LocalDateAdapter;


@XmlRootElement(name = "dailyS11")  //tag <dailyS11> in XML


/**
 * 
 * data for one measurement of one car(with many tires), including
 *  - timestamp (timestamp for the measurement)
 *  - mileage (mileage of the car at the moment of measurement)
 *  - sensorIdAndS11_list (list of measured data for each tire)
 *  
 *  measured data for each tire contains
 * 	- sensorID (used as tire ID)
 *  - S11 (S11 value measured)
 *  - pressure (pressure of the tire)
 * 
 * important methods:
 * convertToString() string : convert this measurement to string
 * print() void : convert this measurement to string & print on stdout
 */
public class DailyS11 {
	
	private LocalDate timestamp;
	private int mileage;
	private ArrayList<sensorIdAndS11> sensorIdAndS11_list;
	@XmlElement(name = "tire")   //tag <tire> in XML
								//every tire sensor info has a tag <tire> in XML
	
	public void setSensorIdAndS11_List(ArrayList<sensorIdAndS11> sensorIdAndS11_list) {
		this.sensorIdAndS11_list = sensorIdAndS11_list;
		}
	 
	public ArrayList<sensorIdAndS11> getSensorIdAndS11_List() {
		return sensorIdAndS11_list;
	  }
	
	public void setTimeStamp(LocalDate timestamp) {
		this.timestamp = timestamp;
	}
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	public LocalDate getTimeStamp() {
		return timestamp;
	}
	
	public void setMileage(int mileage) {
		this.mileage = mileage;
	}
	
	public int getMileage() {
		return mileage;
	}
	  
	public DailyS11() {
		super();
		this.timestamp = null;
		this.mileage = 0;
		this.sensorIdAndS11_list = new ArrayList<>();
	}
	
	
	public DailyS11(LocalDate timestamp, int mileage) {
		super();
		this.timestamp = timestamp;
		this.mileage = mileage;
		this.sensorIdAndS11_list = new ArrayList<>();
	}
	
	/**
	 * add one tire sensor info to one day's data
	 */
	public void addTireS11(String sensorInfo, double s11, double pressure) {
		sensorIdAndS11_list.add(new sensorIdAndS11(sensorInfo, s11, pressure));
	}
	
	/**
	 * Print out DailyS11 class info
	 */
	public void print() {
		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
		System.out.println("Timestamp:" + timestamp.format(formatter));
		System.out.println("Mileage: " + mileage);
		
		for (int i = 0; i < sensorIdAndS11_list.size(); i++) {
			System.out.println("Sensor ID: "+ sensorIdAndS11_list.get(i).getSensorID());
			System.out.println("S11: " + sensorIdAndS11_list.get(i).getS11());
			System.out.println("Pressure: " + sensorIdAndS11_list.get(i).getPressure());
		}
	}


	/**
	 * Convert DailyS11 class info to String, for data Show function
	 */

	public String convertToString() {
		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
		String result = "";
		result += "Timestamp:" + timestamp.format(formatter) + "\n";
		result += "Mileage: " + mileage + "\n";

		for (int i = 0; i < sensorIdAndS11_list.size(); i++) {
			result += "Sensor ID: "+ sensorIdAndS11_list.get(i).getSensorID() + "\n";
			result += "S11: " + sensorIdAndS11_list.get(i).getS11() + "\n";
			result += "Pressure: " + sensorIdAndS11_list.get(i).getPressure() + "\n";
		}

		return result;
	}

}
