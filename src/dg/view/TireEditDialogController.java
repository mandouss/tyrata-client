package dg.view;


import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

//import org.controlsfx.dialog.Dialogs;

import dg.model.Tire;


public class TireEditDialogController {


	@FXML
	private TextField tireIDField;
	@FXML
	private TextField tirePosField;
	@FXML
	private TextField initS11Field;
	@FXML
	private TextField pressureField; 
	@FXML 
	private DatePicker installDatePicker;

	@FXML
	private Text warningText;

	private Stage dialogStage;
	private Tire tire;
	private boolean saveClicked = false;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		warningText.setText("");
	}

	/**
	 * Sets the stage of this dialog.
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/**
	 * Sets the tire to be edited in the dialog. 
	 * Don't pass null to it.
	 * 
	 * @param tire
	 */
	public void setTire(Tire tire) {
		this.tire = tire;

		tireIDField.setText(tire.getTireID());
		tirePosField.setText(tire.getTirePos());
		initS11Field.setText(Double.toString(tire.getInitS11()));
		initS11Field.setPromptText("-2.5 ~ -1 (dB)");
		//initS11Field.setFocusTraversable(false);
		pressureField.setText(Double.toString(tire.getPressure()));
		installDatePicker.setValue(tire.getStartDate());
	}

	/**
	 * Check if the user clicked Save.
	 * @return isSaveClicked
	 * 
	 */
	public boolean isSaveClicked() {
		return saveClicked;
	}

	/**
	 * Called when the user clicks save.
	 */
	@FXML
	private void handleSave() {
		if (isInputValid()) {
			tire.setTireID(tireIDField.getText());
			tire.setTirePos(tirePosField.getText());
			tire.setInitS11(Double.parseDouble(initS11Field.getText()));
			tire.setStartDate(installDatePicker.getValue());
			tire.setPressure(Double.parseDouble(pressureField.getText()));

			saveClicked = true;
			dialogStage.close();
		}
	}

	/**
	 * Called when the user clicks cancel.
	 */
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	
	/**
	 * Called when the user click on keyboard
	 * Only deals with ENTER for save 
	 */
	@FXML
	private void keyReleased(KeyEvent keyEvent) {
		//System.out.println("Release Detected " + keyEvent.getCode());
		if (keyEvent.getCode() == KeyCode.ENTER) {
			handleSave();
		}
	}

	/**
	 * Validates the user input in the text fields.
	 * 
	 * @return true if the input is valid
	 */
	private boolean isInputValid() {
		String errorMessage = "";

		if (tireIDField.getText() == null || tireIDField.getText().length() == 0) {
			errorMessage += "Lack tire ID!\n"; 
		}
		if (tirePosField.getText() == null || tirePosField.getText().length() == 0) {
			errorMessage += "Lack tire Position!\n"; 
		} else {
			//TODO: Handle duplicate tire position
		}

		if (initS11Field.getText() == null || initS11Field.getText().length() == 0) {
			errorMessage += "Lack S11!\n"; 
		} else {
			try {
				Double s11 = Double.parseDouble(initS11Field.getText());
				if( s11 < -2.5  || s11 > -1 ) {
					errorMessage += "Invalid S11_i (-2.5, -1)\n";
				}
			} catch (NumberFormatException e) {
				errorMessage += "Invalid S11_i (-2.5, -1)\n"; 
			}
		}

		if (pressureField.getText() == null || pressureField.getText().length() == 0) {
			errorMessage += "Lack Pressure!\n"; 
		} else {
			// try to parse the time interval into a double.
			try {
				Double.parseDouble(pressureField.getText());
			} catch (NumberFormatException e) {
				errorMessage += "Invalid tire pressure!\n"; 
			}
		}
		
		
		if (errorMessage.length() == 0) {
			return true;
		} else {
			errorMessage = errorMessage.substring(0, errorMessage.length() - 1);
			warningText.setText(errorMessage);
			return false;
		}
	}
}