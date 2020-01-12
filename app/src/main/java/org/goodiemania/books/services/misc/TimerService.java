package org.goodiemania.books.services.misc;

import java.util.function.Supplier;

public class TimerService {
    public <T> TimerResponse<T> time(Supplier<T> supplier) {
        long start = System.currentTimeMillis();
        T t = supplier.get();
        long l = System.currentTimeMillis() - start;
        return new TimerResponse<T>(t, l);
    }

}
