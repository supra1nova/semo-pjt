package com.selfdriven.semo.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
public class Login {

    private String name;
    private String email;
    private Character memberType;
    private Character socialType;
}
