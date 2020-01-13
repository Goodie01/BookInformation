package org.goodiemania.books.services.misc;

import java.util.function.Supplier;

public class TimerService {
    /**
     * Times the given supplier.
     *
     * @param supplier Supplier to time
     * @param <T>      Type of response
     * @return Returns a response containing the supplied object and how long it took
     */
    public <T> TimerResponse<T> time(Supplier<T> supplier) {
        long start = System.currentTimeMillis();
        T t = supplier.get();
        long l = System.currentTimeMillis() - start;
        return new TimerResponse<T>(t, l);
    }

}
