package com.briup.environment.server;

import com.briup.environment.bean.Env;

import java.io.ObjectInputStream;
import java.util.List;

/**
 * 描述:
 * 服务器接口
 *
 * @author WeiGu
 * @create 2019-09-17 14:37
 */
public interface Server {
    List<Env> receive(ObjectInputStream ois);
    void close();
}
