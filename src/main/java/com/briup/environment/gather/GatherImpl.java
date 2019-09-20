package com.briup.environment.gather;

import com.briup.environment.backup.BackUp;
import com.briup.environment.bean.DataType;
import com.briup.environment.bean.Env;
import com.briup.environment.configue.ConfigFactory;
import com.briup.environment.logger.Log;
import com.briup.environment.logger.LogImpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 描述:
 * 采集接口实现类
 *
 * @author WeiGu
 * @create 2019-09-17 9:32
 */
public class GatherImpl implements Gather {
    private static Properties prop= ConfigFactory.getProp();
    private BackUp backUp;
    private static Log log=new LogImpl();

    public GatherImpl() {
        this.backUp = ConfigFactory.getBackUp();
    }

    public void setData(Env env, String[] strs) {
        env.setSrcId(strs[0]);
        env.setDestId(strs[1]);
        env.setDevId(strs[2]);
        env.setSensorId(strs[3]);
        env.setSensorCounter(Integer.parseInt(strs[4]));
        env.setCmdType(strs[5]);
        env.setStatus(Integer.parseInt(strs[7]));
        env.setTimestamp(Long.parseLong(strs[8]));
    }


    @Override
    public List<Env> gather(String filePath) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            List<Env> envs = new ArrayList<>();
            //在采集之前,读取备份文件,转化成行数,然后跳过之后再进行采集
            String backUpFileName = prop.getProperty("gatheredlines");
            Object o = this.backUp.load(backUpFileName);
            int gatheredLines = 0;
            if (o != null) {
                if (o instanceof Integer) gatheredLines = (int) o;
            }

            log.info("需要跳过的行数:"+gatheredLines);
            int count=gatheredLines;
            //本次采集的行数
            int currentGatheredLines = 0;

            while ((line = br.readLine()) != null) {
                //跳过已经采集过的数据,行数
                if (count-- > 0) continue;
                System.out.println(line);
                //记录本次采集的行数
                currentGatheredLines++;

                String[] strs = line.split("[|]");


                if ("16".equals(strs[3])) {
                    //温度
                    Env env = new Env();
                    this.setData(env, strs);
                    env.setDataType(DataType.TMP);
                    float f1 = (float) ((Integer.parseInt(strs[6].substring(0, 4), 16) * 0.00268127) - 46.85);
                    String tmpData = String.format("%.2f", f1);
                    env.setData(tmpData);
                    envs.add(env);

                    //或湿度
                    Env env1 = new Env();
                    this.setData(env1, strs);
                    env1.setDataType(DataType.HUM);
                    float f2 = (float) ((Integer.parseInt(strs[6].substring(4, 8), 16) * 0.00190735) - 6);
                    String humData = String.format("%.2f", f2);
                    env1.setData(humData);
                    envs.add(env1);
                }
                if ("256".equals(strs[3])) {
                    //光照强度
                    Env env2 = new Env();
                    this.setData(env2, strs);
                    env2.setDataType(DataType.ILL);
                    env2.setData(String.valueOf(Integer.parseInt(strs[6], 16)));
                    envs.add(env2);
                }
                if ("1280".equals(strs[3])) {
                    //二氧化碳浓度
                    Env env3 = new Env();
                    this.setData(env3, strs);
                    env3.setDataType(DataType.CO2);
                    env3.setData(String.valueOf(Integer.parseInt(strs[6], 16)));
                    envs.add(env3);
                }
            }

            //备份行数:本次采集加上次采集gatheredLines+currentGatheredLines
            int allLines = gatheredLines+currentGatheredLines;
            this.backUp.backUp(allLines,backUpFileName);

            return envs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        Gather gather=ConfigFactory.getGather();
        gather.gather(prop.getProperty("radwtmp"));
    }
}