/**
 * 
 */
package client.peer1;

import client.Client;
import utilities.ConfigReader;

/**
 * @author shikshagupta
 *
 */
public class Peer1 extends Client {

	public final static int PEER_ID = 1;

	public Peer1 () throws Exception {
		super(PEER_ID,
				ConfigReader.getInstance().getPeer1Port(),
				ConfigReader.getInstance().getPeer5Port(),
				ConfigReader.getInstance().getPeer5ServerName());
	}

	public static void main(String[] args) throws Exception {
		try {
			Client client= new Peer1();
			System.out.println("Starting up Peer1.");
			client.run();
		} catch (Exception e) {
			System.err.println("Error setting up the Peer1.");
			e.printStackTrace();
		}
    }
}
