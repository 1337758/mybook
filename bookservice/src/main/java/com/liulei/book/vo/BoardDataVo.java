package com.liulei.book.vo;

import com.liulei.mybatis.util.ResultPage;
import lombok.Data;

import java.io.Serializable;

@Data
public class BoardDataVo implements Serializable {

    private static final long serialVersionUID = 1L;

    //学习笔记及其分页信息
    private ResultPage<LeavingMsgVo> msgPage;

}
