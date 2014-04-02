package video;

import java.io.File;

/**
 *
 * @author Tamim
 */
public interface ISplitter
{
    public int splitFiles(File videoFile, int maximumSegmentInterval) throws Exception;
}
