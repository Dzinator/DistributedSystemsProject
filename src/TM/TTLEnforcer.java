package TM;

import java.util.Map.Entry;

//import middleRM.ws.TTLEnforcerSlave;

//thread to handle the TTL mechanism for transactions
class TTLEnforcer implements Runnable
{
	TransactionManager tm;
	public TTLEnforcer(TransactionManager txnm)
	{
		tm = txnm;
	}
	
	@Override
	public void run() {
	
	//infinite loop for checking if time-stamps of transactions > TTL
	 while(true)
	 {
		 try
		{
			 //iterate over all transactions
			for(Entry<Integer, Transaction> e : TransactionManager.trxns.entrySet())
			{
				//if transaction's last time-stamp is greater then the TTL threshold, we abort the transaction
				if ( e.getValue().getTimestamp() < System.currentTimeMillis())
				{
					//dispatch new thread slave to handle the abort calls
					new TTLEnforcerSlave(tm, e.getValue()).run();
				}
			}
			
			//sleep for a time inversely proportional to the number of currently opened transactions
			Thread.sleep(Math.max(TransactionManager.TTL - 2 * TransactionManager.trxns.size(), 0)); 
		} 
		//if sleep interrupted, just restart the method
		catch(Exception e)
		{
			run();
		}
	 }
	}
}
