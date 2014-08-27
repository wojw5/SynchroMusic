package pl.airpolsl.synchromusic;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import android.util.Log;

public class MultiCastThread implements Runnable
{
	public static final int PORT_NO = 5436;
    public static final String GROUP_ADDR = "230.123.123.123";
    public static final int DGRAM_LEN = 1024;
    public static final int TIME_TO_LIVE = 2;
    
    public static final String TAG = "MultiCastThread";
    
    @Override
    public void run()
    {
        MulticastSocket socket = null;
        DatagramPacket inPacket = null;
        byte[] inBuf = new byte[DGRAM_LEN];
        try
        {
          //Prepare to join multicast group
          socket = new MulticastSocket(PORT_NO);
          InetAddress address = InetAddress.getByName(GROUP_ADDR);
          socket.joinGroup(address);

          while(true)
          {
                Log.d(TAG,"Listening");
                inPacket = new DatagramPacket(inBuf, inBuf.length);
                socket.receive(inPacket);
                String msg = new String(inBuf, 0, inPacket.getLength());
                Log.d(TAG,"From :" + inPacket.getAddress() + " Msg : " + msg);
                SynchroMusicProtocol.processUDP(msg);
          }
        }
        catch(Exception ioe)
        {
            Log.d(TAG,ioe.getMessage());
        }
    }
}
