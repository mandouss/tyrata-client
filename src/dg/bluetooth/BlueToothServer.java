package dg.bluetooth;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import javafx.concurrent.Task;


public class BlueToothServer extends Task {
 private StreamConnection streamConnection;
 private LocalDevice local = null;
 private StreamConnectionNotifier notifier;
 //private byte[] acceptdByteArray;
 
 public BlueToothServer() {
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
	 }	                 
	 catch(IOException e) {
		 e.printStackTrace();
	 }
 }
 public void run() {
	 try {
		 System.out.println("Make Service visible to remote client!");
  		 streamConnection = notifier.acceptAndOpen();
  		 DataOutputStream optStream = streamConnection.openDataOutputStream();
  		 BufferedReader bufferedReader = new BufferedReader(new FileReader("F:\\test2.xml"));
  		 StringBuffer stringBuffer = new StringBuffer();
  		 String line = null;
  		 while(((line = bufferedReader.readLine())!=null)) {
  			 stringBuffer.append(line).append("\n");
  		 }
  		 //StringBuffer stringbuffer = new StringBuffer();
  		// for (int i = 0; i < 100; ++i) {
  		//	 stringbuffer.append(" Hello! This is a test for the communication between Tyrata Android Mobile and Tyrata Client");
  		 //}
  		 //String test = new String(" Hello! This is a test for the communication between Tyrata Android Mobile and Tyrata Client");
  		 //System.out.println(stringBuffer);
  		 System.out.println("Writing file to the other device....");
  		 optStream.write(stringBuffer.toString().getBytes());
  		 System.out.println("Writing completed....");
  		 bufferedReader.close();
  	}
	 catch (IOException  e) {
			e.printStackTrace();
	} 
 }
@Override
protected Object call() throws Exception {
	// TODO Auto-generated method stub
	run();
	return null;
}
 
}
