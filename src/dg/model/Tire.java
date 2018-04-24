package dg.model;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import dg.util.LocalDateAdapter;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 
 * 
 * tire data (for one tire):
 * - tirePosition (where this tire located)
 * - s11_i (initial S11 measurement of this tire)
 * - startDate (when this tire is installed)	
 * - pressure (tire pressure)
 */

public class Tire {
	private final StringProperty tireID;
	private final StringProperty tirePosition;
	private final DoubleProperty s11_i;
    private final ObjectProperty<LocalDate> startDate;
  	private final DoubleProperty pressure;
	
	
	public Tire() {
		// initialize s11_i to be -2. initialize pressure to be 3.5
		this(null,"UNKNOWN",-2,35);
	}
	public Tire(String tireID, String tirePosition, double s11_i,double pressure) {
		this.tireID = new SimpleStringProperty(tireID);
		this.tirePosition = new SimpleStringProperty(tirePosition);
		this.s11_i = new SimpleDoubleProperty(s11_i);
		this.startDate = new SimpleObjectProperty<LocalDate>(LocalDate.now()); //default: start from current time
		this.pressure = new SimpleDoubleProperty(pressure);
	}
	
	public Tire(String tireID, String tirePosition, float s11_i,LocalDate startDate, double pressure) {
		this.tireID = new SimpleStringProperty(tireID);
		this.tirePosition = new SimpleStringProperty(tirePosition);
		this.s11_i = new SimpleDoubleProperty(s11_i);
		this.startDate = new SimpleObjectProperty<LocalDate>(startDate);
		this.pressure = new SimpleDoubleProperty(pressure);
	}

    public String getTireID() {
        return tireID.get();
    }
    public void setTireID(String TireID) {
        this.tireID.set(TireID);
    }
    public StringProperty getTireIDProperty() {
        return tireID;
    }
    
    public String getTirePos() {
        return tirePosition.get();
    }
    public void setTirePos(String TirePos) {
        this.tirePosition.set(TirePos);
    }
    public StringProperty getTirePosProperty() {
        return tirePosition;
    }
    
    public double getInitS11() {
        return s11_i.get();
    }
    public void setInitS11(double coeff) {
        this.s11_i.set(coeff);
    }
    public DoubleProperty getInitS11Property() {
        return s11_i;
    }
    
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    public LocalDate getStartDate() {
        return startDate.get();
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate.set(startDate);
    }
    public ObjectProperty<LocalDate> getStartDateProperty() {
        return startDate;
    }
    
    public double getPressure() {
        return pressure.get();
    }
    public void setPressure(double coeff) {
        this.pressure.set(coeff);
    }
    public DoubleProperty getPressureProperty() {
        return pressure;
    }
    
}
