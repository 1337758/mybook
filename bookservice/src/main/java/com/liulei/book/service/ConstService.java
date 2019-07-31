package com.liulei.book.service;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.liulei.book.dao.ConstDao;
import com.liulei.book.po.Const;
import com.liulei.book.vo.ConstVo;
import com.liulei.book.vo.PageQueryVo;
import com.liulei.common.util.ParamUtils;
import com.liulei.mybatis.paginator.Page;
import com.liulei.mybatis.paginator.PageParam;
import com.liulei.mybatis.util.Pages;
import com.liulei.mybatis.util.ResultPage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @description
 * @auther runze
 * @date 2019/7/25 16:01
 */
@Service
@Transactional
public class ConstService {

    @Autowired
    private ConstDao constDao;

    /**
     * @description: 获取常量表分页数据
     * @Author: runze
     * @Date: 2019/7/25 16:03
     */
    public ResultPage<ConstVo> getPageData(PageQueryVo pageQueryVo) {
        int pageNo = pageQueryVo.getPageNo();
        int size = pageQueryVo.getPageSize();
        PageParam pageParam = ParamUtils.getDescPageParam(pageNo, size > 0 ? size : 10, "update_time");
        ConstVo entry = new ConstVo();
        // 搜索条件
        if (StringUtils.isNotBlank(pageQueryVo.getQueryValue())) {
            // 描述的模糊匹配
            entry.setQueryValue(pageQueryVo.getQueryValue());
        }
        Page<Const> page = constDao.selectPageByVo(pageParam, entry);
        // 分页数据
        return Pages.convert(pageParam, page, ConstVo.class);
    }

    /**
     * @description: 新增文章
     * @Author: runze
     * @Date: 2019/7/25 16:09
     */
    public void addConst(ConstVo constVo) {
        Const entity = new Const();
        BeanUtils.copyProperties(entity, constVo);
        if (entity.getId() != null) {
            constDao.updateById(entity);
        } else {
            constDao.insert(entity);
        }
    }

    /**
     * @description: 获取文章类型和分类
     * @Author: runze
     * @Date: 2019/7/25 16:09
     */
    public List<ConstVo> getArticleTypes() {
        List<ConstVo> typeVos = new ArrayList<>(0);
        // 获取模块
        List<Const> modules = constDao.findAllByDepict("文章类型");
        for (Const module : modules) {
            // 类型
            List<Const> types = constDao.findAllByDepict(module.getVal());
            if (types.isEmpty()) { // 没有子类型，则添加模块
                ConstVo moduleVo = new ConstVo();
                BeanUtils.copyProperties(moduleVo, module);
                typeVos.add(moduleVo);
            } else {
                for (Const type : types) {
                    ConstVo typeVo = new ConstVo();
                    BeanUtils.copyProperties(typeVo, type);
                    // 分类
                    List<Const> classifys = constDao.findAllByDepict(type.getVal());
                    List<ConstVo> classifyVos = new ArrayList<>(0);
                    for (Const classify : classifys) {
                        ConstVo classifyVo = new ConstVo();
                        BeanUtils.copyProperties(classifyVo, classify);
                        classifyVos.add(classifyVo);
                    }
                    typeVo.setChildren(classifyVos);
                    typeVos.add(typeVo);
                }
            }
        }
        return typeVos;
    }

    /**
     * @description: 获取全部文章类型
     * @Author: runze
     * @Date: 2019/7/25 16:11
     */
    public List<ConstVo> getAllArticleTypes(String depict) {
        List<ConstVo> moduleVos = new ArrayList<>(0);
        // 获取模块
        List<Const> modules = constDao.findAllByDepict(depict);
        for (Const module : modules) {
            ConstVo moduleVo = new ConstVo();
            BeanUtils.copyProperties(moduleVo, module);
            // 类型
            List<Const> types = constDao.findAllByDepict(module.getVal());
            List<ConstVo> typeVos = new ArrayList<>(0);
            for (Const type : types) {
                ConstVo typeVo = new ConstVo();
                BeanUtils.copyProperties(typeVo, type);
                // 分类
                List<Const> classifys = constDao.findAllByDepict(type.getVal());
                List<ConstVo> classifyVos = new ArrayList<>(0);
                for (Const classify : classifys) {
                    ConstVo classifyVo = new ConstVo();
                    BeanUtils.copyProperties(classifyVo, classify);
                    classifyVos.add(classifyVo);
                }
                typeVo.setChildren(classifyVos);
                typeVos.add(typeVo);
            }
            moduleVo.setChildren(typeVos);
            moduleVos.add(moduleVo);
        }
        return moduleVos;
    }

    /**
     * @description: 删除常量
     * @Author: runze
     * @Date: 2019/7/25 16:12
     */
    public void delConst(Integer id) {
        constDao.deleteById(id);
    }
}
