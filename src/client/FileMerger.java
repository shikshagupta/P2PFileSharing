/**
 * 
 */
package client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import fileinfo.FileInfo;
import fileinfo.SplitFileInfo;
import utilities.FileUtils;

/**
 * @author shikshagupta
 * 
 * Merges the file chunks when all chunks have been received to
 * form a copy of the original file.
 *
 */
public class FileMerger {
	
	private FileInputStream fis;
    private FileOutputStream fos;
    private FileInfo fi;

    public FileMerger(FileInfo fi) {
    	this.fi = fi;
    }
    
    public void mergeFiles(int clientId) {
    	List<File> files= getFileParts(clientId);
    	byte[] fileBytes;
    	try {
    		fos = new FileOutputStream(SplitFileInfo.
    				parsePeerFilePath(clientId)+"/"+fi.getFileName(), true);
    		for (File file: files) {
    			fis = new FileInputStream(file);
    			fileBytes = new byte[(int) file.length()];
    			fis.read(fileBytes, 0, (int) file.length());
    			fos.write(fileBytes);
    		}
    		System.out.println("Files merged to form " + fi.getFileName() 
    			+ " stored at " + SplitFileInfo.parsePeerFilePath(clientId));
    	} catch (Exception e) {
    		System.err.println("Error merging files.");
    		e.printStackTrace();
    	} finally {
    		FileUtils.close(fis);
    		FileUtils.close(fos);
    	}
	}
	
	private List<File> getFileParts(int clientId) {
		List<File> files= new ArrayList<File>();
		for (int i=0; i<fi.getNoOfChunks(); i++) {
			SplitFileInfo s = new SplitFileInfo(fi, i, 
					SplitFileInfo.parsePeerFilePath(clientId));
			files.add(s.getFile());
		}
		System.out.println("All split files ready for merging.");
		return files;
	}
}	
