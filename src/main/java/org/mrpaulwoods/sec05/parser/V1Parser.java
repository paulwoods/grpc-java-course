package org.mrpaulwoods.sec05.parser;

import com.google.protobuf.InvalidProtocolBufferException;
import org.mrpaulwoods.models.sec05.v1.Television;
import org.slf4j.Logger;

public class V1Parser {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(V1Parser.class);

    public static void parse(byte[] bytes) throws InvalidProtocolBufferException {
        var tv = Television.parseFrom(bytes);
        log.info("brand: {}", tv.getBrand());
        log.info("year: {}", tv.getYear());


    }

}
