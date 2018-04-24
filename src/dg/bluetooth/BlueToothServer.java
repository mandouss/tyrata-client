package dg.bluetooth;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
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
 
 public BlueToothServer(String path, TextFlow comms, StreamConnectionNotifier to_set) {
	 FilePath = path;
	 commsFlow = comms;
	 notifier = to_set;
 }
 
 @Override 
 protected void failed() {
	super.failed();
	//updateMessage("Failed!");
 }
 
 public void run() { 
	 try {
			 local = LocalDevice.getLocalDevice();
			 if(local.getDiscoverable() != DiscoveryAgent.GIAC){
				 local.setDiscoverable(DiscoveryAgent.GIAC);
			 }
				 //System.out.println("Please set your local Bluetooth as discoverable");
	 //btspp stands for RFCOMM connection
		 //System.out.println(new UUID("fa87c0d0afac11de8a390800200c9a66",false).toString());
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
  		Platform.runLater(new Runnable() {
            @Override public void run() {
           	 String msg = "Connection established! Start Transmitting data...\n";
        		Text t = new Text();
               t.setStyle("-fx-fill: #359E4B;-fx-font-weight:bold;");
               t.setText(msg);
                commsFlow.getChildren().add(t);
            }
		 });
  		 
  		 
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
  		 /*
  		  * The following while loop is to make sure that the data transmitted is not corrupted on its way to Android Mobile. If that is true
  		  * the server side will retransmit the data 
  		  */
  		 System.out.println("Enter the outermost while loop");
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
  			 boolean has_succeeded = false;
  			 while(timer < 10000) { //polling will end if the time elapses 10000 milliseconds
  				 byte [] read_byte_array = new byte [1];
  				 if(isCancelled()) {
  					 return;
  				 }
  				 if(inStream.available() > 0) {
  					 inStream.read(read_byte_array, 0, 1);
  					 String check = new String(read_byte_array);
  					 System.out.println("Check receive byte: " + check);
  					 if (check.contentEquals("S")) {
  	  					 has_succeeded = true; //If I received S, the client side received all the data correctly. I will close connection
  						 Platform.runLater(new Runnable() {
  				             @Override public void run() {
  				            	 String msg = "Data Transmission succeeded!\n";
  				         		Text t = new Text();
  				                t.setStyle("-fx-fill: #359E4B;-fx-font-weight:bold;");
  				                t.setText(msg);
  				                 commsFlow.getChildren().add(t);
  				             }
  						 });
  					 }
  					 break; //I will break anyway, because it will either succeed or fail
  				 }
  				 System.out.println("Data is not available");
  				 //if not available, I still don't know the result. So I sleep for several milliseconds and poll again
  					 TimeUnit.MILLISECONDS.sleep(500);
  				     timer += 500;
  			 }
  			 if(!has_succeeded) {
  				Platform.runLater(new Runnable(){
  		            @Override public void run() {
  		            	String msg = "Data Transmission failed, please try again!\n";
  		            	Text txt = new Text();
  		            	txt.setStyle("-fx-fill: #C8595C;-fx-font-weight:bold;");
  		            	txt.setText(msg);
  		            	commsFlow.getChildren().add(txt);
  		            }
  		  });
  			 }
	 }
	 catch(InterruptedException e) {
		 Platform.runLater(new Runnable(){
	            @Override public void run() {
	            	String msg = "Fatal Error! Bluetooth connection is terminated!\n";
	            	Text txt = new Text();
	            	txt.setStyle("-fx-fill: #C8595C;-fx-font-weight:bold;");
	            	txt.setText(msg);
	            	commsFlow.getChildren().add(txt);
	            }
	  });
	 }
	 
	 catch (java.io.InterruptedIOException e) {
		 System.out.println(local.getDiscoverable() == DiscoveryAgent.GIAC);
		 /*
		 Platform.runLater(new Runnable(){
	            @Override public void run() {
	            	String msg = "Cancel scheduled Bluetooth connection\n";
	            	Text txt = new Text();
	            	txt.setStyle("-fx-fill: #C8595C;-fx-font-weight:bold;");
	            	txt.setText(msg);
	            	commsFlow.getChildren().add(txt);
	            }
	            
	 });
	 */
		 System.out.println("Exception!!!!");
	 }
	 catch (IOException e) {
		 e.printStackTrace();
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
	 catch (java.lang.NullPointerException e) {}
}
 @Override
	protected Void call() throws Exception {
	// TODO Auto-generated method stub
		run();
		return null;
	}	
}
 
