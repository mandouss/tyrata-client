package dg.bluetooth;
import java.io.IOException;
import java.io.InputStream;
//import java.lang.*;
//import java.io.*;
import java.util.*;
//import javax.microedition.io.*;
import javax.bluetooth.*;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

public class Client implements DiscoveryListener{
	private DiscoveryAgent agent;
	private int maxServiceSearches = 0;
	private int serviceSearchCount;
	private int transactionID[];
	private ServiceRecord record;
	private Vector<RemoteDevice> deviceList;
	
	//cosntructor
	public Client() throws BluetoothStateException{
		System.out.println("I'm here.");
		LocalDevice local = LocalDevice.getLocalDevice();
		agent = local.getDiscoveryAgent();
		
		try {
			maxServiceSearches = Integer.parseInt(LocalDevice.getProperty("bluetooth.sd.trans.max"));
			System.out.println(maxServiceSearches);
		}
		catch(NumberFormatException e) {
			System.out.println("General Application Error");
			System.out.println("\tNumberFormatException: " + e.getMessage());
		}
		
		transactionID = new int [maxServiceSearches];
		
		for (int i = 0; i < maxServiceSearches; ++i) {
			transactionID[i] = -1;
		}
		record = null;
		deviceList = new Vector<RemoteDevice>();
	}
	
	private void addToTransactionTable(int trans) {
		for (int i = 0; i < transactionID.length; ++i) {
			if(transactionID[i] == -1) {
				transactionID[i] = trans;
				return;
			}
		}
	}
	private void removeFromTransactionTable(int trans) {
		for (int i = 0; i < transactionID.length; ++i) {
			if (transactionID[i] == trans) {
				transactionID[i] = -1;
				return;
			}
		}
	}
	
	private boolean searchServices(RemoteDevice[] devList) {
		UUID[] searchList = new UUID[2];
		searchList [0] = new UUID(0x0003); //we may need to change this for a client
		//100 means this is L2CAP
		searchList [1] = new UUID(80087355); //this uuid should match with the server side
		for (int i = 0; i < devList.length; ++i) {
			if (record != null) {
				return true;
			}
			try {
				//why this doesn't need to be synchronized ???
				int trans = agent.searchServices(null, searchList, devList[i],this);
				addToTransactionTable(trans);
				
			}
			catch(BluetoothStateException e) {
				
			}
			synchronized(this) {
				serviceSearchCount++;
				if(serviceSearchCount == maxServiceSearches) {
					try {
						this.wait();
					}
					catch(Exception e) {
					}
				}
			}
		}
		while (serviceSearchCount > 0) {
			synchronized (this) {
				try {
					this.wait();
				}
				catch (Exception e) {
				}
			}
		}
		if (record != null) {
			return true;
		}
		else {
			return false;
		}
	}
	public ServiceRecord findPrinter() {
		RemoteDevice[] devList = agent.retrieveDevices(DiscoveryAgent.CACHED);
		if (devList != null) {
			if (searchServices(devList)) {
				return record;
			}
		}
		devList = agent.retrieveDevices(DiscoveryAgent.PREKNOWN);
		if (devList != null) {
			if (searchServices(devList)) {
				return record;
			}
		}
		try {
			System.out.println("Start Inquiry!");
			agent.startInquiry(DiscoveryAgent.GIAC, this);
			synchronized(this) {
				try {
					this.wait();
				}
				catch (Exception e) {}
			}
		}
		catch(BluetoothStateException e) {
			System.out.println("Unable to find devices to search");
		}
		if (deviceList.size() > 0) {
			devList = new RemoteDevice[deviceList.size()];
			deviceList.copyInto(devList);
			if(searchServices(devList)) {
				return record;
			}
		}
		return null;
	}
	public static void main(String[] args) {
		Client client = null;
		
		if((args == null) || (args.length != 1)) {
			System.out.println("usage: java PrintClient message");
			return;
		}
		try {
			client = new Client();
		}
		catch(BluetoothStateException e) {
			System.out.println("Failed to start Bluetooth System");
			System.out.println("\tBluetoothStateException: " + e.getMessage());
			System.exit(0);
		}
		ServiceRecord communService = client.findPrinter();
		
		if(communService != null) {
			String conURL = communService.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
			int index = conURL.indexOf(':');
			String protocol = conURL.substring(0,index);
			if(protocol.equals("btspp")) {
				//Create an RFCOMM connection
				try {
					StreamConnection con = (StreamConnection)Connector.open(conURL);
					InputStream os = con.openDataInputStream();
					byte[] byteStream = new byte[1024];
					int length = os.read(byteStream);
					String inStr = new String(byteStream,0,length);
					System.out.println(inStr);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			else if(protocol.equals("bt12cap")) {
				//Create another L2CAP connection
			}
			else {
				System.out.println("Unsupported Protocol");
			}
		}
		else {
			System.out.println("No Printer was found");
		}
	}
	public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
		try {
			System.out.println("#Found Device!" + btDevice.getFriendlyName(false));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		deviceList.addElement(btDevice);
	}
	public void serviceSearchCompleted(int transID, int respCode) {
		/*
		* Removes the transaction ID from the transaction table.
		*/
		removeFromTransactionTable(transID);
		serviceSearchCount--;
		synchronized (this) {
			this.notifyAll();
		}
	}	
	public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
		/*
		* If this is the first record found, then store this record
		* and cancel the remaining searches.
		*/
		System.out.println("Service discovered!");
		if (record == null) {
			record = servRecord[0];
			int [] temp = record.getAttributeIDs();
			for (int i = 0; i < temp.length; ++i) {
				System.out.println(record.getAttributeValue(temp[i]));
			}
			//System.out.println(record.serviceName);
		/*
		* Cancel all the service searches that are presently
		* being performed.
		*/
			for (int i = 0; i < transactionID.length; i++) {
				if (transactionID[i] != -1) {
					agent.cancelServiceSearch(transactionID[i]);
				}
			}
		}
	}
	public void inquiryCompleted(int discType) {
		synchronized (this) {
			try {
				this.notifyAll();
			} catch (Exception e) {}
		}	
	}
}

	
	
	
	