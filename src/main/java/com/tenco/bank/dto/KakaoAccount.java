
package com.tenco.bank.dto;

import java.util.LinkedHashMap;
import java.util.Map;


import lombok.Data;

@Data
public class KakaoAccount {

    private Boolean profileNicknameNeedsAgreement;
    private Boolean profileImageNeedsAgreement;
    private Profile profile;


}
