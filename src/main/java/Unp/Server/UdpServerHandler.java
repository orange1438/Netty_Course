package Unp.Server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by IntelliJ IDEA 14.
 * User: karl.zhao
 * Time: 2016/1/21 0021.
 */
public class UdpServerHandler
        extends SimpleChannelInboundHandler<DatagramPacket> {
    private static final  String[] DICTIONARY={
            "看我的，hello world",
            "C++",
            "C#",
            "Java",
            "python"
    };

    private String nextQuoto(){
        // 线程安全的随机类：ThreadLocalRandom
        int quoteId = ThreadLocalRandom.current().nextInt(DICTIONARY.length);
        return DICTIONARY[quoteId];
    }

    @Override
    public void messageReceived(ChannelHandlerContext channelHandlerContext,
                                   DatagramPacket datagramPacket) throws Exception {
        // 因为Netty对UDP进行了封装，所以接收到的是DatagramPacket对象。
        String req = datagramPacket.content().toString(CharsetUtil.UTF_8);
        System.out.println(req);

        if("啪啪啪来拉！！！".equals(req)){
            channelHandlerContext.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
                    "结果："+nextQuoto(),CharsetUtil.UTF_8),datagramPacket.sender()));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause)throws Exception{
        ctx.close();
        cause.printStackTrace();
    }
}
