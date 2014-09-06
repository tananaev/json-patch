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

    private SortedSet<String> extractProps(JsonObject elementA) {
        SortedSet<String> result = new TreeSet<String>();
        for ( Map.Entry<String, JsonElement> entry : elementA.entrySet() ){
            result.add(entry.getKey());
        }
        return result;
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

        if ( startOfCommonInA > 0 ){
            //common subsequence does not start a

            //move on position before to extend lcs
            JsonElement original = listA.get(startOfCommonInA - 1);
            JsonElement target = null;
            try {
               target = listB.get(startOfCommonInB - 1);
            } catch ( IndexOutOfBoundsException e ){
//                try other side?
//                try {
//                    target = listB.get(startOfCommonInB + common.size());
//                } catch ( IndexOutOfBoundsException e2 ){
//                    //fuck me
//                }
            }

            if ( listB.contains(original) && listA.contains(target) ){
                //we can move or copy the target from where its located
                //we leave the original and the common the fuck alone

                if ( common.contains(target) ){
                    //so target is in common and also outside of common lets call this a copy
                    for ( Integer i: findOccurnacesIn(original, listB)){
                        if ( i >= startOfCommonInA || i < startOfCommonInA + listA.size() ){
                            //TODO add copy operation
//                            patch.add(new CopyOperation(path.append(i), path.append(i) ) );
                            patch.add( new AddOperation(path.append(startOfCommonInA),target ));
                            return true;
                        }
                    }
                    throw new JsonPatchException("theoretically Unreachable");

                } else {
                    //move target in then?
                    int from = listA.indexOf(target);
                    int to = startOfCommonInA;
                    if ( from < to ){
                        to--;
                    }

                    patch.add(new MoveOperation(path.append(from), path.append(to) ) );
                    return true;
                }
            }

            if ( listB.contains(original) && !listA.contains(target) ){
//                //original exist but not here and target is something new
//                //dont fuck with the target and just move the original and don't fuck with common
//                for ( Integer i: findOccurnacesIn(original, listB)){
//                    if ( i < startOfCommonInB || i > startOfCommonInB + listB.size()-1 ){
//                        patch.add(new MoveOperation(path.append(startOfCommonInA), path.append(i) ) );
//                        return true;
//                    }
//                }
//
//                //must be unneeded if we made it here
//                patch.add(new RemoveOperation(path.append(startOfCommonInA - 1)));
//                return true;
                if ( target != null ) {
                    patch.add(new AddOperation(path.append(startOfCommonInA), target));
                    return true;
                } else {
                    for ( Integer i: findOccurnacesIn(original, listB)) {
                        if (i >= startOfCommonInA || i < startOfCommonInA + listA.size()) {
                            patch.add(new MoveOperation(path.append(startOfCommonInA-1), path.append(i)));
                            return true;
                        }
                    }
                }
            }

            if ( !listB.contains(original)  ){
                //add or alter
                if ( startOfCommonInA == startOfCommonInB ){
                    //alter
                    return processPatch(patch, path.append(startOfCommonInA - 1), original, target);
                } else {
                    //fuck it
                    patch.add(new RemoveOperation(path.append(startOfCommonInA - 1)));
                    return true;
                }
            }

            if ( !listA.contains(target) ){
                patch.add( new AddOperation(path.append(startOfCommonInA-1),target ));
                return true;
            }

        } else {

            if ( listA.size() == common.size() ){
                if ( startOfCommonInB != 0 ) {
                    patch.add(new AddOperation(path.append(0), listB.get(0)));
                } else {
                    patch.add(new AddOperation(path.append("-"), listB.get(startOfCommonInB + common.size())));
                }
                return true;
            }

            if ( listB.size() == common.size() ){
                patch.add( new RemoveOperation(path.append(listA.size()-1)));
                return true;
            }

            //move on position before to extend lcs
            JsonElement original = listA.get(startOfCommonInA + common.size());
            JsonElement target = null;
            try {
                target = listB.get(startOfCommonInB + common.size());
            } catch ( IndexOutOfBoundsException e ){
                //fuck it
            }

            if ( listB.contains(original) && listA.contains(target) ){
                //we can move or copy the target from where its located
                //we leave the original and the common the fuck alone

                if ( common.contains(target) ){
                    //so target is in common and also outside of common lets call this a copy
                    for ( Integer i: findOccurnacesIn(original, listA)){
                        if ( i >= startOfCommonInA || i < startOfCommonInA + listA.size() ){
                            //TODO add copy operation
//                            patch.add(new CopyOperation(path.append(i), path.append(i) ) );
                            patch.add( new AddOperation(path.append(startOfCommonInA + common.size()),target ));
                            return true;
                        }
                    }
                    throw new JsonPatchException("theoretically Unreachable");

                } else {
                    //move then?
                    patch.add(new MoveOperation(path.append(startOfCommonInA+ common.size() -1), path.append(listA.indexOf(target)) ) );
                    return true;
                }
            }

            if ( listB.contains(original) && !listA.contains(target) ){
                //original exist but not here and target is something new
                //dont fuck with the target and just move the original and don't fuck with common
                for ( Integer i: findOccurnacesIn(original, listA)){
                    if ( i < startOfCommonInA || i > startOfCommonInA + listA.size()-1 ){
                        patch.add(new MoveOperation(path.append(startOfCommonInA + common.size() -1), path.append(i) ) );
                        return true;
                    }
                }

                //must be unneeded if we made it here
                patch.add(new RemoveOperation(path.append(startOfCommonInA + common.size())));
                return true;
            }

            if ( !listB.contains(original)  ){
                //add or alter
                if ( startOfCommonInA == startOfCommonInB ){
                    //alter
                    return processPatch(patch, path.append(startOfCommonInA + common.size()), original, target);
                } else {
                    //fuck it
                    patch.add(new RemoveOperation(path.append(startOfCommonInA + common.size())));
                    return true;
                }
            }

            if ( !listA.contains(target) ){
                patch.add( new AddOperation(path.append(startOfCommonInA+ common.size() ),target ));
                return true;
            }
        }


//        int c;
//        for ( c=0; c < elementA.size() && c < elementB.size(); c++ ){
//            JsonElement valueA = elementA.get(c);
//            JsonElement valueB = elementB.get(c);
//
//            if ( !valueA.equals(valueB)) {
//
//                //see if valueB is in later position of elementA and bring it forward
//                for ( int b = c+1; b < elementA.size(); b++ ){
//                    if ( elementA.get(b).equals(valueB) ){
//                        patch.add(new MoveOperation(path.append(b), path.append(c)));
//                        return true;
//                    }
//                }
//
//                //see if valueA is in later position of elementB and toss it back
//                for ( int b = c+1; b < elementB.size(); b++ ){
//                    if ( elementB.get(b).equals(valueA) ){
//                        if ( b >= elementA.size() ){
//                            break;
//                        }
//                        patch.add(new MoveOperation(path.append(c),path.append(b)));
//                        return true;
//                    }
//                }
//
//                processPatch(patch, path.append(c), valueA, valueB);
//
//                return true;
//            }
//        }
//        if(  c < elementB.size()  ){
//            patch.add(new AddOperation(path.append("-"), elementB.getAsJsonArray().get(c)));
//            return true;
//        }
//        if ( c < elementA.size() ){
//            patch.add(new RemoveOperation(path.append(c)));
//            return true;
//        }
        throw new JsonPatchException("theoretically Unreachable");
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
