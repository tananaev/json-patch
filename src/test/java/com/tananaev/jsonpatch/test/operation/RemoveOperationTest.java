package com.tananaev.jsonpatch.test.operation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.tananaev.jsonpatch.JsonPath;
import com.tananaev.jsonpatch.operation.InPlaceElementWrapper;
import com.tananaev.jsonpatch.operation.MoveOperation;
import com.tananaev.jsonpatch.operation.RemoveOperation;
import org.junit.Assert;
import org.junit.Test;

public class RemoveOperationTest {

    @Test
    public void basicRemoveWorks(){
        JsonElement element = new JsonObject();
        element.getAsJsonObject().add("a", new JsonPrimitive("123"));
        String originalData = element.toString();

        JsonElement result = new RemoveOperation(new JsonPath("/a") ).apply(element);

        Assert.assertEquals(originalData, element.toString());
        Assert.assertEquals("{}", result.toString());
    }

    @Test
    public void basicInPlaceRemoveWorks(){

        JsonElement element = new JsonObject();
        element.getAsJsonObject().add("a", new JsonPrimitive("123"));
        InPlaceElementWrapper inPlaceElement = new InPlaceElementWrapper(element);

        RemoveOperation removeOperation = new RemoveOperation(new JsonPath("/a"));
        removeOperation.applyInPlace(inPlaceElement);

        Assert.assertEquals("{}", inPlaceElement.getJsonElement().toString());

    }
}
