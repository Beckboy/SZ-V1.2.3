package com.junhsue.ksee.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlang on 16/10/26.
 */
public class ArticleSpitComment extends BaseEntity {

    public List<ArticleSplitContent> mycomments = new ArrayList<ArticleSplitContent>();

    public List<ArticleSplitContent> hotcomments = new ArrayList<ArticleSplitContent>();

    }
