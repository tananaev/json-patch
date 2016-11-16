package net.riotopsys.json_patch.operation;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import net.riotopsys.json_patch.JsonPath;

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
        return new JsonParser().parse(original.toString());
    }

}
