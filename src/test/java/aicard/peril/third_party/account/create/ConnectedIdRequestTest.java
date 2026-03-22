package aicard.peril.third_party.account.create;

import aicard.peril.third_party.common.TokenRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ConnectedIdRequest 테스트 클래스
 *
 * - CODEF connectedId 발급 로직 정상 동작 확인용
 * - 실제 API 호출 포함된 통합 테스트
 *
 * 설정
 * - DataSource 자동 설정 제외 (DB 필요 없어서)
 * - SpringBootTest로 Bean 주입 받아서 테스트 진행
 */
@SpringBootTest(
        properties = {
                "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"
        }
)
class ConnectedIdRequestTest {

    /**
     * connectedId 발급 요청 클래스
     */
    @Autowired
    private ConnectedIdRequest connectedIdRequest;

    /**
     * CODEF 토큰/클라이언트 제공 객체
     */
    @Autowired
    private TokenRequest tokenRequest;

    /**
     * connectedId 발급 테스트
     *
     * 테스트 목적
     * - AccountParams 기반으로 connectedId 정상 발급되는지 확인
     *
     * 테스트 흐름
     * 1. AccountParams 생성 및 값 세팅
     * 2. requestParameters 호출
     * 3. 결과값 출력
     * 4. null 아닌지 검증
     *
     * @throws Exception API 호출 및 내부 로직에서 발생 가능한 예외
     */
    @Test
    void connectedId_발급_테스트() throws Exception {

        // given: 요청 파라미터 세팅
        ConnectedIdRequest.AccountParams params = new ConnectedIdRequest.AccountParams();
        params.setClientType("P");
        params.setOrganization("0002"); // 테스트용 기관코드
        params.setLoginType("ID");
        params.setId("test_id");

        // when: connectedId 발급 요청
        String result = connectedIdRequest.requestConnectedId(params);

        // 결과 확인용 출력
        System.out.println("connectedId = " + result);

        // then: 정상 발급 여부 검증
        assertNotNull(result);
    }

    /**
     * 계정 목록 조회 테스트
     *
     * 테스트 목적
     * - connectedId 발급 후 계정 목록 조회 API가 정상 호출되는지 확인
     *
     * 테스트 흐름
     * 1. AccountParams 생성 및 connectedId 발급
     * 2. 발급된 connectedId로 계정 목록 조회 호출
     * 3. 예외 없이 수행되는지 확인
     *
     * @throws Exception API 호출 및 내부 로직에서 발생 가능한 예외
     */
    @Test
    void 계정목록_조회_테스트() throws Exception {

        String connectedId ="byi1wYwD40k8hEIiXl6bRF";

        // when & then: 계정 목록 조회가 예외 없이 수행되는지 확인
        assertDoesNotThrow(() -> connectedIdRequest.getAccountList(connectedId));
    }
}