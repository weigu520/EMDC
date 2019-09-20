package com.briup.environment.backup;

import com.briup.environment.configue.ConfigFactory;
import com.briup.environment.logger.Log;
import com.briup.environment.logger.LogImpl;

import java.io.*;
import java.util.Properties;

/**
 * 描述:
 * 备份接口的实现类
 *
 * @author WeiGu
 * @create 2019-09-19 9:12
 */
public class BackUpImpl implements BackUp{
    private static Properties prop= ConfigFactory.getProp();
    private static final String ROOT_PATH=prop.getProperty("rootpath");
    private File rootDir;
    private static Log log=new LogImpl();

    public BackUpImpl() {
        this.rootDir=new File(ROOT_PATH);
        if(!rootDir.exists()) rootDir.mkdirs();
    }

    @Override
    public void backUp(Object o, String fileName) {
        if(o == null) {
            log.warn("备份的对象不能为空！！！");
            return;
        }
        try {
            File file = new File(this.rootDir, fileName);
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(o);
            oos.flush();
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object load(String fileName) {
        FileInputStream fis=null;
        ObjectInputStream ois=null;
        try {
            File file = new File(rootDir, fileName);
            if(!file.exists()) return null;
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            return ois.readObject();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            if(ois!=null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fis!=null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}