package cn.itcast.core.service;

import cn.itcast.core.pojo.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface UserService {

    public void sendCode(String phone);

    public Boolean checkSmsCode(String phone , String smsCode);

    public  void  add(User user);



    Map<String,Integer> findUsers();

    List<ArrayList<String>> findAllUser();


}
