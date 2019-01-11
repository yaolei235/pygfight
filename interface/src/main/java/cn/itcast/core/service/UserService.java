package cn.itcast.core.service;

import cn.itcast.core.pojo.entity.ActiveUsers;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface UserService {

    public void sendCode(String phone);

    public Boolean checkSmsCode(String phone , String smsCode);

    public  void  add(User user);

    ActiveUsers findActiveUsers( );

    List<ArrayList<String>> findAllUsers();

    User findOne(String username);

    PageResult findPage(User user, Integer page, Integer rows);

    void updateStatus(Long id, String status);

    //登录加经验值
    void creatExperienceValue(String userName);

    List<Order> showUnPayOrders(String userName);
}
