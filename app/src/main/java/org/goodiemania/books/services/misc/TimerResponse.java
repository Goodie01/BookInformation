package org.goodiemania.books.services.misc;

public class TimerResponse<T> {
    private T response;
    private long time;

    TimerResponse(final T response, final long time) {
        this.response = response;
        this.time = time;
    }

    public T getResponse() {
        return response;
    }

    public long getTime() {
        return time;
    }
}
