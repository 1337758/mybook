package com.liulei.common.util;

import com.liulei.mybatis.paginator.Order;
import com.liulei.mybatis.paginator.PageParam;

import java.util.List;

/**
 * @description: 分页组件工具类
 * @Author: runze
 * @Date: 2019/7/25 11:41
 */
public class ParamUtils {

    /**
     * @param page：当前页号
     * @param size：数量
     * @return
     * @description 初始化PageParam
     * @author YangHao
     * @date 2018年9月3日-下午11:21:51
     */
    public static PageParam getPageParam(int page, int size) {
        PageParam pageParam = new PageParam();
        pageParam.setPage(page);
        pageParam.setLimit(size);
        return pageParam;
    }

    /**
     * @param page：当前页号
     * @param size：数量
     * @param orderCol：排序的列
     * @return
     * @description 初始化PageParam
     * @author YangHao
     * @date 2018年9月3日-下午11:21:51
     */
    public static PageParam getAscPageParam(int page, int size, String orderCol) {
        PageParam pageParam = new PageParam(page, size);
        List<Order> orders = Order.formString(orderCol + ".asc");
        pageParam.setOrders(orders);
        return pageParam;
    }

    /**
     * @param page：当前页号
     * @param size：数量
     * @param orderCol：排序的列
     * @return
     * @description 初始化PageParam
     * @author YangHao
     * @date 2018年9月3日-下午11:21:51
     */
    public static PageParam getDescPageParam(int page, int size, String orderCol) {
        PageParam pageParam = new PageParam();
        pageParam.setPage(page);
        pageParam.setLimit(size);
        List<Order> orders = Order.formString(orderCol + ".desc");
        pageParam.setOrders(orders);
        return pageParam;
    }

    /**
     * @param size：数量
     * @return
     * @description 初始化PageParam
     * @author YangHao
     * @date 2018年9月3日-下午11:21:51
     */
    public static PageParam getPageParam(int size) {
        return getPageParam(PageParam.NO_PAGE, size);
    }

    /**
     * @param size：数量
     * @param orderCol：排序的列
     * @return
     * @description 初始化PageParam
     * @author YangHao
     * @date 2018年9月3日-下午11:21:51
     */
    public static PageParam getAscPageParam(int size, String orderCol) {
        return getAscPageParam(PageParam.NO_PAGE, size, orderCol);
    }

    /**
     * @param size：数量
     * @param orderCol：排序的列
     * @return
     * @description 初始化PageParam
     * @author YangHao
     * @date 2018年9月3日-下午11:21:51
     */
    public static PageParam getDescPageParam(int size, String orderCol) {
        return getDescPageParam(PageParam.NO_PAGE, size, orderCol);
    }

}
