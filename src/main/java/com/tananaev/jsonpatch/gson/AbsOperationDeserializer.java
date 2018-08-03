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

package com.tananaev.jsonpatch.gson;

import com.google.gson.*;
import com.tananaev.jsonpatch.operation.*;

import java.lang.reflect.Type;

public class AbsOperationDeserializer implements JsonDeserializer<AbsOperation> {

    @Override
    public AbsOperation deserialize(JsonElement element, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String operation = element.getAsJsonObject().getAsJsonPrimitive("op").getAsString();
        return jsonDeserializationContext.deserialize(element, getType(operation));
    }

    private Type getType(String operation) throws JsonSyntaxException {
        if ("add".equals(operation)) return AddOperation.class;
        if ("move".equals(operation)) return MoveOperation.class;
        if ("remove".equals(operation)) return RemoveOperation.class;
        if ("replace".equals(operation)) return ReplaceOperation.class;
        throw new JsonSyntaxException("operation " + operation + " not supported");
    }

}
