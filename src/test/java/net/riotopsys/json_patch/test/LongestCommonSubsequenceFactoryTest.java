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

package net.riotopsys.json_patch.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import net.riotopsys.json_patch.JsonPatch;
import net.riotopsys.json_patch.JsonPatchFactory;
import net.riotopsys.json_patch.JsonPath;
import net.riotopsys.json_patch.LongestCommonSubsequenceFactory;
import net.riotopsys.json_patch.gson.JsonPathDeserializer;
import net.riotopsys.json_patch.gson.JsonPathSerializer;
import net.riotopsys.json_patch.test.util.JsonPatchTestCase;
import net.riotopsys.json_patch.test.util.LCSFTestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@RunWith(Parameterized.class)
public class LongestCommonSubsequenceFactoryTest {

    private final LCSFTestCase testCase;
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    @Parameterized.Parameters(name = "{index} {1}")
    public static Collection<Object[]> addedNumbers() {
        Gson gson = new Gson();
        List<LCSFTestCase> cases = gson.fromJson(
                new BufferedReader(new InputStreamReader(JsonPatchFactoryTest.class.getResourceAsStream("/sublist.json"))),
                new TypeToken<List<LCSFTestCase>>() {}.getType());

        LinkedList<Object[]> temp = new LinkedList<Object[]>();
        for ( LCSFTestCase singleCase: cases ) {
            Object[] temp2 = new Object[2];
            temp2[0] = singleCase;
            temp2[1] = singleCase.name;
            temp.add(temp2);
        }

        return temp;
    }

    public LongestCommonSubsequenceFactoryTest( LCSFTestCase testCase, String name ) {
        this.testCase = testCase;
    }

    @Test
    public void runCase(){
        LongestCommonSubsequenceFactory lcsf = new LongestCommonSubsequenceFactory();

        List<String> result = lcsf.search(testCase.A, testCase.B);

        Assert.assertTrue(String.format("expected: %s, got: %s", testCase.expected, result), checkEquals(result, testCase.expected));
    }

    private boolean checkEquals(List<String> listA, List<String> listB) {
        if( listA.size() == 0 && listB.size() == 0){
            return true;
        }
        if ( listA.size() == 0 ){
            return false;
        }
        if ( listB.size() == 0 ){
            return false;
        }
        boolean result = true;
        for ( int c = 0; c < listA.size();  c++ ){
            result = result && listA.get(c).equals(listB.get(c));
        }
        return result;
    }

}
