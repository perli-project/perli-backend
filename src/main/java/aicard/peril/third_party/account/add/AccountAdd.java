package aicard.peril.third_party.account.add;

import aicard.peril.third_party.common.ConnectedIdRequest.AccountParams;
import aicard.peril.third_party.common.TokenRequest;
import io.codef.api.EasyCodef;
import io.codef.api.EasyCodefServiceType;
import io.codef.api.EasyCodefUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class AccountAdd {

    private final TokenRequest token;

    public String accountPlus(String connectedId, AccountParams params) {
        try {
            List<HashMap<String, Object>> accountList = new ArrayList<>();
            HashMap<String, Object> accountMap = new HashMap<>();

            accountMap.put("countryCode", params.getCountryCode());
            accountMap.put("businessType", params.getBusinessType());
            accountMap.put("clientType", params.getClientType());
            accountMap.put("organization", params.getOrganization());
            accountMap.put("loginType", params.getLoginType());
            accountMap.put("id", params.getId());

            EasyCodef codef = token.getCodef();

            accountMap.put(
                    "password",
                    EasyCodefUtil.encryptRSA("user_password", codef.getPublicKey())
            );

            accountList.add(accountMap);

            HashMap<String, Object> parameterMap = new HashMap<>();
            parameterMap.put("connectedId", connectedId);
            parameterMap.put("accountList", accountList);

            String responseJson = codef.addAccount(
                    EasyCodefServiceType.SANDBOX,
                    parameterMap
            );

            log.info("CODEF addAccount response = {}", responseJson);
            return responseJson;

        } catch (Exception e) {
            log.error("CODEF 계정 추가 실패", e);
            return null;
        }
    }
}