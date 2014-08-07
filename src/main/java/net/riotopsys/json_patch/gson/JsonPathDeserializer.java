package net.riotopsys.json_patch.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.riotopsys.json_patch.JsonPath;

import java.lang.reflect.Type;

/**
 * Created by afitzgerald on 8/4/14.
 */
public class JsonPathDeserializer implements JsonDeserializer<JsonPath> {
    @Override
    public JsonPath deserialize(JsonElement element, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new JsonPath( element.getAsJsonPrimitive().getAsString());
    }
}
