package dg.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "tire")
public class sensorIdAndS11 {
	private String sensorID;
	private Double S11;
	
	public sensorIdAndS11() {
		super();
		this.sensorID = null;
		this.S11 = (double) 0;
	}
	public sensorIdAndS11(String sensorId, Double s11) {
		super();
		this.sensorID = sensorId;
		this.S11 = s11;
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
	
}
