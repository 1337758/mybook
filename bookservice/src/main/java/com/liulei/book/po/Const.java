package com.liulei.book.po;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 对应表名：const
 */
@Data
public class Const implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应字段：id,备注：主键
     */
    private Integer id;

    /**
     * 对应字段：depict,备注：描述
     */
    private String depict;

    /**
     * 对应字段：code,备注：常量代码
     */
    private String code;

    /**
     * 对应字段：val,备注：常量值
     */
    private String val;

    /**
     * 对应字段：status,备注：状态
     */
    private String status;

    /**
     * 对应字段：insert_time,备注：插入时间
     */
    private Date insertTime;

    /**
     * 对应字段：update_time,备注：更新时间
     */
    private Date updateTime;

}
