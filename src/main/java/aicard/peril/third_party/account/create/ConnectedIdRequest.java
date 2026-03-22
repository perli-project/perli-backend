package aicard.peril.third_party.account.create;

import aicard.peril.third_party.common.JsonParser;
import aicard.peril.third_party.common.TokenRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.codef.api.EasyCodef;
import io.codef.api.EasyCodefServiceType;
import io.codef.api.EasyCodefUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * CODEF Connected ID 발급 요청 클래스
 *
 * - CODEF 계정 등록 API 호출해서 connectedId 받아오는 역할
 * - TokenRequest 통해 EasyCodef 객체 주입받아서 사용함
 *
 * 주요 흐름
 * 1. 계정 파라미터(AccountParams) 구성
 * 2. 비밀번호 RSA 암호화
 * 3. accountList 형태로 요청 데이터 구성
 * 4. CODEF createAccount API 호출
 * 5. 응답 JSON에서 connectedId 추출 후 반환
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class ConnectedIdRequest {

    /**
     * CODEF 토큰 및 EasyCodef 객체 제공용
     */
    private final TokenRequest token;

    /**
     * 계정 요청 파라미터 DTO
     *
     * - CODEF 계정 등록 시 필요한 값들 담는 객체
     *
     * 필드 설명
     * countryCode : 국가 코드 (기본 KR)
     * businessType : 개인/법인 구분 (기본 CD)
     * clientType : 개인/사업자 타입
     * organization : 기관 코드 (은행/카드사 등)
     * loginType : 로그인 방식
     * id : 사용자 아이디
     */
    @Getter
    @Setter
    public static class AccountParams {
        String countryCode = "KR";
        String businessType = "CD";
        String clientType;
        String organization;
        String loginType;
        String id;
        /*
        "birthDate": "980225",
   "loginTypeLevel":"",
   "clientTypeLevel":"",
   "cardNo":"",
   "cardPassword":"",
         */
    }

    /**
     * CODEF connectedId 발급 요청 메서드
     *
     * 처리 과정
     * 1. AccountParams 기반으로 accountMap 생성
     * 2. 비밀번호 RSA 암호화 후 accountMap에 추가
     * 3. accountList로 감싸서 parameterMap 생성
     * 4. CODEF createAccount API 호출
     * 5. 응답 JSON에서 connectedId 추출해서 반환
     *
     * @param params 계정 요청 파라미터
     * @return connectedId (없으면 null 반환)
     *
     * @throws NullPointerException params 또는 내부 값 null일 경우 발생 가능
     * @throws UnsupportedEncodingException 암호화 과정에서 인코딩 문제 발생 시
     * @throws JsonProcessingException JSON 파싱 오류 발생 시
     * @throws InterruptedException API 호출 중 인터럽트 발생 시
     */
    public String requestConnectedId(AccountParams params)
            throws NullPointerException, UnsupportedEncodingException,
            JsonProcessingException, InterruptedException {

        // 계정 리스트 생성
        List<HashMap<String, Object>> accountList = new ArrayList<>();
        HashMap<String, Object> accountMap = new HashMap<>();

        accountMap.put("countryCode", params.countryCode);
        accountMap.put("businessType", params.businessType);
        accountMap.put("clientType", params.clientType);
        accountMap.put("organization", params.organization);
        accountMap.put("loginType", params.loginType);
        accountMap.put("id", params.id);

        EasyCodef codef = token.getCodef();

        // 비밀번호 RSA 암호화
        try {
            accountMap.put(
                    "password",
                    EasyCodefUtil.encryptRSA("user_password", codef.getPublicKey())
            );
        } catch (Exception e) {
            log.error("비밀번호 암호화 실패", e);
            return null;
        }

        accountList.add(accountMap);

        // 최종 요청 파라미터 구성
        HashMap<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("accountList", accountList);

        // CODEF API 호출
        String responseJson = codef.createAccount(
                EasyCodefServiceType.SANDBOX,
                parameterMap
        );

        log.info("CODEF response = {}", responseJson);

        // connectedId 추출 후 반환
        return JsonParser.extractJson(
                responseJson,
                new String[]{"data", "connectedId"}
        );
    }

    /**
     * connectedId 기준 계정 목록 조회 메서드
     *
     * - CODEF에 등록된 계정 리스트 조회하는 API 호출
     *
     * 처리 과정
     * 1. connectedId 기반으로 요청 파라미터(accountMap) 구성
     * 2. CODEF getAccountList API 호출
     * 3. 응답 결과 로그 출력
     *
     * 특징
     * - 반환값 없이 로그로만 결과 확인하는 구조
     * - 실제 서비스에서는 응답값 반환하도록 개선 필요
     *
     * @param connectedId 계정 조회 대상 connectedId
     *
     * @throws UnsupportedEncodingException 인코딩 문제 발생 시
     * @throws JsonProcessingException JSON 처리 중 오류 발생 시
     * @throws InterruptedException API 호출 중 인터럽트 발생 시
     */
    public void getAccountList(String connectedId) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
    {

        EasyCodef codef = token.getCodef();
        HashMap<String, Object> accountMap = new HashMap<>();

        accountMap.put("connectedId", connectedId);
        String result = codef.getAccountList(EasyCodefServiceType.SANDBOX,accountMap);

        log.info(result);
    }

}}