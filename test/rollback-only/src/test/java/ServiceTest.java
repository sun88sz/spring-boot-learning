import com.sun.test.RollbackApplication;
import com.sun.test.service.AService;
import com.sun.test.service.BService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RollbackApplication.class)
public class ServiceTest {

    @Autowired
    private AService aService;
    @Autowired
    private BService bService;

    @Test
    public void xx() {

        aService.a();

    }

}
