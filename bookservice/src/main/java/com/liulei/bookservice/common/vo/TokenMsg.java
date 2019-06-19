package com.liulei.bookservice.common.vo;

import com.liulei.bookservice.book.vo.BlogUserVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    // 用户id
    private Integer id;

    // 用户头像路径
    private String path;

    // 用户账号
    private String code;

    // 用户名
    private String name;

    public TokenMsg setInfo(BlogUserVo user) {
        this.id = user.getUserId();
        this.path = user.getUserImgPath();
        this.code = user.getUserCode();
        this.name = user.getUserNickName();
        return this;
    }

}
