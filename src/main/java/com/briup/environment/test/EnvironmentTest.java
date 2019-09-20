package com.briup.environment.test;

import com.briup.environment.bean.Env;
import com.briup.environment.bean.Environment;
import com.briup.environment.gather.Gather;
import com.briup.environment.gather.GatherImpl;
import com.briup.environment.store.Store;
import com.briup.environment.store.StoreImpl;

import java.util.Collection;
import java.util.List;

/**
 * 描述:
 *
 * @author WeiGu
 * @create 2019-09-16 18:17
 */
public class EnvironmentTest {
    public static void main(String[] args) throws Exception {
//        GatherImpl gather = new GatherImpl();
//        Collection<Environment> gather1 = gather.gather();
//        gather1.forEach(list->System.out.println(list));
        Gather gather=new GatherImpl();
        List<Env> gather1 = gather.gather("radwtmp");
        Store store=new StoreImpl();
        store.store(gather1);
    }
}