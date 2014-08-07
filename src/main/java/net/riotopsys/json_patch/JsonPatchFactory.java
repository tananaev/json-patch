package net.riotopsys.json_patch;

import com.google.gson.*;
import net.riotopsys.json_patch.operation.AddOperation;
import net.riotopsys.json_patch.operation.MoveOperation;
import net.riotopsys.json_patch.operation.RemoveOperation;
import net.riotopsys.json_patch.operation.ReplaceOperation;

import java.util.*;

/**
 * Created by afitzgerald on 8/4/14.
 */
public class JsonPatchFactory {

    public JsonPatch create( JsonElement elementA, JsonElement elementB ){
        JsonPatch patch = new JsonPatch();

        boolean loop = true;

        while ( loop ) {
            JsonElement temp = patch.apply( elementA );
            loop = processPatch(patch, new JsonPath("/"), temp, elementB);
        }

        return patch;
    }

    private boolean processPatch( JsonPatch patch, JsonPath path, JsonElement elementA, JsonElement elementB ){
        if ( elementA == null ){
            patch.add( new ReplaceOperation(path, elementB));
            return true;
        }

        if ( elementA.equals(elementB) ){
            return false;
        }

        if ( elementA.isJsonArray() && elementB.isJsonArray() ){
            return processArrayPatch( patch, path, elementA.getAsJsonArray(), elementB.getAsJsonArray() );
        } else if ( elementA.isJsonObject() && elementB.isJsonObject() ){
            return processObjectPatch(patch, path, elementA.getAsJsonObject(), elementB.getAsJsonObject());
        } else {
            patch.add( new ReplaceOperation(path, elementB));
            return true;
        }
    }

    private boolean processObjectPatch( JsonPatch patch, JsonPath path, JsonObject elementA, JsonObject elementB ) {

        SortedSet<String> elementAProps = extractProps(elementA);
        SortedSet<String> elementBProps = extractProps(elementB);

        for ( String prop : elementAProps ){
            if ( elementB.has( prop ) ){
                elementBProps.remove(prop);
                JsonElement aValue = elementA.get(prop);
                JsonElement bValue = elementB.get(prop);
                if ( aValue.equals(bValue) ) {
                    // items fully match
                    continue;
                } else {
                    // item exist in both but do not match
                    return processPatch( patch, path.append(prop), aValue, bValue );
                }

            } else {
                //item in original but not in target
                //look for move
                //fuck it
                patch.addLast(new RemoveOperation(path.append(prop)));
                return true;
            }
        }

        if ( elementBProps.size() != 0 ){
            String prop = elementBProps.first();
            //look for copy
            //fuck it
            patch.addLast(new AddOperation(path.append(prop), elementB.get(prop)));
            return true;
        }
        throw new JsonPatchException("theoretically Unreachable");
    }

    private SortedSet<String> extractProps(JsonObject elementA) {
        SortedSet<String> result = new TreeSet<String>();
        for ( Map.Entry<String, JsonElement> entry : elementA.entrySet() ){
            result.add(entry.getKey());
        }
        return result;
    }

    private boolean processArrayPatch( JsonPatch patch, JsonPath path, JsonArray elementA, JsonArray elementB ) {
        int c;
        for ( c=0; c < elementA.size() && c < elementB.size(); c++ ){
            JsonElement valueA = elementA.get(c);
            JsonElement valueB = elementB.get(c);

            if ( !valueA.equals(valueB)) {

                //see if valueB is in later position of elementA and bring it forward
                for ( int b = c+1; b < elementB.size(); b++ ){
                    if ( elementB.get(b).equals(valueA) ){
                        if ( b >= elementA.size() ){
                            break;
                        }
                        patch.add(new MoveOperation(path.append(c),path.append(b)));
                        return true;
                    }
                }

                //see if valueB is in later position of elementA and bring it forward
                for ( int b = c+1; b < elementA.size(); b++ ){
                    if ( elementA.get(b).equals(valueB) ){
                        patch.add(new MoveOperation(path.append(b), path.append(c)));
                        return true;
                    }
                }

//                //see if valueB is in later position of elementA and bring it forward
//                for ( int b = c+1; b < elementB.size(); b++ ){
//                    if ( elementB.get(b).equals(valueA) ){
//                        if ( b >= elementA.size() ){
//                            break;
//                        }
//                        patch.add(new MoveOperation(path.append(c),path.append(b)));
//                        return true;
//                    }
//                }

                processPatch(patch, path.append(c), valueA, valueB);

                return true;
            }
        }
        if(  c < elementB.size()  ){
            patch.add(new AddOperation(path.append("-"), elementB.getAsJsonArray().get(c)));
            return true;
        }
        if ( c < elementA.size() ){
            patch.add(new RemoveOperation(path.append(c)));
            return true;
        }
        throw new JsonPatchException("theoretically Unreachable");
    }


}
