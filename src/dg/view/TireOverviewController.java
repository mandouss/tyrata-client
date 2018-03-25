package dg.view;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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
	@FXML private TableColumn<Tire, String> tireIDColumn;
	@FXML private TableColumn<Tire, Number> initS11Column;  //Integer, Double ... Should be Number
	
	@FXML private Label tireIDLabel;
	@FXML private Label tirePosLabel;
	@FXML private Label initS11Label;
	@FXML private Label startTimeLabel;
	@FXML private Label pressureLabel;
	//@FXML private Label timeIntervalLabel;

	@FXML private DatePicker startDatePicker;
	@FXML private TextField timeSpanField;
	@FXML private TextField dailyMileageField;
	@FXML private CheckBox enableOutlierBox;
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
			pressureLabel.setText(Double.toString(tire.getPressure()));
			//timeIntervalLabel.setText(Integer.toString(tire.getTimeInterval()));

		} else {
			// Tire is null, remove all the text.
			tireIDLabel.setText("");
			tirePosLabel.setText("");
			initS11Label.setText("");
			startTimeLabel.setText("");
			pressureLabel.setText("");
			//timeIntervalLabel.setText("");
		}
	}

	@FXML
	private void mouseClicked(MouseEvent mouseEvent) {
		if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
			//TODO: can't clear selection
			int selectedIndex = tireTable.getSelectionModel().getSelectedIndex();
			tireTable.getSelectionModel().clearAndSelect(selectedIndex);
			//tireTable.get
			Node selected = mouseEvent.getPickResult().getIntersectedNode();
			//System.out.println(selected);
			if(selected == null || (selected instanceof TableRow && ((TableRow) selected).isEmpty())) {
				tireTable.getSelectionModel().clearSelection();
			}
			if(mouseEvent.getClickCount() == 2){
				handleEditTire();
				//System.out.println("Double clicked on "+ tireTable.getSelectionModel().getSelectedIndex());
			}
		}
	}

	@FXML
	private void keyPressed(KeyEvent keyEvent) {
		//System.out.println("PRESS Detected " + keyEvent.getCode());
	    if (keyEvent.getCode() == KeyCode.ENTER) {
	    		//System.out.println("Enter Pressed!!!");
	    }
	}
	
	@FXML
	private void keyReleased(KeyEvent keyEvent) {
		//System.out.println("Release Detected " + keyEvent.getCode());
	    if (keyEvent.getCode() == KeyCode.BACK_SPACE || keyEvent.getCode() == KeyCode.DELETE) {
	    		//System.out.println("Enter Released!!!");
			int selectedIndex = tireTable.getSelectionModel().getSelectedIndex();
			if(selectedIndex >= 0) {
				tireTable.getItems().remove(selectedIndex);
			}
	    }
	}

	/** This function will overwrite keyPressed and keyReleased
	 * @param keyEvent
	 */
	@FXML
	private void keyTyped(KeyEvent keyEvent) {
		
		System.out.println("TYPE Detected " + keyEvent.getCharacter());
		if (keyEvent.getCharacter()  == "d") {
			System.out.println("Delete Typed!!!");
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

		enableOutlierBox.setSelected(false);
		outlierIntervalField.setText("-1");
		outlierIntervalField.setDisable(true);
		outlierIntervalField.setPromptText("Days");
		//initS11Field.setFocusTraversable(false);
		startDatePicker.setValue(LocalDate.now());
		statusText.setText("");
	}
	
	@FXML
	public void handleOutlierEnable() {
		if(enableOutlierBox.isSelected()) {
			outlierIntervalField.setDisable(false);
		} else {
			outlierIntervalField.setDisable(true);
		}
	}
	
	
	@FXML
	public void handleDataGenerate() {
		if(isDataInputValid()) {
			LocalDate startDate = startDatePicker.getValue();
			int timeSpan = Integer.parseInt(timeSpanField.getText());
			int dailyMileage = Integer.parseInt(dailyMileageField.getText());
			List<Tire> tireList = mainApp.getTireData();
			int outlierInterval = Integer.parseInt(outlierIntervalField.getText());
			boolean outlierEnabled = enableOutlierBox.isSelected();
			// System.out.println("Successfully Generated Data: outlier ("+ outlierEnabled + "): "+ outlierInterval);
			
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
		
		if (!enableOutlierBox.isSelected()) {
			
		}
		else if(outlierIntervalField.getText() == null || outlierIntervalField.getText().length() == 0) {
			errorMessage += "Lack Outlier Interval!\n"; 
		} else {
			try {
				int dailyMile = Integer.parseInt(outlierIntervalField.getText());
				if(dailyMile <= 0 || dailyMile > 5000) {
					errorMessage += "Invalid Outlier Interval (Between 1 and 5000)\n";
				}
			} catch (NumberFormatException e) {
				errorMessage += "Invalid Outlier Interval (Between 1 and 5000)\n"; 
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
		tireTable.setFocusTraversable(true);
		
		tireIDColumn.setCellValueFactory(cellData -> cellData.getValue().getTireIDProperty());
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
     *********************    Data Saving   *****************************
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
     *****************    Bluetooth Broadcasting   *********************
     ********************************************************************/
    
    
    
    
    
    
}
