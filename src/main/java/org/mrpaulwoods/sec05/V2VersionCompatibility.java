package org.mrpaulwoods.sec05;

import com.google.protobuf.InvalidProtocolBufferException;
import org.mrpaulwoods.models.sec05.v2.Television;
import org.mrpaulwoods.models.sec05.v2.Type;
import org.mrpaulwoods.sec05.parser.V1Parser;
import org.mrpaulwoods.sec05.parser.V2Parser;
import org.mrpaulwoods.sec05.parser.V3Parser;

public class V2VersionCompatibility {

    static void main(String[] ignoredArgs) throws InvalidProtocolBufferException {

        var tv = Television.newBuilder()
                .setBrand("samsung")
                .setModel(2019)
                .setType(Type.UHD)
                .build();

        // you can change the label anytime, as long as you don't
        // change the type or index.
        V1Parser.parse(tv.toByteArray());
        V2Parser.parse(tv.toByteArray());
        V3Parser.parse(tv.toByteArray());
    }

}
