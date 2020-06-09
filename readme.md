#### win10使用wsl2,maven构件docker
> 在win10子系统linux中不能访问主机地址，但是主机可以访问linux中开放的端口服务
```shell
1. docker之间的访问用link形式
docker run -d --name spring-boot-user01 -e discoveryServer=nacos:8080 --link nacos:nacos yiyun8/spring-boot-user:1.0-SNAPSHOT

2. maven构件只在linux中进行，在主机win10下访问不了linux中的docker服务(暂时不知道怎么配置)
mvn clean package spring-boot:repackage dockerfile:build -Dmaven.test.skip=true -s /mnt/f/maven/apache-maven-3.3.9/conf/settings-wsl2.xml

3. 启动gateway
docker run -d --name spring-boot-gateway -p8040:8080 --link nacos:nacos --link mysqlMaster:mysqlServer yiyun8/spring-boot-gateway:1.0-SNAPSHOT

4. idea远程连接win10子系统linux中docker进行debug,添加jvmDebugPort变量
   1）docker run -d --name spring-boot-user01 -ejvmDebugPort=8719 -p8719:8719 --link nacos:nacos yiyun8/spring-boot-user:1.0-SNAPSHOT
   2）idea配置远程debug Host要配置linux的ip,配置localhost或是127.0.0.1是连接不上的。
```


