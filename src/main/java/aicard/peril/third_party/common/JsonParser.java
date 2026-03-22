package aicard.peril.third_party.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSON 파싱 유틸 클래스
 *
 * - JSON 문자열에서 특정 경로(path)에 있는 값을 추출하는 용도
 * - Jackson ObjectMapper 사용해서 트리 구조로 파싱
 *
 * 특징
 * - path 배열 기반으로 depth 순회하면서 값 찾음
 * - 중간에 값 없으면 null 바로 반환
 * - 최종 값은 String 형태로 반환
 *
 * 사용 예시
 * extractJson(json, new String[]{"data", "connectedId"})
 * → json.data.connectedId 값 반환
 */
public class JsonParser {

    /**
     * Jackson JSON 파싱용 ObjectMapper (싱글톤으로 사용)
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 인스턴스 생성 방지용 생성자
     */
    private JsonParser() {
    }

    /**
     * JSON 문자열에서 특정 경로 값 추출 메서드
     *
     * 동작 과정
     * 1. JSON 문자열을 JsonNode 트리로 변환
     * 2. path 배열 순회하면서 node 이동
     * 3. 중간에 값 없으면 null 반환
     * 4. 최종 node 값을 문자열로 반환
     *
     * @param json JSON 문자열
     * @param path 추출할 키 경로 배열 (ex. {"data", "connectedId"})
     * @return 해당 경로의 값 (없으면 null)
     *
     * @throws RuntimeException JSON 파싱 실패 시 발생
     */
    public static String extractJson(String json, String[] path) {
        try {
            // JSON 문자열 → 트리 구조로 변환
            JsonNode node = objectMapper.readTree(json);

            // path 따라가면서 node 이동
            for (String key : path) {
                node = node.path(key);

                // 중간에 값 없으면 바로 null 반환
                if (node.isMissingNode() || node.isNull()) {
                    return null;
                }
            }

            // 최종 값 문자열로 반환
            return node.asText();

        } catch (Exception e) {
            // 파싱 실패 시 런타임 예외로 감싸서 던짐
            throw new RuntimeException("JSON 파싱 중 오류 발생", e);
        }
    }
}