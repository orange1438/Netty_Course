package NIOInduction.NIO.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by IntelliJ IDEA 14.
 * User: karl.zhao
 * Time: 2015/11/25 0025.
 */
public class MultiplexerTimeServer implements Runnable{

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private volatile boolean stop;  //线程在每次使用volatile变量的时候，都会读取变量修改后的最的值。

    public MultiplexerTimeServer(int port){
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port),1024);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("The Server start in port : " + port);
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop(){this.stop=true;}

    public void run(){
        while (!stop){
            try{
                selector.select(1000);
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey>it = selectedKeys.iterator();
                SelectionKey key = null;
                while (it.hasNext()){
                    key = it.next();
                    it.remove();
                    try {
                        handleInput(key);
                    }catch (Exception e){
                        if(key!=null)
                            key.cancel();
                        if(key.channel()!=null){
                            key.channel().close();
                        }
                    }
                }
            }
            catch (Throwable T){
                T.printStackTrace();
            }
        }

        // 多路复用器关闭后，所有注册在上面的Channel和Pipe等资源都会被自动注册并关闭，所以不需要重新释放资源
        if(selector!=null){
            try {
                selector.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key)throws IOException{
        if(key.isValid()){
            // 处理新接入的请求消息
            if(key.isAcceptable()){
                // Accept the new connection
                ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);

                // Add the new connection to the selector
                sc.register(selector,SelectionKey.OP_READ);
            }
            if(key.isReadable()){
                // read data
                SocketChannel sc = (SocketChannel)key.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);
                if(readBytes>0){
                    readBuffer.flip();
                    byte[] bytes=new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes,"UTF-8");
                    System.out.println("recvive order : " + body);

                    String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)?new Date(
                            System.currentTimeMillis()).toString()
                            :"BAD ORDER";
                    doWrite(sc,currentTime);
                }
                else if(readBytes<0){
                    // 对端链路关闭
                    key.cancel();
                    sc.close();
                }
                else ; // 读到0字节，忽略
            }
        }
    }

    private void doWrite(SocketChannel channel,String response)throws IOException{
        if(response!=null&&response.trim().length()>0){
            byte[]bytes = response.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            channel.write(writeBuffer);
        }
    }
}
