package com.selfdriven.semo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Login {

    private char memberType;
    private String email;
    private String name;
    private String loginType;
}
