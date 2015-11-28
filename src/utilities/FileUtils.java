/**
 * 
 */
package utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author shikshagupta
 *
 */
public class FileUtils {
	public static void close(OutputStream os) {
		try {
			os.close();
		} catch (IOException io) {
			System.out.println("Error closing output stream");
		}
	}
	public static void close(InputStream is) {
		try {
			is.close();
		} catch (IOException io) {
			System.out.println("Error closing input stream");
		}
	}
	public static void close(Socket s) {
		try {
			s.close();
		} catch (IOException io) {
			System.out.println("Error closing socket connection");
		}
	}
	public static void close(ServerSocket s) {
		try {
			s.close();
		} catch (IOException io) {
			System.out.println("Error closing socket connection");
		}
	}
}
