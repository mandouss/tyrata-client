package dg.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

import java.io.File;

import dg.MainApp;

/**
 * The controller for the root layout. The root layout provides the 
 * basic application layout containing a menu bar and space where other
 * JavaFx elements can be placed.
 *
 */
public class RootLayoutController {
	// Reference to the main application
	
	private MainApp mainApp;
	
	
	
	
	
	
	
	
	
	/**
	 * Is called by the main application to give a reference back to itself
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	/**
	 * Creates an empty Tire list.
	 */
	/*@FXML
	private void handleNew() {
		mainApp.getTireData().clear();
		mainApp.setTireFilePath(null);
	}
	*/
	@FXML
	private void handleOpen() {
		FileChooser fileChooser = new FileChooser();
		
		//Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);
		
		
		// Show open file dialog
		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
		
		if (file != null) {
			mainApp.loadTireDataFromFile(file);
		}
	}
		
		/**
		 * Saves the file to the tire file that is currently open. If there is
		 * no open file, the "save as" dialog is shown
		 */
		@FXML
		private void handleSave() {
			File tireFile = mainApp.getTireFilePath();
			if (tireFile != null) {
				mainApp.saveTireDataToFile(tireFile);
			} else {
				handleSaveAs();
			}
		}
		/**
		 * Opens a FileChooser to let the user select a file to save to.
		 * 
		 */
		@FXML
		private void handleSaveAs() {
			FileChooser fileChooser = new FileChooser();
			
			// Set extension filter
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
					"XML files (*.xml)","*.xml");
			fileChooser.getExtensionFilters().add(extFilter);
			
			// Show save file dialog
			File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
			
			if (file != null) {
				//Make sure it has the correct extension
				if (!file.getPath().endsWith(".xml")) {
					file = new File(file.getPath() + ".xml");
					
				}
				mainApp.saveTireDataToFile(file);
			}
		}
		
		@FXML
		private void handleAbout() {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("TyrataSimulator");
			alert.setHeaderText("About");
			alert.setContentText("Author: ECE651 Tyrata Client Team");
			
			alert.showAndWait();
		}
		
		/**
		 * Closes the application.
		 * 
		 */
		@FXML
		private void handleExit() {
			System.exit(0);
		}
}	
