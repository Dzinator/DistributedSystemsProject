package TM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Map.Entry;
import java.util.Vector;

import middleRM.ws.*;
import middleRM.ws.Main.Server;
import LockManager.*;

/*
 * questions: customers, should we add it to middleware before actually sending it to the servers? If so, that is what is implemented
 * follow-up question, how to make sure 2 transactions can't delete/create both customers locally before committing? put field in customer allowed transaction? 
 * */
//TODO: implement all query methods after yousuf has responded

/*singleton object that handles transactions of the middle-ware*/
public class TransactionManager implements server.ws.ResourceManager
{
	//unique string identifiers for strdata in locktable adressing
	private static final String CUSTOMER = "cu";
	private static final String HOTEL = "h";
	private static final String FLIGHT = "f";
	private static final String CAR = "c";
	
	//reference to singleton TransactionManager, to lock manager and to Main middle-ware server
	private static TransactionManager tm;
	private static LockManager lm = new LockManager();
	private static Main middleware;
	
	//hashmap of current transactions
	static final HashMap<Integer, Transaction> trxns = new HashMap<Integer,Transaction>(1000);
	
	static final HashMap<Integer, Customer> customers = new HashMap<Integer, Customer>();
	
	//various immutable global variables
	public static final int READ = 0;
	public static final int WRITE = 1;
	public static final long TTL = 20000; //time to live : 20 seconds
	
	//returns instance of the transaction manager
	public static TransactionManager getInstance(Main main)
	{
		if(tm == null)
			tm = new TransactionManager(main);
		return tm;
	}
	
	//initiate new transaction manager. Constructor is only called once and we only get 1 enforcer
	private TransactionManager(Main m)
	{
		//setup Main reference to program
		middleware = m;
		
		//fire new thread to enforce TTL mechanism
		new Thread(new TTLEnforcer(this)).start(); //TODO: CANNOT CREATE THREAD HERE, HALTS ON deployment of middleware
		//tll.run();
	}
	
	//Start a new transaction and return its id
	@Override
	public int start()
	{
		//get new trxn id
		int randomTrnxId = Math.abs(new Random().nextInt());
		while ( trxns.containsKey(randomTrnxId))
			randomTrnxId = Math.abs(new Random().nextInt());
		
		//put in hashpmap of currently executed transactions
		trxns.put(randomTrnxId, new Transaction(randomTrnxId));
		return randomTrnxId;
	}
	
	//Start a new transaction with a specified id
	@Override
	public boolean startid(int tid) 
	{
		if (trxns.containsKey(tid))
			  return false;
		trxns.put(tid, new Transaction(tid));
		return true;
	}
	
	//Attempt to commit the given transaction; return true upon success; upon deadlock, we abort, upon a false reponse from server we abort as well
	@Override
	public boolean commit(int transactionId) 
	{//TODO: implement this method
		 //check if transaction exists
		 if (!trxns.containsKey(transactionId))
			  return false;
		 
		 //get transaction
		Transaction t = trxns.get(transactionId);
		t.refreshTimeStamp(); //refresh time stamp so that TTLenforcer won't remove transaction
		
		//synchronize t to set isAborting to true
		synchronized(t)
		{
			if ( t.isTerminating) //to prevent double aborts of a transaction
				return true;
			t.isTerminating= true;
		}
		
		//in case of a deadlock call
		try 
		{
			//iterate through all commands
			for ( String cmd : t.cmds())
				requestLock(t.tid, cmd); //get locks for each command
			
			//alert servers that transaction is beginning
			alertServersStart(t);
			
			//if we get all locks, we may start to execute the commands
			for ( String cmd : t.cmds())
			{
				//execute command
				executeCommand(t.tid, cmd);
				
				//keep command in executed operations on stack
				t.addOperationExecuted(cmd);
			}
			
			//every operation committed to every server, we unlock all locks
			lm.UnlockAll(t.tid);
			
			//alert servers transaction has committed
			alertServersCommit(t);
			
			//remove transaction from currently executing transactions set
			trxns.remove(t.tid);
			
			//return true to user, everything committed fine
			return true;
		} 
		catch (DeadlockException e) 
		{
			//we abort the transaction
			e.printStackTrace();
			abort(transactionId);
		} 
		catch (FailedOperationException e) 
		{
			//we abort the transaction due to unexpected behaviour
			e.printStackTrace();
			abort(transactionId);
		}
		
		/*for( Server s : trxns.get(transactionId).servers())
		{
			services.get(s).proxy.commit(transactionId);
		}
		lm.UnlockAll(transactionId);
		
		return true;*/
		return true;
	}
	
	private void alertServersStart(Transaction t) 
	{
		for( Server s : t.getServers())
			Main.services.get(s).proxy.start();
	}

	private void alertServersCommit(Transaction t) 
	{
		for( Server s : t.getServers())
			Main.services.get(s).proxy.commit(t.tid);
	}
	
	private void alertServersAbort(Transaction t) 
	{
		for( Server s : t.getServers())
			Main.services.get(s).proxy.abort(t.tid);
	}
	
	

	//executes the command and returns the values obtained to the user
	private void executeCommand(int id, String cmd) throws FailedOperationException
	{
		String[] args = cmd.split(",");
		
		//TODO: check here for throwing execption if returned input is invalid/ tell user results of actions
		//TODO: return customer info + others, can't retrun data, method type is void
		switch (args[0])
		{
			case "+" + FLIGHT:  Main.services.get(Server.Flight).proxy.addFlight(id, Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
								break;		
			case "-" + FLIGHT: Main.services.get(Server.Flight).proxy.deleteFlight(id, Integer.parseInt(args[1]));
					   			break;
			case "q" + FLIGHT: Main.services.get(Server.Flight).proxy.queryFlight(id, Integer.parseInt(args[1]));
					   			break; //nothing to do, only read operation
			case "p" + FLIGHT: Main.services.get(Server.Flight).proxy.queryFlightPrice(id, Integer.parseInt(args[1]));
					   			break; //nothing to do, only read operation
			case "+" + CAR: Main.services.get(Server.Car).proxy.addCars(id, args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]));  
					   		break;	
			case "-" + CAR:  Main.services.get(Server.Car).proxy.deleteCars(id, args[1]);
							break;
			case "q" + CAR: Main.services.get(Server.Car).proxy.queryCars(id, args[1]);
							break; //nothing to do, only read operation
			case "p" + CAR: Main.services.get(Server.Car).proxy.queryCarsPrice(id, args[1]);
							break; //nothing to do, only read operation
			case "+" + HOTEL:  Main.services.get(Server.Hotel).proxy.addRooms(id, args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3])); 
					   		break;
			case "-" + HOTEL:  Main.services.get(Server.Hotel).proxy.deleteRooms(id, args[1]);
	   		   		   		break;
			case "q" + HOTEL: Main.services.get(Server.Hotel).proxy.queryRooms(id,args[1]);
								break; //nothing to do, only read operation
			case "p" + HOTEL: Main.services.get(Server.Hotel).proxy.queryRoomsPrice(id,args[1]);
							 break; //nothing to do, only read operation
			case "+" + CUSTOMER:  Main.removeCustomerFromServices(id, Integer.parseInt(args[1]));
						customers.remove(Integer.parseInt(args[1]));
						break;//careful 2 cases here,id or  newcustomerId + id 
			case "-" + CUSTOMER: Main.addCustomerToServices(id, Integer.parseInt(args[1]));
						customers.put(Integer.parseInt(args[1]), new Customer(Integer.parseInt(args[1])));
						break;
			case "q" + CUSTOMER: String result = "Composite Bill for customer " + Integer.parseInt(args[1])+ " {\n\t\t" +
					Main.services.get(Server.Car).proxy.queryCustomerInfo(id, Integer.parseInt(args[1])) + "\n\n\t\t" +
					Main.services.get(Server.Hotel).proxy.queryCustomerInfo(id, Integer.parseInt(args[1])) + "\n\n\t\t" +
					Main. services.get(Server.Flight).proxy.queryCustomerInfo(id, Integer.parseInt(args[1])) + "\n" +
						"}\n";
						break; //nothing to do, only read operation	
			case "r" + FLIGHT:  Main.services.get(Server.Flight).proxy.reserveFlight(id, Integer.parseInt(args[1]), Integer.parseInt(args[2])); //and recreate the whole flight
					   			break;
			case "r" + CAR: Main.services.get(Server.Car).proxy.reserveCar(id, Integer.parseInt(args[1]), args[2]);
							break;
			case "r" + HOTEL:  Main.services.get(Server.Hotel).proxy.reserveRoom(id, Integer.parseInt(args[1]), args[2]);
					   			break;
			default: break; //reserve itinerary is a composite of the above actions, no need to actually make a cmd of it
		}
		
	}

	//requests the lock of a cmd throws a deadlock exception if we can't acquire the lock
	private void requestLock(int xid, String cmd) throws DeadlockException
	{
		//get parameters from csv string
		String[] args = cmd.split(",");
		
		switch (args[0])
		{
			case "+" + FLIGHT: lm.Lock(xid, FLIGHT + args[1], WRITE);
					   break;		
			case "-" + FLIGHT: lm.Lock(xid, FLIGHT + args[1], WRITE);
					   break;
			case "q" + FLIGHT: lm.Lock(xid, FLIGHT + args[1], READ);
					   break; //nothing to do, only read operation
			case "p" + FLIGHT: lm.Lock(xid, FLIGHT + args[1], READ);
					   break; //nothing to do, only read operation
			case "+" + CAR: lm.Lock(xid, CAR + args[1], WRITE);
					   break;	
			case "-" + CAR: lm.Lock(xid, CAR + args[1], WRITE);
			   		   break;
			case "q" + CAR: lm.Lock(xid, CAR + args[1], READ);
					   break; //nothing to do, only read operation
			case "p" + CAR: lm.Lock(xid, CAR + args[1], READ);
					   break; //nothing to do, only read operation
			case "+" + HOTEL: lm.Lock(xid, HOTEL + args[1], WRITE);
					   break;
			case "-" + HOTEL: lm.Lock(xid, HOTEL + args[1], WRITE);
	   		   		   break;
			case "q" + HOTEL: lm.Lock(xid, HOTEL + args[1], READ);
					   break; //nothing to do, only read operation
			case "p" + HOTEL: lm.Lock(xid, HOTEL + args[1], READ);
					   break; //nothing to do, only read operation
			case "+" + CUSTOMER: lm.Lock(xid, CUSTOMER + args[1], WRITE);
						break;//careful 2 cases here,id or  newcustomerId + id 
			case "-" + CUSTOMER: lm.Lock(xid, CUSTOMER + args[1], WRITE);
						break;
			case "q" + CUSTOMER: lm.Lock(xid, CUSTOMER + args[1], READ);
						break; //nothing to do, only read operation
			case "r" + FLIGHT: lm.Lock(xid, CUSTOMER + args[1], READ);
					   lm.Lock(xid, FLIGHT + args[2], WRITE);
					   break;
			case "r" + CAR: lm.Lock(xid, CUSTOMER + args[1], READ);
			   		   lm.Lock(xid, CAR + args[2], WRITE);
					   break;
			case "r" + HOTEL: lm.Lock(xid, CUSTOMER + args[1], READ);
					   lm.Lock(xid, HOTEL + args[2], WRITE);
					   break;
			default: break; //reserve itinerary is a composite of the above actions, no need to actually make a cmd of it
		}
	}
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	//Aborts the given transaction and rollbacks all cmds that have been executed
	@Override
	public boolean abort(int transactionId) 
	{
		 //check if transaction exists
		 if (!trxns.containsKey(transactionId))
			  return false;
		 
		 //get transaction
		 Transaction t = trxns.get(transactionId);
		 t.refreshTimeStamp();
		 
		//synchronize t to set isAborting to true
		synchronized(t)
		{
			if ( t.isTerminating) //to prevent double aborts/commits of a transaction
				return true;
			t.isTerminating= true;
		}
		 
		 //unlock all resources held by transaction, if any
		 lm.UnlockAll(transactionId);
		 
		 //call all servers to tell them to abort
		 alertServersAbort(t);
		 
		 //delete transaction from pool of currently executing transactions
		 trxns.remove(transactionId);
	
		return true;
	}

	

	//shuts down the servers if no transactions are currently executing on the servers
	@Override
	public boolean shutdown() {
		//kill tllenforcer here
		
		/*LinkedList<Server> nonActiveServers = new LinkedList<Server>(Arrays.asList(Server.Car, Server.Flight, Server.Hotel));
		
		//iterate over each transaction, get all active servers involved in each of them, remove them from the list
		//we are left with non active servers at the end (servers not involved in transactions)
		for ( Entry<Integer, Transaction> e : trxns.entrySet())
		{
			for ( Server s : e.getValue().servers())
			{
				if (nonActiveServers.contains(s))
				{
					nonActiveServers.remove(s);
				}
			}
		}
		
		
		if ( nonActiveServers.size() != 3)
		{
			//at least one server is still being used
			return false;
		}
		
		//call shutdown for each non active server
		for ( Server s : nonActiveServers)
		{
			services.get(s).proxy.shutdown();
		}
		
		//call shutdown for middleware
		System.exit(0);
		// TODO Auto-generated method stub
		 */		return false;
	}

	@Override
	public boolean addFlight(int id, int flightNumber, int numSeats, int flightPrice) {
		
		  //check if transaction exists
		  if (!trxns.containsKey(id))
			  return false;
		  
		  //get transaction and update its structures
		  Transaction t = trxns.get(id);
		  t.addServer(Server.Flight);
		  t.addOperationToExecute("+" + FLIGHT +"," + flightNumber + "," + numSeats +"," + flightPrice);
		  return true;
	}

	@Override
	public boolean deleteFlight(int id, int flightNumber) {
		 
		 //check if transaction exists
		  if (!trxns.containsKey(id))
			  return false;
		  
		  //get transaction and update its structures
		  Transaction t = trxns.get(id);
		  t.addServer(Server.Flight);
		  t.addOperationToExecute("-" + FLIGHT +"," + flightNumber);
		  return true;
	}

	@Override
	public int queryFlight(int id, int flightNumber) {
		
		 //check if transaction exists
		  if (!trxns.containsKey(id))
			  return -1;
		  return 0;
		 /* //get transaction and update its structures
		  Transaction t = trxns.get(id);
		  t.addServer(Server.Flight);
		  t.addOperationToExecute("q" + FLIGHT +"," + flightNumber);
		  return true;
		
		if (!trxns.containsKey(id))
			  return 0;
		 
		 //get transaction
		  Transaction t = trxns.get(id);
		  t.addServer(Server.Flight); //add server to transaction
			  
		  try { //not sure
			lm.Lock(id, FLIGHT + flightNumber, READ);
			String cmd = "qf," + flightNumber;
			t.addCommand(cmd);
			
			//System.out.println("hello 4 in queryflight, data item: " + "f" + flightNumber);
			return services.get(Server.Flight).proxy.queryFlight(id, flightNumber);
		  } 
		  catch (DeadlockException e) 
		  {
			 System.out.println("hello exception in query flight");
			return 0;
		  }*/
		  
		
	}

	@Override
	public int queryFlightPrice(int id, int flightNumber) {
		 if (!trxns.containsKey(id))
			  return 0;
		 
		 //get transaction
		  Transaction t = trxns.get(id);
		  t.addServer(Server.Flight); //add server to transaction
		
		  return 0;
		/*  
		  try { //not sure
			lm.Lock(id, FLIGHT + flightNumber, READ);
			String cmd = "pf," + flightNumber;
			t.addCommand(cmd);
			
			return services.get(Server.Flight).proxy.queryFlightPrice(id, flightNumber);
		  } 
		  catch (DeadlockException e) 
		  {
			return 0;
		  }*/
		  
		
	}

	@Override
	public boolean addCars(int id, String location, int numCars, int carPrice) {

		 //check if transaction exists
		  if (!trxns.containsKey(id))
			  return false;
		  
		  //get transaction and update its structures
		  Transaction t = trxns.get(id);
		  t.addServer(Server.Car);
		  t.addOperationToExecute("+" + CAR +"," + location + "," + numCars +"," + carPrice);
		  return true;
	}

	@Override
	public boolean deleteCars(int id, String location) {
		 
		//check if transaction exists
		  if (!trxns.containsKey(id))
			  return false;
		  
		  //get transaction and update its structures
		  Transaction t = trxns.get(id);
		  t.addServer(Server.Car);
		  t.addOperationToExecute("-" + CAR +"," + location); //should add n and price here for revert
		  return true;
	}

	@Override
	public int queryCars(int id, String location) {
		 if (!trxns.containsKey(id))
			  return 0;
		 return 0;
	/*	//get transaction
		  Transaction t = trxns.get(id);
		  t.addServer(Server.Car); //add server to transaction
		 
		  
		  try { //not sure
			lm.Lock(id, CAR + location, READ);
			String cmd = "qc," + location;
			t.addCommand(cmd);
			
			return services.get(Server.Car).proxy.queryCars(id, location);
		  } 
		  catch (DeadlockException e) 
		  {
			return 0;
		  }*/
	}

	@Override
	public int queryCarsPrice(int id, String location) {
		 if (!trxns.containsKey(id))
			  return 0;
		return 0; 
		/*//get transaction
		  Transaction t = trxns.get(id);
		  t.addServer(Server.Car); //add server to transaction
		 
		  
		  try { //not sure
			lm.Lock(id, CAR + location, READ);
			 String cmd = "pc," + location;
			  t.addCommand(cmd);
			  
			  return services.get(Server.Car).proxy.queryCarsPrice(id, location);
		  } 
		  catch (DeadlockException e) 
		  {
			return 0;
		  }
		  */
		
	}

	@Override
	public boolean addRooms(int id, String location, int numRooms, int roomPrice) 
	{
		  //check if transaction exists
		  if (!trxns.containsKey(id))
			  return false;
		  
		  //get transaction and update its structures
		  Transaction t = trxns.get(id);
		  t.addServer(Server.Hotel);
		  t.addOperationToExecute("+" + HOTEL +"," + location + "," + numRooms +"," + roomPrice);
		  return true;
	}

	@Override
	public boolean deleteRooms(int id, String location) 
	{
		  //check if transaction exists
		  if (!trxns.containsKey(id))
			  return false;
		  
		  //get transaction and update its structures
		  Transaction t = trxns.get(id);
		  t.addServer(Server.Hotel);
		  t.addOperationToExecute("-" + HOTEL +"," + location); //should add price and number of rooms here 
		  return true;		
	}

	@Override
	public int queryRooms(int id, String location) {
		 if (!trxns.containsKey(id))
			  return 0;
		 
		 return 0;
		/* Transaction t = trxns.get(id);
		  t.addServer(Server.Hotel); //add server to transaction
		
		  
		  try { //not sure
			lm.Lock(id, HOTEL + location, READ);
			  String cmd = "qh," + location;
			  t.addCommand(cmd);
			  
			  return services.get(Server.Hotel).proxy.queryRooms(id, location);
		  } 
		  catch (DeadlockException e) 
		  {
			return 0;
		  }*/
		  
		
	}

	@Override
	public int queryRoomsPrice(int id, String location) {
		 if (!trxns.containsKey(id))
			  return 0;
		 
		 Transaction t = trxns.get(id);
		  t.addServer(Server.Hotel); //add server to transaction
		 
		  return 0;
		 /* try { //not sure
			lm.Lock(id, HOTEL + location, READ);
			 String cmd = "ph," + location;
			  t.addCommand(cmd);
			  
			  return services.get(Server.Hotel).proxy.queryRoomsPrice(id, location);
		  } 
		  catch (DeadlockException e) 
		  {
			return 0;
		  }*/
		  
		
	}

	@Override
	public int newCustomer(int id) 
	{
		 //check if transaction exists
		 if (!trxns.containsKey(id))
			  return -1;
		
		 //get transaction and update its structures
		 Transaction t = trxns.get(id);
		 t.addServer(Server.Car); //add server to transaction
		 t.addServer(Server.Flight); //add server to transaction
		 t.addServer(Server.Hotel); //add server to transaction
		 
		 //get randomId for customer
		 int  randomId = Math.abs(new Random().nextInt());
		 while(customers.containsKey(randomId))
			 randomId = Math.abs(new Random().nextInt());
		
		 //add to list of operations to be executed
		 t.addOperationToExecute("+cu," + randomId);
		 
		 try 
		 {
		 	lm.Lock(id, CUSTOMER + randomId, WRITE);
		 } 
		 catch (DeadlockException e) 
		 {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		 
		 //place customer in data structure
		 customers.put(randomId, new Customer(randomId /*,t*/));
		 
		 //return the id
		 return randomId;
	}
	
	

	@Override
	public boolean newCustomerId(int id, int customerId) 
	{
		//check if transaction exists
		 if (!trxns.containsKey(id))
			  return false;
		 if( customers.containsKey(customerId))
			  return false;
		
		 //get transaction and update its structures
		 Transaction t = trxns.get(id);
		 t.addServer(Server.Car); //add server to transaction
		 t.addServer(Server.Flight); //add server to transaction
		 t.addServer(Server.Hotel); //add server to transaction
		 
	
		 //add to list of operations to be executed
		 t.addOperationToExecute("+" + CUSTOMER +"," + customerId);
		 
		 try 
		 {
		 	lm.Lock(id, CUSTOMER+customerId, WRITE);
		 } 
		 catch (DeadlockException e) 
		 {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		 
		 //place customer in data structure
		 customers.put(customerId, new Customer(customerId));
		 
		 //return the id
		 return true;
	}

	@Override
	public boolean deleteCustomer(int id, int customerId) 
	{
		//check if transaction exists
		 if (!trxns.containsKey(id))
			  return false;
		 if( !customers.containsKey(customerId))
			  return false;
		
		 //get transaction
		 Transaction t = trxns.get(id);
		 
		//check if transaction has right to reserve flight (either t is specified or the list is empty for it to reserve)
		//if (!customers.get(customerId).exclusiveAccess.contains(t) && !customers.get(customerId).exclusiveAccess.isEmpty())
		//	 return false;
		try 
		{
			lm.Lock(id, CUSTOMER+customerId, WRITE);
		} 
		catch (DeadlockException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		 //update its structures
		 t.addServer(Server.Car); //add server to transaction
		 t.addServer(Server.Flight); //add server to transaction
		 t.addServer(Server.Hotel); //add server to transaction
		 
		 //add to list of operations to be executed
		 t.addOperationToExecute("-" + CUSTOMER +"," + customerId);
		 
		 //place customer in data structure
		 customers.remove(customerId); //TODO: SEE QUESTION 1 ABOVE, add check for transaction allowed to do  this?not sure
		 
		 //return the id
		 return true;	
	}

	@Override
	public String queryCustomerInfo(int id, int customerId) {
		 if (!trxns.containsKey(id))
			  return "wrong transaction id";
		 if (!customers.containsKey(customerId))
			 return "";
		 return "";
		/* Transaction t = trxns.get(id);
		 t.addServer(Server.Car); //add server to transaction
		 t.addServer(Server.Flight); //add server to transaction
		 t.addServer(Server.Hotel); //add server to transaction
		 	
		
		 
		 try { //not sure
				lm.Lock(id, CUSTOMER + customerId, READ);
				String cmd = "qcu," + customerId;
				t.addCommand(cmd);
				
				return "hello";"Composite Bill for customer " + customerId + " {\n\t\t" +
				   services.get(Server.Car).proxy.queryCustomerInfo(id, customerId) + "\n\n\t\t" +
				   services.get(Server.Hotel).proxy.queryCustomerInfo(id, customerId) + "\n\n\t\t" +
			       services.get(Server.Flight).proxy.queryCustomerInfo(id, customerId) + "\n" +
				   "}\n";
		  } 
		  catch (DeadlockException e) 
		  {
			return "";
		  } */
	}

	@Override
	public boolean reserveFlight(int id, int customerId, int flightNumber) 
	{
		//check if transaction exists
		  if (!trxns.containsKey(id))
			  return false;
		  if( !customers.containsKey(customerId))
			  return false;
		  
		  //get transaction and update its structures
		  Transaction t = trxns.get(id);
		  
		//check if transaction has right to reserve flight (either t is specified or the list is empty for it to reserve)
		//if (!customers.get(customerId).exclusiveAccess.contains(t) && !customers.get(customerId).exclusiveAccess.isEmpty())
		//	 return false;
		try 
		{
			lm.Lock(id, CUSTOMER + customerId, READ);
		} 
		catch (DeadlockException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		  
		 //update the transaction fields
		  t.addServer(Server.Flight);
		  t.addOperationToExecute("r" + FLIGHT +"," + customerId + "," + flightNumber);
		  return true;
	}

	@Override
	public boolean reserveCar(int id, int customerId, String location) 
	{
		//check if transaction exists
		if (!trxns.containsKey(id))
			  return false;
		if( !customers.containsKey(customerId))
			  return false;
		  
		//get transaction and update its structures
		Transaction t = trxns.get(id);
		  
		//check if transaction has right to reserve flight (either t is specified or the list is empty for it to reserve)
		//if (!customers.get(customerId).exclusiveAccess.contains(t) && !customers.get(customerId).exclusiveAccess.isEmpty())
		//	 return false;
		try 
		{
			lm.Lock(id, CUSTOMER+customerId, READ);
		} 
		catch (DeadlockException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		//update the transaction fields
		t.addServer(Server.Car);
		t.addOperationToExecute("r" + CAR +"," + customerId + "," +  location);
		return true;
	}

	@Override
	public boolean reserveRoom(int id, int customerId, String location) 
	{
		//check if transaction exists
		if (!trxns.containsKey(id))
			  return false;
		if( !customers.containsKey(customerId))
			  return false;
		  
		//get transaction and update its structures
		Transaction t = trxns.get(id);
		  
		//check if transaction has right to reserve flight (either t is specified or the list is empty for it to reserve)
		//if (!customers.get(customerId).exclusiveAccess.contains(t) && !customers.get(customerId).exclusiveAccess.isEmpty())
		//	 return false;
		try 
		{
			lm.Lock(id, CUSTOMER+customerId, READ);
		} 
		catch (DeadlockException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		  
		//update the transaction fields
		t.addServer(Server.Hotel);
		t.addOperationToExecute("r" + HOTEL +"," + customerId + "," + location);
		return true; 
	}

	
	//TODO: what if piling up commands at certain point fails, say after querying all flights, query car returns false? we need to revert back and undo all commands in transaction
	@Override
	public synchronized boolean reserveItinerary(int id, int customerId, Vector flightNumbers, String location, boolean car, boolean room) 
	{
		 if (!trxns.containsKey(id))
			  return false;
		 if( !customers.containsKey(customerId))
			  return false;
		 
		//get transaction and update its structures
		Transaction t = trxns.get(id);
		  
		//check if transaction has right to reserve flight (either t is specified or the list is empty for it to reserve)
		//if (!customers.get(customerId).exclusiveAccess.contains(t) && !customers.get(customerId).exclusiveAccess.isEmpty())
		//	 return false;
		try 
		{
			lm.Lock(id, CUSTOMER + customerId, READ);
		} 
		catch (DeadlockException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		 if (!flightNumbers.isEmpty()) t.addServer(Server.Flight); //add server to transaction
		 if (car) t.addServer(Server.Car); //add server to transaction
		 if (room)t.addServer(Server.Hotel); //add server to transaction
		 
		//check for available slots on flights
    	for(Object flightNum : flightNumbers)
    	{
    			//get lock on flight number
    			int flightNumber =  Integer.parseInt(flightNum.toString());
    		
    			//query flight
    			if (queryFlight(id, flightNumber) == 0)
    				return false;
    	}
		 
    	//check for available car, if needed
    	if (car)
    	{
    		if (queryCars(id, location) == 0)
    			return false;
    	}
    	
    	//check for available room, if needed
    	if (room)
    	{
    		if (queryRooms(id, location) == 0)
    			return false;
    	}
		 
    	//reserve seats
    	for(Object flightNum : flightNumbers)
    	{
			int flightNumber =  Integer.parseInt(flightNum.toString());
			if (!reserveFlight(id, customerId, flightNumber))
				return false;
    	}
    	
    	if (car)
    		if (!reserveCar(id, customerId, location))
    			return false;

    	if (room)
    		if (reserveRoom(id, customerId,  location))
    			return false;
    	return true;
	}

	
	
	//rollsback operations performed so far by the transaction
	private void rollbackOperations(int id) 
	{
		
		//TODO: implement this
		
		//get trx
	/*	Transaction t = trxns.get(id);
	
		//get list of cmds so far
		String[] cmds =  new String[t.cmds().size()];
	    t.cmds().toArray(cmds);
		
		//iterate backwards through the commands
		for ( int i = cmds.length -1; i >= 0 ; i--)
		{
			//get parameters from csv string
			String[] args = cmds[i].split(",");
			
			switch (args[0])
			{
				case "+f":  services.get(Server.Flight).proxy.deleteFlight(id, Integer.parseInt(args[1])); //lm.Lock(id, "-f" + args[1], WRITE); //a;lready have this
							break;		
				case "-f": services.get(Server.Flight).proxy.addFlight(id, Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
						   break;
				case "qf": break; //nothing to do, only read operation
				case "pf": break; //nothing to do, only read operation
				case "+c": services.get(Server.Car).proxy.deleteCars(id, args[1]);
						   break;	
				case "-c": services.get(Server.Car).proxy.addCars(id, args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]));
				   		   break;
				case "qc": break; //nothing to do, only read operation
				case "pc": break; //nothing to do, only read operation
				case "+h": services.get(Server.Hotel).proxy.deleteRooms(id, args[1]);
						   break;
				case "-h": services.get(Server.Hotel).proxy.addRooms(id, args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]));
		   		   		   break;
				case "qh": break; //nothing to do, only read operation
				case "ph": break; //nothing to do, only read operation
				case "+cu": removeCustomerFromServices(id, Integer.parseInt(args[1]));
							customers.remove(Integer.parseInt(args[1]));
							break;//careful 2 cases here,id or  newcustomerId + id 
				case "-cu": addCustomerToServices(id, Integer.parseInt(args[1]));
							customers.put(Integer.parseInt(args[1]), new Customer(Integer.parseInt(args[1])));
							break;
				case "qcu": break; //nothing to do, only read operation
				
				case "rf": //services.get(Server.Flight).proxy.deleteFlight(id, Integer.parseInt(args[2])); //delete the whole flight
						   services.get(Server.Flight).proxy.addFlight(id, Integer.parseInt(args[2]), 1, Integer.parseInt(args[4])); //and recreate the whole flight
						   break;
				case "rc": //services.get(Server.Car).proxy.deleteCars(id, args[2]);
						   services.get(Server.Car).proxy.addCars(id, args[2], 1, Integer.parseInt(args[4]));
						   break;
				case "rh": //services.get(Server.Hotel).proxy.deleteRooms(id, args[2]);
						   services.get(Server.Hotel).proxy.addRooms(id, args[2], 1, Integer.parseInt(args[4]));
						   break;
				default: break; //reserve itinerary is a composite of the above actions, no need to actually make a cmd of it
			}
		}*/
	}
	
	
	
}

class Customer
{
	public final int id;	
	//public final LinkedList<Transaction> exclusiveAccess = new LinkedList<Transaction>();
	public Customer(int pid /*, Transaction t*/)
	{
		id = pid;
		//exclusiveAccess.add(t);
	}

}