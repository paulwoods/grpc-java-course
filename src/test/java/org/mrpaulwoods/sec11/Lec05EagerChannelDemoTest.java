package org.mrpaulwoods.sec11;

import org.junit.jupiter.api.Test;
import org.mrpaulwoods.common.AbstractChannelTest;
import org.slf4j.Logger;

public class Lec05EagerChannelDemoTest extends AbstractChannelTest {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec05EagerChannelDemoTest.class);

    @Test
    public void eagerChannelDemo() {
        log.info("{}", channel.getState(true));
    }
}
