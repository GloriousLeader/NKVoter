/**
 * Copyright (c) 2012, Sini
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy 
 * of this software and associated documentation files (the "Software"), to deal 
 * in the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN 
 * THE SOFTWARE.
 */

package net.sini.nkvoter.io.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import net.sini.nkvoter.io.SocketFactory;
import net.sini.nkvoter.util.RequestBuilder;

/**
 * Created by Sini
 */
public final class ProxySocketFactory extends SocketFactory {
    
    /**
     * The proxy address for the socket.
     */
    private final InetSocketAddress proxyAddress;
    
    /**
     * Constructs a new {@link ProxySocketFactory};
     */
    public ProxySocketFactory(InetSocketAddress proxyAddress) {
        this.proxyAddress = proxyAddress;
    }

    @Override
    public Socket createSocket(InetSocketAddress address) throws IOException {
        Socket socket = new Socket();
        socket.setSoTimeout(15000);
        socket.connect(new InetSocketAddress(proxyAddress.getHostName(), proxyAddress.getPort()));
        RequestBuilder builder = new RequestBuilder(address.getHostName(), address);
        socket.getOutputStream().write(builder.build().getBytes());
        socket.getOutputStream().flush();
        
        /* Read the response sent from the server */
        Scanner scanner = new Scanner(socket.getInputStream());
        String response = "";
        while(scanner.hasNextLine()) {
            response += scanner.nextLine() + "\n";
        }
        
        /* Check if the response was a success */
        if(!response.contains("HTTP/1.0 200 Connection established")) {
            throw new IOException();
        }
        
        return socket;
    }
}
