package video;

/**
 *
 * @author Tamim
 */
public enum SplitterFactory
{
    INSTANCE;
    
    public ISplitter getSplitter(int i)
    {
        switch(i)
        {
            case 0:
                return new TimeBasedSplitter();
            case 1:
                return new GopBasedSplitter();
             default:
                 return new ClosedGopBasedSplitter();
        }
    }
}
