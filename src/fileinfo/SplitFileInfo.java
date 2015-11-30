/**
 * 
 */
package fileinfo;

import utilities.ConfigReader;

/**
 * @author shikshagupta
 * 
 * SplitfileInfo {@code extends} {@link FileInfo} class. It 
 * adds following information to the file info class related  
 * to the split file chunks - chunk id, parsed file name of the 
 * split file to identify each unique chunk of the original file.
 *
 */
public class SplitFileInfo extends FileInfo{
	
	private int partId;

	public SplitFileInfo(FileInfo fi, int partId, String filePath) {
		super(parsePartFileName(fi.getFileName(), partId),
				fi.getFilePath());
		this.partId = partId;
	}
	public SplitFileInfo(String fullFileName, String filePath, int partId) {
		super(parsePartFileName(fullFileName, partId), 
				filePath);
		this.partId = partId;
	}

	public int getPartId() {
		return partId;
	}
	
	public static String parsePartFileName(String fullname, int id) {
		return (fullname + ".part" + id);
	}
	
	public static String parsePeerFilePath(int clientId) {
		return ConfigReader.getInstance().
				getClientFilePath() + clientId;
	}

}
