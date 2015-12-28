package Serialization_ProtoBuf.Server;


import Serialization_ProtoBuf.ProtoBuf.PersonProbuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by IntelliJ IDEA 14.
 * User: karl.zhao
 * Time: 2015/12/28 0028.
 */
@Sharable
public class ReqServerHandler extends ChannelHandlerAdapter{
    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg)throws Exception{
        PersonProbuf.Person people  = (PersonProbuf.Person)msg;
        if("Orange".equalsIgnoreCase(people.getName())){
     //   if("Orange".equals(people.getName())){
            System.out.println("accept client people:[" + people.toString() + "]");
            ctx.writeAndFlush(resp(people.getId()));
        }
    }

    private PersonProbuf.Person resp(long peopleID){
        PersonProbuf.Person.Builder builder = PersonProbuf.Person.newBuilder();
        builder.setId(peopleID);
        builder.setName("karl");
        builder.setSex("boy");
        builder.setTel("110");
        return builder.build();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}
