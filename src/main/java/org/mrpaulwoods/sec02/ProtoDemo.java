package org.mrpaulwoods.sec02;

import org.mrpaulwoods.models.sec02.Person;
import org.slf4j.Logger;

public class ProtoDemo {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(ProtoDemo.class);

    static void main(String[] ignoredArgs) {

        // create person1
        Person person1 = createPerson();

        // create another instance with some values
        Person person2 = createPerson();

        // compare
        log.info("equals {}", person1.equals(person2));
        log.info("== {}", person1 == person2);

        // mutable? No
        // person1.setName("mike");

        // create another instance with diff values
        Person person3 = person1.toBuilder().setName("mike").build();

        // compare
        log.info("equals {}", person1.equals(person3));
        log.info("== {}", person1 == person3);

        // null? No - throws NPE
        // Person person4 = person1.toBuilder().setName(null).build();

        // use clearX instead
        Person person4 = person1.toBuilder().clearName().build();
        log.info("person4 {}", person4);
        //noinspection ConstantValue
        log.info("person4 name null {}", person4.getName() == null);
        log.info("person4 name blank {}", person4.getName().isBlank());
        log.info("person4 name empty {}", person4.getName().isEmpty());


    }

    private static Person createPerson() {
        return Person.newBuilder()
                .setName("sam")
                .setAge(12)
                .build();
    }

}
