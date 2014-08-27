package pl.airpolsl.synchromusic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import pl.airpolsl.synchromusic.util.PathUtils;

import android.content.Context;
import android.net.Uri;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;
/**
 * File server. Sharing audio files from request.
 * @author Wojciech Widenka
 *
 */
public class FileServer extends NanoHTTPD {

	private Context context;
	
	/**
	 * Start file server
	 * @param nContext android context
	 */
	public FileServer(Context nContext) {
		super(3125);
		this.context = nContext;
		
	}

	
	@Override
    public Response serve(String uriString, Method method,
        Map<String, String> header, Map<String, String> parameters,
        Map<String, String> files) {
		
		Uri uri = Uri.parse(uriString.substring(1));
	    FileInputStream fis = null;
	    try {
	    	String path = PathUtils.getPath(context,uri);
	        fis = new FileInputStream(path);
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	        return new NanoHTTPD.Response(Status.NOT_FOUND,"text/plain","File not exist");
	    }
	    return new NanoHTTPD.Response(Status.OK, "audio/mpeg", fis);
	  }
}
