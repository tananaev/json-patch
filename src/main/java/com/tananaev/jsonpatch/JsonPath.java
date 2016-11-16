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

package com.tananaev.jsonpatch;

import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonPath {

    private List<String> path;

    public JsonPath(String path) {
        this.path = new ArrayList<>(Arrays.asList(path.split("/")));
        if ( this.path.size() > 0 ) {
            this.path.remove(0);
        }
    }

    private JsonPath(List<String> path) {
        this.path = new ArrayList<>(path);
    }

    @Override
    public String toString() {
        if ( path.size() == 0 ){
            return "/";
        }
        StringBuilder sb = new StringBuilder();
        for ( String s: path ){
            sb.append("/").append(s);
        }

        return sb.toString();
    }

    public String tail(){
       if ( path.size() == 0 ){
           return null;
       }
       return path.get(path.size()-1);
    }

    public JsonPath head(){
        if ( path.size() == 0){
            return this;
        }
        return new JsonPath(path.subList(0, path.size()-1));
    }

    @Override
    public boolean equals(Object obj) {
        if ( obj == null ){
            return false;
        } else if ( !( obj instanceof JsonPath ) ){
            return false;
        } else if ( ((JsonPath)obj).path.size() != path.size()){
            return false;
        } else {
            JsonPath other = (JsonPath) obj;
            boolean result = true;
            for ( int c = 0; c < path.size(); c++ ){
                result = result && path.get(c).equals( other.path.get(c));
            }
            return result;
        }
    }

    public JsonPath append(String path) {
        JsonPath result = new JsonPath(this.path);
        result.path.addAll(Arrays.asList(path.split("/")));
        return result;
    }

    public JsonPath append(int path) {
        return append(Integer.toString(path));
    }

    public JsonElement navigate(JsonElement original) {
        for ( String segment: path ){
            if ( original.isJsonObject() ){
                original = original.getAsJsonObject().get(segment);
            } else if ( original.isJsonArray() ) {
                original = original.getAsJsonArray().get(Integer.parseInt(segment));
            }
        }
        return original;
    }

}
