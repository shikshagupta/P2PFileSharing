/**
 * 
 */
package client.peer2;

import client.Client;
import utilities.ConfigReader;

/**
 * @author shikshagupta
 * 
 * See {@link client.peer1.Peer1}
 *
 */
public class Peer2 extends Client{
	
	public final static int PEER_ID = 2;

	public Peer2 () throws Exception {
		super(PEER_ID,
				ConfigReader.getInstance().getPeer2Port(),
				ConfigReader.getInstance().getPeer1Port(),
				ConfigReader.getInstance().getPeer1ServerName());
	}

	public static void main(String[] args) throws Exception {
		try {
			Client client= new Peer2();
			System.out.println("Starting up Peer2.");
			client.run();
		} catch (Exception e) {
			System.err.println("Error setting up the Peer2.");
			e.printStackTrace();
		}
    }
}
