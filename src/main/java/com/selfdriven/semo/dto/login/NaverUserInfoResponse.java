package com.selfdriven.semo.dto.login;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NaverUserInfoResponse {

    private String resultcode;
    private String message;
    private Response response;

    @Getter
    @Setter
    @RequiredArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public class Response{
        private String id;
        private String name;
        private String nickname;
        private String email;
        private String profileImage;

        @Override
        public String toString() {
            return "Response{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", email='" + email + '\'' +
                    ", profileImage='" + profileImage + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "NaverUserInfoResponse{" +
                "resultcode='" + resultcode + '\'' +
                ", message='" + message + '\'' +
                ", response=" + response +
                '}';
    }
}
