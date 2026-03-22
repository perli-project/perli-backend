package aicard.peril.third_party.common;

import io.codef.api.EasyCodef;
import io.codef.api.EasyCodefServiceType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        properties = {
                "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"
        }
)
class TokenRequestTest {

    @Autowired
    private TokenRequest tokenRequest;

    @Test
    void 샌드박스_토큰_재사용_및_신규발급_테스트() throws Exception {
        EasyCodef codef = tokenRequest.getCodef();

        String accessToken1 = codef.requestToken(EasyCodefServiceType.SANDBOX);
        System.out.println("accessToken1 = " + accessToken1);

        assertNotNull(accessToken1);
    }
}
