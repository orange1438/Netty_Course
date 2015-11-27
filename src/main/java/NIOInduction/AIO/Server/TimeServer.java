package AIO.Server;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA 14.
 * User: karl.zhao
 * Time: 2015/11/26 0026.
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

        AsyneTimeServerHandler timeServer=new AsyneTimeServerHandler(port);
        new Thread(timeServer,"xxxx").start();
    }
}
