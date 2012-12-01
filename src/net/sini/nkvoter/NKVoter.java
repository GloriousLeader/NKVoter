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

package net.sini.nkvoter;

import net.sini.nkvoter.core.VoteEngine;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import net.sini.nkvoter.task.TaskManager;

/**
 * Created by Sini
 */
public final class NKVoter {

    /**
     * The singleton instance.
     */
    private static final NKVoter singleton = new NKVoter();
    
    /**
     * The executor for this application.
     */
    private static final Executor executor = Executors.newSingleThreadExecutor();
    
    /**
     * The vote engine for the application.
     */
    private final VoteEngine engine = new VoteEngine();
    
    /**
     * The task manager for the application.
     */
    private final TaskManager taskManager = new TaskManager();
    
    /**
     * Constructs a new {@link NKVoter};
     */
    public NKVoter() {}
    
    /**
     * Gets the task manager.
     * 
     * @return  The task manager.
     */
    public TaskManager getTaskManager() {
        return taskManager;
    }
    
    /**
     * Gets the vote engine for the application.
     * 
     * @return  The vote engine.
     */
    public VoteEngine getEngine() {
        return engine;
    }
    
    /**
     * Gets the singleton instance.
     * 
     * @return  The singleton instance.
     */
    public static NKVoter getSingleton() {
        return singleton;
    }
}
