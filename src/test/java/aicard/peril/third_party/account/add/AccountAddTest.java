package aicard.peril.third_party.account.add;

import aicard.peril.third_party.account.create.ConnectedIdRequest.AccountParams;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CODEF 계정 추가 동작을 검증하는 테스트 클래스입니다.
 * <p>
 * 테스트 실행 시 {@link AccountAdd} 빈을 주입받아
 * 기존 connectedId에 계정을 추가하는 API 호출이
 * 정상적으로 수행되는지 확인합니다.
 * 또한 테스트 환경에서 불필요한 데이터소스 자동 설정을 제외하여
 * DB 설정 없이 계정 추가 테스트만 수행할 수 있도록 구성합니다.
 */
@SpringBootTest(
        properties = {
                "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"
        }
)
class AccountAddTest {

    /**
     * 계정 추가 요청 클래스
     */
    @Autowired
    private AccountAdd accountAdd;

    /**
     * 계정 추가 테스트
     */
    @Test
    void account_add_test() throws Exception {

        // given
        String connectedId = "byi1wYwD40k8hEIiXl6bRF";

        AccountParams params = new AccountParams();
        params.setClientType("P");
        params.setOrganization("0001");
        params.setLoginType("ID");
        params.setId("test_id");

        // when
        String result = accountAdd.accountPlus(connectedId, params);

        // then
        System.out.println("response = " + result);

        assertNotNull(result);
        assertFalse(result.isBlank());
    }
}