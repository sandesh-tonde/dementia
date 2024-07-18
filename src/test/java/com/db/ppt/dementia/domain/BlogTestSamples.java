package com.db.ppt.dementia.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BlogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Blog getBlogSample1() {
        return new Blog().id(1L).content("content1").vote(1);
    }

    public static Blog getBlogSample2() {
        return new Blog().id(2L).content("content2").vote(2);
    }

    public static Blog getBlogRandomSampleGenerator() {
        return new Blog().id(longCount.incrementAndGet()).content(UUID.randomUUID().toString()).vote(intCount.incrementAndGet());
    }
}
