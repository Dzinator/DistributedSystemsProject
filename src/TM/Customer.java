package TM;

import java.util.LinkedList;

class Customer
{
	//customer id
	public final int id;
	
	//field to check if customer is new or existing in database
	boolean isNew = true;
	
	//list of reservations made by customer
	private final LinkedList<Item> reservations = new LinkedList<Item>();
	
	//public final LinkedList<Transaction> exclusiveAccess = new LinkedList<Transaction>();
	
	public Customer(int pid /*, Transaction t*/)
	{
		id = pid;
		//exclusiveAccess.add(t);
	}
	
	public Item[] getReservations()
	{
		Item[] items = new Item[reservations.size()];
		reservations.toArray(items);
		return items;
	}
	
	public void addReservation(Item i)
	{
		if (!reservations.contains(i))
			reservations.add(i);
	}
	
	
}