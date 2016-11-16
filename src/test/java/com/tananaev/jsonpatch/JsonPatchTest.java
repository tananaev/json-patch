package com.tananaev.jsonpatch;

import com.google.gson.JsonPrimitive;
import com.tananaev.jsonpatch.operation.ReplaceOperation;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonPatchTest {

    @Test
    public void toStringTest() throws Exception {
        JsonPatch patch = new JsonPatch();
        patch.add(new ReplaceOperation(new JsonPath("/object/key"), new JsonPrimitive("test")));
        assertEquals("[{\"value\":\"test\",\"op\":\"replace\",\"path\":\"/object/key\"}]", patch.toString());
    }

}
