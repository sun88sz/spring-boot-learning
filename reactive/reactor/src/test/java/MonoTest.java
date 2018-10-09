import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Mono;

/**
 * @author : Sun
 * @date : 2018/10/8 17:40
 */
@Slf4j
public class MonoTest {
    
    @Test
    public void monoTest(){
        Mono.just(1).log();
    }
}
