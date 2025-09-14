package org.mrpaulwoods.sec03;

import org.mrpaulwoods.models.sec03.Person;
import org.slf4j.Logger;

public class Lec01Scalar {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec01Scalar.class);

    public static void main(String[] args) {
        Person person = Person.newBuilder()
                .setLastName("sam")
                .setAge(12)
                .setEmail("sam@example.com")
                .setEmployed(true)
                .setSalary(1000.2345)
                .setBankAccountNumber(123456789012L)
                .setBalance(-10000)
                .build();

        log.info("{}", person);
    }

}
