/**
 * 
 */
package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import fileinfo.FileInfo;
import fileinfo.SplitFileInfo;
import utilities.ConfigReader;
import utilities.FileUtils;
import utilities.LoadBalancer;

/**
 * @author shikshagupta
 *
 */
public class Server {
	
	private static FileInfo fi;
	private final int sPort;
	private final ServerUtilities svrUtils;
	private ServerSocket listener;
	
	Server() {
		sPort = ConfigReader.getInstance().getServerPort();
		svrUtils = new ServerUtilities();
	}


	public static void main(String[] args) {
		Server server= new Server();
		try {
			server.getTheFileReadyToUpload();
			server.getTheServerReadyToListen();
		} catch (Exception e) {
			System.out.println("Error setting up the server.");
			e.printStackTrace();
		}
    }
	
	private void getTheServerReadyToListen() throws Exception {
		try {
			listener = new ServerSocket(sPort);
			System.out.println("The server is running."); 
			int clientNum = 1;
			while(clientNum<=ConfigReader.getInstance().getNoOfPeers()) {
				new Handler(clientNum, listener.accept()).start();
				System.out.println("Client "  + clientNum + " is connected!");
				clientNum++;
			}
		} catch (Exception e) {
			System.out.println("Error setting up listener.");
			throw new Exception();
		} finally {
			FileUtils.close(listener);
		}
	}

	private FileInfo getTheFileReadyToUpload() throws Exception {
		try {
			fi = svrUtils.setFileInfo();
			svrUtils.splitFile(fi);
		} catch (Exception e) {
			System.err.println("Error splitting up the file.");
			throw e;
		}
		return fi;
	}

	/**
 	* A handler thread class.  Handlers are spawned from the listening
 	* loop and are responsible for dealing with a single client's requests.
 	*/
	private static class Handler extends Thread {
    	private Socket connection;
    	BufferedOutputStream bos;
    	DataOutputStream dos;

    	private int no;		//The index number of the client

    	public Handler(int no, Socket connection) {
    		this.no = no;
    		this.connection = connection;
    	}

    	public void run() {
			try {
				bos = new BufferedOutputStream(connection.getOutputStream());
				dos = new DataOutputStream(bos);
				sendFiles();
			} catch(IOException ioException){
				System.err.println("Disconnected with Client " + no);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				FileUtils.close(dos);
				FileUtils.close(bos);
				FileUtils.close(connection);
			}
    	}

		private void sendFiles() throws IOException, Exception {
			List<Integer> partFileList = LoadBalancer.decideServerToPeerTraffic(no, fi);
		    String name = fi.getFileName();
		    dos.writeUTF(name);
		    System.out.println("Sending name="+name+" to client"+no);
			dos.writeInt(fi.getNoOfChunks());
		    System.out.println("Sending the number of total of chunks="+fi.getNoOfChunks()+" to client"+no);
	    	dos.writeInt(partFileList.size());
		    System.out.println("Sending "+partFileList.size()+" chunks to client"+no);
			for (int partId : partFileList) {
				SplitFileInfo s = new SplitFileInfo(fi, partId,
						ConfigReader.getInstance().getServerFilePath());
				File partFile = s.getFile();
				sendFile(partFile, partId, name);
			}
			System.out.println("Sent total " + partFileList.size() + " files to client" + no);
		}

		private void sendFile(File file, int partId, String fullName) throws IOException {
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			try {
		    	long length = file.length();
			    dos.writeLong(length);
			    System.out.println("Sending file length="+length/1000+" to client" + no);
			    dos.writeInt(partId);
			    System.out.println("Sending part id="+partId+" to client" + no);
			    fis = new FileInputStream(file);
			    bis = new BufferedInputStream(fis);
			    int theByte = 0;
			    while((theByte = bis.read()) != -1) 
			    	bos.write(theByte);
			    System.out.println("Sent part " + partId + " of file " + fullName + " to client "+no);
		    } catch (Exception e) {
		    	e.printStackTrace();
		    	throw e;
		    }
		    finally {
		    	FileUtils.close(bis);
		    	FileUtils.close(fis);
		    }
		}
	}	
}
