package com.briup.environment.backup;

/**
 * 描述:
 * 备份模块接口
 *
 * @author WeiGu
 * @create 2019-09-19 9:09
 */
public interface BackUp {
    //将对象o备份至fileName中
    void backUp(Object o,String fileName);
    //从文件fileName中读取备份数据o
    Object load(String fileName);
}
