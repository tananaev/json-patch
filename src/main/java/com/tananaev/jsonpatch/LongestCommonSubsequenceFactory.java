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

import java.util.List;

public class LongestCommonSubsequenceFactory {

    public <T> List<T> search(List<T> seqA, List<T> seqB) {
        if ( seqA.size() > seqB.size()){
            return subsearch( seqB, seqA );
        } else {
            return subsearch( seqA, seqB );
        }
    }

    private <T> List<T> subsearch(List<T> shortList, List<T> longList) {
        if ( shortList.isEmpty() ){
            return shortList;
        }

        int delta = longList.size() - shortList.size();

        for( int c = 0; c <= delta; c++ ){
            if ( checkEquals( shortList, longList, c)){
                return shortList;
            }
        }
        //remove one from right
        List<T> rightList = subsearch(shortList.subList(0, shortList.size() - 1), longList);
        //remove one from left
        List<T> leftList = subsearch(shortList.subList(1, shortList.size()), longList);
        if ( leftList.size() > rightList.size() ){
            return leftList;
        } else {
            return rightList;
        }
    }

    private <T> boolean checkEquals(List<T> shortList, List<T> longList, int index) {
        boolean result = true;
        for ( int c = 0; c < shortList.size();  c++ ){
            result = result && shortList.get(c).equals(longList.get(c+index));
        }
        return result;
    }

    public <T> int findStartIndex( List<T> sub, List<T> full ){
        for( int c = 0; c < full.size(); c++ ) {
            if (checkEquals(sub, full, c)) {
                return c;
            }
        }
        return -1;
    }

}
