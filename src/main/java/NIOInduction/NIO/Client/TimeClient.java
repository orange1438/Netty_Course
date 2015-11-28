package NIOInduction.NIO.Client;

/**
 * Created by IntelliJ IDEA 14.
 * User: karl.zhao
 * Time: 2015/11/25 0025.
 */
public class TimeClient {
    public static void main(String[]agrs){
        int port = 8080;
        String hostAddr = "127.0.0.1";

        if(agrs!=null && agrs.length==1){
            try {
                port = Integer.valueOf(agrs[0]);
            }
            catch (NumberFormatException ex){}
        }else if(agrs!=null && agrs.length==2){
            try {
                hostAddr=agrs[0];
                port = Integer.valueOf(agrs[1]);
            }
            catch (NumberFormatException ex){}
        }

        for (int i=1;i<=1000;i++) {
            TimeClientHandle tc = new TimeClientHandle(hostAddr, port);
            Thread tt = new Thread(tc,"线程"+i);
            try {
             //   tt.sleep(1000);
                tt.start();
            }
            catch (Exception e){}
        }
    }
}
