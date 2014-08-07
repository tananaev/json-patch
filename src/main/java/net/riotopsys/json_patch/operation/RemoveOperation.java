package net.riotopsys.json_patch.operation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.riotopsys.json_patch.JsonPath;

import java.util.List;
import java.util.Map;

/**
 * Created by afitzgerald on 8/5/14.
 */
public class RemoveOperation extends AbsOperation{

    public RemoveOperation( JsonPath path ) {
        this.path = path;
    }

    @Override
    public String getOperationName() {
        return "remove";
    }

    @Override
    public JsonElement apply(JsonElement original) {
        JsonElement result = duplicate( original );

        JsonElement item = path.head().navigate(result);

        if ( item.isJsonObject() ){
            Map<String, JsonElement> map = getBackingMap(item.getAsJsonObject());

            map.remove(path.tail());

        } else if ( item.isJsonArray() ){

            JsonArray array = item.getAsJsonArray();

            int index = (path.tail().equals("-")) ? array.size() : Integer.valueOf(path.tail());

            List<JsonElement> list = getBackingList( array );
            list.remove(index);

        }

        return result;
    }
}
