package org.mrpaulwoods.sec03;

import org.mrpaulwoods.models.sec03.BodyStyle;
import org.mrpaulwoods.models.sec03.Car;
import org.mrpaulwoods.models.sec03.Dealer;
import org.slf4j.Logger;

public class Lec06Map {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec06Map.class);

    static void main(String[] ignoredArgs) {

        var car1 = Car.newBuilder()
                .setMake("Honda")
                .setModel("Civic")
                .setYear(2000)
                .setBodyStyle(BodyStyle.COUPE)
                .build();

        var car2 = Car.newBuilder()
                .setMake("Honda")
                .setModel("Accord")
                .setYear(2002)
                .setBodyStyle(BodyStyle.SEDAN)
                .build();

        var dealer = Dealer.newBuilder()
                .putInventory(car1.getYear(), car1)
                .putInventory(car2.getYear(), car2)
                .build();

        log.info("dealer {}", dealer);

        log.info("2002 ? {}", dealer.containsInventory(2002));
        log.info("2003 ? {}", dealer.containsInventory(2003));
        log.info("2002 model: {}", dealer.getInventoryOrThrow(2002));
        log.info("car2 body style: {}", car2.getBodyStyle());
    }

}
