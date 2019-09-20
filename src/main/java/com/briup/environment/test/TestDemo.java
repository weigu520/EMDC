package com.briup.environment.test;

import com.briup.environment.bean.Env;
import com.briup.environment.client.ClientImpl;
import com.briup.environment.gather.Gather;
import com.briup.environment.gather.GatherImpl;
import com.briup.environment.server.ServerImpl;
import com.briup.environment.store.Store;
import com.briup.environment.store.StoreImpl;
import org.junit.Test;

import java.util.List;

/**
 * 描述:
 * 测试采集端
 *
 * @author WeiGu
 * @create 2019-09-17 10:02
 */
public class TestDemo {
    @Test
    public void testGather(){
        Gather gather=new GatherImpl();
        List<Env> envs = gather.gather("radwtmp");
        envs.forEach(System.out::println);
    }
    @Test
    public void testStore(){
        Gather gather=new GatherImpl();
        List<Env> gather1 = gather.gather("radwtmp");
        Store store=new StoreImpl();
        store.store(gather1);
    }
    @Test
    public void testClient(){
        Gather gather=new GatherImpl();
        List<Env> envs = gather.gather("radwtmp");
        ClientImpl client = new ClientImpl();
        client.send(envs);
    }
    @Test
    public void testServer(){
        ServerImpl server = new ServerImpl();
//        List<Env> receive = server.receive();
//        receive.forEach(System.out::println);
        server.close();
    }
}