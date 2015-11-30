/**
 * 
 */
package fileinfo;

import java.io.File;

/**
 * @author shikshagupta
 * 
 * The class is like a bean class that contains the following 
 * information about the file : filename, filepath, file, 
 * size of chunks in kilo bytes, number of chunks.
 *
 */
public class FileInfo {
	private String fileName;
	private String filePath;
	private File file;
	private int sizeOfChunksInKB;
	private int noOfChunks;

	public FileInfo(String fileName, String filePath) {
		setFileName(fileName);
		setFilePath(filePath);
		setFile();
		setSizeOfChunksInKB(getFileLength());
		setNoOfChunks(1);
	}
	
	public FileInfo(String fileName, String filePath, int chunkSizeInKBs, int noOfChunks) {
		setFileName(fileName);
		setFilePath(filePath);
		setFile();
		setSizeOfChunksInKB(chunkSizeInKBs);
		setNoOfChunks(noOfChunks);
	}
	
	public String getFileName() {
		return fileName;
	}
	private void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}
	private void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public File getFile() {
		return file;
	}
	private void setFile() {
		String strFile = getFilePath() + "/" + getFileName();
		file = new File(strFile);
	}
		
	public int getFileLength() {
		return (int) file.length();
	}
	
	public int getSizeOfChunksInKB() {
		return sizeOfChunksInKB;
	}
	public void setSizeOfChunksInKB(int sizeOfChunksInKB) {
		this.sizeOfChunksInKB = sizeOfChunksInKB;
	}

	public int getNoOfChunks() {
		return noOfChunks;
	}
	public void setNoOfChunks(int noOfChunks) {
		this.noOfChunks = noOfChunks;
	}

}
