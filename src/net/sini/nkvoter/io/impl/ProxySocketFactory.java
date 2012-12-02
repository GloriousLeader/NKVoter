/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sini.nkvoter.io.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketException;
import net.sini.nkvoter.io.HttpProxySocket;
import net.sini.nkvoter.io.SocketFactory;

/**
 *
 * @author bla
 */
public class ProxySocketFactory  extends SocketFactory{
    	private InetSocketAddress proxyAddress;
        private boolean httpProxy;

    public ProxySocketFactory(InetSocketAddress proxyAddress)
    {
	this.proxyAddress = proxyAddress;
        httpProxy = false;
    }

    @Override
    public Socket createSocket(InetSocketAddress address) throws IOException {
        Proxy proxy;
        Socket socket;
        
        if(!httpProxy){
            try{
                proxy = new Proxy(Proxy.Type.SOCKS, proxyAddress);
                socket = new Socket(proxy);
                socket.setSoTimeout(15000);

                socket.connect(address);
                return socket;
            } catch(SocketException se)
            {
                System.out.println("Trying http proxy");
                try{
                    socket = httpConnect(address);
                    httpProxy = true;
                    return socket;
                } catch(IOException e)
                {
                    throw e;
                }
            }
        }
        
        System.out.println("The whole thing's fucked.");
        return new Socket();
    }
    
    private Socket httpConnect(InetSocketAddress address) throws IOException
    {
        HttpProxySocket hSock =  new HttpProxySocket(new Proxy(Proxy.Type.HTTP, proxyAddress));
        
        hSock.connect(address, 15000);
        
        return hSock;
    }
}
