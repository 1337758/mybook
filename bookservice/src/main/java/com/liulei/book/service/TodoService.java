package com.liulei.book.service;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.liulei.book.dao.TodoDao;
import com.liulei.book.po.Todo;
import com.liulei.book.vo.PageQueryVo;
import com.liulei.book.vo.TodoVo;
import com.liulei.common.util.ParamUtils;
import com.liulei.mybatis.paginator.Page;
import com.liulei.mybatis.paginator.PageParam;
import com.liulei.mybatis.util.Pages;
import com.liulei.mybatis.util.ResultPage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description
 * @auther runze
 * @date 2019/7/25 16:24
 */
@Service
@Transactional
public class TodoService {

    @Autowired
    private TodoDao todoDao;

    /**
     * @description: 获取待办任务表分页数据
     * @Author: runze
     * @Date: 2019/7/25 16:33
     */
    public ResultPage<TodoVo> getPageData(PageQueryVo pageQueryVo) {
        int pageNo = pageQueryVo.getPageNo();
        int size = pageQueryVo.getPageSize();
        PageParam pageParam = ParamUtils.getDescPageParam(pageNo, size > 0 ? size : 10, "update_time");
        TodoVo entry = new TodoVo();
        // 搜索条件
        if (StringUtils.isNotBlank(pageQueryVo.getQueryValue())) {
            // 描述的模糊匹配
            entry.setQueryValue(pageQueryVo.getQueryValue());
        }
        Page<Todo> page = todoDao.selectPageByVo(pageParam, entry);
        // 分页数据
        return Pages.convert(pageParam, page, TodoVo.class);
    }

    /**
     * @description: 新增文章
     * @Author: runze
     * @Date: 2019/7/25 16:33
     */
    public void addTodo(TodoVo todoVo) {
        Todo entity = new Todo();
        BeanUtils.copyProperties(entity, todoVo);
        if (entity.getId() != null) {
            todoDao.updateById(entity);
        } else {
            todoDao.insert(entity);
        }
    }

    /**
     * @description: 删除待办任务
     * @Author: runze
     * @Date: 2019/7/25 16:43
     */
    public void delTodo(Integer id) {
        todoDao.deleteById(id);
    }
}
