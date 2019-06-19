package com.liulei.bookservice.book.service;


import com.liulei.bookservice.book.vo.BlogUserVo;

public interface UserService {
    BlogUserVo userLogin(BlogUserVo blogUserVo);
}
