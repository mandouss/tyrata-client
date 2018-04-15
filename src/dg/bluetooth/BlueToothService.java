package dg.bluetooth;

import java.io.IOException;

import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnectionNotifier;

import javafx.concurrent.Service;
import javafx.fxml.FXML;
import javafx.scene.text.TextFlow;

public class BlueToothService extends Service<Void>{
	private String filePath;
	private StreamConnectionNotifier notifier; 
	@FXML private TextFlow commsFlow;
	public void set_file_path(String to_set) {
		filePath = to_set;
	}
	public void set_notifier(){
		String url = "btspp://localhost:" + new UUID("fa87c0d0afac11de8a390800200c9a66",false).toString() + ";authenticate=false;encrypt=false;name=RemoteBluetooth";
		 //open the service
		System.out.println("I am stuck here!");
		try {
			notifier = (StreamConnectionNotifier)Connector.open(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("I pass here!");
	}
	public void set_comms_Flow(TextFlow to_set) {
		commsFlow = to_set;
	}
	@Override
	protected BlueToothServer createTask() {
		// TODO Auto-generated method stub		
		return new BlueToothServer(filePath,commsFlow,this.notifier);
	}
	public void stop() {
		try {
			notifier.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
}
