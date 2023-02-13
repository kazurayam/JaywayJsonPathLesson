package my

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.burt.jmespath.JmesPath;
import io.burt.jmespath.Expression;
import io.burt.jmespath.jackson.JacksonRuntime
import org.junit.jupiter.api.Test;

class JMESParseReadTest {

    @Test
    void testSmoke() {
        File har = new File("./src/test/fixtures/sample.har")

        // The first thing you need is a runtime. These objects can compile expressions
        // and they are specific to the kind of structure you want to search in.
        // For most purposes you want the Jackson runtime, it can search in JsonNode
        // structures created by Jackson.
        JmesPath<JsonNode> jmespath = new JacksonRuntime();
        // Expressions need to be compiled before you can search. Compiled expressions
        // are reusable and thread safe. Compile your expressions once, just like database
        // prepared statements.
        String expr = "log.version"
        //String expr = "locations[?state == 'WA'].name | sort(@) | {WashingtonCities: join(', ', @)}";
        Expression<JsonNode> expression = jmespath.compile(expr);
        // This you have to fill in yourself, you're probably using Jackson's ObjectMapper
        // to load JSON data, and that should fit right in here.
        ObjectMapper om = new ObjectMapper();
        JsonNode input = om.readTree(har);
        // Finally this is how you search a structure. There's really not much more to it.
        JsonNode result = expression.search(input);
        System.out.println(String.format("result=%s",result))
    }
}
