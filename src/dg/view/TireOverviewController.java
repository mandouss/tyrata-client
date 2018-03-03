package dg.view;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;

import dg.MainApp;
import dg.model.DailyS11;
import dg.model.DataGenerator;
import dg.model.Tire;
import dg.util.DateUtil;

public class TireOverviewController {
    @FXML private TableView<Tire> tireTable;
    @FXML private TableColumn<Tire, String> tirePosColumn;
    @FXML private TableColumn<Tire, Number> initS11Column;  //Integer, Double ... Should be Number

    @FXML private Label tireIDLabel;
    @FXML private Label tirePosLabel;
    @FXML private Label initS11Label;
    @FXML private Label startTimeLabel;
    @FXML private Label timeIntervalLabel;

    @FXML private DatePicker startDatePicker;
    @FXML private TextField timeSpanField;
    @FXML private TextField dailyMileageField;
    
    // Reference to the main application.
    private MainApp mainApp;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public TireOverviewController() {
    }
    
    
    /*******************************************************************
    *********************    Tire Table Part   *************************
    ********************************************************************/
    /**
     * Fills all text fields to show details about the tire.
     * If the specified tire is null, all text fields are cleared.
     * 
     * @param Tire or null
     */
    private void showTireDetails(Tire tire) {
        if (tire != null) {
            // Fill the labels with info from the tire object.
        		tireIDLabel.setText(tire.getTireID());
        		tirePosLabel.setText(tire.getTirePos());
        		initS11Label.setText(Double.toString(tire.getInitS11()));
        		timeIntervalLabel.setText(Integer.toString(tire.getTimeInterval()));
        		startTimeLabel.setText(DateUtil.format(tire.getStartDate()));

        } else {
            // Tire is null, remove all the text.
        		tireIDLabel.setText("");
        		tirePosLabel.setText("");
        		initS11Label.setText("");
        		startTimeLabel.setText("");
        		timeIntervalLabel.setText("");
        }
    }
    
    /********************************************************************
    *********************    Tire Config Part  *************************
    ********************************************************************/
    
    /**
     * Called when the user clicks on the new button.
     */
    @FXML
    private void handleNewTire() {
    		Tire newTire = new Tire();
    		boolean saveClicked = mainApp.showTireEditDialog(newTire);
    		if (saveClicked) {
    			mainApp.getTireData().add(newTire);
    		}
    }
    /**
     * Called when the user clicks on the edit button.
     */
    @FXML
    private void handleEditTire() {
        Tire selectedTire = tireTable.getSelectionModel().getSelectedItem();
        if(selectedTire != null) {
        		boolean saveClicked = mainApp.showTireEditDialog(selectedTire);
        		if (saveClicked) {
        			showTireDetails(selectedTire);
            }
        }
        else {
        		//No item selected 
        		//do nothing
        		//TODO: Maybe I can prompt a warning sign
        }
    }
    
    /**
     * Called when the user clicks on the delete button.
     */
    @FXML
    private void handleDeleteTire() {
        int selectedIndex = tireTable.getSelectionModel().getSelectedIndex();
        if(selectedIndex > 0) {
        		tireTable.getItems().remove(selectedIndex);
        }
        else {
        		//No item selected 
        		//do nothing
        		//TODO: Maybe I can prompt a warning sign
        }
    }
    
//    @FXML
//    private void handleDeleteAllTire() {
//        tireTable.getItems().removeAll();
//    }

    
    /********************************************************************
     ********************  DataGen Config Part  *************************
     ********************************************************************/
	/**
	 * Sets the data to be edited in the dialog.
	 * 
	 * @param DataGenerator
	 */
	public void showGenInfo() {
		//this.tire = tire;
		
		timeSpanField.setText("");
		timeSpanField.setPromptText("(Integer) Days");
		dailyMileageField.setText("");
		dailyMileageField.setPromptText("(Integer) Miles");
		//initS11Field.setFocusTraversable(false);
		startDatePicker.setValue(LocalDate.now());
	}
	
	public void configDataGen() {
		LocalDate startDate = startDatePicker.getValue();
		int timeSpan = Integer.parseInt(timeSpanField.getText());
		int dailyMileage = Integer.parseInt(dailyMileageField.getText());
		List<Tire> tireList = mainApp.getTireData();
		
        //dataGen
        DataGenerator dataGen = new DataGenerator(startDate, timeSpan, dailyMileage, tireList);
        //day_list
        ArrayList<DailyS11> result = dataGen.GenerateSeries();
        result.forEach((dailyResult) -> dailyResult.print());
	}
    
    
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the tire table with the two columns.
    	    tirePosColumn.setCellValueFactory(cellData -> cellData.getValue().getTirePosProperty());
    	    initS11Column.setCellValueFactory(cellData -> cellData.getValue().getInitS11Property());
    	    
    	    // Clear tire details.
    	    showTireDetails(null);
    	    showGenInfo();
    	    // Listen for selection changes and show the tire details when changed.
    	    tireTable.getSelectionModel().selectedItemProperty().addListener(
    	            (observable, oldValue, newValue) -> showTireDetails(newValue));
    	}
    
    
    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        tireTable.setItems(mainApp.getTireData());
    }
}
