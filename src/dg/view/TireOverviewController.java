package dg.view;

import javafx.collections.ListChangeListener;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import java.util.ArrayList;

import dg.MainApp;
import dg.bluetooth.BlueToothService;
import dg.model.DailyS11;
import dg.model.DataGenerator;
import dg.model.Tire;
import dg.util.DateUtil;

public class TireOverviewController {
	@FXML private TableView<Tire> tireTable;
	@FXML private TableColumn<Tire, String> tireIDColumn;
	@FXML private TableColumn<Tire, Number> initS11Column;  //Integer, Double ... Should be Number
	@FXML private Text tireCountText;
	@FXML private Label addTireLabel;
	@FXML private ImageView instructionImage;
	
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
	
	@FXML private ScrollPane commsPane;
	@FXML private TextFlow commsFlow;
	@FXML private TextArea commsArea;
	
	@FXML private Button editTireButton;
	@FXML private Button deleteTireButton;
	
	@FXML private Button saveDataButton;
	@FXML private Button showDataButton;
	

	// Reference to the main application.
	private MainApp mainApp;
	private BlueToothService service = null;

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
	
	/**
	 * Fills all text fields to show details about the tire.
	 * If the specified tire is null, all text fields are cleared.
	 * 
	 * @param Tire or null
	 */	
	private void setTireCount() {
//		System.out.println(tireTable.getItems().size());
		int count = tireTable.getItems().size();
		tireCountText.setText(String.valueOf(count));
		if(count == 0) { 
			addTireLabel.setVisible(true);
			instructionImage.setVisible(true);
			editTireButton.setDisable(true);
			deleteTireButton.setDisable(true);
		} else {
			addTireLabel.setVisible(false);
			instructionImage.setVisible(false);
			editTireButton.setDisable(false);
			deleteTireButton.setDisable(false);
		}
	}

	@FXML
	private void mouseClicked(MouseEvent mouseEvent) {
		if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
			//			cancel selections
			//			int selectedIndex = tireTable.getSelectionModel().getSelectedIndex();
			//			tireTable.getSelectionModel().clearAndSelect(selectedIndex);
			//			Node selected = mouseEvent.getPickResult().getIntersectedNode();
			//			System.out.println(selected);
			//			if(selected == null || (selected instance of TableRow && ((TableRow) selected).isEmpty())) {
			//				tireTable.getSelectionModel().clearSelection();
			//			}
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
		boolean saveClicked = mainApp.showTireEditDialog(newTire, "New Tire");
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
			boolean saveClicked = mainApp.showTireEditDialog(selectedTire, "Edit Tire");
			if (saveClicked) {
				showTireDetails(selectedTire);
			}
		}
		else {
			//No item selected, do nothing
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
			//No item selected, do nothing
		}
	}


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
		outlierIntervalField.setDisable(true);
		outlierIntervalField.setPromptText("Days");
		//initS11Field.setFocusTraversable(false);
		startDatePicker.setValue(LocalDate.now());
		statusText.setText("");
//		statusText.setTextAlignment();
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
			boolean outlierEnabled = enableOutlierBox.isSelected();
			int outlierInterval = -1;
			if (outlierEnabled) {
				outlierInterval = Integer.parseInt(outlierIntervalField.getText());
			}
			// System.out.println("Successfully Generated Data: outlier ("+ outlierEnabled + "): "+ outlierInterval);

			//dataGen
			DataGenerator dataGen = new DataGenerator(startDate, timeSpan, dailyMileage, 
					tireList, outlierEnabled, outlierInterval);
			//day_list
			ArrayList<DailyS11> newS11List = dataGen.generateSeries();
			newS11List.forEach((dailyS11) -> dailyS11.print());
			statusText.setStyle("-fx-fill: #359E4B;");
			statusText.setText("Data Generated");
			mainApp.getS11List().clear();
			mainApp.getS11List().addAll(newS11List);
			
			saveDataButton.setDisable(false);
			showDataButton.setDisable(false);
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
			//List is empty, do nothing
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
				errorMessage += "Invalid Time Span (Between 1 and 3650)\n"; 
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
			// does not check for outlierIntervalField 
		} else if(outlierIntervalField.getText() == null || outlierIntervalField.getText().length() == 0) {
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
		if(tireTable.getItems().size()==0) {
			errorMessage = "Tire Table is empty, please add tires to the table first!";
		}
		if (errorMessage == null || errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message.
			//	            Dialogs.create()
			//	                .title("Invalid Fields")
			//	                .masthead("Please correct invalid fields")
			//	                .message(errorMessage)
			//	                .showError();
			errorMessage = errorMessage.substring(0, errorMessage.length() - 1);
			statusText.setStyle("-fx-text-fill: #C8595C; -fx-font-size: 12px");
			statusText.setText(errorMessage);
			return false;
		}
	}

	@FXML
	private void DG_handleSaveAs() {
		if(mainApp.getS11List().size() != 0) {
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
		else {
			statusText.setText("No data generated yet!");
			return;
		}
	}



    /*******************************************************************
     *****************    Bluetooth Broadcasting   *********************
     ********************************************************************/
    
    @FXML
    public void handleBroadcast() {
    		/*String msg = "Starting BroadCasting ... (Not Really)\n";
    		Text t = new Text();
        t.setStyle("-fx-fill: #359E4B;-fx-font-weight:bold;");
        t.setText(msg);
    		commsFlow.getChildren().add(t);
    		*/
    		FileChooser fileChooser = new FileChooser();
    		//Set extension filter
    		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
    				"XML files (*.xml)", "*.xml");
    		fileChooser.getExtensionFilters().add(extFilter);
    		// Show open file dialog
    		if(service == null || (!service.isRunning())) {
    			service = null;
    			File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
    			if (file != null) {
    				String path = file.getAbsolutePath();
    				service = new BlueToothService();
    				service.set_file_path(path);
    				service.set_comms_Flow(commsFlow);
    				service.set_notifier();
    				String msg = "Creating Bluetooth Connection to Android Mobile!\n";
    				Text txt = new Text();
    				txt.setStyle("-fx-fill: #359E4B;-fx-font-weight:bold;");
    				txt.setText(msg);
    				commsFlow.getChildren().add(txt);
    				service.setOnSucceeded(new EventHandler<WorkerStateEvent>(){    				  
    					@Override
    					public void handle(WorkerStateEvent t) {
    						String msg = "Bluetooth Transmition succeeded!\n";
    						Text txt = new Text();
    						txt.setStyle("-fx-fill: #359E4B;-fx-font-weight:bold;");
    						txt.setText(msg);
    						commsFlow.getChildren().add(txt);
    		            }
    				});
    				service.setOnFailed(new EventHandler<WorkerStateEvent>(){
    					@Override
    					public void handle(WorkerStateEvent t) {
    						String msg = "Bluetooth Connection Failed!\n";
    						Text txt = new Text();
    						txt.setStyle("-fx-fill: #359E4B;-fx-font-weight:bold;");
    						txt.setText(msg);
    						commsFlow.getChildren().add(txt);
    					}
    				});
    				
    				service.setOnCancelled(new EventHandler<WorkerStateEvent>(){
    					@Override
    					public void handle(WorkerStateEvent t) {
    						String msg = "Bluetooth Connection has been cancelled!\n";
    						Text txt = new Text();
    						txt.setStyle("-fx-fill: #359E4B;-fx-font-weight:bold;");
    						txt.setText(msg);
    						commsFlow.getChildren().add(txt);
    					}
    				});
    				service.start();
    			}
    			else {
    				String msg = "File doesn't exist!\n";
    		    	Text t = new Text();
    		    	t.setStyle("-fx-fill: #C8595C;-fx-font-weight:bold;");
    		    	t.setText(msg);
    		    	commsFlow.getChildren().add(t);
    			}
    		}
    }
    @FXML
    public void handleBroadcastCancel() {
    	    if(service != null && service.isRunning()) {
    	    	String msg = "Shutting down BroadCasting ...\n";
    	    	Text t = new Text();
    	    	t.setStyle("-fx-fill: #C8595C;-fx-font-weight:bold;");
    	    	t.setText(msg);
    	    	commsFlow.getChildren().add(t);
    	    	service.stop();
    	    	service.cancel();
    	    	System.out.println(service.getState().toString());
    	    	service = null;
//	    		commsArea.appendText(msg);
    	  }
    		
    }


	/**
	 * Initialize the comms info area
	 * 
	 */
	public void setCommsInfo() {
		commsPane.setFitToWidth(true);
		commsPane.setFitToHeight(true);
//		commsArea.setText("");
//		commsArea.setWrapText(true);
//		commsArea.setEditable(false);
	}
    
	


    /*******************************************************************
     *****************    initialization & setup   *********************
     ********************************************************************/
    

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		// Initialize the tire table with the two columns.
		tireTable.setFocusTraversable(true);
		addTireLabel.setVisible(false);
		instructionImage.setVisible(false);
		tireIDColumn.setCellValueFactory(cellData -> cellData.getValue().getTireIDProperty());
		initS11Column.setCellValueFactory(cellData -> cellData.getValue().getInitS11Property());

		// Clear tire details.
		showTireDetails(null);
		showGenInfo();
		
		// Listen for selection changes and show the tire details when changed.
		tireTable.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> showTireDetails(newValue));

//		tireTable.getItems().addListener(
//				(ListChangeListener<Tire>) ((change) -> {
//					System.out.println("Changed!!");
//				}));

		commsFlow.getChildren().addListener(
				(ListChangeListener<Node>) ((change) -> {
					//                    commsFlow.layout();
					//                    commsPane.layout();
					commsPane.setVvalue(1.0f);
				}));
	    
		setCommsInfo();
		
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
		// Listen for changes in tire number and display on Screen
		setTireCount();
		saveDataButton.setDisable(true);
		showDataButton.setDisable(true);
		mainApp.getTireData().addListener(
				(ListChangeListener<Tire>) ((change) -> {
					setTireCount();
				}));
//		mainApp.getS11List().addListener
	}
	
}
