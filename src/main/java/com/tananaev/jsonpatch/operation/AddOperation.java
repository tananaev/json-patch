package com.tananaev.jsonpatch.operation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import com.tananaev.jsonpatch.JsonPath;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AddOperation extends AbsOperation {

    @SerializedName("value")
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

        JsonElement item = path.head().navigate(original);

        if ( item.isJsonObject() ){
            item.getAsJsonObject().add(path.tail(),data);
        } else if ( item.isJsonArray() ){

            JsonArray array = item.getAsJsonArray();

            int index = (path.tail().equals("-")) ? array.size() : Integer.valueOf(path.tail());

            List<JsonElement> temp = new ArrayList<JsonElement>();

            Iterator<JsonElement> iter = array.iterator();
            while (iter.hasNext()){
                JsonElement stuff = iter.next();
                iter.remove();
                temp.add( stuff );
            }

            temp.add(index, data);

            for ( JsonElement stuff: temp ){
               array.add(stuff);
            }

        }

        return original;
    }

}
