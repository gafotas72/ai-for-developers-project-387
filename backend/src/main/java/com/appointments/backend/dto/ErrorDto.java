package com.appointments.backend.dto;

/**
 * Error DTO.
 */
public class ErrorDto {
    private Integer code;
    private String message;

    /**
     * Gets code.
     *
     * @return code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(final Integer code) {
        this.code = code;
    }

    /**
     * Gets message.
     *
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(final String message) {
        this.message = message;
    }
}