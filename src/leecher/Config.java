package leecher;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.LogManager;

/**
 *
 * @author Tamim
 */
public class Config
{
    public static int CHUNK_SIZE = 32;
    public static int MAXIMUM_NUMBER_OF_SLAVE;
    public static int LOCAL_PORT;
    public static String localDir;
    public static String fileName;
    public static String fileDirectory;
    public static String infoHash;
    public static String trackerIP;
    public static int SEEDER_PORT = 32;
    public static String SEEDER_IP;
    public static String FILE_NAME;
    public static String FILE_EXTENSION;
    public static int NUM_OF_FILES;
    public static boolean IS_SEEDER;
    
    static
    {
        Properties prop = new Properties();
        try
        {
            prop.load(new FileInputStream("app.config"));
            CHUNK_SIZE = Integer.parseInt(prop.getProperty("chunk_size"));
            MAXIMUM_NUMBER_OF_SLAVE = Integer.parseInt(prop.getProperty("maximum_slave_number"));
            LOCAL_PORT = Integer.parseInt(prop.getProperty("local_port"));
            localDir = prop.getProperty("local_dir");
            fileName = prop.getProperty("filename");
            infoHash = prop.getProperty("info_hash");
            trackerIP = prop.getProperty("tracker");
            SEEDER_IP = prop.getProperty("seeder_ip");
            SEEDER_PORT = Integer.parseInt(prop.getProperty("seeder_port"));
            FILE_EXTENSION = prop.getProperty("file_extension");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
}
