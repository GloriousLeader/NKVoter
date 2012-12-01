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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import net.sini.nkvoter.io.SocketFactory;
import net.sini.nkvoter.io.SocketFactory;

/**
 * Created by Sini
 * 
 * Mostly ripped from the TorLib created by Joe Foley
 */
public final class TorSocketFactory extends SocketFactory {
    
    /**
     * Constructs a new {@link TorSocketFactory};
     */
    public TorSocketFactory() {}
        
    /**
     *  The tor proxy address.
     */
    private static String PROXY_ADDRESS = "127.0.0.1";
    
    /**
     * The tor proxy port.
     */
    private static int PROXY_PORT = 9050;
    
    /**
     * SOCKS4/4a connect request parameter.
     */
    private static final int TOR_CONNECT = 0x01;
    
    /**
     * The version of SOCKS that the socket utilizes.
     */
    private final static int SOCKS_VERSION = 0x04;

    /**
     * Setting the IP field to 0.0.0.1 causes SOCKS4a to
     * be enabled.
     */
    private final static int SOCKS4A_FAKEIP = 0x01;

    @Override
    public Socket createSocket(InetSocketAddress address) throws IOException {
        Socket socket = new Socket(PROXY_ADDRESS, PROXY_PORT);
        socket.setSoTimeout(30000);
        DataOutputStream os = new DataOutputStream(socket.getOutputStream());
        os.writeByte(SOCKS_VERSION);
        os.writeByte(TOR_CONNECT);
        os.writeShort(address.getPort());
        os.writeInt(SOCKS4A_FAKEIP);
        os.writeByte('\0');
        os.writeBytes(address.getHostName());
        os.writeByte('\0');
        
        DataInputStream is = new DataInputStream(socket.getInputStream());

        byte version = is.readByte();
        byte status = is.readByte();
        if(status != 90) {		
            throw new IOException();
        }
        int port = is.readShort();
        int ipAddr = is.readInt();
        return socket;
    }
}
