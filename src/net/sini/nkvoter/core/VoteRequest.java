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

import java.net.Socket;
import net.sini.nkvoter.io.SocketFactory;

/**
 * Created by Sini
 */
public final class VoteRequest {
    
    /**
     * The socket to use to request the votes from.
     */
    private final SocketFactory socketFactory;
    
    /**
     * The vote strategy to use when voting.
     */
    private final VoteStrategy strategy;
    
    /**
     * The amount of times to vote.
     */
    private final int amountVotes;
    
    /**
     * Constructs a new {@link VoteRequest};
     * 
     * @param socketFactory The socket factory to use to create sockets with.
     * @param strategy      The vote strategy to use when voting.
     * @param amountVotes   The amount of votes to request.
     */
    public VoteRequest(SocketFactory socketFactory, VoteStrategy strategy, int amountVotes) {
        this.socketFactory = socketFactory;
        this.strategy = strategy;
        this.amountVotes = amountVotes;
    }
    
    /**
     * Gets the socket factory to use to create sockets with.
     * 
     * @return  The socket factory.
     */
    public SocketFactory getSocketFactory() {
        return socketFactory;
    }
    
    /**
     * Gets the vote strategy to use when voting.
     * 
     * @return  The vote strategy.
     */
    public VoteStrategy getVoteStrategy() {
        return strategy;
    }
    
    /**
     * Gets the amount of times to vote.
     * 
     * @return  The amount of times to vote.
     */
    public int getAmountVotes() {
        return amountVotes;
    }
}