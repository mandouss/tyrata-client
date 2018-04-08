package dg.bluetooth;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class BlueToothServer extends Task<Void> {
 private StreamConnection streamConnection;
 private LocalDevice local = null;
 private StreamConnectionNotifier notifier;
 private String FilePath;
 @FXML private TextFlow commsFlow; 
 //private byte[] acceptdByteArray;
 
 public BlueToothServer(String path, TextFlow comms) {
	 FilePath = path;
	 commsFlow = comms;
 }
 
 @Override 
 protected void failed() {
	super.failed();
	//updateMessage("Failed!");
 }
 
 public void run() { 
	 try {
		 local = LocalDevice.getLocalDevice();
		 if(!local.setDiscoverable(DiscoveryAgent.GIAC)) {
			 System.out.println("Please set your local bluetooth as discoverable");
		 }
		 
	 //btspp stands for RFCOMM connection
		 //System.out.println(new UUID("fa87c0d0afac11de8a390800200c9a66",false).toString());
		 String url = "btspp://localhost:" + new UUID("fa87c0d0afac11de8a390800200c9a66",false).toString() + ";authenticate=false;encrypt=false;name=RemoteBluetooth";
	 //open the service
		 System.out.println("I am stuck here!");
		 notifier = (StreamConnectionNotifier)Connector.open(url);
		 System.out.println("I pass here!");
		 System.out.println("Make Service visible to remote client!");
		 Platform.runLater(new Runnable() {
             @Override public void run() {
            	 String msg = "Waiting for Android Mobile to connect...\n";
         		Text t = new Text();
                t.setStyle("-fx-fill: #359E4B;-fx-font-weight:bold;");
                t.setText(msg);
                 commsFlow.getChildren().add(t);
             }
		 });
		 
  		 streamConnection = (this.notifier).acceptAndOpen(); 
  		 DataOutputStream optStream = streamConnection.openDataOutputStream();
  		 DataInputStream inStream = streamConnection.openDataInputStream();
  		 BufferedReader bufferedReader = new BufferedReader(new FileReader(FilePath));
  		 StringBuffer stringBuffer = new StringBuffer();
  		 String line = null;
  		 while(((line = bufferedReader.readLine())!=null)) {
  			 stringBuffer.append(line).append("\n");
  		 }
  		 bufferedReader.close();
  		 System.out.println("Writing file to the other device....");
  		 //optStream.write(stringBuffer.toString().getBytes());
  		 boolean has_succeeded = false;
  		 /*
  		  * The following while loop is to make sure that the data transmitted is not corrupted on its way to Android Mobile. If that is true
  		  * the server side will retransmit the data 
  		  */
  		 while (!has_succeeded) {
  			 byte[] send_byte_array = stringBuffer.toString().getBytes();
  			 int off = 0;
  			 while(off < send_byte_array.length) {
  				 if(isCancelled()) {
  					 return;
  				 }
  				 int len = 512;
  				 if (off+len > send_byte_array.length) len = send_byte_array.length - off;
  				 optStream.write(send_byte_array, off, len);
  				 off += len;
  				 TimeUnit.MILLISECONDS.sleep(50);
  			 }
  			 byte EOF = 0;
  			 optStream.write(EOF);
  			 System.out.println("Writing completed....");	 
  			 int timer = 0;
  			 while(timer < 10000) { //polling will end if the time elapses 10000 miliseconds
  				 byte [] read_byte_array = new byte [1];
  				 if(isCancelled()) {
  					 return;
  				 }
  				 if(inStream.available() > 0) {
  					 inStream.read(read_byte_array, 0, 1);
  					 String check = new String(read_byte_array);
  					 if (check.contentEquals("T")) {
  	  					has_succeeded = true; //If I received T, the client side received all the data correctly. I will close connection
  					 }
  					 break; //I will break anyway, because it will either succeed or fail
  				 }
  				 //if not available, I still don't know the result. So I sleep for several miliseconds and poll again
  				 TimeUnit.MILLISECONDS.sleep(500); 
  				 timer += 500;
  			 }
  		 }	
	 }
	 catch (IOException e ) {
		 //e.printStackTrace();
		  Platform.runLater(new Runnable(){
            @Override public void run() {
            	String msg = "Please set your local bluetooth as discoverable\n";
            	Text txt = new Text();
            	txt.setStyle("-fx-fill: #C8595C;-fx-font-weight:bold;");
            	txt.setText(msg);
            	commsFlow.getChildren().add(txt);
            }
		  });
	 }
	 catch (InterruptedException e) {
		// TODO Auto-generated catch block
		 if(isCancelled()) {
			 return;
		 }
			e.printStackTrace();
	 } 
 }
 @Override
	protected Void call() throws Exception {
	// TODO Auto-generated method stub
		run();
		return null;
	}	
}
 
