package dg.view;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
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
	//@FXML private Label timeIntervalLabel;

	@FXML private DatePicker startDatePicker;
	@FXML private TextField timeSpanField;
	@FXML private TextField dailyMileageField;
	@FXML private TextField outlierIntervalField;
	@FXML private Text statusText;

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
			startTimeLabel.setText(DateUtil.format(tire.getStartDate()));
			//timeIntervalLabel.setText(Integer.toString(tire.getTimeInterval()));

		} else {
			// Tire is null, remove all the text.
			tireIDLabel.setText("");
			tirePosLabel.setText("");
			initS11Label.setText("");
			startTimeLabel.setText("");
			//timeIntervalLabel.setText("");
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
		if(selectedIndex >= 0) {
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
		timeSpanField.setPromptText("(1-3650) Days");
		dailyMileageField.setText("");
		dailyMileageField.setPromptText("(1-5000) Miles");
		outlierIntervalField.setText("-1");
		outlierIntervalField.setPromptText("Days (-1 to Disable)");
		//initS11Field.setFocusTraversable(false);
		startDatePicker.setValue(LocalDate.now());
		statusText.setText("");
	}

	@FXML
	public void handleDataGenerate() {
		if(isDataInputValid()) {
			LocalDate startDate = startDatePicker.getValue();
			int timeSpan = Integer.parseInt(timeSpanField.getText());
			int dailyMileage = Integer.parseInt(dailyMileageField.getText());
			List<Tire> tireList = mainApp.getTireData();
			int outlierInterval = Integer.parseInt(outlierIntervalField.getText());
			boolean outlierEnabled = true;
			if(outlierInterval == -1) {
				outlierEnabled = false;
			}
			//dataGen
			DataGenerator dataGen = new DataGenerator(startDate, timeSpan, dailyMileage, 
					tireList, outlierEnabled, outlierInterval);
			//day_list
			ArrayList<DailyS11> newS11List = dataGen.generateSeries();
			newS11List.forEach((dailyS11) -> dailyS11.print());
			statusText.setText("Data Generated");
			mainApp.getS11List().clear();
			mainApp.getS11List().addAll(newS11List);
		}
	}

	@FXML
	private void handleDataShow() {

		//if(!mainApp.getS11List().isEmpty()) {
		if(mainApp.getS11List() != null) {
			statusText.setText("");
			mainApp.showGeneratedData(); //the method returns a boolean if succeed
		}
		else {
			//List is empty
			//do nothing
			//TODO: Maybe I can prompt a warning sign
		}
	}

	/**
	 * Validates the user input in the text fields.
	 * 
	 * @return true if the input is valid
	 */
	private boolean isDataInputValid() {
		String errorMessage = "";
		/*
		if (startDatePicker.getValue() == null || startDatePicker.getText().length() == 0) {
			errorMessage += "Lack tire ID!\n"; 
		}
		 */

		if (timeSpanField.getText() == null || timeSpanField.getText().length() == 0) {
			errorMessage += "Lack Time Span!\n"; 
		} else {
			try {
				int timeSpan = Integer.parseInt(timeSpanField.getText());
				if(timeSpan <= 0 || timeSpan > 3650) {
					errorMessage += "Invalid Time Span (Between 1 and 3650)\n";
				}
			} catch (NumberFormatException e) {
				errorMessage += "Invalid Time Span (Between 1 and 3650)\\n"; 
			}
		}

		if (dailyMileageField.getText() == null || dailyMileageField.getText().length() == 0) {
			errorMessage += "Lack Daily Mileage!\n"; 
		} else {
			try {
				int dailyMile = Integer.parseInt(dailyMileageField.getText());
				if(dailyMile <= 0 || dailyMile > 5000) {
					errorMessage += "Invalid Daily Mileage (Between 1 and 5000)\n";
				}
			} catch (NumberFormatException e) {
				errorMessage += "Invalid Daily Mileage (Between 1 and 5000)\n"; 
			}
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message.
			//	            Dialogs.create()
			//	                .title("Invalid Fields")
			//	                .masthead("Please correct invalid fields")
			//	                .message(errorMessage)
			//	                .showError();
			statusText.setText(errorMessage);
			//TODO: Change color
			return false;
		}
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
	
	
	
	
	
	
	 /*******************************************************************
     *********************    Save Data Branch   *************************
     ********************************************************************/
     /**
      
      /**
     * Opens a FileChooser to let the user select a file to save to.
     */
    
	@FXML
	private void DG_handleOpen() {
		// getS11list clear
		mainApp.getS11List().clear();
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show open file dialog
		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

		if (file != null) {
			mainApp.loadDGDataFromFile(file);
		}
	}

    @FXML
    private void DG_handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            mainApp.saveDGDataToFile(file);
        }
    }

    /*******************************************************************
     *********************    Save Data Branch   *************************
     ********************************************************************/
    
    
    
    
    
    
}
