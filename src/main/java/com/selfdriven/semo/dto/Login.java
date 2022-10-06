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

    private String name;
    private String email;
    private Character memberType;
    private Character socialType;
}
