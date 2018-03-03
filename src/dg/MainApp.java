package dg;
import dg.model.Tire;
import dg.view.TireEditDialogController;
import dg.view.TireOverviewController;

import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
    private ObservableList<Tire> tireData = FXCollections.observableArrayList();

    public MainApp() {
    		tireData	.add(new Tire("C-1234","LF",-1));
    		tireData	.add(new Tire("C-1302","RF",-2));
    		tireData	.add(new Tire("C-4124","LR",-1.2));
    		tireData	.add(new Tire("C-9175","RR",-2.14));
    }

    /**
     * Return the data as an observable list of Tires. 
     * @return
     */
    public ObservableList<Tire> getTireData() {
        return tireData;
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
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    
    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

	public static void main(String[] args) {
		launch(args);
	}
}
