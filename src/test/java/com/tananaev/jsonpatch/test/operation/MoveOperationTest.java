package com.tananaev.jsonpatch.test.operation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.tananaev.jsonpatch.JsonPath;
import com.tananaev.jsonpatch.operation.AddOperation;
import com.tananaev.jsonpatch.operation.InPlaceElementWrapper;
import com.tananaev.jsonpatch.operation.MoveOperation;
import org.junit.Assert;
import org.junit.Test;

public class MoveOperationTest {

    @Test
    public void basicMoveWorks(){
        JsonElement element = new JsonObject();
        element.getAsJsonObject().add("a", new JsonPrimitive("123"));
        String originalData = element.toString();

        JsonElement result = new MoveOperation(new JsonPath("/a"), new JsonPath("/b") ).apply(element);

        Assert.assertEquals(originalData, element.toString());
        Assert.assertEquals("{\"b\":\"123\"}", result.toString());
    }

    @Test
    public void basicInPlaceMoveWorks(){

        JsonElement element = new JsonObject();
        element.getAsJsonObject().add("a", new JsonPrimitive("123"));
        InPlaceElementWrapper inPlaceElement = new InPlaceElementWrapper(element);

        MoveOperation moveOperation = new MoveOperation(new JsonPath("/a"), new JsonPath("/b") );
        moveOperation.applyInPlace(inPlaceElement);

        Assert.assertEquals("{\"b\":\"123\"}", inPlaceElement.getJsonElement().toString());

    }
}
