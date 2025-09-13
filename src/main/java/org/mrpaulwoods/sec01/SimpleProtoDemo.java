package org.mrpaulwoods.sec01;

import org.mrpaulwoods.models.PersonOuterClass;
import org.slf4j.Logger;

public class SimpleProtoDemo {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(SimpleProtoDemo.class);

    public static void main(String[] args) {
        log.info("Hello World!");

        PersonOuterClass.Person person = PersonOuterClass.Person.newBuilder()
                .setName("sam")
                .setAge(12)
                .build();

        log.info("{}", person);

    }

}
