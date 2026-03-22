package aicard.peril.third_party.common;

import io.codef.api.EasyCodef;
import io.codef.api.EasyCodefServiceType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CODEF 샌드박스 토큰 발급 동작을 검증하는 테스트 클래스입니다.
 * <p>
 * 테스트 실행 시 {@link TokenRequest} 빈을 주입받아
 * {@link EasyCodef} 객체를 통해 샌드박스 access token 발급을 확인합니다.
 * 또한 테스트 환경에서 불필요한 데이터소스 자동 설정을 제외하여
 * DB 설정 없이 토큰 발급 테스트만 수행할 수 있도록 구성합니다.
 */
@SpringBootTest(
        properties = {
                "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"
        }
)
class TokenRequestTest {

    /**
     * CODEF 설정이 완료된 {@link TokenRequest} 빈입니다.
     */
    @Autowired
    private TokenRequest tokenRequest;

    /**
     * CODEF 샌드박스 환경에서 access token이 정상 발급되는지 검증합니다.
     *
     * @throws Exception 토큰 발급 과정에서 예외가 발생한 경우
     */
    @Test
    void sandbox_token_request() throws Exception {

        // given
        EasyCodef codef = tokenRequest.getCodef();

        // when
        String accessToken = codef.requestToken(EasyCodefServiceType.SANDBOX);

        // then
        System.out.println("accessToken = " + accessToken);

        assertNotNull(accessToken);
    }
}