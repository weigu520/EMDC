package com.briup.environment.store;

import com.briup.environment.bean.Env;

import java.util.List;

/**
 * 描述:
 * 入库接口
 *
 * @author WeiGu
 * @create 2019-09-17 11:05
 */
public interface Store {
    void store(List<Env> envs);
}
