/**
 * 
 */
package utilities;

import java.util.ArrayList;
import java.util.List;

import fileinfo.FileInfo;

/**
 * @author shikshagupta
 *
 */
public class LoadBalancer {

	private static int noOfPeers = ConfigReader.getInstance().getNoOfPeers();

	public static List<Integer> decideServerToPeerTraffic(int peerId, FileInfo fi) {
		List<Integer> chunkIds = new ArrayList<Integer>();
		int i;
		for(i=0; i < fi.getNoOfChunks()/noOfPeers; i++)
			chunkIds.add(i*noOfPeers+peerId-1);
		if (i*noOfPeers+peerId-1 < fi.getNoOfChunks())
			chunkIds.add(i*noOfPeers+peerId-1);
		return chunkIds;
	}
	
	public static List<Integer> decidePeerToPeerTraffic() {
		return null;
	}
	
	public static int getUploadNeighbourForPeer(int peerId) {
		return (peerId+1)%noOfPeers;
	}
	public static int getDownloadNeighbourForPeer(int peerId) {
		if (peerId-1 != 0)
			return peerId-1;
		return noOfPeers;		
	}
}
