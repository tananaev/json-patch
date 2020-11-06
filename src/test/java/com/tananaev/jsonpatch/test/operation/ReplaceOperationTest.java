package com.tananaev.jsonpatch.test.operation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.tananaev.jsonpatch.JsonPath;
import com.tananaev.jsonpatch.operation.InPlaceElementWrapper;
import com.tananaev.jsonpatch.operation.RemoveOperation;
import com.tananaev.jsonpatch.operation.ReplaceOperation;
import org.junit.Assert;
import org.junit.Test;

public class ReplaceOperationTest {
    @Test
    public void basicReplaceWorks(){
        JsonElement element = new JsonObject();
        element.getAsJsonObject().add("a", new JsonPrimitive("123"));
        String originalData = element.toString();

        JsonElement newElement = new JsonPrimitive("1234");

        JsonElement result = new ReplaceOperation(new JsonPath("/a"), newElement).apply(element);

        Assert.assertEquals(originalData, element.toString());
        Assert.assertEquals("{\"a\":\"1234\"}", result.toString());
    }

    @Test
    public void basicInPlaceReplaceWorks(){

        JsonElement element = new JsonObject();
        element.getAsJsonObject().add("a", new JsonPrimitive("123"));

        JsonElement newElement = new JsonPrimitive("1234");
        InPlaceElementWrapper inPlaceElement = new InPlaceElementWrapper(element);

        ReplaceOperation replaceOperation = new ReplaceOperation(new JsonPath("/a"), newElement);
        replaceOperation.applyInPlace(inPlaceElement);

        Assert.assertEquals("{\"a\":\"1234\"}", inPlaceElement.getJsonElement().toString());

    }
}
