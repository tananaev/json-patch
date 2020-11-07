package com.tananaev.jsonpatch.operation;

import com.google.gson.JsonElement;

/**
 * Wrapper class over json element to pass as an input for in place operations.
 * This is needed because we cannot convert actual JsonElement types into another in place
 * i.e, we cannot convert a JsonPrimitive (or other) into a JsonObject (or other) in-place
 * if needed by the patch operation.
 */
public class InPlaceElementWrapper {

    private JsonElement jsonElement;

    public JsonElement getJsonElement() {
        return jsonElement;
    }

    public InPlaceElementWrapper(JsonElement element) {
        this.jsonElement = element;
    }

    public void setJsonElement(JsonElement jsonElement) {
        this.jsonElement = jsonElement;
    }
}
