package net.riotopsys.json_patch;

import java.util.List;

/**
 * Created by afitzgerald on 9/4/14.
 */
public class LongestCommonSubsequenceFactory {

    public <T> List<T> search(List<T> seqA, List<T> seqB) {
        if ( seqA.size() > seqB.size()){
            return subsearch( seqB, seqA );
        } else {
            return subsearch( seqA, seqB );
        }
    }

    private <T> List<T> subsearch(List<T> shortList, List<T> longList) {
//        System.out.println(String.format("%s %s",shortList, longList) );
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
