package com.briup.environment.store;

import com.briup.environment.backup.BackUp;
import com.briup.environment.bean.Env;
import com.briup.environment.configue.ConfigFactory;
import com.briup.environment.logger.Log;
import com.briup.environment.logger.LogImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * 描述:
 * 入库接口实现类
 *
 * @author WeiGu
 * @create 2019-09-17 11:06
 */
public class StoreImpl implements Store{
    private static Properties prop= ConfigFactory.getProp();
    private static BackUp backUp=ConfigFactory.getBackUp();
    private static Log log=new LogImpl();

    @Override
    public void store(List<Env> envs) {
        Connection conn =null;
        int batchSize=Integer.parseInt(prop.getProperty("batchsize"));
        try {
            Class.forName(prop.getProperty("driver"));
            String url=prop.getProperty("url");
            String user=prop.getProperty("username");
            String password=prop.getProperty("password");
            conn = DriverManager.getConnection(url, user, password);

            String sql="insert into emdc.envs value(?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement pstat = conn.prepareStatement(sql);

            int count=1;

            //需要再JDBC中设置手动提交事务
            conn.setAutoCommit(false);

            //入库之前,需要从备份文件中加载上次未保存成功的集合对象,然后将该集合对象和本次保存的集合对象保存到一起
            Object o = backUp.load(prop.getProperty("storeenvs"));
            if (o != null) {
                if (o instanceof List) {
                    List<Env> storeEnvs = (List<Env>) o;
                    log.info("入库采集:" + storeEnvs.size());
                    envs.addAll(storeEnvs);
                }
            }
            log.info("本次入库:" + envs.size());

            for (Env env : envs) {
                pstat.setString(1,env.getSrcId());
                pstat.setString(2,env.getDestId());
                pstat.setString(3,env.getDevId());
                pstat.setString(4,env.getSensorId());
                pstat.setInt(5,env.getSensorCounter());
                pstat.setString(6,env.getCmdType());
                pstat.setString(7,env.getDataType().toString());
                pstat.setString(8,env.getData());
                pstat.setInt(9,env.getStatus());
                pstat.setLong(10,env.getTimestamp());

                pstat.addBatch();

                if(count++%batchSize==0) {
                    pstat.executeBatch();
                    conn.commit();
                }
            }
            pstat.executeBatch();
            conn.commit();
            pstat.close();
            conn.close();

        } catch (Exception e) {
//            e.printStackTrace();
            //备份
            log.warn("入库发送异常,正在备份数据");
            backUp.backUp(envs, prop.getProperty("storeenvs"));
            log.warn("备份完成");
            //事务回滚,已经提交的事务不能进行回滚
            if(conn!=null){
                try {
                    log.warn("事务正在回滚");
                    conn.rollback();
                    log.warn("事务回滚完成");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

        }


    }
}