package dg.view;


import javafx.fxml.FXML;
//import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.ArrayList;

import dg.model.DailyS11;
import dg.util.DailyS11ToStringUtil;


public class GeneratedDataViewController {

	@FXML
	private TextArea dataArea;

	private Stage dialogStage;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
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
	 * 
	 * @param tire
	 */
	public void setDataContent(ArrayList<DailyS11> s11List) {

		String resultString = DailyS11ToStringUtil.dailyS11ToString(s11List);

		dataArea.setText(resultString);
		dataArea.setWrapText(true);
		dataArea.setEditable(false);
	}


	@FXML
	private void handleClose() {
		dialogStage.close();
	}
}