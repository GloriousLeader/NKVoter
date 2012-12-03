package net.sini.nkvoter.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author bla
 */
public class HttpProxySocket extends java.net.Socket{
    /**
     * Various states of this socket.
     */
    private boolean created = false;
    private boolean bound = false;
    private boolean connected = false;
    private boolean closed = false;
    private Object closeLock = new Object();
    private boolean shutIn = false;
    private boolean shutOut = false;

    private Proxy prx;
    private URLConnection conn;

    public HttpProxySocket(Proxy proxy) {
        if (proxy != null && proxy.type() == Proxy.Type.HTTP) {
            prx = proxy;
        } else {
            throw new IllegalArgumentException("Invalid Proxy");
        }
    }

    public void connect(SocketAddress endpoint) throws IOException {
        connect(endpoint, 0);
    }

    public void connect(InetSocketAddress endpoint, int timeout) throws IOException {
        if (endpoint == null)
            throw new IllegalArgumentException("connect: The address can't be null");

        if (timeout < 0)
          throw new IllegalArgumentException("connect: timeout can't be negative");

        if (isClosed())
            throw new SocketException("Socket is closed");

        if (!(endpoint instanceof InetSocketAddress))
            throw new IllegalArgumentException("Unsupported address type");

       URL url = new URL("http://" + endpoint.getAddress().getHostName());
       
       try{
           conn = url.openConnection(prx);
           
           conn.setDoInput(true);
           conn.setDoOutput(true);
           
           connected = true;
           bound = true;
       } catch(SocketException e)
       {
           throw new IOException("Eat shit.");
       }
    }

    public InputStream getInputStream() throws IOException {
        if (isClosed())
            throw new SocketException("Socket is closed");
        if (!connected)
            throw new SocketException("Socket is not connected");
        if (isInputShutdown())
            throw new SocketException("Socket input is shutdown");

        InputStream is = conn.getInputStream();

        return is;
    }

    public OutputStream getOutputStream() throws IOException {
        if (isClosed())
            throw new SocketException("Socket is closed");
        if (!connected)
            throw new SocketException("Socket is not connected");
        if (isOutputShutdown())
            throw new SocketException("Socket output is shutdown");

        OutputStream os = conn.getOutputStream();
        return os;
    }
}
