package NIOInduction.AIO.Server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;

/**
 * Created by IntelliJ IDEA 14.
 * User: karl.zhao
 * Time: 2015/11/26 0026.
 */
public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {
    private AsynchronousSocketChannel channel;

    public ReadCompletionHandler(AsynchronousSocketChannel channel){
        this.channel=channel;
    }

    @Override
    public  void completed(Integer result,ByteBuffer attachment){
        attachment.flip();
        byte[]body = new byte[attachment.remaining()];
        attachment.get(body);
        try {
            String req = new String(body,"UTF-8");
            System.out.println("the time server seceive order : "+req);
            String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(req)? new Date(
                    System.currentTimeMillis()
            ).toString():"BAD ORDER";
            doWrite(currentTime);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    private void doWrite(String currentTime){
        if(currentTime != null&&currentTime.trim().length()>0){
            byte[]bytes=(currentTime).getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            channel.write(writeBuffer, writeBuffer,
                    new CompletionHandler<Integer, ByteBuffer>() {
                        public void completed(Integer result, ByteBuffer attachment) {
                            // 如果没有发送完成，则继续发送
                            if(attachment.hasRemaining())
                                channel.write(attachment,attachment,this);
                        }

                        public void failed(Throwable exc, ByteBuffer attachment) {
                            try {
                                channel.close();
                            }catch (IOException e){}
                        }
                    });
        }
    }

    @Override
    public void failed(Throwable exc,ByteBuffer attachment){
        try {
            this.channel.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
