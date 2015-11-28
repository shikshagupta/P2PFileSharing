/**
 * 
 */
package client.peer3;


import client.Client;
import utilities.ConfigReader;

/**
 * @author shikshagupta
 *
 */
public class Peer3 extends Client {
	
	public final static int PEER_ID = 3;

	public Peer3 () throws Exception {
		super(PEER_ID,
				ConfigReader.getInstance().getPeer3Port(),
				ConfigReader.getInstance().getPeer2Port(),
				ConfigReader.getInstance().getPeer2ServerName());
	}

	public static void main(String[] args) throws Exception {
		try {
			Client client= new Peer3();
			System.out.println("Starting up Peer3.");
			client.run();
		} catch (Exception e) {
			System.err.println("Error setting up the Peer3.");
			e.printStackTrace();
		}
    }
}
