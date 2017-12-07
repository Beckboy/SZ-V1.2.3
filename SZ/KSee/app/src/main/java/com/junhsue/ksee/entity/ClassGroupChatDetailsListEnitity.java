package com.junhsue.ksee.entity;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hunter_J on 17/5/9.
 */

public class ClassGroupChatDetailsListEnitity implements Serializable {

  public String english;

  public List<ClassGroupChatDetailsEntity> list = new ArrayList<>();

}
