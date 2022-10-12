package com.selfdriven.semo.dto.login;

import lombok.*;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
public class Login {

    private String id;
    private String name;
    private String email;
    private String memberType;
    private String socialType;
}
