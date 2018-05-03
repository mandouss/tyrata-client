package dg.view;

import java.io.File;
import java.util.Optional;
import java.util.Random;

import dg.MainApp;
import dg.model.Tire;
import dg.view.GenerateTireDialogController.Generated;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
	 * Clean current list and Creates a new tire list.
	 *  
	 */
	@FXML
	private void handleGenerateTires() {
		Generated fields = mainApp.showGenerateTireDialog();
		if (fields != null) {
			int numOfTires = fields.numTire;
			int startingNo = fields.startingNo;
			if (numOfTires != 0) {
				if(!mainApp.getTireData().isEmpty()) {
					mainApp.getTireData().clear();
				}
				for(int i=0; i<numOfTires; i++) {
					Random rand = new Random();
					//Double newTireS11 = rand.nextDouble()*1.5 - 2.5;
					Double newTireS11 = (rand.nextInt(15000) - 25000)/10000.0;  // 4-digit precision
					//String newTireID = "T-" + String.format("%04d", rand.nextInt(9999)); //4-digit id
					String newTireID = "T-" + String.format("%04d", i+startingNo); //4-digit id
					mainApp.getTireData().add(new Tire(newTireID,"UNKOWN",newTireS11,35));
				}
			}
		}
		
	}
	
	/**
	 * Clean current list, prompt a confirmation dialog.
	 *  
	 */
	@FXML
	private void handleClearAllTires() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(
		   getClass().getResource("myDialogs.css").toExternalForm());
		alert.setTitle("Clear All Tires");
		alert.setHeaderText("Clear all tires?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			mainApp.getTireData().clear();
		} else {
		    // ... user chose CANCEL or closed the dialog
		}
	}

	/**
	 * Import tire configuration from file. 
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

//	@Override 
//	public void start(Stage stage) throws Exception {
//		WebView web = new WebView();
//		web.getEngine().load("https://gitlab.oit.duke.edu/ECE651_S18/tyrata-client/blob/master/doc/Tyrata_Simulator_User_Manual.pdf");
//		Scene scene = new Scene(web);
//		stage.setScene(scene);
//		stage.show();
//	}

	@FXML
	private void handleHelpLink() {
		Stage webpageStage = new Stage();
		webpageStage.setTitle("UserManual");
		
		final WebView webView = new WebView();
		final WebEngine engine = webView.getEngine();
        engine.load("https://drive.google.com/open?id=1d1NAtgDyfQSPhr4b033Wr090reGTBT-7");

		Scene scene = new Scene(webView);
		webpageStage.setScene(scene);
		webpageStage.show();
		
        	}

	@FXML
	private void handleAbout() {
		Alert alert = new Alert(AlertType.INFORMATION);
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(
		   getClass().getResource("myDialogs.css").toExternalForm());
		
		alert.setTitle("TyrataSimulator");
		//alert.setHeaderText("About");
		alert.setHeaderText(null);
		String versionInfo = "Version: 1.2.0 \nAuthor: ECE651 Tyrata Client Team\n";
		alert.setContentText(versionInfo);

		String copyrightInfo = versionInfo + "(c) Copyright TyrataSimulator contributors and others 2018.  All rights reserved. Tyrata logo is trademark of the Tyrata Inc., https://www.tyrata.com/.";
		Label label = new Label("Copyright Info");

		TextArea textArea = new TextArea(copyrightInfo);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		//textArea.setMaxWidth(Double.MAX_VALUE);
		//textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane verContent = new GridPane();
		verContent.setMaxWidth(Double.MAX_VALUE);
		verContent.add(label, 0, 0);
		verContent.add(textArea, 0, 1);

		alert.getDialogPane().setExpandableContent(verContent);
		//alert.getDialogPane().setContent(verContent);

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

