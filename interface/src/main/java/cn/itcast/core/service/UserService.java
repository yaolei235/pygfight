package cn.itcast.core.service;

import cn.itcast.core.pojo.user.User;

import java.util.ArrayList;
import java.util.List;

public interface UserService {

    public void sendCode(String phone);

    public Boolean checkSmsCode(String phone , String smsCode);

    public  void  add(User user);
    List<ArrayList<String>> findAllUser();
    //List<String> findTitle();
}
