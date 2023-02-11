package my

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.Predicate
import com.jayway.jsonpath.Predicate.PredicateContext
import com.jayway.jsonpath.ReadContext

class ParseReadTest {

    Path har

    @BeforeEach
    void beforeEach() {
        har = Paths.get("src/test/fixtures/sample.har")
    }

    @Test
    void testHarExists() {
        System.out.println(har.toString())
        assertTrue(Files.exists(har))
    }

    @Test void testSizeOfUrls() {
        ReadContext ctx = JsonPath.parse(har.toFile())
        List<String> urls = ctx.read('$.log.entries[*].request.url')
        //System.out.println("urls.size()=${urls.size()}")
        assertTrue(urls.size() > 0)   // 27
    }

    @Test void testIfJqueryJsIsRequested() {
        ReadContext ctx = JsonPath.parse(har.toFile())
        Predicate predicate = new Predicate() {
            boolean apply(PredicateContext context) {
                String url = context.item(Map.class).get("url").toString();
                return url.endsWith('jquery.min.js')
            }
        }
        List<String> urls = ctx.read('$.log.entries[*].request', predicate)
        println "urls.size()=${urls.size()}"
        assertEquals(1, urls.size())
    }

}
