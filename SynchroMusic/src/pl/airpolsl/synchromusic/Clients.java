package pl.airpolsl.synchromusic;

import java.util.ArrayList;
import java.util.List;

/**
 * List of clients
 *
 */
public class Clients {
	private List<Client> clients;
	
	/**
	 * Initialize Client list
	 */
	public Clients(){
		this.clients = new ArrayList<Client>();
	}
	
	/**
	 * Adds Client to the list
	 * @param nClient
	 */
	public void addNew(Client nClient){
		clients.add(nClient);
	};
	
	/**
	 * Returns clients list
	 * @return clients list
	 */
	public List<Client> getList(){
		return clients;
	}
	
	/**
	 * Removes passed client form Clients list
	 * @param client
	 */
	public void remove(Client client) {
		clients.remove(client);
	}
	
}
