import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import net.riotopsys.json_patch.JsonPatch;
import net.riotopsys.json_patch.JsonPatchFactory;
import net.riotopsys.json_patch.JsonPath;
import net.riotopsys.json_patch.gson.JsonPathDeserializer;
import net.riotopsys.json_patch.gson.JsonPathSerializer;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import util.JsonPatchTestCase;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by afitzgerald on 8/6/14.
 */
//@Ignore
@RunWith(Parameterized.class)
public class JsonPatchFactoryTest {

    private final JsonPatchTestCase testCase;
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(JsonPath.class, new JsonPathDeserializer())
            .registerTypeAdapter(JsonPath.class, new JsonPathSerializer())
            .create();

    @Parameters
    public static Collection<JsonPatchTestCase[]> addedNumbers() {
        Gson gson = new Gson();
        List<JsonPatchTestCase> cases = gson.fromJson(
                new BufferedReader(new InputStreamReader(JsonPatchFactoryTest.class.getResourceAsStream("/test_cases.json"))),
                new TypeToken<List<JsonPatchTestCase>>() {}.getType());

        LinkedList<JsonPatchTestCase[]> temp = new LinkedList<JsonPatchTestCase[]>();
        for ( JsonPatchTestCase singleCase: cases ) {
            JsonPatchTestCase[] temp2 = new JsonPatchTestCase[1];
            temp2[0] = singleCase;
            temp.add(temp2);
        }
//        int index = 14;//remove and add on same data should be move;
//        int index = 15;//remove and add on same data should be move;

        int index = 17;

//        return temp.subList(index, index+1);
        return temp;
    }

    public JsonPatchFactoryTest( JsonPatchTestCase testCase ) {
        this.testCase = testCase;
    }

    @Test
    public void runCase(){
//        System.out.println(String.format("running ... A: '%s' B: '%s'", testCase.first, testCase.second));

        JsonPatchFactory jpf = new JsonPatchFactory();
        JsonPatch patch = jpf.create(testCase.first, testCase.second);

        JsonElement result = patch.apply(testCase.first);


//        System.out.println(String.format("patch \n%s", gson.toJson(patch)));
//        System.out.println(String.format("output '%s'", result));


        Assert.assertEquals(testCase.second, result);

        System.out.println(String.format("running ...\nA: '%s'\nB: '%s'\nC: '%s'\npatch\n%s\n--------------\n\n", testCase.first, testCase.second, result, gson.toJson(patch)));
        System.out.flush();
    }

}
