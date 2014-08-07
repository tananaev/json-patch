package net.riotopsys.json_patch.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.riotopsys.json_patch.JsonPath;

import java.lang.reflect.Type;

/**
 * Created by afitzgerald on 8/4/14.
 */
public class JsonPathSerializer implements JsonSerializer<JsonPath> {

    @Override
    public JsonElement serialize(JsonPath jsonPath, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(jsonPath.toString());
    }
}
