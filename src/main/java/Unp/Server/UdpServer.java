package Unp.Server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * Created by IntelliJ IDEA 14.
 * User: karl.zhao
 * Time: 2016/1/21 0021.
 */
public class UdpServer {

    // 相比于TCP而言，UDP不存在客户端和服务端的实际链接，因此
    // 不需要为连接(ChannelPipeline)设置handler
    public void run(int port)throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST,true)
                    .handler(new UdpServerHandler());

            b.bind(port).sync().channel().closeFuture().await();
        }
        finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception{
        int port = 8080;
        if(args.length>0){
            try{
                port = Integer.parseInt(args[0]);
            }
            catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
        new UdpServer().run(port);
    }
}
