package com.briup.environment.client;

import com.briup.environment.backup.BackUp;
import com.briup.environment.bean.Env;
import com.briup.environment.configue.ConfigFactory;
import com.briup.environment.gather.Gather;
import com.briup.environment.logger.Log;
import com.briup.environment.logger.LogImpl;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Properties;

/**
 * 描述:
 * 客户端接口实现类
 *
 * @author WeiGu
 * @create 2019-09-17 14:26
 */
public class ClientImpl implements Client {
    private Socket socket;
    private BackUp backUp;
    private static Properties prop= ConfigFactory.getProp();
    private static Log log=new LogImpl();

    public ClientImpl() {
        this.backUp = ConfigFactory.getBackUp();
    }

    public static void main(String[] args) {
        Client client = ConfigFactory.getClient();

        Gather gather = ConfigFactory.getGather();

        List<Env> envs = gather.gather(prop.getProperty("radwtmp"));

        client.send(envs);

        client.close();
    }

    @Override
    public void send(List<Env> envs) {
        OutputStream os = null;
        ObjectOutputStream oos = null;
        try {
            this.socket=new Socket(prop.getProperty("ip"),Integer.parseInt(prop.getProperty("port")));
            //发送之前,需要从文件中读取备份的集合对象,和当前需要发送的集合对象并到一起发送给服务器
            //如果再发送至服务器的过程中出现了异常,则需要备份本次需要的集合对象
            Object o = this.backUp.load(prop.getProperty("clientsendenvs"));
            if (o != null) {
                if (o instanceof List) {
                    List<Env> backUpedEnvs = (List<Env>) o;
                    log.info("备份采集:" + backUpedEnvs.size());
                    envs.addAll(backUpedEnvs);
                }
            }
            log.info("本次采集:" + envs.size());
            os = this.socket.getOutputStream();
            oos = new ObjectOutputStream(new BufferedOutputStream(os));
            oos.writeObject(envs);
            oos.flush();
        } catch (Exception e) {
//            e.printStackTrace();
            log.warn("采集异常,正在备份发送的数据");
            //需要备份当前正在发送的List集合
            this.backUp.backUp(envs, prop.getProperty("clientsendenvs"));
            log.warn("备份完成");
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            log.info("采集完成");
        }
    }

    @Override
    public void close() {
        try {
            if (this.socket != null) this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}