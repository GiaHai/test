package com.company.truonghoc.service;

import com.company.truonghoc.entity.Hocsinh;
import com.company.truonghoc.entity.UserExt;
import com.haulmont.cuba.core.global.DataManager;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service(DulieuUserService.NAME)
public class DulieuUserServiceBean implements DulieuUserService {
    @Inject
    protected DataManager dataManager;

    public List<UserExt> timbrowerdonvi(String login) {
        List<UserExt> userExts = Loadtimbrowertendonvi(login);
        return userExts;
    }


    private List<UserExt> Loadtimbrowertendonvi(String login) {
        return dataManager.load(UserExt.class)
                .query("select e from truonghoc_UserExt e where e.donvitrungtam = 1 and e.login = :login")
                .parameter("login", login)
                .list();
    }
//    -----------------------------------------
    String abc;
    @Override
    public UserExt timEditdonvi(String login) {
        UserExt userExt = LoadtimEditdonvi(login);
        abc = userExt.getTendonvi();
        return userExt;
    }


    private UserExt LoadtimEditdonvi(String login) {
        return dataManager.load(UserExt.class)
                .query("select e from truonghoc_UserExt e where e.login = :login")
                .parameter("login",login )
                .one();
    }
    //    -----------------------------------------
}