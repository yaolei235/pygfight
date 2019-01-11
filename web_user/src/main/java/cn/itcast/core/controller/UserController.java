package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.service.UserService;
import cn.itcast.core.util.PhoneFormatCheckUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    /**
     * 发送验证码
     * @param phone 手机号
     * @return
     */
    @RequestMapping("/sendCode")
    public Result sendCode(String phone) {
        try {
            if (phone == null || "".equals(phone)) {
                return new Result(false, "请正确填写手机号!");
            }
            if (!PhoneFormatCheckUtils.isPhoneLegal(phone)) {
                return new Result(false, "手机号格式非法!");
            }
            userService.sendCode(phone);
            return  new Result(true, "发送成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false, "发送失败!");
        }
    }

    /**
     * 用户注册
     * @param smscode   验证码
     * @param user      用户对象
     * @return
     */
    @RequestMapping("/add")
    public Result add(String smscode, @RequestBody User user) {
        try {
            //1. 初始化用户对象信息
            user.setCreated(new Date());
            user.setUpdated(new Date());
            user.setSourceType("1");
            user.setStatus("Y");

            //2. 验证验证码是否正确, 如果不正确直接返回错误信息
            Boolean isCheck = userService.checkSmsCode(user.getPhone(), smscode);
            if (!isCheck) {
                return new Result(false, "手机号或者验证码不正确!");
            }
            //3. 如果验证码正确保存用户信息
            userService.add(user);
            return  new Result(true, "注册成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false, "注册失败!");
        }
    }

    //展示该用户的未支付订单
    // 根据用户名 查询所有的订单 ,页面展示的是每个订单详情对象
    @RequestMapping("/showUnPay")
    public List<Order> showUnPay(){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        return userService.showUnPayOrders(userName);

    }

}
