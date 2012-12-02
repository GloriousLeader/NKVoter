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

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import net.sini.nkvoter.io.SocketFactory;

/**
 * Created by Sini
 */
public final class VoteDispatcher implements Runnable {
    
    /**
     * The amount of votes to make per worker.
     */
    private static final int AMOUNT_VOTES_PER_WORKER = 5;
    
    /**
     * The amount of threads to create per CPU core.
     */
    private static final int THREADS_PER_CPU_CORE = 1;
    
    /**
     * The executor for this engine.
     */
    private final Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * THREADS_PER_CPU_CORE);
    
    /**
     * The workers in this dispatcher.
     */
    private final BlockingQueue<VoteWorker> workers = new LinkedBlockingQueue<VoteWorker>();
    
    /**
     * The socket factory for this vote dispatcher.
     */
    private final SocketFactory socketFactory;
    
    /**
     * The strategy for this vote dispatcher.
     */
    private final VoteStrategy strategy;
    
    /**
     * The flag for if this dispatcher is running.
     */
    private boolean isRunning;
    
    /**
     * Constructs a new {@link VoteDispatcher};
     * 
     * @param socketFactory     The socket factory to use when submitting requests.
     * @param strategyFactory   The strategy factory to use when submitting requests.
     */
    public VoteDispatcher(SocketFactory socketFactory, VoteStrategy strategy) {
        this.socketFactory = socketFactory;
        this.strategy = strategy;
    }
    
    /**
     * Submits a vote request to this vote dispatcher.
     * 
     * @param amountVotes   The amount of votes to submit.
     * @reutrn              The created workers.
     */
    public VoteWorker[] submit(int amountVotes) { 
        VoteWorker[] voteWorkers = new VoteWorker[(amountVotes / AMOUNT_VOTES_PER_WORKER) + (amountVotes % AMOUNT_VOTES_PER_WORKER != 0 ? 1 : 0)];
        for(int i = 0; i < voteWorkers.length; i++) {
            int votes = amountVotes;
            if(votes > AMOUNT_VOTES_PER_WORKER) {
                votes = AMOUNT_VOTES_PER_WORKER;
            }
            amountVotes -= votes;
            
            VoteRequest request = new VoteRequest(socketFactory, strategy, votes);
            VoteWorker worker = voteWorkers[i] = new VoteWorker(request);
            workers.add(worker);
        }
        return voteWorkers;
    }

    @Override
    public void run() {
        isRunning = true;
        synchronized(this) {
            Iterator<VoteWorker> iterator = workers.iterator();
            while(iterator.hasNext()) {
                VoteWorker worker = iterator.next();
                executor.execute(worker);
                iterator.remove();
            }
            isRunning = false;
        }
    }
    
    /**
     * Gets if this dispatcher is running.
     * 
     * @return  If the dispatcher is running.
     */
    public boolean isRunning() {
        return isRunning;
    }
}
