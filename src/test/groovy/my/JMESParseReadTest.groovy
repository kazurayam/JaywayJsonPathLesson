package my

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.burt.jmespath.JmesPath;
import io.burt.jmespath.Expression;
import io.burt.jmespath.jackson.JacksonRuntime
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals;

class JMESParseReadTest {

    private static File har
    private JmesPath<JsonNode> jmespath
    ObjectMapper om

    @BeforeAll
    static void beforeAll() {
        har = new File("./src/test/fixtures/sample.har")
    }

    @BeforeEach
    void beforeEach() {
        // The first thing you need is a runtime. These objects can compile expressions
        // and they are specific to the kind of structure you want to search in.
        // For most purposes you want the Jackson runtime, it can search in JsonNode
        // structures created by Jackson.
        jmespath = new JacksonRuntime();
        om = new ObjectMapper();
    }

    @Test
    void testSelectingVersion() {
        // Expressions need to be compiled before you can search. Compiled expressions
        // are reusable and thread safe. Compile your expressions once, just like database
        // prepared statements.
        String expr = "log.version"
        //String expr = "locations[?state == 'WA'].name | sort(@) | {WashingtonCities: join(', ', @)}";
        Expression<JsonNode> expression = jmespath.compile(expr);
        // This you have to fill in yourself, you're probably using Jackson's ObjectMapper
        // to load JSON data, and that should fit right in here.
        JsonNode input = om.readTree(har);
        // Finally this is how you search a structure. There's really not much more to it.
        JsonNode result = expression.search(input);
        assertEquals("\"1.2\"", result.toString())
    }

    @Test
    void testSelectingURL() {
        //String expr = "log.entries[?request.url=='https://cdnjs.cloudflare.com/ajax/libs/jquery/1.11.3/jquery.min.js']"
        String expr = "log.entries[?contains(to_string(request.url), 'jquery.min.js')]"
        Expression<JsonNode> expression = jmespath.compile(expr);
        JsonNode input = om.readTree(har);
        JsonNode result = expression.search(input);
        assertEquals(1, result.size())
    }


}
