package dg.bluetooth;

import javafx.concurrent.Service;
import javafx.fxml.FXML;
import javafx.scene.text.TextFlow;

public class BlueToothService extends Service<Void>{
	private String filePath;
	@FXML private TextFlow commsFlow;
	public void set_file_path(String to_set) {
		filePath = to_set;
	}
	public void set_comms_Flow(TextFlow to_set) {
		commsFlow = to_set;
	}
	@Override
	protected BlueToothServer createTask() {
		// TODO Auto-generated method stub		
		return new BlueToothServer(filePath,commsFlow);
	}
}