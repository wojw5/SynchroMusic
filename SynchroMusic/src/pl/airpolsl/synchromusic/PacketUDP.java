package pl.airpolsl.synchromusic;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import android.util.Log;


public class PacketUDP implements Runnable
{
    MulticastSocket s;
    DatagramPacket pack;
    public static final int PORT_NO = 5436;
    public static final String GROUP_ADDR = "230.123.123.123";
    public static final int DGRAM_LEN = 1024;
    private String data = null;
    public static final int TIME_TO_LIVE = 2;
    
    public PacketUDP(String nData)
    {
    	data = nData;
        try
        {
            s = new MulticastSocket(PORT_NO);
            s.joinGroup(InetAddress.getByName(GROUP_ADDR));
        }
        catch(Exception e)
        {
            Log.v("Socket Error: ",e.getMessage());
        }
    }
    @Override
    public void run()
    {
        try
        {
            pack = new DatagramPacket(data.getBytes(),data.getBytes().length, InetAddress.getByName(GROUP_ADDR), PORT_NO);
            s.setTimeToLive(TIME_TO_LIVE);
            s.send(pack);
        }
        catch(Exception e)
        {
            Log.v("Packet Sending Error: ",e.getMessage());
        }
    }
}