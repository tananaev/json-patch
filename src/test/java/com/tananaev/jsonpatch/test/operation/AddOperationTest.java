package com.tananaev.jsonpatch.test.operation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tananaev.jsonpatch.JsonPath;
import com.tananaev.jsonpatch.operation.AddOperation;
import com.tananaev.jsonpatch.operation.InPlaceElementWrapper;
import org.junit.Assert;
import org.junit.Test;

public class AddOperationTest {

    @Test
    public void basicAddWorks(){
        JsonElement element = new JsonObject();
        String originalData = element.toString();

        JsonElement result = new AddOperation(new JsonPath("/a"), new JsonObject() ).apply(element);

        Assert.assertEquals(originalData, element.toString());
        Assert.assertEquals("{\"a\":{}}", result.toString());
    }

    @Test
    public void basicInPlaceAddWorks(){
        JsonElement element = new JsonObject();
        InPlaceElementWrapper inPlaceElement = new InPlaceElementWrapper(element);
        AddOperation addOperation = new AddOperation(new JsonPath("/a"), new JsonObject());

        addOperation.applyInPlace(inPlaceElement);
        JsonElement result = inPlaceElement.getJsonElement();

        Assert.assertEquals("{\"a\":{}}", result.toString());
    }
}
