package pl.airpolsl.synchromusic;

import java.util.List;

public class Clients {
	private List<Client> clients;
	
	public void addNew(Client nClient){
		boolean exist=false;
		for (Client client : clients)
		{
			if (client.getName()==nClient.getName())
			{
				exist=true;
				break;
			}
		}
		if(!exist) clients.add(nClient);
	};
	
	public Client getClientByName(String name){
		for (Client client : clients)
		{
			if (client.getName()==name)
			{
				return client;
			}
		}
		return null;
	};
	
	public void delete(Client nClient){
		for (Client client : clients)
		{
			if (client.getName()==nClient.getName())
			{
				clients.remove(nClient);
			}
		}
	}
	
	public void delete(String clientName){
		for (Client client : clients)
		{
			if (client.getName()==clientName)
			{
				clients.remove(client);
			}
		}
	}
}
