/*
 Copyright 2014 C. A. Fitzgerald

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package com.tananaev.jsonpatch.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.tananaev.jsonpatch.JsonPatchFactory;
import com.tananaev.jsonpatch.JsonPath;
import com.tananaev.jsonpatch.gson.JsonPathDeserializer;
import com.tananaev.jsonpatch.gson.JsonPathSerializer;
import com.tananaev.jsonpatch.test.util.JsonPatchTestCase;
import com.tananaev.jsonpatch.JsonPatch;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@RunWith(Parameterized.class)
public class JsonPatchFactoryTest {

    private final JsonPatchTestCase testCase;
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(JsonPath.class, new JsonPathDeserializer())
            .registerTypeAdapter(JsonPath.class, new JsonPathSerializer())
            .create();

    @Parameters
    public static Collection<JsonPatchTestCase[]> addedNumbers() {
        Gson gson = new Gson();
        List<JsonPatchTestCase> cases = gson.fromJson(
                new BufferedReader(new InputStreamReader(JsonPatchFactoryTest.class.getResourceAsStream("/test_cases.json"))),
                new TypeToken<List<JsonPatchTestCase>>() {}.getType());

        LinkedList<JsonPatchTestCase[]> temp = new LinkedList<JsonPatchTestCase[]>();
        for ( JsonPatchTestCase singleCase: cases ) {
            JsonPatchTestCase[] temp2 = new JsonPatchTestCase[1];
            temp2[0] = singleCase;
            temp.add(temp2);
        }

        return temp;
    }

    public JsonPatchFactoryTest( JsonPatchTestCase testCase ) {
        this.testCase = testCase;
    }

    @Test
    public void runCase(){

        JsonPatchFactory jpf = new JsonPatchFactory();
        JsonPatch patch = jpf.create(testCase.first, testCase.second);

        JsonElement result = patch.apply(testCase.first);

        Assert.assertEquals(testCase.second, result);

        System.out.println(String.format("running ...\nA: '%s'\nB: '%s'\nC: '%s'\npatch\n%s\n--------------\n\n", testCase.first, testCase.second, result, gson.toJson(patch)));
        System.out.flush();
    }

}
