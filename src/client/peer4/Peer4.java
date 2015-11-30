/**
 * 
 */
package client.peer4;

import client.Client;
import utilities.ConfigReader;

/**
 * @author shikshagupta
 * 
 * See {@link client.peer1.Peer1}
 *
 */ 
class Peer4 extends Client {
	
	public final static int PEER_ID = 4;

	public Peer4 () throws Exception {
		super(PEER_ID,
				ConfigReader.getInstance().getPeer4Port(),
				ConfigReader.getInstance().getPeer3Port(),
				ConfigReader.getInstance().getPeer3ServerName());
	}

	public static void main(String[] args) throws Exception {
		try {
			Client client= new Peer4();
			System.out.println("Starting up Peer4.");
			client.run();
		} catch (Exception e) {
			System.err.println("Error setting up the Peer4.");
			e.printStackTrace();
		}
    }
}
