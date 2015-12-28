package Serialization_ProtoBuf.Client;

import Serialization_ProtoBuf.ProtoBuf.PersonProbuf;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
/**
 * Created by IntelliJ IDEA 14.
 * User: karl.zhao
 * Time: 2015/12/28 0028.
 */
public class ReqClient {

    public void connect(String host,int port)throws Exception{
        // 配置服务端的NIO线程组
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            // Bootstrap 类，是启动NIO服务器的辅助启动类
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception{
                            //
                            ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                            ch.pipeline().addLast(new ProtobufDecoder(PersonProbuf.Person.getDefaultInstance()));
                            ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                            ch.pipeline().addLast(new ProtobufEncoder());
                            ch.pipeline().addLast(new ReqClientHandler());
                        }
                    });

            // 发起异步连接操作
            ChannelFuture f= b.connect(host,port).sync();

            // 等待客服端链路关闭
            f.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully();
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
        new ReqClient().connect("127.0.0.1",port);
    }
}
