package com.liulei.book.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @auther runze
 * @date 2019/7/25 10:59
 */

@Data
public class HomeDataVo implements Serializable {

    private static final long serialVersionUID = 1L;

    //学习笔记
    private List<ArticleVo> studys = new ArrayList<>(0);

    //心情随笔
    private List<ArticleVo> essays = new ArrayList<>(0);

    //留言
    private List<LeavingMsgVo> msgs = new ArrayList<>(0);

    //友情链接
    private List<OuterLinkVo> shipLinks = new ArrayList<>(0);

    //项目服务路径
    private String serverPath;

}
