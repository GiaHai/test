package com.company.truonghoc.service;

import com.company.truonghoc.entity.UserExt;

import java.util.List;

public interface DulieuUserService {
    String NAME = "truonghoc_DulieuUserService";

    List<UserExt> timbrowerdonvi(String login);

    UserExt timEditdonvi(String login);

}