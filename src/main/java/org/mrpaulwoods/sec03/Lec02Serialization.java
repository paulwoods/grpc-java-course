package org.mrpaulwoods.sec03;

import org.mrpaulwoods.models.sec03.Person;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Lec02Serialization {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec02Serialization.class);
public static final Path PATH = Path.of("person.out");

    public static void main(String[] args) throws IOException {
        Person person = Person.newBuilder()
                .setLastName("sam")
                .setAge(12)
                .setEmail("sam@gmail.com")
                .setEmployed(true)
                .setSalary(1000.2345)
                .setBankAccountNumber(123456789012L)
                .setBalance(-10000)
                .build();

        serialize(person);
        log.info("{}", deserialize());
        log.info("equals {}", person.equals(deserialize()));
        log.info("bytes length: {}", person.toByteArray().length);
    }

    public static void serialize(Person person) throws IOException {
        person.writeTo(Files.newOutputStream(PATH));
    }

    public static Person deserialize() throws IOException {
        return Person.parseFrom(Files.newInputStream(PATH));
    }

}
