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

//    @Reference
//    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //定义权限集合
        List<GrantedAuthority> authList = new ArrayList<>();
        
//        //用户登录时候,经验值加1
//        if (username!=null){
//
//            UserQuery query = new UserQuery();
//            UserQuery.Criteria criteria = query.createCriteria();
//            criteria.andUsernameEqualTo(username);
//
//            //获取该用户民对应的用户(列表)
//            List<cn.itcast.core.pojo.user.User> userList = userDao.selectByExample(query);
//
//            for (cn.itcast.core.pojo.user.User user : userList) {
//                //经验值加1
//                user.setExperienceValue(user.getExperienceValue()+1);
//                //更新到数据库
//                userDao.updateByPrimaryKeySelective(user);
//            }
//        }

        //向权限集合中加入访问权限
        authList.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new User(username, "", authList);
    }
}
