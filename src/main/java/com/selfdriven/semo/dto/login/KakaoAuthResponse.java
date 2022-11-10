package com.selfdriven.semo.dto.login;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoAuthResponse {

    private String tokenType;
    private String accessToken;
    private String idToken;
    private Integer expiresIn;
    private String refreshToken;
    private Integer refreshTokenExpiresIn;
    private String scope;

}
