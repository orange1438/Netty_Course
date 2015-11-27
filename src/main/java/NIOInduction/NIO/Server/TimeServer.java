package NIO.Server;

import java.io.IOException;
import java.nio.channels.MulticastChannel;

/**
 * Created by IntelliJ IDEA 14.
 * User: karl.zhao
 * Time: 2015/11/25 0025.
 */
public class TimeServer {

    public static void main(String[]args)throws IOException{
        int port = 8080;
        if(args!=null && args.length>0){
            try {
                port = Integer.valueOf(args[0]);
            }
            catch (NumberFormatException ex){}
        }

        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
        new Thread(timeServer,"NIO-MultiplexerTimeServer-001").start();
    }
}
