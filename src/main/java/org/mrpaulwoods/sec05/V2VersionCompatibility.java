package org.mrpaulwoods.sec05;

import com.google.protobuf.InvalidProtocolBufferException;
import org.mrpaulwoods.models.sec05.v2.Television;
import org.mrpaulwoods.models.sec05.v2.Type;
import org.mrpaulwoods.sec05.parser.V1Parser;
import org.mrpaulwoods.sec05.parser.V2Parser;
import org.slf4j.Logger;

public class V2VersionCompatibility {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(V2VersionCompatibility.class);

    public static void main(String[] args) throws InvalidProtocolBufferException {

        var tv = Television.newBuilder()
                .setBrand("samsung")
                .setModel(2019)
                .setType(Type.UHD)
                .build();

        // you can change the label anytime, as long as you don't
        // change the type or index.
        V1Parser.parse(tv.toByteArray());
        V2Parser.parse(tv.toByteArray());
    }

}
