package Serialization_ProtoBuf.Client;

import Serialization_ProtoBuf.ProtoBuf.PersonProbuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by IntelliJ IDEA 14.
 * User: karl.zhao
 * Time: 2015/12/28 0028.
 */
public class ReqClientHandler extends ChannelHandlerAdapter {
    public ReqClientHandler() {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        for (int i = 0; i < 2; i++) {
            ctx.write(PReq(i));
        }
        ctx.flush();
    }

    private PersonProbuf.Person PReq(int id) {
        PersonProbuf.Person.Builder builder = PersonProbuf.Person.newBuilder();
        builder.setId(id);
        builder.setName("orange");
        builder.setSex("man");
        builder.setTel("999");

        return builder.build();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("receive server response:[" + msg + "]");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
