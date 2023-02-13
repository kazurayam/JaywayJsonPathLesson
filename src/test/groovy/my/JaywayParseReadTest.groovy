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

class JaywayParseReadTest {

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

    /**
     * make sure that in the har file contained a request
     * of which url ends with "jquery.min.js"
     */
    @Test void testIfJqueryJsIsRequested() {
        ReadContext ctx = JsonPath.parse(har.toFile())
        String path = '$.log.entries[?(@.request.url =~ /.*jquery\\.min\\.js/)]'
        List<String> requests = ctx.read(path)
        //println "requests.size()=${requests.size()}"
        assertEquals(1, requests.size())
    }

}
