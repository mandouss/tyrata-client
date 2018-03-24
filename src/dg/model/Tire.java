package dg.model;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import dg.util.LocalDateAdapter;
//import javafx.beans.property.IntegerProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
//import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Tire {
	private final StringProperty tireID;
	private final StringProperty tirePosition;
	private final DoubleProperty s11_i;
    private final ObjectProperty<LocalDate> startDate;
  //private final IntegerProperty timeInterval;
  	private final DoubleProperty pressure;
	
	
	public Tire() {
		// initialize s11_i to be -2. initialize pressure to be 3.5
		this(null,null,-2,3.5);
	}
	public Tire(String tireID, String tirePosition, double s11_i,double pressure) {
		this.tireID = new SimpleStringProperty(tireID);
		this.tirePosition = new SimpleStringProperty(tirePosition);
		this.s11_i = new SimpleDoubleProperty(s11_i);
		this.startDate = new SimpleObjectProperty<LocalDate>(LocalDate.now()); //default: start from current time
		//this.timeInterval = new SimpleIntegerProperty(1); //default: once a day
		this.pressure = new SimpleDoubleProperty(pressure);
	}
	
	public Tire(String tireID, String tirePosition, float s11_i,LocalDate startDate, double pressure) {
		this.tireID = new SimpleStringProperty(tireID);
		this.tirePosition = new SimpleStringProperty(tirePosition);
		this.s11_i = new SimpleDoubleProperty(s11_i);
		this.startDate = new SimpleObjectProperty<LocalDate>(startDate);
		//this.timeInterval = new SimpleIntegerProperty(timeInterval); 
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
    
    /*public int getTimeInterval() {
        return timeInterval.get();
    }
    public void setTimeInterval(int timeInterval) {
        this.timeInterval.set(timeInterval);
    }
    public IntegerProperty getTimeIntervalProperty() {
        return timeInterval;
    }*/
}
