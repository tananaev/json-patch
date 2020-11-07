package com.tananaev.jsonpatch.operation;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import com.tananaev.jsonpatch.JsonPath;

public abstract class AbsOperation {

    @SerializedName("op")
    private String operationName;

    public JsonPath path;

    public AbsOperation(){
        operationName = getOperationName();
    }

    public abstract String getOperationName();

    /**
     * Applies the operation on the source and returns the patched object. The source is unmodified.
     * @param sourceElement to which the patch is to be applied
     * @return final json after patch is applied
     */
    public JsonElement apply( JsonElement sourceElement ){
        JsonElement copiedSource = sourceElement.deepCopy();
        InPlaceElementWrapper inPlaceElement = new InPlaceElementWrapper(copiedSource);
        applyInPlace(inPlaceElement);
        return inPlaceElement.getJsonElement();
    };

    /**
     * An optimised version of apply where the source is not copied and is directly modified if possible.
     * Updates the inPlaceElement to contain the patched json element.
     * Refer to {@link InPlaceElementWrapper} for details on why a wrapper is needed
     * @param inPlaceElement input to which the patch is to be applied
     */
    public abstract void applyInPlace(InPlaceElementWrapper inPlaceElement );

}
