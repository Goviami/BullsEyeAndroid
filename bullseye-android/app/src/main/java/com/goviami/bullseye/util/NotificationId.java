package com.goviami.bullseye.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by subbu on 11/14/2014.
 */
public class NotificationId {
    private final static AtomicInteger i = new AtomicInteger(0);

    public static int getId() {
        return i.incrementAndGet();
    }
}
