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

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Sini
 */
public final class VoteEngine {
    
    /**
     * The executor for this engine.
     */
    private final Executor executor = Executors.newSingleThreadExecutor();

    /**
     * The vote dispatchers for this engine.
     */
    private final Map<String, VoteDispatcher> dispatchers = new HashMap<String, VoteDispatcher>();
    
    /**
     * Adds a vote dispatcher to this engine.
     * 
     * @param name          The name of the engine to add.
     * @param dispatcher    The vote dispatcher.
     */
    public void add(String name, VoteDispatcher dispatcher) {
        dispatchers.put(name, dispatcher);
    }
    
    /**
     * Gets a dispatcher from this engine.
     * 
     * @param name  The name of the dispatcher.
     * @return      The vote dispatcher.
     */
    public VoteDispatcher get(String name) {
        return dispatchers.get(name);
    }
    
    /**
     * Removes a vote dispatcher from this engine.
     * 
     * @param name  The name of the dispatcher to remove. 
     */
    public void remove(String name) {
        dispatchers.remove(name);
    }
    
    /**
     * Pulses this engine.
     */
    public void pulse() {
        Collection<VoteDispatcher> dispatcherList = dispatchers.values();
        for(VoteDispatcher dispatcher : dispatcherList) {
            if(!dispatcher.isRunning()) {
                executor.execute(dispatcher);
            }
        }
    }
}