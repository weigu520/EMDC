package com.briup.environment.server;

import com.briup.environment.bean.Env;
import com.briup.environment.configue.ConfigFactory;
import com.briup.environment.logger.Log;
import com.briup.environment.logger.LogImpl;
import com.briup.environment.store.Store;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Properties;

/**
 * 描述:
 * 服务器接口实现类
 *
 * @author WeiGu
 * @create 2019-09-17 14:40
 */
public class ServerImpl implements Server{
    private static Properties prop= ConfigFactory.getProp();
    private static Log log=new LogImpl();

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(Integer.parseInt(prop.getProperty("port")));
        log.info("服务器启动了");
        while (true){
            Socket socket = ss.accept();
            log.info(socket.getInetAddress().getHostAddress()+"连接");
            new Thread(()->{
                try {
                    InputStream is = socket.getInputStream();
                    ObjectInputStream ois = new ObjectInputStream(is);
                    List<Env> envs = ConfigFactory.getServer().receive(ois);
                    Store store = ConfigFactory.getStore();
                    store.store(envs);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    @Override
    public List<Env> receive(ObjectInputStream ois) {
        try {
            Object o = ois.readObject();
            if(o instanceof List) {
                List<Env> envs= (List<Env>) o;
                return envs;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void close() {
    }
}