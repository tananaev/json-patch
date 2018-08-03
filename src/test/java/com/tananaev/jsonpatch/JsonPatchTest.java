package com.tananaev.jsonpatch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.tananaev.jsonpatch.gson.AbsOperationDeserializer;
import com.tananaev.jsonpatch.gson.JsonPathDeserializer;
import com.tananaev.jsonpatch.operation.AbsOperation;
import com.tananaev.jsonpatch.operation.ReplaceOperation;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStreamReader;

import static org.junit.Assert.*;

public class JsonPatchTest {

    @Test
    public void toStringTest() throws Exception {
        JsonPatch patch = new JsonPatch();
        patch.add(new ReplaceOperation(new JsonPath("/object/key"), new JsonPrimitive("test")));
        assertEquals("[{\"value\":\"test\",\"op\":\"replace\",\"path\":\"/object/key\"}]", patch.toString());
    }


    @Test
    public void applySmallTest() throws Exception {
        test("small");
    }

    private void test(String directory) throws Exception {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(JsonPath.class, new JsonPathDeserializer())
                .registerTypeAdapter(AbsOperation.class, new AbsOperationDeserializer())
                .create();

        JsonElement origin = gson.fromJson(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(directory + "/origin.json")), JsonElement.class);
        JsonPatch patch = gson.fromJson(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(directory + "/patch.json")), JsonPatch.class);
        JsonElement reference = gson.fromJson(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(directory + "/result.json")), JsonElement.class);

        JsonElement patched = patch.apply(origin);

        Assert.assertEquals(reference, patched);
    }


}
