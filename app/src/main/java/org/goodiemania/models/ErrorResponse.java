package org.goodiemania.models;

public class ErrorResponse {
    private String message;

    /**
     * Constructs a new instance of a ErrorResponse object, with the provided error message.
     *
     * @param message The associated error message
     * @return ErrorResponse with the provided message
     */
    public static ErrorResponse of(final String message) {
        final ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(message);

        return errorResponse;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
