package com.briup.environment.configue;

import com.briup.environment.backup.BackUp;
import com.briup.environment.client.Client;
import com.briup.environment.gather.Gather;
import com.briup.environment.server.Server;
import com.briup.environment.store.Store;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 描述:
 * 使用工厂模式创建各个类的对象
 *
 * @author WeiGu
 * @create 2019-09-20 9:15
 */
public class ConfigFactory {
    private static Map<String,Object> map=new HashMap<>();
    private static Properties prop=new Properties();
    static {
        SAXReader reader = new SAXReader();
        try {
            Document doc = reader.read("G:\\BD1903\\SmartHome\\code\\src\\main\\resources\\emdc.xml");

            List<Element> es = doc.getRootElement().elements();

            es.forEach(element -> {
                try {
                    List<Element> els = element.elements();
                    els.forEach(e -> {
                        String propName = e.getName();
                        String propValue = e.getStringValue();
                        prop.setProperty(propName,propValue);
                    });

                    Attribute attr = element.attribute("class");
                    String value = attr.getStringValue();
                    Object o = Class.forName(value).newInstance();
                    map.put(element.getName(),o);

                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });

//            map.forEach((k,v)->{
//                System.out.println(k+"::"+v);
//            });
//
//            prop.forEach((k,v)->{
//                System.out.println(k+"="+v);
//            });

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public static Properties getProp(){
        return prop;
    }

    public static Client getClient(){
        return (Client) map.get("Client");
    }
    public static Server getServer(){
        return (Server) map.get("Server");
    }
    public static Gather getGather(){
        return (Gather) map.get("Gather");
    }
    public static Store getStore(){
        return (Store) map.get("Store");
    }
    public static BackUp getBackUp(){
        return (BackUp) map.get("BackUp");
    }

}