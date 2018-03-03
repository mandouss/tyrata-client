package dg.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


import dg.MainApp;
import dg.model.Tire;
import dg.util.DateUtil;

public class TireOverviewController {
    @FXML
    private TableView<Tire> tireTable;
    @FXML
    private TableColumn<Tire, String> tirePosColumn;
    //@FXML
    //private TableColumn<Tire, LocalDate> startTimeColumn;
    @FXML
    private TableColumn<Tire, Number> initS11Column;  //Integer, Double ... Should be Number

    @FXML
    private Label tireIDLabel;
    @FXML
    private Label tirePosLabel;
    @FXML
    private Label initS11Label;
    @FXML
    private Label startTimeLabel;
    @FXML
    private Label timeIntervalLabel;

    // Reference to the main application.
    private MainApp mainApp;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public TireOverviewController() {
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

    /**
     * Fills all text fields to show details about the person.
     * If the specified person is null, all text fields are cleared.
     * 
     * @param person the person or null
     */
    private void showTireDetails(Tire tire) {
        if (tire != null) {
            // Fill the labels with info from the person object.
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
     ********************  DataGen Config Part  *************************
     ********************************************************************/
    
    
    
    
    
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
    	    tirePosColumn.setCellValueFactory(cellData -> cellData.getValue().getTirePosProperty());
    	    //startTimeColumn.setCellValueFactory(cellData -> cellData.getValue().getStartDateProperty());
    	    initS11Column.setCellValueFactory(cellData -> cellData.getValue().getInitS11Property());
    	    
    	    // Clear person details.
    	    showTireDetails(null);
    	    // Listen for selection changes and show the person details when changed.
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
