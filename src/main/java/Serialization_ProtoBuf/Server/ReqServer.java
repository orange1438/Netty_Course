package Serialization_ProtoBuf.Server;

import Serialization_ProtoBuf.ProtoBuf.PersonProbuf;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * Created by IntelliJ IDEA 14.
 * User: karl.zhao
 * Time: 2015/12/25 0025.
 */
public class ReqServer {

    public void bind(int port)throws Exception{
        /* 配置服务端的NIO线程组 */
        // NioEventLoopGroup类 是个线程组，包含一组NIO线程，用于网络事件的处理
        // （实际上它就是Reactor线程组）。
        // 创建的2个线程组，1个是服务端接收客户端的连接，另一个是进行SocketChannel的
        // 网络读写
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup WorkerGroup = new NioEventLoopGroup();

        try {
            // ServerBootstrap 类，是启动NIO服务器的辅助启动类
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,WorkerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch){
                            // 半包的处理
                            ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                            // 需要解码的目标类
                            ch.pipeline().addLast(new ProtobufDecoder(PersonProbuf.Person.getDefaultInstance()));

                            ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());

                            ch.pipeline().addLast(new ProtobufEncoder());

                            ch.pipeline().addLast(new ReqServerHandler());
                        }
                    });

            // 绑定端口,同步等待成功
            ChannelFuture f= b.bind(port).sync();

            // 等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        }finally {
            // 释放线程池资源
            bossGroup.shutdownGracefully();
            WorkerGroup.shutdownGracefully();
        }
    }

    public static void main(String[]args)throws Exception{
        int port = 8080;
        if(args!=null && args.length>0){
            try {
                port = Integer.valueOf(args[0]);
            }
            catch (NumberFormatException ex){}
        }
        new ReqServer().bind(port);
    }

}
