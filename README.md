# EMDC
shit物联网环境监测系统
# 物联网环境监测系统

## 介绍

使用了所学的java,mysql,xml内容实现了简单环境监测系统

## 采集模块：gather包

​	1.分析radwtmp文件(该文件为数据文件,原始实验箱数据)，将数据按行封装成Environment对象；在bean包的Env类
​	2.将多个Environment对象存储到集合中；

## 入库模块：store包

​	1.创建表:src_id,dist_id,dev_id,sensor_id,counter,cmd_type,data,status,gather_time
​	2.存储到数据库中，将接收到的存放Environment对象的集合中的所有Environment对象保存至数据库；

​	sql语句如下:

```mysql
create database emdc;
use emdc;

create table envs(
    src_id varchar(10),
    dest_id varchar(10),
    dev_id varchar(10),
    sensor_id varchar(10),
    sensor_counter int(3),
    cmd_type varchar(10),
    data_type enum('CO2','ILL','HUM','TMP'),
    data varchar(10),
    status int(3),
    gather_time bigint
);
select * from envs;
select count(*) from emdc.envs e;
delete from emdc.envs e;
```



## 网络模块——发送端/客户端：client包

​	1.调用采集模块程序，获取存放Environment对象的集合对象；
​	2.将上述集合对象发送至服务器端；

## 网络模块——接收端/服务器端：server包

​	1.接收客户端发送的集合；
​	2.调用入库模块的程序；
​	3.多线程程序

## 备份模块：backup包

​	1.备份已经采集过的数据，例如已经采集过的字节数，或者已经采集过的行数，在下次采集之前需要跳过这些数据；
​		1.在采集之前，读取文件内容，转化为行数或者字节数，然后跳过；
​		2.在采集结束，将本次采集过的字节数或者行数做备份；

2.在客户端的发送程序中，在处理异常的时候备份当前正在发送的List集合，在下次采集时获取该备份数据形成List集合再次发送；
	1.发送之前，需要从文件中读取备份的集合对象，和当前需要发送的集合对象合并到一起发送给服务器；

​	2.如果在发送至服务器的过程中出现了异常，则备份本次需要发送的集合对象；

3.如果保存至数据库出现异常，则需要备份，并且数据库事务回滚；
	0.事务需要设置手动提交；
	1.在保存至数据库之前先加载备份的数据；
	2.保存至数据库出现异常，则备份数据，回滚事务；	

## 配置模块：configue包

​	1.对程序中出现的一些字符串【并不仅仅是字符串】数据，程序中有可能需要修改或者变化的数据已配置文件的方式提供；
​	2.对程序中所创建的对象对应的实现类，以配置文件的形式提供类全名，在程序中以反射的方式提供该实现类的对象；

​	3.解析emdc.xml文档,该文档的内容如下(**并不是良构的xml文档,所以标签顺序有要求:主要是由于业务依赖关系**):

```xml
<emdc>
    <BackUp class="com.briup.environment.backup.BackUpImpl">
        <rootpath>G:\\emdc</rootpath>
        <clientsendenvs>client_send_envs.bak</clientsendenvs>
        <gatheredlines>gathered_lines.bak</gatheredlines>
        <storeenvs>store_envs.bak</storeenvs>
    </BackUp>
    <Client class="com.briup.environment.client.ClientImpl">
        <ip>127.0.0.1</ip>
        <port>8888</port>
    </Client>
    <Server class="com.briup.environment.server.ServerImpl">
        <port>8888</port>
    </Server>
    <Gather class="com.briup.environment.gather.GatherImpl">
        <radwtmp>G:\BD1903\SmartHome\code\radwtmp2</radwtmp>
    </Gather>
    <Store class="com.briup.environment.store.StoreImpl">
        <driver>com.mysql.cj.jdbc.Driver</driver>
        <url>jdbc:mysql://127.0.0.1:8017/emdc?rewriteBatchedStatements=true</url>
        <username>root</username>
        <password>root</password>
        <batchsize>1000</batchsize>
    </Store>
</emdc>
```

## 日志模块：logger包

1.使用log4j进行日志的控制台输出和输出到文件中;

2.log4j.properties文件为配置文件,主要配置log4j的一些日志等级和自定义输出格式内容

