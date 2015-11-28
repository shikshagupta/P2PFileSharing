/**
 * 
 */
package utilities;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author shikshagupta
 *
 */
public class ConfigReader {
	
	private InputStream inputStream;
	private Map<String,String> propertyValueMap;
	private static final String SERVER_PORT="serverport";
	private static final String PEER1_LISTENER_PORT="peer1listenerport";
	private static final String PEER2_LISTENER_PORT="peer2listenerport";
	private static final String PEER3_LISTENER_PORT="peer3listenerport";
	private static final String PEER4_LISTENER_PORT="peer4listenerport";
	private static final String PEER5_LISTENER_PORT="peer5listenerport";
	private static final String SERVER_NAME="servername";
	private static final String PEER1_SERVER_NAME="peer1servername";
	private static final String PEER2_SERVER_NAME="peer2servername";
	private static final String PEER3_SERVER_NAME="peer3servername";
	private static final String PEER4_SERVER_NAME="peer4servername";
	private static final String PEER5_SERVER_NAME="peer5servername";
	private static final String SERVER_FILE_PATH="serverfilepath";
	private static final String CLIENT_FILE_PATH="clientfilepath";
	private static final String SIZE_OF_CHUNKS_IN_KB="sizeofchunksinKB";
	private static final String NO_OF_PEERS="noofpeers";
	private static final int DEFAULT_SERVER_PORT=8000;
	private static final int DEFAULT_PEER1_PORT=9018;
	private static final int DEFAULT_PEER2_PORT=9019;
	private static final int DEFAULT_PEER3_PORT=9104;
	private static final int DEFAULT_PEER4_PORT=9114;
	private static final int DEFAULT_PEER5_PORT=9826;
	private static final String DEFAULT_SERVER_NAME="localhost";
	private static final String DEFAULT_PEER1_SERVER_NAME="localhost";
	private static final String DEFAULT_PEER2_SERVER_NAME="localhost";
	private static final String DEFAULT_PEER3_SERVER_NAME="localhost";
	private static final String DEFAULT_PEER4_SERVER_NAME="localhost";
	private static final String DEFAULT_PEER5_SERVER_NAME="localhost";
	private static final String DEFAULT_SERVER_FILE_PATH="";	
	private static final String DEFAULT_CLIENT_FILE_PATH="";	
	private static final int DEFAULT_SIZE_OF_CHUNKS_IN_KB=100;
	private static final int DEFAULT_NO_OF_PEERS=5;
	private static ConfigReader singletonConfigReader=null;
	
	private ConfigReader() {
		loadPropValues();
	}
	
	public static ConfigReader getInstance() {
		if (singletonConfigReader==null)
			singletonConfigReader = new ConfigReader();
		return singletonConfigReader;
	}
	
	private void loadPropValues() {		 
		propertyValueMap = new HashMap<String,String>();		
		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			if (inputStream != null)
				prop.load(inputStream);
			else
				throw new FileNotFoundException(
						"property file '" + propFileName + "' not found in the classpath"); 
			propertyValueMap.put(SERVER_PORT,prop.getProperty(SERVER_PORT));
			propertyValueMap.put(SERVER_NAME, prop.getProperty(SERVER_NAME));
			propertyValueMap.put(SERVER_FILE_PATH, prop.getProperty(SERVER_FILE_PATH));
			propertyValueMap.put(CLIENT_FILE_PATH, prop.getProperty(CLIENT_FILE_PATH));
			propertyValueMap.put(SIZE_OF_CHUNKS_IN_KB, prop.getProperty(SIZE_OF_CHUNKS_IN_KB));
			propertyValueMap.put(NO_OF_PEERS, prop.getProperty(NO_OF_PEERS));
			propertyValueMap.put(PEER1_LISTENER_PORT,prop.getProperty(PEER1_LISTENER_PORT));
			propertyValueMap.put(PEER2_LISTENER_PORT,prop.getProperty(PEER2_LISTENER_PORT));
			propertyValueMap.put(PEER3_LISTENER_PORT,prop.getProperty(PEER3_LISTENER_PORT));
			propertyValueMap.put(PEER4_LISTENER_PORT,prop.getProperty(PEER4_LISTENER_PORT));
			propertyValueMap.put(PEER5_LISTENER_PORT,prop.getProperty(PEER5_LISTENER_PORT));
			propertyValueMap.put(PEER1_SERVER_NAME, prop.getProperty(PEER1_SERVER_NAME));
			propertyValueMap.put(PEER2_SERVER_NAME, prop.getProperty(PEER2_SERVER_NAME));
			propertyValueMap.put(PEER3_SERVER_NAME, prop.getProperty(PEER3_SERVER_NAME));
			propertyValueMap.put(PEER4_SERVER_NAME, prop.getProperty(PEER4_SERVER_NAME));
			propertyValueMap.put(PEER5_SERVER_NAME, prop.getProperty(PEER5_SERVER_NAME));
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				System.out.println("Error closing inputstream.");
			}
			
		}
	}
	
	public int getServerPort() {
		try {
			return Integer.parseInt(propertyValueMap.get(SERVER_PORT));
		} catch (Exception e) {
			return DEFAULT_SERVER_PORT;
		}
	}

	public String getServerName() {
		try {
			return propertyValueMap.get(SERVER_NAME);
		} catch (Exception e) {
			return DEFAULT_SERVER_NAME;
		}		
	}

	public String getServerFilePath() {
		try {
			return propertyValueMap.get(SERVER_FILE_PATH);
		} catch (Exception e) {
			return DEFAULT_SERVER_FILE_PATH;
		}		
	}
	
	public String getClientFilePath() {
		try {
			return propertyValueMap.get(CLIENT_FILE_PATH);
		} catch (Exception e) {
			return DEFAULT_CLIENT_FILE_PATH;
		}		
	}
	
	public int getSizeOfChunksInKB() {
		try {
			return Integer.parseInt(propertyValueMap.get(SIZE_OF_CHUNKS_IN_KB));
		} catch (Exception e) {
			return DEFAULT_SIZE_OF_CHUNKS_IN_KB;
		}		
	}
	
	public int getNoOfPeers() {
		try {
			return Integer.parseInt(propertyValueMap.get(NO_OF_PEERS));
		} catch (Exception e) {
			return DEFAULT_NO_OF_PEERS;
		}
	}

	public int getPeer1Port() {
		try {
			return Integer.parseInt(propertyValueMap.get(PEER1_LISTENER_PORT));
		} catch (Exception e) {
			return DEFAULT_PEER1_PORT;
		}
	}
	public String getPeer1ServerName() {
		try {
			return propertyValueMap.get(PEER2_SERVER_NAME);
		} catch (Exception e) {
			return DEFAULT_PEER1_SERVER_NAME;
		}		
	}
	public int getPeer2Port() {
		try {
			return Integer.parseInt(propertyValueMap.get(PEER2_LISTENER_PORT));
		} catch (Exception e) {
			return DEFAULT_PEER2_PORT;
		}
	}
	public String getPeer2ServerName() {
		try {
			return propertyValueMap.get(PEER2_SERVER_NAME);
		} catch (Exception e) {
			return DEFAULT_PEER2_SERVER_NAME;
		}		
	}
	public int getPeer3Port() {
		try {
			return Integer.parseInt(propertyValueMap.get(PEER3_LISTENER_PORT));
		} catch (Exception e) {
			return DEFAULT_PEER3_PORT;
		}
	}
	public String getPeer3ServerName() {
		try {
			return propertyValueMap.get(PEER3_SERVER_NAME);
		} catch (Exception e) {
			return DEFAULT_PEER3_SERVER_NAME;
		}		
	}
	public int getPeer4Port() {
		try {
			return Integer.parseInt(propertyValueMap.get(PEER4_LISTENER_PORT));
		} catch (Exception e) {
			return DEFAULT_PEER4_PORT;
		}
	}
	public String getPeer4ServerName() {
		try {
			return propertyValueMap.get(PEER4_SERVER_NAME);
		} catch (Exception e) {
			return DEFAULT_PEER4_SERVER_NAME;
		}		
	}
	public int getPeer5Port() {
		try {
			return Integer.parseInt(propertyValueMap.get(PEER5_LISTENER_PORT));
		} catch (Exception e) {
			return DEFAULT_PEER5_PORT;
		}
	}
	public String getPeer5ServerName() {
		try {
			return propertyValueMap.get(PEER5_SERVER_NAME);
		} catch (Exception e) {
			return DEFAULT_PEER5_SERVER_NAME;
		}		
	}
}
