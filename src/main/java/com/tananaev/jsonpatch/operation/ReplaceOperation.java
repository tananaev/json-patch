package com.tananaev.jsonpatch.operation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.tananaev.jsonpatch.JsonPath;

public class ReplaceOperation extends AbsOperation {

    @SerializedName("value")
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

        JsonElement item = path.head().navigate(original);

        if ( item.isJsonObject() ){
            JsonObject object = item.getAsJsonObject();

            object.add( path.tail(), data );;

        } else if ( item.isJsonArray() ){

            JsonArray array = item.getAsJsonArray();

            int index = (path.tail().equals("-")) ? array.size() : Integer.valueOf(path.tail());

            if ( index < array.size() ) {
                array.set(index, data);
            } else {
                array.add(data);
            }

        } else {
            return data;
        }

        return original;
    }

}
