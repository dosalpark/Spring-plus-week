package com.example.foodthought.repository.comment;

import com.example.foodthought.config.QueryFactoryConfig;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {


    private final QueryFactoryConfig config;

}
