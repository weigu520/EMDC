package com.briup.environment.client;

import com.briup.environment.bean.Env;

import java.util.List;

/**
 * 描述:
 * 客户端接口
 *
 * @author WeiGu
 * @create 2019-09-17 14:24
 */
public interface Client {
    void send(List<Env> envs);
    void close();
}
