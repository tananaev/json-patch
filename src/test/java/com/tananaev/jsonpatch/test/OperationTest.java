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

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.tananaev.jsonpatch.JsonPath;
import com.tananaev.jsonpatch.operation.AddOperation;
import com.tananaev.jsonpatch.operation.InPlaceElementWrapper;
import com.tananaev.jsonpatch.operation.ReplaceOperation;
import org.junit.Assert;
import org.junit.Test;

public class OperationTest {


    @Test
    public void replaceRootNullWithPrimitive(){
        JsonElement element = JsonNull.INSTANCE;

        ReplaceOperation operation = new ReplaceOperation( new JsonPath("/"), new JsonPrimitive(1) );

        JsonElement result = operation.apply(element);

        Assert.assertEquals("1", result.toString());
    }

}
