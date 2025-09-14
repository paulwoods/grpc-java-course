package org.mrpaulwoods.sec02;

import org.mrpaulwoods.models.sec02.Person;
import org.slf4j.Logger;

public class ProtoDemo {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(ProtoDemo.class);

    public static void main(String[] args) {

        Person person = Person.newBuilder()
                .setName("sam")
                .setAge(12)
                .build();

        log.info("{}", person);
    }
}
