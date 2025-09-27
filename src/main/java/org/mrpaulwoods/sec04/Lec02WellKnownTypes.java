package org.mrpaulwoods.sec04;

import com.google.protobuf.Int32Value;
import com.google.protobuf.Timestamp;
import org.mrpaulwoods.models.sec04.Sample;
import org.slf4j.Logger;

import java.time.Instant;

public class Lec02WellKnownTypes {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec02WellKnownTypes.class);

    static void main(String[] ignoredArgs) {

        var sample = Sample.newBuilder()
                .setAge(Int32Value.of(12))
                .setLoginTime(Timestamp.newBuilder()
                        .setSeconds(Instant.now().getEpochSecond())
                        .build());

        log.info("sample: {}", sample);
        log.info("has age: {}", sample.hasAge());
        log.info("has loginTime: {}", sample.hasLoginTime());

        log.info("time: {}", Instant.ofEpochSecond(sample.getLoginTime().getSeconds()));

    }
}
