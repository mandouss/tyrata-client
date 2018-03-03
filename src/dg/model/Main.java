package dg.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //tireInfo_list
        Tire tireLF = new Tire("C-1", "LF", -2.0);
        Tire tireRF = new Tire("C-154", "RF", -1.5);
        List<Tire> tireList = new ArrayList<>();
        tireList.add(tireLF);
        tireList.add(tireRF);
        //startDate
        ObjectProperty<LocalDate> startDate = new SimpleObjectProperty<>(LocalDate.of(2018,03,01));
        //timeLength
        IntegerProperty timeLength = new SimpleIntegerProperty(10);
        //dailyMileage
        IntegerProperty dailyMileage = new SimpleIntegerProperty(15);
        //dataGen
        DataGenerator dataGen = new DataGenerator(startDate, timeLength, dailyMileage, tireList);
        //day_list
        ArrayList<DailyS11> result = dataGen.GenerateSeries();
        result.forEach((dailyResult) -> dailyResult.print());
    }
}
