/*
 Copyright 2014 C. A. Fitzgerald

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

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

    private LongestCommonSubsequenceFactory lcsf = new LongestCommonSubsequenceFactory();

    public JsonPatch create( JsonElement elementA, JsonElement elementB ){
        JsonPatch patch = new JsonPatch();
        PostProcessor pp = new PostProcessor();

        boolean loop = true;

        while ( loop ) {
            JsonElement temp = patch.apply( elementA );
            loop = processPatch(patch, new JsonPath("/"), temp, elementB);
//            pp.process(patch);
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
                JsonElement aValue = elementA.get(prop);
            if ( elementB.has( prop ) ){
                elementBProps.remove(prop);
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
                for ( String bProp: elementBProps ){
                    if ( elementB.get(bProp).equals(aValue) ){
                        patch.addLast(new MoveOperation(path.append(prop), path.append(bProp)));
                        return true;
                    }
                }

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

    private boolean processArrayPatch( JsonPatch patch, JsonPath path, JsonArray elementA, JsonArray elementB ) {
        List<JsonElement> listA = convertToList(elementA);
        List<JsonElement> listB = convertToList(elementB);

        if ( listA.isEmpty() ){
            patch.add( new AddOperation(path.append("-"),listB.get(0) ));
            return true;
        }

        List<JsonElement> common = lcsf.search(listA, listB);

        int startOfCommonInA = lcsf.findStartIndex(common, listA);
        int startOfCommonInB = lcsf.findStartIndex(common, listB);

        if ( listB.size() == common.size() ){
            if ( startOfCommonInA != 0 ){
                patch.addLast(new RemoveOperation(path.append(0)));
                return true;
            } else {
                patch.addLast(new RemoveOperation(path.append(startOfCommonInA+common.size())));
                return true;
            }
        }

        if ( startOfCommonInB != 0 ){
            int targetPos = startOfCommonInB - 1;
            int targetPosA = startOfCommonInA ;
            return expandCommon(patch, path, listA, listB, common, startOfCommonInA, targetPos, targetPosA);
        } else {
            int targetPos = startOfCommonInB + common.size();
            int targetPosA = startOfCommonInA + common.size();
            return expandCommon(patch, path, listA, listB, common, startOfCommonInA, targetPos, targetPosA);
        }

    }

    private SortedSet<String> extractProps(JsonObject elementA) {
        SortedSet<String> result = new TreeSet<String>();
        for ( Map.Entry<String, JsonElement> entry : elementA.entrySet() ){
            result.add(entry.getKey());
        }
        return result;
    }

    private boolean expandCommon(JsonPatch patch, JsonPath path, List<JsonElement> listA, List<JsonElement> listB, List<JsonElement> common, int startOfCommonInA, int targetPos, int targetPosA) {
        JsonElement target = listB.get(targetPos);

        if ( listA.size() > targetPosA ) {
            if (!listB.contains(listA.get(targetPosA))) {
                patch.addLast(new RemoveOperation(path.append(targetPosA)));
                return true;
            }
        }

        for ( int occurance : findOccurnacesIn(target, listA )){
            if ( occurance >= startOfCommonInA+common.size() ) {
                patch.addLast(new MoveOperation(path.append(occurance), path.append(targetPosA)));
                return true;
            }
            if ( occurance < startOfCommonInA ) {

                int to = (targetPosA < occurance ) ? targetPosA -1 : targetPosA ;

                patch.addLast(new MoveOperation(
                        path.append(  occurance ),
                        path.append( (to >= listA.size())? "-": Integer.toString(to))));
                return true;
            }
        }
        patch.addLast(new AddOperation(path.append(targetPosA), target));
        return true;
    }


    private <T> List<Integer> findOccurnacesIn(T item, List<T> list){
        List<Integer> result = new ArrayList<Integer>();
        for ( int c = 0; c < list.size(); c++){

            if ( list.get(c).equals(item) ){
                result.add(c);
            }
        }
        return result;
    }

    private List<JsonElement> convertToList(JsonArray array){
        List<JsonElement> result = new ArrayList<JsonElement>();

        for ( JsonElement element: array){
            result.add(element);
        }

        return result;
    }


}
