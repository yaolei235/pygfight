package cn.itcast.core.service;

import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.pojo.user.UserQuery;
import com.alibaba.dubbo.config.annotation.Reference;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义认证类:
 * springSecurity和cas集成后, 用户名, 密码的判断认证工作交给cas来完成
 * springSecurity只负责cas验证完后, 给用户赋权工作
 * 如果能进入到这个实现类, 说明cas已经认证通过, 这里只做赋权操作
 */
public class UserDetailServiceImpl implements UserDetailsService {


    @Reference
    private UserService userService;





    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //定义权限集合
        List<GrantedAuthority> authList = new ArrayList<>();

        authList.add(new SimpleGrantedAuthority("ROLE_USER"));
        //用户登录时候,经验值加1


        //1. 如果获取的数据为空则证明用户名输入错误, 如果能获取到数据, 将用户名, 密码返回并且给这个用户赋予对应的访问权限
        if (username!=null) {

            if (userService.findOne(username) != null) {
                //判断用户权限状态
                if ("1".equals(userService.findOne(username).getAuditstatus())) {
                    //用户登录的话 增加经验值
                    userService.creatExperienceValue(username);
                    return new User(username, userService.findOne(username).getPassword(), authList);
                }
            }
        }
        return null;

    }
}
