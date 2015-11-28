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
 */
public class ServerUtilities {
		
	FileInfo setFileInfo() throws Exception{
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the file name");
		String fname="";
		//while(sc.hasNext()) {
			//fname = sc.nextLine();
			fname = "SG.pdf";
		//}
		sc.close();
		FileInfo fi = new FileInfo(fname, ConfigReader.getInstance().getServerFilePath());
		return fi;
	}
		
	void splitFile(FileInfo fi) throws Exception {
		fi.setSizeOfChunksInKB(ConfigReader.getInstance().getSizeOfChunksInKB());
		FileSplitter fs = new FileSplitter(fi);
		fs.splitFile();		
	}
	
}
