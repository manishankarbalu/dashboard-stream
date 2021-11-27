package com.stream.dashboard;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.ThreadLocalRandom;

public class Utils {
    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }

    public static void simulateDelay(int min, int max) {
        try {
            int delay = ThreadLocalRandom.current().nextInt(min, max + 1);
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            //pass
        }
    }
}
