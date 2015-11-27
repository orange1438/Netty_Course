package AIO.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * Created by IntelliJ IDEA 14.
 * User: karl.zhao
 * Time: 2015/11/26 0026.
 */
public class AsyneTimeServerHandler implements Runnable {
    private int port;
    CountDownLatch latch;/* 一个同步辅助类，在完成一组正在其他线程中执行的操作之前，它允许一个或多个线程一直等待。*/
    AsynchronousServerSocketChannel asynchronousServerSocketChannel;

    public AsyneTimeServerHandler(int port){
        this.port=port;
        try{
            asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
            asynchronousServerSocketChannel.bind(new InetSocketAddress(port));

            System.out.println("The Server start in port : " + port);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void run(){
        latch = new CountDownLatch(1);
        doAccept();
        try {
            latch.await();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void doAccept(){
        asynchronousServerSocketChannel.accept(this,new AcceptCompletionHandler());
    }
}
