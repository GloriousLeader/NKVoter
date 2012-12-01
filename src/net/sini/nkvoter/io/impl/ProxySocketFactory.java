/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sini.nkvoter.io.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import net.sini.nkvoter.io.SocketFactory;

/**
 *
 * @author Gabe
 */
public class ProxySocketFactory  extends SocketFactory{
    	private InetSocketAddress proxyAddress;

    public ProxySocketFactory(InetSocketAddress proxyAddress)
    {
	this.proxyAddress = proxyAddress;
    }

    @Override
    public Socket createSocket(InetSocketAddress address) throws IOException {
		Proxy proxy = new Proxy(Proxy.Type.SOCKS, proxyAddress);
		Socket socket = new Socket(proxy);
		socket.setSoTimeout(15000);
		
		socket.connect(address);
        return socket;
    }
}
