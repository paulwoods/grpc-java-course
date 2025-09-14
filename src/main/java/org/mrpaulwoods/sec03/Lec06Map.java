package org.mrpaulwoods.sec03;

import org.mrpaulwoods.models.sec03.Car;
import org.mrpaulwoods.models.sec03.Dealer;
import org.slf4j.Logger;

public class Lec06Map {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec06Map.class);

    public static void main(String[] args) {

        var car1 = Car.newBuilder()
                .setMake("Honda")
                .setModel("Civic")
                .setYear(2000)
                .build();

        var car2 = Car.newBuilder()
                .setMake("Honda")
                .setModel("Accord")
                .setYear(2002)
                .build();

        var dealer = Dealer.newBuilder()
                .putInventory(car1.getYear(), car1)
                .putInventory(car2.getYear(), car2)
                .build();

        log.info("dealer {}", dealer);
    }

}
