/**
 * 
 */
package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import fileinfo.FileInfo;
import fileinfo.SplitFileInfo;
import utilities.FileUtils;

/**
 * @author shikshagupta
 *
 */
public class FileSplitter {

	private FileInputStream inputStream;
    private FileOutputStream filePart;
    private FileInfo fi;
    
    FileSplitter(FileInfo fi) {
    	this.fi = fi;
    }
	public Map<Integer,String> splitFile() throws Exception {
		File file = fi.getFile();
		String fname = fi.getFileName();
        int fileSize = fi.getFileLength();
        int readLength = fi.getSizeOfChunksInKB()*1000;
        byte[] byteChunkPart;
        Map<Integer,String> mapOfPartIdToFileName= new HashMap<Integer,String>();
        try {
        	inputStream = new FileInputStream(file);
            int nChunks = 0;
            int read = 0; 
        	while (fileSize > 0) {
        		if (fileSize <= readLength)
        			readLength = fileSize;
        		byteChunkPart = new byte[readLength];
        		read = inputStream.read(byteChunkPart, 0, readLength);
        		fileSize -= read;
        		String partFileName = SplitFileInfo.parsePartFileName(fname, nChunks);
        		writeToFile(partFileName, byteChunkPart);
        		mapOfPartIdToFileName.put(nChunks, partFileName);
        		nChunks++;
		    }
        	fi.setNoOfChunks(nChunks);
        	System.out.println("Split the file into " + nChunks + " parts.");
        } catch (Exception e) {
        	e.printStackTrace();
        	throw e;
        } finally {
            FileUtils.close(inputStream);
        }
        return mapOfPartIdToFileName;
	}
	private void writeToFile(String partFileName, byte[] byteChunkPart) throws Exception {
		try {
			String file = fi.getFilePath()+"/"+partFileName;
			filePart = new FileOutputStream(new File(file));
			filePart.write(byteChunkPart);
			System.out.println("Written to file " + file + " stored at " + fi.getFilePath());
		} catch (Exception e) {
			System.out.println("Error writing to part file");
			throw e;
		} finally {
			FileUtils.close(filePart);
		}	
	}
}
