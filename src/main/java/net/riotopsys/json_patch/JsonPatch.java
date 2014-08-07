package net.riotopsys.json_patch;

import com.google.gson.JsonElement;
import net.riotopsys.json_patch.operation.AbsOperation;

import java.util.LinkedList;

/**
 * Created by afitzgerald on 8/4/14.
 */
public class JsonPatch extends LinkedList<AbsOperation> {

    public JsonElement apply(JsonElement original) {
        JsonElement result = original;
        for ( AbsOperation operation: this){
            result = operation.apply(result);
        }
        return result;
    }

}
