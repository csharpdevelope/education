package uz.example.flower.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonRootName("result")
public final class JSend<T> {

    public enum Status {
        SUCCESS("success"), ERROR("error"), NOTFOUND("not found"), FAIL("fail");

        private String value;

        Status(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        public boolean isSuccess() {
            return equals(SUCCESS);
        }

        public boolean isNotFound() {
            return equals(NOTFOUND);
        }

        public boolean isError() {
            return equals(ERROR);
        }

        public boolean isFail() {
            return equals(FAIL);
        }
    }

    @JsonProperty
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private final Status status;

    @JsonProperty
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private int code;

    @JsonProperty
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String message;

    @JsonProperty
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private T data;

    public JSend(Status status, int code, String message, T data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public JSend(Status status, T data) {
        this.status = status;
        this.data = data;
    }
    public JSend(Status status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public static <E> JSend<E> success(Status status, int code, E data) {
        return new JSend<>(status, code, null, data);
    }

    public static <E> JSend<E> success() {
        return success(null);
    }

    public static <E> JSend<E> success(E data) {
        return success(Status.SUCCESS, 200, data);
    }

    public static <E> JSend<E> notFound(int code, String message) {
        return new JSend<>(Status.NOTFOUND, code, message);
    }

    public static <E> JSend<E> notFound(int code, E data) {
        return new JSend<>(Status.NOTFOUND, code, null, data);
    }

    public static <E> JSend<E> notFound(String message) {
        return notFound(404, message);
    }

    public static <E> JSend<E> fail(String message) {
        return fail(501, message);
    }

    public static <E> JSend<E> badRequest(String message) {
        return badRequest(400, message);
    }

    public static <E> JSend<E> badRequest(int code, String message) {
        return new JSend<>(Status.FAIL, code, message);
    }

    public static <E> JSend<E> fail(int code, String message) {
        return new JSend<>(Status.FAIL, code, message);
    }

    public Status getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
