/**
 * 
 */
package server;

import java.util.Scanner;

import fileinfo.FileInfo;
import utilities.ConfigReader;

/**
 * @author shikshagupta
 * 
 * A utilities class to help {@link server.Server}
 *
 */
public class ServerUtilities {
		
	FileInfo setFileInfo() throws Exception{
		System.out.println("Enter the file name:");
		Scanner in = new Scanner(System.in);
		String fname="";
		try { 
			fname = in.nextLine(); 
		} catch (Exception e) {
			fname = ConfigReader.getInstance().getFileName(); e.printStackTrace();
		}
		fname = ConfigReader.getInstance().getFileName();
		in.close();
		FileInfo fi = new FileInfo(fname, ConfigReader.getInstance().getServerFilePath());
		return fi;
	}
		
	void splitFile(FileInfo fi) throws Exception {
		fi.setSizeOfChunksInKB(ConfigReader.getInstance().getSizeOfChunksInKB());
		FileSplitter fs = new FileSplitter(fi);
		fs.splitFile();		
	}
	
}
