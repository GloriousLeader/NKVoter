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
    
    @Override
    public VoteStrategy createStrategy()
    {
        return null;
    }
    public VoteStrategy createStrategy(String candID) {
        String[] relevantStrings = candidates.get(candID);
        return new PollDaddyVoteStrategy(relevantStrings[0], relevantStrings[1], relevantStrings[2]);
    }
    
    private void initCandidates()
    {
        String[] KJU_strings = new String[3];
        KJU_strings[0] = "Our Glorious Leader";
        KJU_strings[1] = "/n/113df4577acffec0e03c79cfc7210eb6/6685610?";
        KJU_strings[2] = "/vote-js.php?p=6685610&b=1&a=30279773&o=&va=16&c=1&url=http%3A//www.time.com/time/specials/packages/article/0%2C28804%2C2128881_2128882_2129192%2C00.html&n=";
        candidates.put("KJU", KJU_strings);
        
        String[] Jon_strings = new String[3];
        Jon_strings[0] = "Jon";
        Jon_strings[1] = "/n/6b1aa0c281ebbeec4419dbcdb76a7da0/6685709?";
        Jon_strings[2] = "/vote-js.php?p=6685709&b=1&a=30280231&o=&va=16&c=1&url=http%3A//www.time.com/time/specials/packages/article/0%2C28804%2C2128881_2128882_2129211%2C00.html&n=";
        candidates.put("Jon", Jon_strings);
        
        String[] Undoc_strings = new String[3];
        Undoc_strings[0] = "Undocumented";
        Undoc_strings[1] = "/n/e1386f1d203929ebe8c0202ab9b529bf/6685607?";
        Undoc_strings[2] = "/vote-js.php?p=6685607&b=1&a=30279757,&o=&va=16&c=1&url=http%3A//www.time.com/time/specials/packages/article/0%2C28804%2C2128881_2128882_2129191%2C00.html&n=";
        candidates.put("Undoc", Undoc_strings);
        
        String[] Stephen_strings = new String[3];
        Stephen_strings[0] = "Stephen";
        Stephen_strings[1] = "/n/45a54d304efd9783196e1db13da69194/6685714?";
        Stephen_strings[2] = "/vote-js.php?p=6685714&b=1&a=30280243,&o=&va=16&c=1&url=http%3A//www.time.com/time/specials/packages/article/0%2C28804%2C2128881_2128882_2129212%2C00.html&n=";
        candidates.put("Stephen", Stephen_strings);
        
        String[] Gabrielle_strings = new String[3];
        Gabrielle_strings[0] = "Gabrielle";
        Gabrielle_strings[1] = "/n/77beeeb2bb776cc255eb4e3fac1cb624/6685689?";
        Gabrielle_strings[2] = "/vote-js.php?p=6685689&b=1&a=30280171,&o=&va=16&c=1&url=http%3A//www.time.com/time/specials/packages/article/0%2C28804%2C2128881_2128882_2129203%2C00.html&n";
        candidates.put("Gabrielle", Gabrielle_strings);
    }
}