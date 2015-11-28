/**
 * 
 */
package client.peer5;

import client.Client;
import utilities.ConfigReader;

/**
 * @author shikshagupta
 *
 */
public class Peer5 extends Client{
	
	public final static int PEER_ID = 5;

	public Peer5 () throws Exception {
		super(PEER_ID,
				ConfigReader.getInstance().getPeer5Port(),
				ConfigReader.getInstance().getPeer4Port(),
				ConfigReader.getInstance().getPeer4ServerName());
	}

	public static void main(String[] args) throws Exception {
		try {
			Client client= new Peer5();
			System.out.println("Starting up Peer5.");
			client.run();
		} catch (Exception e) {
			System.err.println("Error setting up the Peer5.");
			e.printStackTrace();
		}
    }
}
