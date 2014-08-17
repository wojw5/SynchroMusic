package pl.airpolsl.synchromusic;

import java.util.ArrayList;
import java.util.List;

public class Clients {
	private List<Client> clients;
	
	public Clients(){
		this.clients = new ArrayList<Client>();
	}
	
	public void addNew(Client nClient){
		clients.add(nClient);
	};
	
	public List<Client> getList(){
		return clients;
	}
	
}
