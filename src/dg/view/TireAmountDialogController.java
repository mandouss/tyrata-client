package dg.view;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TireAmountDialogController {
	@FXML 
	private TextField numberOfTiresField;
	@FXML
	private Text warningText;
	
	
	private Stage dialogStage;
	private boolean saveClicked = false;
	
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

	public boolean isSaveClicked() {
		return saveClicked;
	}
	
	@FXML
	private void handleSave() {
		if (isInputValid()) {
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
	
	public int numoftires() {
		if(isInputValid() && isSaveClicked()) {
			return Integer.parseInt(numberOfTiresField.getText());
		} else {
			return 0;
		}
	}
		
	private boolean isInputValid() {
		String errorMessage = "";

		
		if (numberOfTiresField.getText() == null || numberOfTiresField.getText().length() == 0) {
			errorMessage += "Missing Number!\n"; 
		} else {
			// try to parse the time interval into an int.
			try {
				int numOfTires = Integer.parseInt(numberOfTiresField.getText());
				
				if(numOfTires <= 2 || numOfTires > 18 || numOfTires % 2 == 1) {
					errorMessage += "Invalid number of tires \n(Between 4 and 18, excluding odd numbers)\n";
				}
			} catch (NumberFormatException e) {
				errorMessage += "Invalid input!\n"; 
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
