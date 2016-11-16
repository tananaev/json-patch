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
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import net.riotopsys.json_patch.JsonPath;
import net.riotopsys.json_patch.gson.JsonPathDeserializer;
import net.riotopsys.json_patch.gson.JsonPathSerializer;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class JsonPathTest {

    @Test
    public void canParseRoot(){
        JsonPath path = new JsonPath("/");

        Assert.assertEquals("/", path.toString());

        Assert.assertEquals(null, path.tail());

        Assert.assertEquals("/", path.head().toString());
    }


    @Test
    public void canParseMore(){
        JsonPath path = new JsonPath("/a/0/b");

        Assert.assertEquals("/a/0/b", path.toString());

        Assert.assertEquals("b", path.tail());

        Assert.assertEquals("/a/0", path.head().toString());
    }

    @Test
    public void canAppend(){
        JsonPath path = new JsonPath("/a/0/b");

        Assert.assertEquals("/a/0/b/c", path.append("c").toString());

        Assert.assertEquals("/a/0/b/d/e", path.append("d/e").toString());
    }

    @Test
    public void equalsIsSane(){
        Assert.assertEquals(new JsonPath("/"), new JsonPath("/") );
        Assert.assertEquals(new JsonPath("/a/0/b"), new JsonPath("/a/0/b") );
        Assert.assertNotEquals(new JsonPath("/"), new JsonPath("/a/0/b"));
        Assert.assertNotEquals(new JsonPath("/"), null);
    }

    @Test
    public void gsonIsSane(){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(JsonPath.class, new JsonPathDeserializer())
                .registerTypeAdapter(JsonPath.class, new JsonPathSerializer())
                .create();

        List<JsonPath> paths = gson.fromJson("[\"/a/0/b/1\"]",  new TypeToken<List<JsonPath>>(){}.getType());

        Assert.assertNotNull(paths);
        Assert.assertEquals(1, paths.size() );
        Assert.assertEquals("/a/0/b/1", paths.get(0).toString());
    }

    @Test
    public void canNavigateRoot(){
        JsonPath path = new JsonPath("/");
        JsonElement element = new JsonParser().parse("{\"a\":{}}");

        Assert.assertEquals(element, path.navigate(element));
    }

    @Test
    public void canNavigateObject(){
        JsonPath path = new JsonPath("/a");
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse("{\"a\":{\"b\":\"stuff\"}}");

        Assert.assertEquals(parser.parse("{\"b\":\"stuff\"}"), path.navigate(element));
    }

}
