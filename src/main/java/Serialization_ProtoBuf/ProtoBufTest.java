package Serialization_ProtoBuf;

import Serialization_ProtoBuf.ProtoBuf.PersonProbuf;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * Created by IntelliJ IDEA 14.
 * User: karl.zhao
 * Time: 2015/12/25 0025.
 */
public class ProtoBufTest {

    public static void main(String[] args) {
        PersonProbuf.Person.Builder builder = PersonProbuf.Person.newBuilder();

        builder.setId(1);
        builder.setName("Karl");
        builder.setSex("boy");
        builder.setTel("110");

        PersonProbuf.Person person = builder.build();
        byte[] buf = person.toByteArray();

        try {
            PersonProbuf.Person person2 = PersonProbuf.Person.parseFrom(buf);
            System.out.println(person2.getName() + ", " + person2.getTel());

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        System.out.println(buf.toString());

    }
}
