package aicard.peril.third_party.common;

import io.codef.api.EasyCodef;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Spring 설정값을 이용해 CODEF 연동에 필요한 {@link EasyCodef} 객체를
 * 초기화하고 보관하는 컴포넌트입니다.
 * <p>
 * CODEF 클라이언트 아이디, 시크릿, 퍼블릭 키를 주입받아
 * 데모/정식 환경 설정과 RSA 공개키 설정을 한 번에 수행합니다.
 */
@Getter
@Component
public class TokenRequest {

    /**
     * CODEF API 호출에 사용하는 EasyCodef 객체입니다.
     */
    private final EasyCodef codef;

    /**
     * CODEF 클라이언트 아이디입니다.
     */
    private final String clientId;

    /**
     * CODEF 클라이언트 시크릿입니다.
     */
    private final String clientSecret;

    /**
     * CODEF RSA 퍼블릭 키입니다.
     */
    private final String publicKey;

    /**
     * CODEF 연동에 필요한 인증 정보를 주입받아 {@link EasyCodef} 객체를 초기화합니다.
     *
     * @param clientId CODEF 클라이언트 아이디
     * @param clientSecret CODEF 클라이언트 시크릿
     * @param publicKey CODEF RSA 퍼블릭 키
     */
    public TokenRequest(
            @Value("${codef.client_id}") String clientId,
            @Value("${codef.client_secret}") String clientSecret,
            @Value("${codef.public_key}") String publicKey
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.publicKey = publicKey;

        this.codef = new EasyCodef();
        codef.setClientInfoForDemo(this.clientId, this.clientSecret);
        codef.setPublicKey(this.publicKey);
    }
}
