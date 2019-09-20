package com.briup.environment.gather;

import com.briup.environment.bean.Env;

import java.util.List;

/**
 * 描述:采集接口
 *
 * @author WeiGu
 * @create 2019-09-17 9:30
 */
public interface Gather {
    List<Env> gather(String filePath);
}
