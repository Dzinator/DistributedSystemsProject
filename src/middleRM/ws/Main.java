package middleRM.ws;


import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.jws.WebService;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

import LockManager.*; //for ass2

import javax.naming.NamingException;

import org.apache.catalina.startup.Tomcat;

@WebService
public class Main implements server.ws.ResourceManager { //server.ws.ResourceManager
	
	//the 3 RM connections to the middle ressource manager
	public static final HashMap<Server, Connection> services = new HashMap<Server, Connection>(3);

	//customer support
	private static final HashMap<Integer, Customer> customers = new HashMap<Integer, Customer>();
	
	private static final ReentrantLock itineraryLock = new ReentrantLock();

	public enum Server {Flight, Car, Hotel};
	public static final int READ = 0;
	public static final int WRITE = 1;
	public static final long TTL = 20000; //20 seconds
	
	/*
	 * Assignment 2 data structures required:
	 * 
	 * Memory to undo transactions
	 * lock manager
	 * set of transactions currently opened
	 * */
	public static final HashMap<Integer, Transaction> trxns = new HashMap<Integer, Transaction>(1000);
	private static final LockManager lm = new LockManager();
	
	//TODO: customers data structure is fine, since we add teh customer after locking it 
	// 		put cmds after locking in try block
	
	/*
	 *  f : flight
		h : hotel
		c : car
		cu : customer
		i : itinerary
		
		
		cmds
		"+f," + flightNumber + "," + numSeats +"," + flightPrice; (add flight)
		"-f," + flightNumber;									  (deleteFlight)
		"qf," + flightNumber;									  (queryFlight)
		"pf," + flightNumber; 									  (queryFlightPrice)
		
		 "+c," + location + "," + numCars +"," + carPrice		  (addCar)
		 "-c," + location; 										  (deleteCar)
		 "qc," + location;										  (queryCar)
		 "pc," + location;										  (queryCarsPrice)
		
		 "+h," + location + "," + numRooms +"," + roomPrice;      (addRoom)
		 "-h," + location;										  (deleteRooms)
		 "qh," + location;										  (queryRooms)
		 "ph," + location;										  (queryRoomsPrice)
		
		
		"+cu," + randomId										  (newCustomer)
		"+cu," + customerId;									  (newCustomerId)
		"-cu," + customerId;									  (deleteCustomer)
		"qcu," + customerId;									  (queryCustomerInfo)
		
		 "rf," + "," + customerId + "," + flightNumber;			  (reserveFlight)
		 "rc," + "," + customerId + "," + location;				  (reserveCar)
		 "rh," + "," + customerId + "," + location;				  (reserveRoom)
 */
	
	
	public Main() throws NamingException, MalformedURLException
	{
		//Context env = (Context) new InitialContext().lookup("java:comp/env");
	
		//get flight info and place in services
		//String flightServiceHost = "lab9-28";//(String) env.lookup("flight-service-host");
		String flightServiceHost = "localhost";
		Integer flightServicePort = 1410;//(Integer) env.lookup("flight-service-port");
		String flightServiceName = "flight";//(String) env.lookup("flight-service-name");
		Connection flightServer = new Connection(flightServiceName, flightServiceHost, flightServicePort );
		services.put(Server.Flight, flightServer);
		
		//get car info and place in services
		//String carServiceHost = "lab9-28";//(String) env.lookup("flight-service-host");
		String carServiceHost = "localhost";
		Integer carServicePort = 1411;//(Integer) env.lookup("flight-service-port");
		String carServiceName = "car";//(String) env.lookup("flight-service-name");
		Connection carServer = new Connection(carServiceName, carServiceHost, carServicePort );
		services.put(Server.Car, carServer);
		
		//get hotel info and place in services
		//String hotelServiceHost = "lab9-28";//(String) env.lookup("flight-service-host");
		String hotelServiceHost = "localhost";
		Integer hotelServicePort = 1412;//(Integer) env.lookup("flight-service-port");
		String hotelServiceName = "room";//(String) env.lookup("flight-service-name");
		Connection hotelServer = new Connection(hotelServiceName, hotelServiceHost, hotelServicePort );
		services.put(Server.Hotel, hotelServer);
		
		//dispatch a thread to enforce TTL fro transactions
		new TTLEnforcer(this).run();
	}
	
	public static void main(String[] args) throws Exception 
	{  
        if (args.length != 3) {
           System.out.println(
                "Usage: java Main <service-name> <service-port> <deploy-dir> but for middleRM, so 12 inputs");
            System.exit(-1);
        }
        //services = new HashMap<String, Connection>(3);
        for(String s : args)
        {
        	System.out.println(s);
        }
       
        
        //setup Tomcat
        String serviceName = args[0];
        int port = Integer.parseInt(args[1]);
        String deployDir = args[2];
        
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.setBaseDir(deployDir);

        tomcat.getHost().setAppBase(deployDir);
        tomcat.getHost().setDeployOnStartup(true);
        tomcat.getHost().setAutoDeploy(true);

        //tomcat.addWebapp("", new File(deployDir).getAbsolutePath());

        
       // System.out.println("inputs: /" + serviceName + " ,file path : " + new File(deployDir + "/" + serviceName).getAbsolutePath());
        tomcat.addWebapp("/" + serviceName, 
                new File(deployDir + "/" + serviceName).getAbsolutePath());
      
        tomcat.enableNaming();
        tomcat.start();
        tomcat.getServer().await();
	}

	@Override
	public boolean addFlight(int id, int flightNumber, int numSeats, int flightPrice) {
		
		  if (!trxns.containsKey(id))
			  return false;
		  
		  //get transaction
		  Transaction t = trxns.get(id);
		  t.addServer(Server.Flight); //add server to transaction
		 
		  
		  try { //not sure
			lm.Lock(id, "f" + flightNumber, WRITE);
			String cmd = "+f," + flightNumber + "," + numSeats +"," + flightPrice;
			t.addCommand(cmd);
			System.out.println("in addflight, data item: " + "f" + flightNumber);
			return services.get(Server.Flight).proxy.addFlight(id, flightNumber, numSeats, flightPrice); 
		  } 
		  catch (DeadlockException e) 
		  {
			  System.out.println("hello exception in addflight");
			  return false;
		  }
		  //System.out.println("unreachable : hello 5");
		  
	}

	@Override
	public boolean deleteFlight(int id, int flightNumber) {
		 if (!trxns.containsKey(id))
			  return false;
		 
		 //get transaction
		  Transaction t = trxns.get(id);
		  t.addServer(Server.Flight); //add server to transaction
		 
		  try { //not sure
			lm.Lock(id, "f" + flightNumber, WRITE);
			int seats = services.get(Server.Flight).proxy.queryFlight(id, flightNumber);
			int price = services.get(Server.Flight).proxy.queryFlightPrice(id, flightNumber);
			String cmd = "-f," + flightNumber + "," + seats + "," + price;
			t.addCommand(cmd);
			return services.get(Server.Flight).proxy.deleteFlight(id, flightNumber);
		  } 
		  catch (DeadlockException e) 
		  {
			return false;
		  }
		 
		
	}

	@Override
	public int queryFlight(int id, int flightNumber) {
		 if (!trxns.containsKey(id))
			  return 0;
		 
		 //get transaction
		  Transaction t = trxns.get(id);
		  t.addServer(Server.Flight); //add server to transaction
		
		  
		  try { //not sure
			lm.Lock(id, "f" + flightNumber, READ);
			String cmd = "qf," + flightNumber;
			t.addCommand(cmd);
			
			System.out.println("hello 4 in queryflight, data item: " + "f" + flightNumber);
			return services.get(Server.Flight).proxy.queryFlight(id, flightNumber);
		  } 
		  catch (DeadlockException e) 
		  {
			 System.out.println("hello exception in query flight");
			return 0;
		  }
		  
		
	}

	@Override
	public int queryFlightPrice(int id, int flightNumber) {
		 if (!trxns.containsKey(id))
			  return 0;
		 
		 //get transaction
		  Transaction t = trxns.get(id);
		  t.addServer(Server.Flight); //add server to transaction
		
		  
		  try { //not sure
			lm.Lock(id, "f" + flightNumber, READ);
			String cmd = "pf," + flightNumber;
			t.addCommand(cmd);
			
			return services.get(Server.Flight).proxy.queryFlightPrice(id, flightNumber);
		  } 
		  catch (DeadlockException e) 
		  {
			return 0;
		  }
		  
		
	}

	@Override
	public boolean addCars(int id, String location, int numCars, int carPrice) {
		 if (!trxns.containsKey(id))
			  return false;
		 
		//get transaction
		  Transaction t = trxns.get(id);
		  t.addServer(Server.Car); //add server to transaction
		 
		  
		  try { //not sure
			lm.Lock(id, "c" + location + "," + numCars +"," + carPrice, WRITE);
			String cmd = "+c," + location + "," + numCars +"," + carPrice;
			t.addCommand(cmd);
			
			return services.get(Server.Car).proxy.addCars(id, location, numCars, carPrice);
		  } 
		  catch (DeadlockException e) 
		  {
			return false;
		  }
		  
		
	}

	@Override
	public boolean deleteCars(int id, String location) {
		 if (!trxns.containsKey(id))
			  return false;
		 
		//get transaction
		  Transaction t = trxns.get(id);
		  t.addServer(Server.Car); //add server to transaction
		  
		 
		  
		  try { //not sure
			lm.Lock(id, "c" + location, WRITE);
			 int numCars = services.get(Server.Car).proxy.queryCars(id, location);
			  int price = services.get(Server.Car).proxy.queryCarsPrice(id, location);
			  String cmd = "-c," + location + "," + numCars + "," + price;
			  t.addCommand(cmd);
			  
			  return services.get(Server.Car).proxy.deleteCars(id, location);
		  } 
		  catch (DeadlockException e) 
		  {
			return false;
		  }	  
	}

	@Override
	public int queryCars(int id, String location) {
		 if (!trxns.containsKey(id))
			  return 0;
		 
		//get transaction
		  Transaction t = trxns.get(id);
		  t.addServer(Server.Car); //add server to transaction
		 
		  
		  try { //not sure
			lm.Lock(id, "c" + location, READ);
			String cmd = "qc," + location;
			t.addCommand(cmd);
			
			return services.get(Server.Car).proxy.queryCars(id, location);
		  } 
		  catch (DeadlockException e) 
		  {
			return 0;
		  }
	}

	@Override
	public int queryCarsPrice(int id, String location) {
		 if (!trxns.containsKey(id))
			  return 0;
		 
		//get transaction
		  Transaction t = trxns.get(id);
		  t.addServer(Server.Car); //add server to transaction
		 
		  
		  try { //not sure
			lm.Lock(id, "c" + location, READ);
			 String cmd = "pc," + location;
			  t.addCommand(cmd);
			  
			  return services.get(Server.Car).proxy.queryCarsPrice(id, location);
		  } 
		  catch (DeadlockException e) 
		  {
			return 0;
		  }
		  
		
	}

	@Override
	public boolean addRooms(int id, String location, int numRooms, int roomPrice) {
		 if (!trxns.containsKey(id))
			  return false;
		 
		 
		 //get transaction
		  Transaction t = trxns.get(id);
		  t.addServer(Server.Hotel); //add server to transaction
		 
		  
		  try { //not sure
			lm.Lock(id, "h" + location, WRITE);
			 String cmd = "+h," + location + "," + numRooms +"," + roomPrice;
			  t.addCommand(cmd);
			  
			  return services.get(Server.Hotel).proxy.addRooms(id, location, numRooms, roomPrice);
		  } 
		  catch (DeadlockException e) 
		  {
			return false;
		  }
			  
		
	}

	@Override
	public boolean deleteRooms(int id, String location) {
		 if (!trxns.containsKey(id))
			  return false;
		 
		  Transaction t = trxns.get(id);
		  t.addServer(Server.Hotel); //add server to transaction
		  
		  try { //not sure
			lm.Lock(id, "h" + location, WRITE);
			 int numRooms = services.get(Server.Hotel).proxy.queryRooms(id, location);
			  int price = services.get(Server.Hotel).proxy.queryRoomsPrice(id, location);
			  String cmd = "-h," + location + "," + numRooms + "," + price;
			  t.addCommand(cmd);
			  
			  return services.get(Server.Hotel).proxy.deleteRooms(id, location);
		  } 
		  catch (DeadlockException e) 
		  {
			return false;
		  }
		  
		
	}

	@Override
	public int queryRooms(int id, String location) {
		 if (!trxns.containsKey(id))
			  return 0;
		 
		 Transaction t = trxns.get(id);
		  t.addServer(Server.Hotel); //add server to transaction
		
		  
		  try { //not sure
			lm.Lock(id, "h" + location, READ);
			  String cmd = "qh," + location;
			  t.addCommand(cmd);
			  
			  return services.get(Server.Hotel).proxy.queryRooms(id, location);
		  } 
		  catch (DeadlockException e) 
		  {
			return 0;
		  }
		  
		
	}

	@Override
	public int queryRoomsPrice(int id, String location) {
		 if (!trxns.containsKey(id))
			  return 0;
		 
		 Transaction t = trxns.get(id);
		  t.addServer(Server.Hotel); //add server to transaction
		 
		  
		  try { //not sure
			lm.Lock(id, "h" + location, READ);
			 String cmd = "ph," + location;
			  t.addCommand(cmd);
			  
			  return services.get(Server.Hotel).proxy.queryRoomsPrice(id, location);
		  } 
		  catch (DeadlockException e) 
		  {
			return 0;
		  }
		  
		
	}

	@Override
	public int newCustomer(int id) {
		 if (!trxns.containsKey(id))
			  return -1;
		
		 Transaction t = trxns.get(id);
		 t.addServer(Server.Car); //add server to transaction
		 t.addServer(Server.Flight); //add server to transaction
		 t.addServer(Server.Hotel); //add server to transaction
		 
		 int  randomId = Math.abs(new Random().nextInt());
		 while(customers.containsKey(randomId))
			 randomId = Math.abs(new Random().nextInt());
			
		
		 try { //not sure
				lm.Lock(id, "cu" + randomId, WRITE);
				 String cmd = "+cu," + randomId;
				 t.addCommand(cmd);
				 
				addCustomerToServices(id, randomId);
				return randomId;
		  } 
		  catch (DeadlockException e) 
		  {
			return -1;
		  }
		 
	}
	
	

	@Override
	public boolean newCustomerId(int id, int customerId) {
		 if (!trxns.containsKey(id))
			  return false;
		 if(  customers.containsKey(customerId))
				return false;
		 
		 Transaction t = trxns.get(id);
		 t.addServer(Server.Car); //add server to transaction
		 t.addServer(Server.Flight); //add server to transaction
		 t.addServer(Server.Hotel); //add server to transaction
		 	
		
		 
		 try { //not sure
				lm.Lock(id, "cu" + customerId, WRITE);
				 String cmd = "+cu," + customerId;
				 t.addCommand(cmd);
				 
				addCustomerToServices(id, customerId);
				return true;
		  } 
		  catch (DeadlockException e) 
		  {
			  return false;
		  }		
	}

	@Override
	public boolean deleteCustomer(int id, int customerId) {
		 if (!trxns.containsKey(id))
			  return false;
		 if(  !customers.containsKey(customerId))
				return false;
		 
		 
		 Transaction t = trxns.get(id);
		 t.addServer(Server.Car); //add server to transaction
		 t.addServer(Server.Flight); //add server to transaction
		 t.addServer(Server.Hotel); //add server to transaction
		 	
		
		 
		 try { //not sure
				lm.Lock(id, "cu" + customerId, WRITE);
				 String cmd = "-cu," + customerId;
				 t.addCommand(cmd);
				 
				removeCustomerFromServices(id, customerId);
				return true;
		  } 
		  catch (DeadlockException e) 
		  {
			return false;
		  }
			
	}

	@Override
	public String queryCustomerInfo(int id, int customerId) {
		 if (!trxns.containsKey(id))
			  return "wrong transaction id";
		 if (!customers.containsKey(customerId))
			 return "";
		 
		 Transaction t = trxns.get(id);
		 t.addServer(Server.Car); //add server to transaction
		 t.addServer(Server.Flight); //add server to transaction
		 t.addServer(Server.Hotel); //add server to transaction
		 	
		
		 
		 try { //not sure
				lm.Lock(id, "cu" + customerId, READ);
				String cmd = "qcu," + customerId;
				t.addCommand(cmd);
				
				return "Composite Bill for customer " + customerId + " {\n\t\t" +
				   services.get(Server.Car).proxy.queryCustomerInfo(id, customerId) + "\n\n\t\t" +
				   services.get(Server.Hotel).proxy.queryCustomerInfo(id, customerId) + "\n\n\t\t" +
			       services.get(Server.Flight).proxy.queryCustomerInfo(id, customerId) + "\n" +
				   "}\n";
		  } 
		  catch (DeadlockException e) 
		  {
			return "";
		  } 
	}

	@Override
	public boolean reserveFlight(int id, int customerId, int flightNumber) {
		
		 if (!trxns.containsKey(id))
			  return false;
		 if(  !customers.containsKey(customerId))
				return false;
		 
		 Transaction t = trxns.get(id);
		  t.addServer(Server.Flight); //add server to transaction
		
		  
		  try { //not sure
			lm.Lock(id, "f" +  flightNumber, WRITE);
			lm.Lock(id, "cu" +  customerId, READ);
			int seats = services.get(Server.Flight).proxy.queryFlight(id, flightNumber);
			int price = services.get(Server.Flight).proxy.queryFlightPrice(id, flightNumber);
		    String cmd = "rf," + customerId + "," + flightNumber + "," + seats + "," + price;
		    t.addCommand(cmd);
		    
		    return services.get(Server.Flight).proxy.reserveFlight(id, customerId, flightNumber);
		  } 
		  catch (DeadlockException e) 
		  {
			return false;
		  }

		
	}

	@Override
	public boolean reserveCar(int id, int customerId, String location) {
		 if (!trxns.containsKey(id))
			  return false;
		 if(  !customers.containsKey(customerId))
				return false;
		 
		 Transaction t = trxns.get(id);
		  t.addServer(Server.Car); //add server to transaction
		  
		  try { //not sure
			lm.Lock(id, "c" +  location, WRITE);
			lm.Lock(id, "cu" +  customerId, READ);
			int numCars = services.get(Server.Car).proxy.queryCars(id, location);
			int price = services.get(Server.Car).proxy.queryCarsPrice(id, location);
		    String cmd = "rc," + customerId + "," +  location + "," + numCars + "," + price;
		    t.addCommand(cmd);
		    
		    return services.get(Server.Car).proxy.reserveCar(id, customerId, location);
		  } 
		  catch (DeadlockException e) 
		  {
			return false;
		  }
		
	}

	@Override
	public boolean reserveRoom(int id, int customerId, String location) {
		 
		if (!trxns.containsKey(id))
			  return false;
		if(  !customers.containsKey(customerId))
			return false;
		 
		 Transaction t = trxns.get(id);
		  t.addServer(Server.Hotel); //add server to transaction
		  
		  try { //not sure
			lm.Lock(id, "h" +  location, WRITE);
			lm.Lock(id, "cu" +  customerId, READ);
			int numRooms = services.get(Server.Hotel).proxy.queryRooms(id, location);
			int price = services.get(Server.Hotel).proxy.queryRoomsPrice(id, location);
		    String cmd = "rh," + customerId + "," + location + "," + numRooms + "," + price;
		    t.addCommand(cmd);
		    
		    return services.get(Server.Hotel).proxy.reserveRoom(id, customerId, location);
		  } 
		  catch (DeadlockException e) 
		  {
			return false;
		  }
	}

	
	//TODO: if we get stuck on lock, do we abort whole transaction or just send false to user and unlock everything and abort transaction?
	@Override
	public synchronized boolean reserveItinerary(int id, int customerId, Vector flightNumbers, String location, boolean car, boolean room) 
	{
		 if (!trxns.containsKey(id))
			  return false;
		 if( !customers.containsKey(customerId))
			  return false;
		
		 Transaction t = trxns.get(id);
		 if (!flightNumbers.isEmpty()) t.addServer(Server.Flight); //add server to transaction
		 if ( car) t.addServer(Server.Car); //add server to transaction
		 if ( room)t.addServer(Server.Hotel); //add server to transaction
		 
		 try { //not sure

				//check for available slots
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
		    	if ( room)
		    	{
		    		if (queryRooms(id, location) == 0)
		    			return false;
		    	}
		    	
		    	//checks are done, everything in order up until now. We reserve:
		    	
		    	//reserve seats
		    	for(Object flightNum : flightNumbers)
		    	{
	    			int flightNumber =  Integer.parseInt(flightNum.toString());
	    			if (!reserveFlight(id, customerId, flightNumber))
	    			{
	    				return false;
	    			}
		    	}
		    	
		    	if (car)
		    	{
		    		if (!reserveCar(id, customerId, location) )
		    			return false;
		    	}
		    	
		    	if (room)
		    	{
		    		if (reserveRoom(id, customerId,  location))
		    			return false;
		    	}
		    	
		    	return true;
		  } 
		  catch (Exception e) 
		  {
			return false;
		  } 
	}
	
	private boolean addCustomerToServices(int id, int customerId)
	{
		customers.put(customerId, new Customer(customerId));
		int count = 0;
		for( Entry<Server, Connection> connection : services.entrySet())
		{
			if(connection.getValue().proxy.newCustomerId(id, customerId))
				count++;
		}
		return count==services.size();
	}

	private boolean removeCustomerFromServices(int id, int customerId)
	{
		customers.remove(customerId);
		int count = 0;
		for( Entry<Server, Connection> connection : services.entrySet())
		{
			if (connection.getValue().proxy.deleteCustomer(id, customerId))
				count++;
		}
		return count==services.size();
	}

	@Override
	//Start a new transaction and return its id
	public int start() {
		//get new trxn id
		int randomTrnxId = Math.abs(new Random().nextInt());
		while ( trxns.containsKey(randomTrnxId))
			randomTrnxId = Math.abs(new Random().nextInt());
		
		trxns.put(randomTrnxId, new Transaction(randomTrnxId, System.currentTimeMillis() + TTL));
		return randomTrnxId;
	}

	@Override
	//Attempt to commit the given transaction; return true upon success
	public boolean commit(int transactionId) {
		
		for( Server s : trxns.get(transactionId).servers())
		{
			services.get(s).proxy.commit(transactionId);
		}
		lm.UnlockAll(transactionId);
		
		return true;
	}

	@Override
	//Abort the given transaction.
	public boolean abort(int transactionId) {
		
		//call all servers to abort
		for( Server s : trxns.get(transactionId).servers())
		{
			services.get(s).proxy.abort(transactionId);
		}
		
		//handle undo operations here
		rollbackOperations(transactionId);
		
		//unlock objects held by this transaction
		lm.UnlockAll(transactionId);
		
		//remove the transaction from the set of currently executing transactions
		trxns.remove(transactionId);
		
		return true;
	}

	
	
	
	/*table of possible strings as cmds
	 * 
	 * f : flight
		h : hotel
		c : car
		cu : customer
		i : itinerary
		
		
		cmds
		"+f," + flightNumber + "," + numSeats +"," + flightPrice; (add flight)
		"-f," + flightNumber + "," + seats + "," + price;		  (deleteFlight)
		"qf," + flightNumber;									  (queryFlight)
		"pf," + flightNumber; 									  (queryFlightPrice)
		
		 "+c," + location + "," + numCars +"," + carPrice		  (addCar)
		 "-c," + location + "," + numCars + "," + price;		  (deleteCar)
		 "qc," + location;										  (queryCar)
		 "pc," + location;										  (queryCarsPrice)
		
		 "+h," + location + "," + numRooms +"," + roomPrice;      (addRoom)
		 "-h," + location + "," + numRooms + "," + price;		  (deleteRooms)
		 "qh," + location;										  (queryRooms)
		 "ph," + location;										  (queryRoomsPrice)
		
		
		"+cu," + randomId										  (newCustomer)
		"+cu," + customerId;									  (newCustomerId)
		"-cu," + customerId;									  (deleteCustomer)
		"qcu," + customerId;									  (queryCustomerInfo)
		
		 "rf," + customerId + "," + flightNumber + "," + seats + "," + price (reserveFlight)
		 "rc," + customerId + "," +  location + "," + numCars + "," + price  (reserveCar)
		 "rh," + customerId + "," + location + "," + numRooms + "," + price; (reserveRoom)
		
		 READ/f/
	 * */	
	private void rollbackOperations(int id) {
		
		//get trx
		Transaction t = trxns.get(id);
	
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
				
				case "rf": services.get(Server.Flight).proxy.deleteFlight(id, Integer.parseInt(args[2])); //delete the whole flight
						   services.get(Server.Flight).proxy.addFlight(id, Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4])); //and recreate the whole flight
						   break;
				case "rc": services.get(Server.Car).proxy.deleteCars(id, args[2]);
						   services.get(Server.Car).proxy.addCars(id, args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]));
						   break;
				case "rh": services.get(Server.Hotel).proxy.deleteRooms(id, args[2]);
						   services.get(Server.Car).proxy.addRooms(id, args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]));
						   break;
				default: break; //reserve itinerary is a composite of the above actions, no need to actually make a cmd of it
			}
		}
	}

	@Override
	/* Shutdown gracefully
	 * 		at RM: it is assumed that shutdown is only called when there is no transaction active at the RM.
	 * 			   When a shutdown RM restarts, it does not need to perform any recovery;
	 * 		At middleware layer:  call shutdown of all RMs;
	 * 		at TM: if relevant.
	 */
	public boolean shutdown() {
		
		LinkedList<Server> nonActiveServers = new LinkedList<Server>(Arrays.asList(Server.Car, Server.Flight, Server.Hotel));
		
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
		return false;
	}
}

class Connection
{
	 ResourceManagerImplService service;
	 ResourceManager proxy; //server.ws.ResourceManager
	 
	 public Connection(String serviceName, String serviceHost, int servicePort) 
	 throws MalformedURLException {
	 
	     URL wsdlLocation = new URL("http", serviceHost, servicePort, 
	             "/" + serviceName + "/service?wsdl");
	     
	     
	     service = new ResourceManagerImplService(wsdlLocation);
	     
	     proxy = service.getResourceManagerImplPort();
	 }
}

class Customer
{
	int id;	
	public Customer(int pid)
	{
		id = pid;
	}
}

//thread to handle the TTL mechanism for transactions
class TTLEnforcer implements Runnable
{
	Main program;
	public TTLEnforcer(Main m)
	{
		program = m;
	}
	
	@Override
	public void run() {
	
	//infinite loop for checking
	 while(true)
	 {
		 try
		{
			 //iterate over all transactions
			for(Entry<Integer, Transaction> e : Main.trxns.entrySet())
			{
				//if transaction's last time-stamp is greater then the TTL threshold, we abort the transaction
				if ( e.getValue().timestamp < System.currentTimeMillis())
				{
					//dispatch new thread slave to handle the abort calls
					new TTLEnforcerSlave(program, e.getValue()).run();
				}
			}
			
			//sleep for a time inversely proportional to the number of currently opened transactions
			Thread.sleep(Main.TTL - Main.trxns.size() * 2); 
		} 
		//if sleep interrupted, just restart the method
		catch(Exception e)
		{
			run();
		}
	 }
	}
}

class TTLEnforcerSlave implements Runnable
{
	Main program;
	Transaction txn;
	public TTLEnforcerSlave(Main m, Transaction t)
	{
		program = m;
		txn = t;
	}
	
	//simple method to execute the abort call of a transaction
	@Override
	public void run() 
	{
		program.abort(txn.tid);
	}
}

class Transaction
{
	private ArrayList<Main.Server> servers = new ArrayList<Main.Server>(3);
	private LinkedList<String> operations = new LinkedList<String>();
	public int tid;
	public long timestamp;
	
	public Transaction(int txid, long ts)
	{
		tid = txid;
		timestamp = ts;
	}
	
	public ArrayList<Main.Server> servers() 
	{ 
		return servers;
	}
	
	public void addServer(Main.Server s)
	{
		if (!servers.contains(s))
		{
			servers.add(s);
			Main.services.get(s).proxy.start();
		}
			
	}
	
	public void removeServer(Main.Server s)
	{
		if (servers.contains(s))
			servers.remove(s);
	}
	
	public void addCommand(String cmd)
	{
		operations.add(cmd);
		timestamp = System.currentTimeMillis() + Main.TTL;
	}
	
	public LinkedList<String> cmds()
	{
		return operations;
	}
}
