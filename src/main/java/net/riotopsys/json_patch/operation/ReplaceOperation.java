package net.riotopsys.json_patch.operation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import net.riotopsys.json_patch.JsonPath;

import java.util.List;
import java.util.Map;

/**
 * Created by afitzgerald on 8/5/14.
 */
public class ReplaceOperation extends AbsOperation {

    public JsonElement data;

    public ReplaceOperation(JsonPath path, JsonElement data) {
        this.path = path;
        this.data = data;
    }

    @Override
    public String getOperationName() {
        return "replace";
    }

    @Override
    public JsonElement apply(JsonElement original) {
        JsonElement result = duplicate( original );

        JsonElement item = path.head().navigate(result);

        if ( item.isJsonObject() ){
            JsonObject object = item.getAsJsonObject();

            object.add( path.tail(), data );;

        } else if ( item.isJsonArray() ){

            JsonArray array = item.getAsJsonArray();

            int index = (path.tail().equals("-")) ? array.size() : Integer.valueOf(path.tail());

            array.remove(index);
            if ( index < array.size() ) {
                array.set(index, data);
            } else {
                array.add(data);
            }

//            List<JsonElement> list = getBackingList( array );
//            list.remove(index);
//            list.add(index, data);

        } else {
            return data;
        }

        return result;
    }

}
