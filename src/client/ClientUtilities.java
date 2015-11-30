/**
 * 
 */
package client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import fileinfo.FileInfo;
import fileinfo.SplitFileInfo;
import utilities.FileUtils;
import utilities.LoadBalancer;

/**
 * @author shikshagupta
 * 
 * A utilities class to help {@link client.Client}
 *
 */
public class ClientUtilities {
	
 	private final int clientId;
 	 
	ClientUtilities(int clientId) {
		this.clientId = clientId;
	}
	
	int getUploadNeighbourId() {
		return LoadBalancer.getUploadNeighbourForPeer(clientId);
	}
	int getDownloadNeighbourId() {
		return LoadBalancer.getDownloadNeighbourForPeer(clientId);
	}

	
	void readFilesFromSender(int filesCount, String fileName, Set<Integer> chunkIds, ObjectInputStream in, String sender) throws Exception {
		for(int i = 0; i < filesCount; i++) {
		    long fileLength = (Long) in.readObject(); System.out.println("FileLength="+fileLength);
		    int partId = (Integer) in.readObject(); System.out.println("partId="+partId);
		    String partFileName = SplitFileInfo.parsePartFileName(fileName, partId);
		    readFileFromSender(partFileName, fileLength, partId, chunkIds, in, sender);
		}
		System.out.println("Read " + filesCount + "files from " + sender);
	}
	private File readFileFromSender(String partFileName, long fileLength, int partId, 
			Set<Integer> chunkIds, ObjectInputStream in, String sender) throws Exception {
		File f = null;
		FileOutputStream fos = null;
		try {
			f = new File(SplitFileInfo.parsePeerFilePath(clientId) +"/"+ partFileName);
		    fos = new FileOutputStream(f);
            int val;
		    do {
		    	String temp=(String) in.readObject();
                val=Integer.parseInt(temp);
                if(val!=-1)
                    fos.write(val);                    
            } while(val!=-1);
	        System.out.println("Read file " + partFileName + " of length " + (fileLength/1000) + "kbs from " + sender);
		    chunkIds.add(partId);
		} catch (FileNotFoundException e) {
			System.err.println("Error reading files from " + sender + ". File Not Found.");
			throw e;
		} catch (IOException e) {
			System.err.println("Error reading files from " + sender + ". I/O Exception");
			throw e;
		}
		finally {
			FileUtils.close(fos);
		}
		return f;
	}

	
	
	void readFilesFromSender1(int filesCount, String fileName, Set<Integer> chunkIds, DataInputStream dis, 
			BufferedInputStream bis, String sender) throws Exception {
		for(int i = 0; i < filesCount; i++) {
		    long fileLength = dis.readLong();
		    int partId = dis.readInt();
		    String partFileName = SplitFileInfo.parsePartFileName(fileName, partId);
		    readFileFromSender1(partFileName, fileLength, partId, chunkIds, bis, sender);
		}
		System.out.println("Read " + filesCount + "files from " + sender);
	}
	private File readFileFromSender1(String partFileName, long fileLength, int partId, 
			Set<Integer> chunkIds, BufferedInputStream bis, String sender) throws Exception {
		File f = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			f = new File(SplitFileInfo.parsePeerFilePath(clientId) +"/"+ partFileName);
		    fos = new FileOutputStream(f);
		    bos = new BufferedOutputStream(fos);
		    for(int j = 0; j < fileLength; j++) 
		    	bos.write(bis.read());
		    System.out.println("Read file " + partFileName + " of length " + (fileLength/1000) + "kbs from " + sender);
		    chunkIds.add(partId);
		} catch (FileNotFoundException e) {
			System.err.println("Error reading files from " + sender + ". File Not Found.");
			throw e;
		} catch (IOException e) {
			System.err.println("Error reading files from " + sender + ". I/O Exception");
			throw e;
		}
		finally {
			FileUtils.close(bos);
			FileUtils.close(fos);
		}
		return f;
	}
	

	void sendFilesToPeer(Set<Integer> chunksToBeSent, String fileName, ObjectOutputStream out) throws Exception {
		try {
	    	out.writeObject((Integer)chunksToBeSent.size());
			System.out.println("Sending "+chunksToBeSent.size()+" chunks to client"+getUploadNeighbourId());
			for (int partId : chunksToBeSent) {
			SplitFileInfo s = new SplitFileInfo(fileName,
					SplitFileInfo.parsePeerFilePath(clientId), partId);
			sendFile(s.getFile(), partId, fileName, out);
			}
		} catch (Exception e) {
			System.err.println("Error while sending files to peer");
			throw e;
		}
		
	}

	private void sendFile(File file, int partId, String fullName, ObjectOutputStream out) throws IOException {
		FileInputStream fis = null;
		try {
	    	long length = file.length();
	    	out.writeObject((Long)length);
		    System.out.println("Sending file length="+length/1000+"kbs to client" + getUploadNeighbourId());
	    	out.writeObject((Integer)partId);
		    System.out.println("Sending part id="+partId+" to client" + getUploadNeighbourId());
		    fis = new FileInputStream(file);
			int val;
		    do {
			   val=fis.read();
			   out.writeObject(String.valueOf(val));
			}
			while(val!=-1);
			System.out.println("Sent part " + partId + " of file " + fullName + " to client "+ getUploadNeighbourId());
	    } catch (Exception e) {
			System.err.println("Peer" + clientId + " not able to send file to upload neighbour.");
	    	throw e;
	    }
	    finally {
	    	FileUtils.close(fis);
	    }
	}

	void mergeFiles(String fileName, int noOfChunks) {
		FileInfo fi = new FileInfo(fileName, SplitFileInfo.parsePeerFilePath(clientId));
		fi.setNoOfChunks(noOfChunks);
		FileMerger fm = new FileMerger(fi);
		fm.mergeFiles(clientId);
	}

	Set<Integer> findAssymetricSetDifference(Set<Integer> chunkIds, String s) {
		Set<Integer> setUploadNeighbourSet = convertCommaSeparatedStringToChunkIdSet(s);
		Set<Integer> copyOfChunkIdSet = new TreeSet<Integer>(chunkIds);
		copyOfChunkIdSet.removeAll(setUploadNeighbourSet);
		System.out.println("Chunks that are of use to upload neighbour " + copyOfChunkIdSet.toString());
		return copyOfChunkIdSet;		
	}
	private Set<Integer> convertCommaSeparatedStringToChunkIdSet(String s) {
		StringTokenizer st = new StringTokenizer(s,",");
		Set<Integer> set = new TreeSet<Integer>();
		while(st.hasMoreTokens())
			set.add(Integer.parseInt(st.nextToken()));
		return set;
	}
	String convertChunkIdSetToCommaSeparatedString(Set<Integer> chunkIds) {
		StringBuffer sb = new StringBuffer();
		for(int partId : chunkIds) {
			sb.append(","+partId);
		}
		if(sb.length()>0)
			return sb.substring(1);
		return sb.toString();
	}
}
