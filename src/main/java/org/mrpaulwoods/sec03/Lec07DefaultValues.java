package org.mrpaulwoods.sec03;

import org.mrpaulwoods.models.sec03.*;
import org.slf4j.Logger;

public class Lec07DefaultValues {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec07DefaultValues.class);

    public static void main(String[] args) {

        var school1 = School.newBuilder().build();

        var school2 = School.newBuilder().setAddress(Address.newBuilder().setStreet("main").build()).build();

        log.info("id: {}", school1.getId());
        log.info("name: {}", school1.getName());
        log.info("name length: {}", school1.getName().length());
        log.info("address: {}", school1.getAddress());
        log.info("address city: {}", school1.getAddress().getCity());
        log.info("address city length: {}", school1.getAddress().getCity().length());
        log.info("is default? {}", school1.getAddress().equals(Address.getDefaultInstance()));

        // proto doesn't have null values, so the has methods will tell you if it was set
        log.info("school1 has address? {}", school1.hasAddress());
        log.info("school2 has address? {}", school2.hasAddress());

        // collections
        var library1 = Library.newBuilder().build();
        log.info("library1 books: {}", library1.getBooksList());

        var dealer1 = Dealer.newBuilder().build();
        log.info("dealer1 inventory: {}", dealer1.getInventoryMap());

        // enum - the zero value (sec03/map.proto BodyStyle UNKNOWN=0) is the default
        var car1 = Car.newBuilder().build();
        log.info("car1 body style: {}", car1.getBodyStyle());

   }

}
