package org.mrpaulwoods.sec05.parser;

import com.google.protobuf.InvalidProtocolBufferException;
import org.mrpaulwoods.models.sec05.v2.Television;
import org.slf4j.Logger;

public class V2Parser {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(V2Parser.class);

    public static void parse(byte[] bytes) throws InvalidProtocolBufferException {
        var tv = Television.parseFrom(bytes);
        log.info("brand: {}", tv.getBrand());
        log.info("model: {}", tv.getModel());
        log.info("type: {}", tv.getType());
    }

}
