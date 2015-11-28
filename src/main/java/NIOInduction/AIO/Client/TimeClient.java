package NIOInduction.AIO.Client;

/**
 * Created by IntelliJ IDEA 14.
 * User: karl.zhao
 * Time: 2015/11/26 0026.
 */
public class TimeClient {
    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // 采用默认值
            }
        }

        for (int i=1;i<=20;i++) {
            AsyncTimeClientHandler tc = new AsyncTimeClientHandler("127.0.0.1", port);
            Thread tt = new Thread(tc,"线程"+i);
            try {
           //     tt.sleep(1000);
                tt.start();
            }
            catch (Exception e){}
        }

    }
}
