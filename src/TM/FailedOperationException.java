package TM;

public class FailedOperationException extends Exception
{
	private int xid = 0;
    
    public FailedOperationException (int xid, String cmd)
    {
        super("The transaction " + xid + " has failed after cmd :" + cmd);
        this.xid = xid;
    }
    
    int GetXId()
    {
        return xid;
    }
}
