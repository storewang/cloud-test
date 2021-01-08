--- web socket 消息发送---
1. 登录:  返回ws.token
> ws.token:   uid:uname:longTime

- ws握手时: 验证accesstoken有效性
- 返回用户上线通知: userCode ==> token:userId:channelId
- onlineNum:     redis只记录在线数量 
- 每台机器连接量: 在每台机器上记录连接数.
- redis记录服务器信息 host+webPort
- 每台机器记录客户端建立连接的对应session信息
  userCode: host+port+token+channelId+channel
- redis保存用户与web host的对应关系
  userCode: host+webPort
  
2. 发送消息
> from:userCode,to:userCode,msg:"",msgType:""
- 根据userCode,知道接收者是否连接在本地机器上,如果没有，把消息转发到其他机器上.

3. 连接信息存储
- 如果是消息定时推送,需要根据用户id查找到对应的ip:port, 
- 如果十万用户，就要存储十万个用户id对应的机器信息，
- 如果我们只用3台机器，把消息都发过去，在本机上进行查询。按ip:port:uid为key查看本地连接的session有没有。有就发送，没有就忽略,
- 这里每台机器上需要存储用户连接的session对应关系（这个信息没有办法去掉，因为需要给接收者发送消息）
- 这里可以给每台机器的连接数量限定。超出时断开连接，让客户端去重连。
- 这样就不用在redis中存储这么多用户id数据了，只要在本地记录ip:port:uid应用的连接session

4. 用户退出或是断连
- redis中的在线数量减1, 
- 当前连接的机器上的记录清除对应的ip:port:uid记录,
- 当前机器上的连接数减1

5. 需要扩展websocket自动注册到注册中心
- nacos注册中心实现
- 要不能使用gateway进行转时没法配置到ws上的端口。
```text
  要额外添加websocket服务注册到注册中心
  1. WebSocketNacosAutoServiceRegistration 继承AbstractAutoServiceRegistration
  2. WebSocketNacosServiceRegistry实现ServiceRegistry
  3. 启动类上添加排除,取消springCloud和nacos的服务自动注册功能
     @SpringBootApplication(exclude = {AutoServiceRegistrationAutoConfiguration.class,NacosDiscoveryAutoConfiguration.class})
  4. 添加自定义配置: NettyWebsocketConf
  PS: 这样业务服务和websocket服务就同时注册到注册中心上去了。
     （这个就可以区分http的微服务和websocket的服务(另一个端口服务,如果websocket的端口和web服务的端口一致，是不需要另外添加注册),
       这样在springGateWay进行路由配置时就可以区分配置了，要不能gateWay进行路由转发时会使用web服务的端口，从而转发失败。
      ）  
```

6. nacos注册中心扩展参考
- NacosAutoServiceRegistration 主要继承了AbstractAutoServiceRegistration
- spring cloud的自动注册功能主要是在监听WebServerInitializedEvent事件后开始进行注册的
- AbstractAutoServiceRegistration => onApplicationEvent => bind() => start() => register() => serviceRegistry.register(registration)
- nacos扩展的自动注册主要是三个类,NacosServiceRegistry,NacosRegistration,NacosAutoServiceRegistration
- 服务与nacos注册中心保持的心跳
```text
    1. NacosWatch这个类实现了SmartLifecycle 
       NacosDiscoveryClientAutoConfiguration => NacosWatch => start() => nacosServicesWatch() => publishEvent(HeartbeatEvent)
    
    SmartLifecycle介绍:
       SmartLifecycle 是一个接口。当Spring容器加载所有bean并完成初始化之后，会接着回调实现该接口的类中对应的方法(start()方法)
       1) public void start(): 
       I.  我们主要在该方法中启动任务或者其他异步服务，比如开启MQ接收消息<br/>
       II. 当上下文被刷新（所有对象已被实例化和初始化之后）时，将调用该方法，默认生命周期处理器将检查每个SmartLifecycle对象的isAutoStartup()方法返回的布尔值。
           如果为“true”，则该方法会被调用，而不是等待显式调用自己的start()方法。
       2) public int getPhase():
       I.  如果工程中有多个实现接口SmartLifecycle的类，则这些类的start的执行顺序按getPhase方法返回值从小到大执行。<br/>
       II. 例如：1比2先执行，-1比0先执行。 stop方法的执行顺序则相反，getPhase返回值较大类的stop方法先被调用，小的后被调用。
       3) public boolean isAutoStartup()
       I.  根据该方法的返回值决定是否执行start方法。<br/>
       II. 返回true时start方法会被自动执行，返回false则不会。
       4) public boolean isRunning()
       I.  只有该方法返回false时，start方法才会被执行。<br/>
       II. 只有该方法返回true时，stop(Runnable callback)或stop()方法才会被执行。
       5) public void stop(Runnable callback){ callback.run(); isRunning = false; }
       I.  SmartLifecycle子类的才有的方法，当isRunning方法返回true时，该方法才会被调用。
           如果你让isRunning返回true，需要执行stop这个方法，那么就不要忘记调用callback.run()。
           否则在你程序退出时，Spring的DefaultLifecycleProcessor会认为你这个TestSmartLifecycle没有stop完成，程序会一直卡着结束不了，等待一定时间（默认超时时间30秒）后才会自动结束。
       II. PS：如果你想修改这个默认超时时间，可以按下面思路做，当然下面代码是springmvc配置文件形式的参考，在SpringBoot中自然不是配置xml来完成，这里只是提供一种思路
           <bean id="lifecycleProcessor" class="org.springframework.context.support.DefaultLifecycleProcessor">
               <!-- timeout value in milliseconds -->
               <property name="timeoutPerShutdownPhase" value="10000"/>
           </bean>         
       6) public void stop():
       I.  接口Lifecycle的子类的方法，只有非SmartLifecycle的子类才会执行该方法。<br/>
       II. 该方法只对直接实现接口Lifecycle的类才起作用，对实现SmartLifecycle接口的类无效。<br/>
       III.方法stop()和方法stop(Runnable callback)的区别只在于，后者是SmartLifecycle子类的专属。     
```

7. 服务注册ip址设置
```properties
# 如果选择固定Ip注册可以配置
spring.cloud.nacos.discovery.ip = 10.2.11.11
spring.cloud.nacos.discovery.port = 9090

# 如果选择固定网卡配置项
spring.cloud.nacos.discovery.networkInterface = eth0

# 如果想更丰富的选择，可以使用spring cloud 的工具 InetUtils进行配置
spring.cloud.inetutils.ignored-interfaces[0]=eth0   # 忽略网卡，eth0
spring.cloud.inetutils.ignored-interfaces=eth.*     # 忽略网卡，eth.*，正则表达式
spring.cloud.inetutils.preferred-networks=10.34.12  # 选择符合前缀的IP作为服务注册IP

spring.cloud.nacos.discovery.server-addr  #Nacos Server 启动监听的ip地址和端口
spring.cloud.nacos.discovery.service  #给当前的服务命名
spring.cloud.nacos.discovery.weight  #取值范围 1 到 100，数值越大，权重越大
spring.cloud.nacos.discovery.network-interface #当IP未配置时，注册的IP为此网卡所对应的IP地址，如果此项也未配置，则默认取第一块网卡的地址
# spring.cloud.nacos.discovery.ip  # 优先级最高
# spring.cloud.nacos.discovery.port  # 默认情况下不用配置，会自动探测
spring.cloud.nacos.discovery.namespace # 常用场景之一是不同环境的注册的区分隔离，例如开发测试环境和生产环境的资源（如配置、服务）隔离等。

# 更多配置
spring.cloud.nacos.discovery.access-key  # 当要上阿里云时，阿里云上面的一个云账号名
spring.cloud.nacos.discovery.secret-key # 当要上阿里云时，阿里云上面的一个云账号密码
spring.cloud.nacos.discovery.metadata    #使用Map格式配置，用户可以根据自己的需要自定义一些和服务相关的元数据信息
spring.cloud.nacos.discovery.log-name   # 日志文件名
spring.cloud.nacos.discovery.enpoint   # 地域的某个服务的入口域名，通过此域名可以动态地拿到服务端地址
ribbon.nacos.enabled  # 是否集成Ribbon 默认为true
```