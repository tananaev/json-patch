package net.riotopsys.json_patch.operation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.riotopsys.json_patch.JsonPath;

import java.util.List;

/**
 * Created by afitzgerald on 8/5/14.
 */
public class AddOperation extends AbsOperation {

    public JsonElement data;

    public AddOperation(JsonPath path, JsonElement data) {
        this.data = data;
        this.path = path;
    }

    @Override
    public String getOperationName() {
        return "add";
    }

    @Override
    public JsonElement apply(JsonElement original) {
        JsonElement result = duplicate( original );

        JsonElement item = path.head().navigate(result);

        if ( item.isJsonObject() ){
            item.getAsJsonObject().add(path.tail(),data);
        } else if ( item.isJsonArray() ){

            JsonArray array = item.getAsJsonArray();

            int index = (path.tail().equals("-")) ? array.size() : Integer.valueOf(path.tail());

            List<JsonElement> list = getBackingList( array );
            list.add(index, data);

        }

        return result;
    }


}
