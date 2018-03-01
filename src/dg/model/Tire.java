package dg.model;

import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Tire {
	private final StringProperty tireID;
	private final StringProperty tirePosition;
	private final DoubleProperty s11_i;
    private final ObjectProperty<LocalDate> startDate;
	private final IntegerProperty timeInterval;
	
	
	public Tire() {
		// initialize s11_i to be -2.
		this(null,null,-2);
	}
	public Tire(String tireID, String tirePosition, double s11_i) {
		this.tireID = new SimpleStringProperty(tireID);
		this.tirePosition = new SimpleStringProperty(tirePosition);
		this.s11_i = new SimpleDoubleProperty(s11_i);
		this.startDate = new SimpleObjectProperty<LocalDate>(LocalDate.now()); //default: start from current time
		this.timeInterval = new SimpleIntegerProperty(1); //default: once a day
	}
	
	public Tire(String tireID, String tirePosition, float s11_i,LocalDate startDate, int timeInterval) {
		this.tireID = new SimpleStringProperty(tireID);
		this.tirePosition = new SimpleStringProperty(tirePosition);
		this.s11_i = new SimpleDoubleProperty(s11_i);
		this.startDate = new SimpleObjectProperty<LocalDate>(startDate);
		this.timeInterval = new SimpleIntegerProperty(timeInterval); 
	}

    public String getTireID() {
        return tireID.get();
    }
    public void setTireID(String firstName) {
        this.tireID.set(firstName);
    }
    public StringProperty getTireIDProperty() {
        return tireID;
    }
    
    public String getTirePos() {
        return tirePosition.get();
    }
    public void setTirePos(String firstName) {
        this.tirePosition.set(firstName);
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
    
    public LocalDate getStartDate() {
        return startDate.get();
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate.set(startDate);
    }
    public ObjectProperty<LocalDate> getStartDateProperty() {
        return startDate;
    }
    
    public int getTimeInterval() {
        return timeInterval.get();
    }
    public void setTimeInterval(int timeInterval) {
        this.timeInterval.set(timeInterval);
    }
    public IntegerProperty getTimeIntervalProperty() {
        return timeInterval;
    }
}
