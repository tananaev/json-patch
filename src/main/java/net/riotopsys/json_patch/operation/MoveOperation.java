package net.riotopsys.json_patch.operation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.riotopsys.json_patch.JsonPath;

import java.util.List;
import java.util.Map;

/**
 * Created by afitzgerald on 8/5/14.
 */
public class MoveOperation extends AbsOperation{

    private final JsonPath from;

    public MoveOperation(JsonPath from, JsonPath path) {
        this.path = path;
        this.from = from;
    }

    @Override
    public String getOperationName() {
        return "move";
    }

    @Override
    public JsonElement apply(JsonElement original) {
        JsonElement result = duplicate( original );

        JsonElement value = from.navigate(result);

        JsonElement source = from.head().navigate(result);
        JsonElement destination = path.head().navigate(result);


        //remove from source
        if ( source.isJsonObject() ){
            Map<String, JsonElement> map = getBackingMap(source.getAsJsonObject());

            map.remove(from.tail());

        } else if ( source.isJsonArray() ){
            JsonArray array = source.getAsJsonArray();

            int index = (from.tail().equals("-")) ? array.size() : Integer.valueOf(from.tail());

            List<JsonElement> list = getBackingList( array );
            list.remove(index);
        }

        //add to destination
        if ( destination.isJsonObject() ){
            destination.getAsJsonObject().add(path.tail(),value);
        } else if ( destination.isJsonArray() ){

            JsonArray array = destination.getAsJsonArray();

            int index = (path.tail().equals("-")) ? array.size() : Integer.valueOf(path.tail());

            List<JsonElement> list = getBackingList( array );
            list.add(index, value);
        }

        return result;
    }
}
