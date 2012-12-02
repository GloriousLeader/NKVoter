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

import java.util.HashMap;

/**
 * Created by Sini
 */
public final class PollDaddyVoteStrategyFactory extends VoteStrategyFactory {
    private HashMap<String, String[]> candidates;
    
    /**
     * Constructs a new {@link PollDaddyVoteStrategy};
     */
    public PollDaddyVoteStrategyFactory()
    {
        candidates = new HashMap<String, String[]>();
        initCandidates();
    }
    
    private void initCandidates()
    {
        String[] KJU_strings = new String[2];
        KJU_strings[0] = "/n/113df4577acffec0e03c79cfc7210eb6/6685610?";
        KJU_strings[1] = "/vote-js.php?p=6685610&b=1&a=30279773&o=&va=16&c=1&url=http%3A//www.time.com/time/specials/packages/article/0%2C28804%2C2128881_2128882_2129192%2C00.html&n=";
        candidates.put("KJU", KJU_strings);
        
    }
    
    @Override
    public VoteStrategy createStrategy()
    {
        return null;
    }
    public VoteStrategy createStrategy(String candID) {
        String[] relevantStrings = candidates.get(candID);
        return new PollDaddyVoteStrategy(relevantStrings[0], relevantStrings[1]);
    }
}