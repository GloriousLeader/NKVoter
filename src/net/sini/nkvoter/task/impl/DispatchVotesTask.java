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

package net.sini.nkvoter.task.impl;

import java.util.LinkedList;
import java.util.List;
import net.sini.nkvoter.core.VoteDispatcher;
import net.sini.nkvoter.core.VoteRequest;
import net.sini.nkvoter.core.VoteWorker;
import net.sini.nkvoter.core.VoteWorkerListener;
import net.sini.nkvoter.task.Task;

/**
 * Created by Sini
 */
public final class DispatchVotesTask extends Task {
    
    /**
     * The list of listeners to add to the created vote request.
     */
    private final List<VoteWorkerListener> listeners = new LinkedList<VoteWorkerListener>();
    
    /**
     * The vote dispatcher for this dispatch task.
     */
    private final VoteDispatcher dispatcher;
    
    /**
     * The amount of votes to dispatch.
     */
    private final int amountVotes;
    
    /**
     * Constructs a new {@link DispatchVotesTask};
     * 
     * @param delay         The delay between dispatching votes.
     * @param dispatcher    The vote dispatcher to submit votes to.
     * @param amountVotes   The amount of votes to submit.
     */
    public DispatchVotesTask(long delay, VoteDispatcher dispatcher, int amountVotes) {
        super(delay);
        this.dispatcher = dispatcher;
        this.amountVotes = amountVotes;
    }
    
    /**
     * Adds a listener for workers created by this dispatch task.
     */
    public void addWorkerListener(VoteWorkerListener listener) {
        listeners.add(listener);
    }

    @Override
    public void execute() {
        VoteWorker[] workers = dispatcher.submit(amountVotes);
        for(VoteWorker worker : workers) {
            for(VoteWorkerListener listener : listeners) {
                worker.addListener(listener);
            }
        }
    }      
}