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

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Scanner;
import java.io.*;
import java.net.URL;
import net.sini.nkvoter.core.PollDaddyVoteStrategyFactory;
import net.sini.nkvoter.core.VoteDispatcher;
import net.sini.nkvoter.core.VoteEngine;
import net.sini.nkvoter.core.listeners.BasicListener;
import net.sini.nkvoter.io.SocketFactory;
import net.sini.nkvoter.io.impl.NormalSocketFactory;
import net.sini.nkvoter.io.impl.ProxySocketFactory;
import net.sini.nkvoter.io.impl.TorSocketFactory;
import net.sini.nkvoter.task.TaskManager;
import net.sini.nkvoter.task.impl.DispatchVotesTask;
import net.sini.nkvoter.task.impl.PulseEngineTask;

/**
 * Created by Sini
 */
public final class Main {
    
    /**
     * The maximum amount of votes.
     */
    private static final int MAXIMUM_VOTES = 50;
    
    /**
     * The delay between dumping the maximum amount of votes.
     */
    private static final long DELAY_BETWEEN_DUMPS = 10 * 60 * 1000;
    
    /**
     * The version of NKVoter.
     */
    private static final Version VERSION = new Version(1, 1, 0);
    
    private static VoteEngine engine;
    private static  PollDaddyVoteStrategyFactory strategyFactory;
    private static TaskManager taskManager;
    private static BasicListener listener;
    
    /**
     * The main entry point of the program.
     * 
     * @param args  The command line arguments.
     */
    public static void main(String[] args) throws Exception {
        System.out.println("" 
                         + " _   _ _  __ __     _____ _____ _____ ____                        \n"
                         + "| \\ | | |/ / \\ \\   / / _ |_   _| ____|  _ \\    Created by Sini\n"
                         + "|  \\| | ' /   \\ \\ / | | | || | |  _| | |_) |   Team VoteFuckers\n"
                         + "| |\\  | . \\    \\ V /| |_| || | | |___|  _ <                    \n"
                         + "|_| \\_|_|\\_\\    \\_/  \\___/ |_| |_____|_| \\_\\               \n"
                         + "                                                                  \n"
                         + "CREDITS to Kim Jong Un, Sini, Brother, Pholey, Orion, Onon, Bla,  \n"
                         + "           TheFeel, Drunkenevil, #opfuckmorsy                     \n"
                         + "                                                                  \n"
                         + "(" + VERSION + ")                                                 \n"
                         + "==================================================================");
        System.out.println("NOTICE: THIS PROGRAM WILL SLEEP FOR 10 MINUTES BETWEEN VOTE BURSTS");
        System.out.println("Would you like to use the Tor or a proxy? (t = tor, p = proxy, n = normal dispatcher)");
        
        boolean useNormal = true;
        
        int input = System.in.read();
        
        boolean useTor = input == 't';
        boolean useProxy = input == 'p';
        
        if(useTor || useProxy)
        {
            useNormal = false;
        }
        
        
        strategyFactory = new PollDaddyVoteStrategyFactory();
        engine = NKVoter.getSingleton().getEngine();
        taskManager = NKVoter.getSingleton().getTaskManager();
        listener = new BasicListener();
        
        if(useTor) {
            TorSocketFactory socketFactory = new TorSocketFactory();
            VoteDispatcher dispatcher = new VoteDispatcher(socketFactory, strategyFactory.createStrategy("KJU"));
            
            setupDispatchTasks("TOR", socketFactory);
        }

        if(useNormal) {
            NormalSocketFactory socketFactory = new NormalSocketFactory();
            VoteDispatcher dispatcher = new VoteDispatcher(socketFactory, strategyFactory.createStrategy("KJU"));
            
            setupDispatchTasks("NORMAL", socketFactory);
        }
        
        if(useProxy)
        {
            System.out.println("What is the proxy's ip address and port? (only use Socks4/5 proxies)");
            System.out.println("(e.g.: 123.123.123.123:1337)"); 
            
            InputStreamReader pinput = new InputStreamReader(System.in);
            BufferedReader ipScan = new BufferedReader(pinput);
            
            int l = System.in.read(); //Mac Fuckup Protection
            
            String[] addressParts = ipScan.readLine().split(":");
            String[] ipParts = addressParts[0].split("\\.");
            
            while((ipParts.length != 4) || (addressParts[1].length() < 2)) {
                    System.out.println("You've entered a wrong IP Adress or Port number, please re-enter");
                    pinput = new InputStreamReader(System.in);
                    ipScan = new BufferedReader(pinput);
                    l = System.in.read(); //Mac Fuckup Protection
                    addressParts = ipScan.readLine().split(":");
                    ipParts = addressParts[0].split("\\.");
            }
            
            byte[] ipbytes = new byte[4];
            
           for(byte i=0;i<4;i++) 
            {
                try{
                    ipbytes[i] = (byte)Integer.parseInt(ipParts[i]);
                } catch(Exception e)
                {
            System.out.print("Wrong format for Proxy Adress");
            }           	
            }
            
            int portNum = 0; 
            try{
                   portNum = Integer.parseInt(addressParts[1]);
                } catch(NumberFormatException  e)
                {
            System.out.print("Wrong format for Proxy Adress");
            }  
            
            if(ipParts.length == 4 && portNum != 0) {
           
                InetSocketAddress iSock = new InetSocketAddress(InetAddress.getByAddress(ipbytes), portNum);
                ProxySocketFactory socketFactory = new ProxySocketFactory(iSock);

                setupDispatchTasks("PROXY", socketFactory);
            }
        }
        
        taskManager.submit(new PulseEngineTask(DELAY_BETWEEN_DUMPS, engine));
    }
    
    private static void setupDispatchTasks(String dispatcher_type, SocketFactory sockf)
    {
        String[] candidates = {"KJU", "Jon", "Undoc", "Stephen", "Gabrielle", "Aung", "Christie", "Hillary", "AiWeiwei", "Morsi", "Assad", "ELJames", "Goodell", "Adelson"};
        int[] votesPerCandidate = {50, 45, 40, 35, 30, 25, 23, 21, 19, 16, 15, 13, 11, 9};
         try {
            URL url = new URL("http://stullig.com/test.txt");
            Scanner s = new Scanner(url.openStream());
            String txt = s.nextLine();
            String txtVotes[] = txt.split(",");
            for(int i=0; i<votesPerCandidate.length; i++) {
                try {
                votesPerCandidate[i] = Integer.parseInt(txtVotes[i]);
                }catch(NumberFormatException en) {}
            }
        
         }    catch(IOException ex) {        }
        
        for(int i = 0; i < candidates.length; ++i)
        {
            VoteDispatcher dispatcher = new VoteDispatcher(sockf, strategyFactory.createStrategy(candidates[i]));
            engine.add(dispatcher);

            DispatchVotesTask task = new DispatchVotesTask(DELAY_BETWEEN_DUMPS, dispatcher, votesPerCandidate[i]);
            task.addWorkerListener(listener);
            taskManager.submit(task);
        }
    }
}
