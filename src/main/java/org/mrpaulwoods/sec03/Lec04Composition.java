package org.mrpaulwoods.sec03;

import org.mrpaulwoods.models.sec03.Address;
import org.mrpaulwoods.models.sec03.School;
import org.mrpaulwoods.models.sec03.Student;
import org.slf4j.Logger;

public class Lec04Composition {
    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec04Composition.class);

    static void main(String[] ignoredArgs) {

        var address = Address.newBuilder()
                .setStreet("123 main st")
                .setCity("Atlanta")
                .setState("GA")
                .build();

        var student = Student.newBuilder()
                .setName("sam")
                .setAddress(address)
                .build();

        var school = School.newBuilder()
                .setId(1)
                .setName("high school")
                .setAddress(address.toBuilder().setStreet("234 main st"))
                .build();

        log.info("student {}", student);
        log.info("school {}", school);
    }

}
