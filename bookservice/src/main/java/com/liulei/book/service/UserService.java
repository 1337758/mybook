package com.liulei.book.service;

import com.liulei.book.dao.UserDao;
import com.liulei.book.po.User;
import com.liulei.book.vo.BlogUserVo;
import com.liulei.common.exception.CustomUnauthorizedException;
import com.liulei.common.util.EncryptUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service(value = "userService")
@Transactional
public class UserService {

    @Autowired
    private UserDao userDao;


    public BlogUserVo userLogin(BlogUserVo blogUserVo) {
        if (null == blogUserVo || StringUtils.isEmpty(blogUserVo.getUserCode()) || StringUtils.isEmpty(blogUserVo.getPassword())) {
            throw new CustomUnauthorizedException("input is null");
        }
        String password = EncryptUtils.getMD5(blogUserVo.getPassword());
        User user = userDao.findUserByUserNameAndPassword(blogUserVo.getUserCode(), password);
        if (null != user) {
            blogUserVo.setUserCode(user.getUserCode());
            blogUserVo.setUserNickName(user.getUserNickName());
            blogUserVo.setUserImgPath(user.getUserImgPath());
            blogUserVo.setUserId(user.getUserId());
        }
        return blogUserVo;
    }


    public int userRegist(BlogUserVo blogUserVo) {
        if (null == blogUserVo || StringUtils.isEmpty(blogUserVo.getUserCode()) || StringUtils.isEmpty(blogUserVo.getPassword())) {
            throw new CustomUnauthorizedException("input is null");
        }
        User user = new User();
        BeanUtils.copyProperties(blogUserVo, user);
        String md5password = EncryptUtils.getMD5(blogUserVo.getPassword());
        user.setPassword(md5password);
        int insert = userDao.insert(user);
        return insert;
    }


    public Boolean checkCode(String userCode) {
        if (null == userCode) {
            throw new CustomUnauthorizedException("input is null");
        }
        User user = userDao.findUserByUserCode(userCode);
        return user != null;
    }

    public User getUser(String userCode) {
        if (null == userCode) {
            throw new CustomUnauthorizedException("userCode is null");
        }
        User user = userDao.findUserByUserCode(userCode);
        return user;
    }
}
