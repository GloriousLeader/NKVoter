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

package net.sini.nkvoter.core;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sini.nkvoter.io.SocketFactory;
import net.sini.nkvoter.util.RequestBuilder;

/**
 * Created by Sini
 */
public final class PollDaddyVoteStrategy extends VoteStrategy {
    
    /**
     * The poll daddy socket address.
     */
    private static final InetSocketAddress POLL_DADDY_ADDRESS = new InetSocketAddress("polldaddy.com", 80);
    
    /**
     * The poll daddy polls socket address.
     */
    private static final InetSocketAddress POLL_DADDY_POLLS_ADDRESS = new InetSocketAddress("polls.polldaddy.com", 80);
    
    /**
     * The target to get the vote id. 
     */
    private String VOTE_ID_TARGET = "/n/113df4577acffec0e03c79cfc7210eb6/6685610?";
    
    /**
     * The target to use to vote.
     */
    private String VOTE_TARGET = "/vote-js.php?p=6685610&b=1&a=30279773&o=&va=16&c=1&url=http%3A//www.time.com/time/specials/packages/article/0%2C28804%2C2128881_2128882_2129192%2C00.html&n=";
    
    /**
     * The cookies to use when getting the vote id. Note: Haven't tested most of these for use yet.
     */
    private static final String VOTE_ID_COOKIE = "pd-adsrc=google; __qca=P0-1338909366-1354132214914; "
                                               + "km_ai=ug8cTGqQAJli5Y4JiLC11%2BI%2FC28%3D; kvcd=1354133975089; "
                                               + "km_lv=1354133975; km_uq=; __utma=182033702.639645329.1354135226.1354135226.1354135226.1; "
                                               + "__utmc=182033702; __utmz=182033702.1354135226.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)";
    
    /**
     * The cookies to use when voting. Note: Haven't tested most of these for use yet.
     */
    private static final String VOTE_COOKIE = "pd-adsrc=google; __qca=P0-1338909366-1354132214914;"
                                            + " km_ai=ug8cTGqQAJli5Y4JiLC11%2BI%2FC28%3D;"
                                            + " kvcd=1354133975089; km_lv=1354133975;"
                                            + " km_uq=;"
                                            + " __utma=182033702.639645329.1354135226.1354135226.1354135226.1;"
                                            + " __utmc=182033702; __utmz=182033702.1354135226.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); ";
    
    /**
     * The vote id pattern to use.
     */
    private static final Pattern VOTE_ID_PATTERN = Pattern.compile("=\'(.*)\'");
    
    /**
     * The timestamp to use while voting.
     */
    private long timestamp;
    
    /**
     * Constructs a new {@link PollDaddyVoteStrategy};
     */
    public PollDaddyVoteStrategy(String IDTarget, String voteTarget)
    {
        this.VOTE_ID_TARGET = IDTarget;
        this.VOTE_TARGET = voteTarget;
    }
   
    @Override
    public VoteReturnStatus vote(SocketFactory socketFactory) throws Exception {
        
        /* Get a new timestamp */
        timestamp = System.currentTimeMillis();
        
        /* Get the vote id */
        String voteId = getVoteId(socketFactory);
        
        /* Open the socket for the poll and vote */
        Socket socket = socketFactory.createSocket(POLL_DADDY_POLLS_ADDRESS);
        RequestBuilder builder = createRequestBuilder(VOTE_TARGET + voteId, POLL_DADDY_POLLS_ADDRESS);
        builder.addHeader("Cookie", VOTE_COOKIE + "PD_poll_6685715_1=" + (timestamp - 1000 * 60 * 180));
        socket.getOutputStream().write(builder.build().getBytes());
        socket.getOutputStream().flush();
        
        /* Read the response sent from the server */
        Scanner scanner = new Scanner(socket.getInputStream());
        String response = "";
        while(scanner.hasNextLine()) {
            response += scanner.nextLine() + "\n";
        }
        
        /* Close the socket */
        socket.close();

        /* Check if our leader has been honored with a vote */
        if(response.contains("Thank you for voting!")) {
            return VoteReturnStatus.SUCCESS;

        } else if(response.contains("You will be unblocked after a cooling off period.")) {
            return VoteReturnStatus.BANNED;
        }
        
        return VoteReturnStatus.UNKNOWN;
    }
    
    /**
     * Gets a new vote id.
     * 
     * @param socketFactory The socket factory to use.
     * @return              The created vote id.
     */
    private String getVoteId(SocketFactory socketFactory) throws Exception {
        
        /* Open a socket and get the vote id */
        Socket socket = socketFactory.createSocket(POLL_DADDY_ADDRESS);
        RequestBuilder builder = createRequestBuilder(VOTE_ID_TARGET + timestamp, POLL_DADDY_ADDRESS);
        builder.addHeader("Cookie", VOTE_ID_COOKIE);
        socket.getOutputStream().write(builder.build().getBytes());
        socket.getOutputStream().flush();
        
        /* Read the response sent from the server */
        Scanner scanner = new Scanner(socket.getInputStream());
        String response = "";
        while(scanner.hasNextLine()) {
            response += scanner.nextLine() + "\n";
        }
        
        /* Close the socket */
        socket.close();

        /* Prase out the vote id */
        Matcher matcher = VOTE_ID_PATTERN.matcher(response);
        String id = "";
        if(matcher.find()) {
            id = matcher.group(1);
        }
        
        return id;
    }
    
    /**
     * Creates a new request builder.
     * 
     * @param target    The target file to get.
     * @param address   The address to connect to.
     * @return          The request builder.
     */
    public RequestBuilder createRequestBuilder(String target, InetSocketAddress address) {
        RequestBuilder builder = new RequestBuilder(target, address);
        builder.addHeader("Host", "polldaddy.com");
        builder.addHeader("Referer", "http://www.time.com/time/specials/packages/article/0,28804,2128881_2128882_2129214,00.html");
        builder.addHeader("User-Agent", "Mozilla/5.0 (X11; Linux i686) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.57 Safari/537.1");
        return builder;
    }
}
