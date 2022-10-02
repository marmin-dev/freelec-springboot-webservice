package com.marmin.book.springboot.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Role {
    GUEST("ROLE_GUEST","손님"),
    USER("ROLE_USER","일반사용자");

    private final String Key;
    private final String title;

}
