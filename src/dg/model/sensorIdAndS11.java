package dg.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "tire")
@XmlType(propOrder = {"sensorID", "s11", "pressure"})

/**
 * 
 *	
 *	data for one measurement of one tire, including
 * 	- sensorID (used as tire ID)
 *  - S11 (S11 value measured)
 *  - pressure (pressure of the tire)
 *  
 */ 

public class sensorIdAndS11 {
	private String sensorID;
	private Double S11;
	private Double pressure;
	
	public sensorIdAndS11() {
		super();
		this.sensorID = null;
		this.S11 = (double) 0;
		this.pressure = (double) 0;
	}
	public sensorIdAndS11(String sensorId, Double s11, Double Pressure) {
		super();
		this.sensorID = sensorId;
		this.S11 = s11;
		this.pressure = Pressure;
	}
	public void setSensorID(String sensorId) {
		this.sensorID = sensorId;
	}
	
	public String getSensorID() {
		return sensorID;
	}
	
	public void setS11(Double s11) {
		this.S11 = s11;
	}
	
	public Double getS11() {
		return S11;
	}
	public void setPressure(Double Pressure) {
		this.pressure = Pressure;
	}
	
	public Double getPressure() {
		return pressure;
	}
	
}
