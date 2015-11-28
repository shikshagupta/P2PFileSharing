/**
 * 
 */
package client;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.TreeSet;

import utilities.ConfigReader;
import utilities.FileUtils;

/**
 * @author shikshagupta
 *
 */
public class Client {

 	private final int clientId;
	private final ClientUtilities clUtil;
	private final Socket socketToServer;
	private final int listenerPort;
	private int downloadNeighbourPort;
	private String downloadNeighbourIP;
	private BufferedInputStream bis;
	private DataInputStream dis;
	private Set<Integer> chunkIds;
	private int totalSegments;
	private String fileName;
	
	public Client(int clientId, int listenerPort, int downloadNeighbourPort, 
			String downloadNeighbourIP) throws Exception {
		this.clientId = clientId;
		this.listenerPort = listenerPort;
		this.downloadNeighbourPort = downloadNeighbourPort;
		this.downloadNeighbourIP = downloadNeighbourIP;
		socketToServer = new Socket(ConfigReader.getInstance().getServerName(),
				ConfigReader.getInstance().getServerPort());		
		System.out.println("Client"+clientId+" connected to server.");	
		clUtil = new ClientUtilities(clientId);
		chunkIds = new TreeSet<Integer>();
	}


	public void run() {
		try {
			getTheFilesFromServer();
			getTheClientReadyToListenToUploadNeighbour();
			connectToAndGetTheFilesFromDownloadNeighbour();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void getTheFilesFromServer() throws Exception {
		try {
			bis = new BufferedInputStream(socketToServer.getInputStream());
			dis = new DataInputStream(bis);
		    fileName = dis.readUTF();			
		    System.out.println("File Name is " + fileName);
		    totalSegments = dis.readInt();
			System.out.println("Total segments = " + totalSegments);
			int filesCount = dis.readInt();
			clUtil.readFilesFromSender1(filesCount, fileName, chunkIds, dis, bis, "server");
		} catch(Exception e) {
			System.err.println("Error during setting up Client"+clientId);
			e.printStackTrace();
			throw e;
		} finally{
			FileUtils.close(dis);
			FileUtils.close(socketToServer);
		}
	}

	private void getTheClientReadyToListenToUploadNeighbour() throws Exception {
		ServerSocket listener = null;
		try {
			listener = new ServerSocket(listenerPort);
			new UploadHandler(listener).start();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error setting up listener");
			throw e;
		}
	}
	
	private void connectToAndGetTheFilesFromDownloadNeighbour() throws Exception {
		boolean scanning = true;
		Socket socketToDownloadNeighbour;
		while (scanning) {
			try {
				socketToDownloadNeighbour = new Socket(downloadNeighbourIP, downloadNeighbourPort);
				scanning = false;
				new DownloadHandler(socketToDownloadNeighbour).start();
			} catch (Exception e) {
				System.out.println("Peer"+clientId+" not able to connect to download neighbour"
						+clUtil.getDownloadNeighbourId()+". Waiting and trying again.");
				Thread.sleep(5000);
			}
		}
		System.out.println("Client"+clientId+
				" connected to download neighbour id "+clUtil.getDownloadNeighbourId()); 
	}

	


	/**
 	* A handler thread class.  Handlers are spawned from the listening
 	* loop and are responsible for dealing with a single client's requests.
 	*/
	private class UploadHandler extends Thread {
		private ServerSocket listener;
		private ObjectInputStream in;
		private ObjectOutputStream out;
		private Socket socketToUploadNeighbour;
		private int uploadNeighbourId;

		public UploadHandler(ServerSocket listener) {
	    	this.listener = listener;
			this.uploadNeighbourId = clUtil.getUploadNeighbourId();
		}

		public void run() {
			try {
				System.out.println("The client"+clientId+" is listening for upload neighbour."); 
				socketToUploadNeighbour = listener.accept();
				System.out.println("Client"+clientId+
						" connected to upload neighbour "+uploadNeighbourId);
				out = new ObjectOutputStream(socketToUploadNeighbour.getOutputStream());
				out.flush();
				in = new ObjectInputStream(socketToUploadNeighbour.getInputStream());
				while(true) {
					String commaSeparatedChunkIds = (String)in.readObject();
					System.out.println("Comma separated chunkIds that upload neighbour has = "+commaSeparatedChunkIds); 
					Set<Integer> chunksToBeSent = clUtil.findAssymetricSetDifference(chunkIds, commaSeparatedChunkIds);
					out.flush();
					clUtil.sendFilesToPeer(chunksToBeSent, fileName, out);
				}
			} catch(Exception e){
				System.out.println("Disconnect with upload neighbour " + uploadNeighbourId);
			} finally {
				FileUtils.close(in);
				FileUtils.close(out);
				FileUtils.close(socketToUploadNeighbour);
			}
		}
		
		
	}	

	/**
 	* A handler thread class.  Handlers are spawned from the listening
 	* loop and are responsible for dealing with a single client's requests.
 	*/
	private class DownloadHandler extends Thread {
		private Socket socketToDownloadNeighbour;
    	ObjectOutputStream out;         //stream write to the socket
     	ObjectInputStream in;          //stream read from the socket

    	public DownloadHandler(Socket socketToDownloadNeighbour) {
    		this.socketToDownloadNeighbour = socketToDownloadNeighbour;
    	}
    	
     	public void run() {
    		getAllsegments();
    		mergeFiles();
    	}
     	public void getAllsegments() {
    		try {				
    			out = new ObjectOutputStream(socketToDownloadNeighbour.getOutputStream());
    			in = new ObjectInputStream(socketToDownloadNeighbour.getInputStream());
    			while(chunkIds.size()<totalSegments) {
    				out.flush();
    				System.out.println("Have chunks : "+ clUtil.convertChunkIdSetToCommaSeparatedString(chunkIds));
    				out.writeObject(clUtil.convertChunkIdSetToCommaSeparatedString(chunkIds)); 
    				System.out.println("Sent.");
    				int filesCount = (Integer) in.readObject();
    				clUtil.readFilesFromSender(filesCount, fileName, chunkIds, in , "client"+clUtil.getDownloadNeighbourId());
    				Thread.sleep(2000);
    			}

    		} catch(Exception e){
    			System.err.println("Disconnect with Client " + clUtil.getDownloadNeighbourId()); e.printStackTrace();
    		} finally {
    			FileUtils.close(in);
    			FileUtils.close(out);
    			FileUtils.close(socketToDownloadNeighbour);
    		}
    	}
    	
     	private void mergeFiles() {
    		clUtil.mergeFiles(fileName, totalSegments);
    	}
    	
	}	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + clientId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Client other = (Client) obj;
		if (clientId != other.clientId)
			return false;
		return true;
	}

}
