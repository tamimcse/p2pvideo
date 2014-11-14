package leecher;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

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
    public static String SEEDER_HOST;
    public static String FILE_NAME;
    public static String FILE_EXTENSION;
    public static int NUM_OF_FILES;
    public static boolean IS_SEEDER;
    public static String LEECHER_IP;
    public static int LEECHER_PORT;
    public static int THREAD_POOL;
    public static boolean isProxy;
    public static boolean isRealSeeder;
    public static String proxy_server;
    public static int proxy_port;
    public static int IS_GOP_BASED_SPLITTING;
    public static String LIB_VLC_PATH;

    
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
            SEEDER_HOST = prop.getProperty("seeder_host");
            SEEDER_PORT = Integer.parseInt(prop.getProperty("seeder_port"));
            FILE_EXTENSION = prop.getProperty("file_extension");
            THREAD_POOL = Integer.parseInt(prop.getProperty("thread_pool"));
            isProxy = Boolean.valueOf(prop.getProperty("isProxy"));
            proxy_server = prop.getProperty("proxy_server");
            proxy_port = Integer.parseInt(prop.getProperty("proxy_port"));
            IS_GOP_BASED_SPLITTING = Integer.parseInt(prop.getProperty("is_GOP_Based_splitting"));
            isRealSeeder = Boolean.valueOf(prop.getProperty("isRealSeeder"));
            LIB_VLC_PATH = prop.getProperty("LibVLCPath");
            if(isRealSeeder)
            {
                
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
}
