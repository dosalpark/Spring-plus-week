package com.example.foodthought.test;

import com.example.foodthought.entity.User;

public interface CommonTest {
    String ANOTHER_PREFIX = "another-";
    String BLOCKED_PREFIX = "blocked-";
    Long TEST_USER_ID = 1L;
    Long TEST_ANOTHER_USER_ID = 2L;
    Long TEST_ADMIN_USER_ID = 3L;
    String TEST_USER_USERID = "username";
    String TEST_USER_USERNAME = "name";
    String TEST_USER_PASSWORD = "password";
    User TEST_USER = User.builder()
            .id(TEST_USER_ID)
            .userId(TEST_USER_USERID)
            .username(TEST_USER_USERNAME)
            .password(TEST_USER_PASSWORD)
            .build();
    User TEST_ANOTHER_USER = User.builder()
            .id(TEST_ANOTHER_USER_ID)
            .userId(ANOTHER_PREFIX + TEST_USER_USERID)
            .username(ANOTHER_PREFIX + TEST_USER_USERNAME)
            .password(ANOTHER_PREFIX + TEST_USER_PASSWORD)
            .build();

}
