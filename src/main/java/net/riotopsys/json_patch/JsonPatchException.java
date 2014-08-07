package net.riotopsys.json_patch;

/**
 * Created by afitzgerald on 8/5/14.
 */
public class JsonPatchException extends RuntimeException {

    public JsonPatchException() {
    }

    public JsonPatchException(String message) {
        super(message);
    }


    public JsonPatchException(Throwable cause) {
        super(cause);
    }
}
