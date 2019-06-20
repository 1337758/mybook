package com.liulei.bookservice.book.service.impl;

import com.liulei.bookservice.book.dao.UserDao;
import com.liulei.bookservice.book.service.UserService;
import com.liulei.bookservice.book.vo.BlogUserVo;
import com.liulei.bookservice.book.vo.User;
import com.liulei.bookservice.common.exception.CustomUnauthorizedException;
import com.liulei.bookservice.common.util.EncryptUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service(value = "userServiceImpl")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
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

    @Override
    public int userRegist(BlogUserVo blogUserVo) {
        if (null == blogUserVo || StringUtils.isEmpty(blogUserVo.getUserCode()) || StringUtils.isEmpty(blogUserVo.getPassword())) {
            throw new CustomUnauthorizedException("input is null");
        }
        User user = new User();
        BeanUtils.copyProperties(user, blogUserVo);
        String md5password = EncryptUtils.getMD5(blogUserVo.getPassword());
        user.setPassword(md5password);
        int insert = userDao.insert(user);
        return insert;
    }

    @Override
    public Boolean checkCode(String userCode) {
        if (null == userCode) {
            throw new CustomUnauthorizedException("input is null");
        }
        User user = userDao.findUserByUserCode(userCode);
        return user != null;
    }
}
