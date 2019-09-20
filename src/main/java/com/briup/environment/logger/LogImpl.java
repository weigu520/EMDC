package com.briup.environment.logger;

import org.apache.log4j.Logger;

/**
 * 描述:
 * 日志模块接口实现类
 *
 * @author WeiGu
 * @create 2019-09-20 14:19
 */
public class LogImpl implements Log{
    private static Logger logger=Logger.getRootLogger();

    @Override
    public void debug(String message) {
        logger.debug(message);
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void warn(String message) {
        logger.warn(message);
    }

    @Override
    public void error(String message) {
        logger.error(message);
    }

    @Override
    public void fatal(String message) {
        logger.fatal(message);
    }
}