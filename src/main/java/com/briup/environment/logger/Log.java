package com.briup.environment.logger;

/**
 * 描述:
 * 日志模块接口类
 *
 * @author WeiGu
 * @create 2019-09-20 14:17
 */
public interface Log {
    void debug(String message);
    void info(String message);
    void warn(String message);
    void error(String message);
    void fatal(String message);
}
