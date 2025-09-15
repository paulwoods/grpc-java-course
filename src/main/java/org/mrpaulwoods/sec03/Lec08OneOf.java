package org.mrpaulwoods.sec03;

import org.mrpaulwoods.models.sec03.Credentials;
import org.mrpaulwoods.models.sec03.Email;
import org.mrpaulwoods.models.sec03.Phone;
import org.slf4j.Logger;

public class Lec08OneOf {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec08OneOf.class);

    public static void main(String[] args) {
        var email = Email.newBuilder()
                .setAddress("sam@gmail.com")
                .setPassword("admin")
                .build();

        var phone = Phone.newBuilder()
                .setNumber(123456789)
                .setCode(123)
                .build();

        login(Credentials.newBuilder().setEmail(email).build());
        login(Credentials.newBuilder().setPhone(phone).build());

        // last one set wins
        login(Credentials.newBuilder().setEmail(email).setPhone(phone).build());
        login(Credentials.newBuilder().build());

    }

    public static void login(Credentials credentials) {
        switch (credentials.getLoginTypeCase()) {
            case EMAIL -> log.info("email -> {}", credentials.getEmail());
            case PHONE -> log.info("phone -> {}", credentials.getPhone());
            default -> log.info("Unknown login type");
        }
    }
}
