package net.riotopsys.json_patch.operation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import net.riotopsys.json_patch.JsonPatchException;
import net.riotopsys.json_patch.JsonPath;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Created by afitzgerald on 8/4/14.
 */
public abstract class AbsOperation {

    @SerializedName("op")
    private String operationName;

    public JsonPath path;

    public AbsOperation(){
        operationName = getOperationName();
    }

    public abstract String getOperationName();

    public abstract JsonElement apply( JsonElement original );

    protected JsonElement duplicate(JsonElement original) {
//        if ( original == null ){
//            return null;
//        }
        return new JsonParser().parse(original.toString());
    }

    protected List<JsonElement> getBackingList(JsonArray array) {
        try {
            Field f = array.getClass().getDeclaredField("elements");
            f.setAccessible(true);
            return (List<JsonElement>) f.get(array);
        } catch (NoSuchFieldException e) {
            throw new JsonPatchException( e );
        } catch (IllegalAccessException e) {
            throw new JsonPatchException( e );
        }
    }

    protected Map<String, JsonElement> getBackingMap(JsonObject object) {
        try {
            Field f = object.getClass().getDeclaredField("members");
            f.setAccessible(true);
            return (Map<String, JsonElement>) f.get(object);
        } catch (NoSuchFieldException e) {
            throw new JsonPatchException( e );
        } catch (IllegalAccessException e) {
            throw new JsonPatchException( e );
        }
    }
}
