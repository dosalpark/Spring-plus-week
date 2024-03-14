package com.example.foodthought.test;

import com.example.foodthought.entity.User;

public interface CommonTest {
    String ANOTHER_PREFIX = "another-";
    Long TEST_USER_ID = 1L;
    Long TEST_ANOTHER_USER_ID = 2L;
    String TEST_USER_NAME = "username";
    String TEST_USER_PASSWORD = "password";
    User TEST_USER = User.builder()
            .id(TEST_USER_ID)
            .username(TEST_USER_NAME)
            .password(TEST_USER_PASSWORD)
            .build();
    User TEST_ANOTHER_USER = User.builder()
            .id(TEST_ANOTHER_USER_ID)
            .username(ANOTHER_PREFIX + TEST_USER_NAME)
            .password(ANOTHER_PREFIX + TEST_USER_PASSWORD)
            .build();
}
