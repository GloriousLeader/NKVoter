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

package net.sini.nkvoter.util;

import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Sini
 */
public final class RequestBuilder {
    
    /**
     * The list of headers for this request.
     */
    private final List<Header> headers = new LinkedList<Header>();
   
    /**
     * The target of the request.
     */
    private final String target;
    
    /**
     * The address of the request.
     */
    private final InetSocketAddress address;
    
    /**
     * Represents a HTTP header.
     */
    private static class Header {
        
        /**
         * The name of the header.
         */
        private final String name;
        
        /**
         * The value of the header.
         */
        private final String value;
        
        /**
         * Constructs a new {@link Header};
         * 
         * @param name  The name of the header.
         * @param value The value of the header.
         */
        public Header(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }
    
    /**
     * Constructs a new {@link RequestBuilder};
     * 
     * @param target    The target of the request.
     * @param address   The address of the request.
     */
    public RequestBuilder(String target, InetSocketAddress address) {
        this.target = target;
        this.address = address;
    }

    /**
     * Adds a header to this request.
     * 
     * @param name  The name of the header.
     * @param value The value of the header.
     */
    public void addHeader(String name, String value) {
        Header header = new Header(name, value);
        headers.add(header);
    }
    /**
     * Builds the request.
     * 
     * @return  The build request.
     */
    public String build() {
        String request = "GET  " + target +" HTTP/1.0\r\n"
            + "Host: " + address.getHostName() + ":" + address.getPort() + "\r\n"
            + "Accept: */*\r\n"
            + "Connection: Keep-Alive\r\n"
            + "Pragma: no-cache\r\n";
        for(Header header : headers) {
            request += header.name + ": " + header.value + "\r\n";
        }
        request += "\r\n";
        return request;
    }
}