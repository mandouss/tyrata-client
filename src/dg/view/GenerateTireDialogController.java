package dg.view;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GenerateTireDialogController {
	@FXML 
	private TextField numberOfTiresField;
	@FXML
	private TextField startingNoField;
	@FXML
	private Text warningText;
	
	
	private Stage dialogStage;
	private boolean saveClicked = false;
	
	@FXML
	private void initialize() {
		warningText.setText("");
		numberOfTiresField.setPromptText("4 ~ 18");
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
	 * Return the number of tires user entered
	 */
	public int getTireNum() {
		if(isInputValid() && isSaveClicked()) {
			return Integer.parseInt(numberOfTiresField.getText());
		} else {
			return 0;
		}
	}
	
	public class Generated {
	    public final int numTire;
	    public final int startingNo;
	    public Generated(int numTire, int startingNo) {
	        this.numTire = numTire; this.startingNo = startingNo;
	    }
	}
	
	public Generated getGenerated() {
		return new Generated(Integer.parseInt(numberOfTiresField.getText()),Integer.parseInt(startingNoField.getText()));
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

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}
	
	@FXML
	private void keyReleased(KeyEvent keyEvent) {
		//System.out.println("Release Detected " + keyEvent.getCode());
		if (keyEvent.getCode() == KeyCode.ENTER) {
			handleSave();
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
				int startingNo = Integer.parseInt(startingNoField.getText());
				if(numOfTires <= 2 || numOfTires > 18 || numOfTires % 2 == 1 || startingNo < 0) {
					errorMessage += "Invalid number of tires \n(Even Num between 4 and 18)\n";
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
