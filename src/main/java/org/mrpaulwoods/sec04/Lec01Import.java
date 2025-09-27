package org.mrpaulwoods.sec04;

import org.mrpaulwoods.models.common.Address;
import org.mrpaulwoods.models.common.BodyStyle;
import org.mrpaulwoods.models.common.Car;
import org.mrpaulwoods.models.sec04.Person;
import org.slf4j.Logger;

public class Lec01Import {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec01Import.class);

    static void main(String[] ignoredArgs) {
        var address = Address.newBuilder()
                .setCity("Atlanta")
                .build();

        var car = Car.newBuilder().setBodyStyle(BodyStyle.COUPE).build();
        var person = Person.newBuilder()
                .setName("sam")
                .setAge(12)
                .setCar(car)
                .setAddress(address)
                .build();

        log.info("address: {}", address);
        log.info("car: {}", car);
        log.info("person: {}", person);
    }
}
