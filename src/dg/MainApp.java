package dg;
import dg.model.DGListWrapper;
import dg.model.DailyS11;
import dg.model.Tire;
import dg.model.TireListWrapper;
import dg.view.GeneratedDataViewController;
import dg.view.RootLayoutController;
import dg.view.TireEditDialogController;
import dg.view.TireOverviewController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;

	/**
	 * The data as an observable list of Tires.
	 */
	/**
	 * We need to save ObservableList<Tire>, this is the data we want.
	 */
	private ObservableList<Tire> tireData = FXCollections.observableArrayList();
	private ArrayList<DailyS11> s11List = new ArrayList<DailyS11>();

	public MainApp() {
		tireData	.add(new Tire("C-1234","LF",-1,3.5));
		tireData	.add(new Tire("C-1302","RF",-2,3.5));
		tireData	.add(new Tire("C-4124","LR",-1.2,3.5));
		tireData	.add(new Tire("C-9175","RR",-2.14,3.5));
	}

	/**
	 * Return the data as an observable list of Tires. 
	 * @return
	 */
	public ObservableList<Tire> getTireData() {
		return tireData;
	}
	public ArrayList<DailyS11> getS11List(){
		return s11List;
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("TyrataSimulator");

		initRootLayout();
		showTireOverview();
	}
	/**
	 * Initialize root layout.
	 */
	public void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			
			// Initialize window size
			primaryStage.setWidth(800);  
			primaryStage.setHeight(600);
			
			//Give the controller access to the main app
			RootLayoutController controller = loader.getController();
			controller.setMainApp(this);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//Try to load last opened person file

		/*File file = getTireFilePath();
		if(file != null) {
			loadTireDataFromFile(file);
		}*/
	}

	/**
	 * Show tire overview inside the root layout.
	 */
	public void showTireOverview() {
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/TireOverview.fxml"));
			AnchorPane tireOverview = (AnchorPane) loader.load();

			// Insert mainframe into the center of root layout.
			rootLayout.setCenter(tireOverview);

			// Give controller access to the main app.
			TireOverviewController controller = loader.getController();
			controller.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Opens a dialog to edit details for the specified tire. If the user clicks OK,
	 * the changes are saved into the provided tire object and true is returned.
	 * 
	 * @param Tire the tire object to be edited
	 * @return true if the user clicked OK, false otherwise.
	 */
	public boolean showTireEditDialog(Tire tire) {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/TireEditDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Person");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the tire into the controller.
			TireEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setTire(tire);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isSaveClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean showGeneratedData() {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/GeneratedDataView.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage;
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Generated Data");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the tire into the controller.
			GeneratedDataViewController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setDataContent(s11List);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Returns the main stage.
	 * @return
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}


	/**
	 * Returns the person file preference, i.e the file that was lat opened.
	 * The preference is read from the OS specific registry. If no such
	 * preference can be found, null is returned.
	 * 
	 * @return
	 */
	public File getTireFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		String filePath = prefs.get("filePath", null);
		if (filePath != null) {
			return new File(filePath);
		}
		else {
			return null;
		}
	}

	/**
	 * Sets the file path of the currently loaded file. The path is persisted in
	 * the OS specific registry
	 * 
	 * @param file the file or null to remove the path
	 */
	public void setTireFilePath(File file) {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		if(file != null) {
			prefs.put("filePath", file.getPath());

			//Update the stage title
			primaryStage.setTitle("TyrataSimulator - " + file.getName());
		}
		else {
			prefs.remove("filePath");
			primaryStage.setTitle("TyrataSimulator");
		}
	}

	public void loadTireDataFromFile(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(TireListWrapper.class);
			Unmarshaller um = context.createUnmarshaller();

			// Reading XML from the file and unmarshalling
			TireListWrapper wrapper = (TireListWrapper) um.unmarshal(file);
			tireData.clear();
			
			// TODO: add warning message for unsuccessful load
			tireData.addAll(wrapper.getTires());
			
			//save the file path to the registry.
			setTireFilePath(file);
		}
		catch(Exception e) { //catches ANY exception
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("Could not load data from file:\n" + file.getPath());
			alert.showAndWait();
		}
	}

	public void saveTireDataToFile(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(TireListWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// Wrapping our person data.
			TireListWrapper wrapper = new TireListWrapper();
			wrapper.setTires(tireData);

			//Marshalling and saving XML to the file
			m.marshal(wrapper, file);
		}
		catch(Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not save data");
			alert.setContentText("Could not save data to file:\n" + file.getPath());
			alert.showAndWait();
		}
	}
	
	

	/****************************************************    
	******************* Save Data Branch ****************
	*****************************************************/
	    

	    /**
	     * Sets the file path of the currently loaded file. The path is persisted in
	     * the OS specific registry.
	     * 
	     * @param file the file or null to remove the path
	     */
	   /* public void setDGFilePath(File file) {
	        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
	        if (file != null) {
	            prefs.put("filePath", file.getPath());

	            // Update the stage title.
	            primaryStage.setTitle("TyrataSimulator - " + file.getName());
	        } else {
	            prefs.remove("filePath");

	            // Update the stage title.
	            primaryStage.setTitle("TyrataSimulator");
	        }
	    }*/
	    
	    
	    
	    
	    /**
		 * Loads person data from the specified file. The current person data will
		 * be replaced.
		 * 
		 * @param file
		 */
		/*public void loadDGDataFromFile(File file) {
		    try {
		        JAXBContext context = JAXBContext
		                .newInstance(DGListWrapper.class);
		        Unmarshaller um = context.createUnmarshaller();

		        // Read XML from the file and unmarshal.
		        DGListWrapper wrapper = (DGListWrapper) um.unmarshal(file);
		        s11List.clear();
		        s11List.addAll(wrapper.getDailyS11List());
		        // Save the file path to the registry.
		        //setDGFilePath(file);

		    } catch (Exception e) { // catches ANY exception
		        Alert alert = new Alert(AlertType.ERROR);
		        alert.setTitle("Error");
		        alert.setHeaderText("Could not load data");
		        alert.setContentText("Could not load data from file:\n" + file.getPath());
		        alert.showAndWait();
		    }
		}
		*/
	    
	    
	    /**
		 * Saves the current DG data to the specified file.
		 * 
		 * @param file
		 */
	    public void saveDGDataToFile(File file) {
		    try {
		        JAXBContext context = JAXBContext
		                .newInstance(DGListWrapper.class);
		        Marshaller m = context.createMarshaller();
		        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		        // Wrapping our person data.
		        DGListWrapper wrapper = new DGListWrapper();
		        wrapper.setDailyS11List(s11List);

		        // Marshalling and saving XML to the file.
		        m.marshal(wrapper, file);

		        // Save the file path to the registry.
		        //ƒsetDGFilePath(file);
		    } catch (Exception e) { // catches ANY exception
		        Alert alert = new Alert(AlertType.ERROR);
		        alert.setTitle("Error");
		        alert.setHeaderText("Could not save data");
		        alert.setContentText("Could not save data to file:\n" + file.getPath());

		        alert.showAndWait();
		    }
		}

	  
	    /****************************************************    
	    /*************************** Save Data Branch ************/ 
	    
	    
	    
	    

	public static void main(String[] args) {
		launch(args);
	}
}
