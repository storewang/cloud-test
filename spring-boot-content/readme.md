#### nacos
> 服务发现,配置中心
* namespace 命名空间
  * Group 分组 不同服务可以分到一组，默认为default_group
  * Service 微服务
    * Cluster 对指定微服务的一个虚拟划分，默认为default
      * Instance 微服务实例


#### ribbon组件
接口 | 作用 | 默认值
:-:|:-:|:-:
IClientConfig | 读取配置 | DefaultClientConfigImpl |
IRue | 负载均衡规则 | ZoneAvoidanceRule |
IPing | 筛选掉ping不能的实例 | DummyPing |
ServerList<Server> | 交给ribbon的实例列表 | ConfigurationBasedServerList |
ServerListFilter<Server> | 过滤不符合条件的实例 | ZonePreferenceServerListFilter |
ILoadBalance | ribbon入口 | ZoneAwareLoadBalancer |
ServerListUpdater | 更新交给ribbon的实例列表策略 | PollingServerListUpdater |


#### ribbon内置规则
规则名称 | 特点 | 
:-:|:-:
AvailabilityFilteringRule | 过滤掉一直连接失败的被标记为ciruit tripped的后端server，并过滤掉那些高并发的后端server或者使用一个 |AvailabilityPredicate | 来包含过滤server的逻辑，其实就是检查status里记录的各个server的运行状态 | 
BestAvailableRule | 选择一个最小的并发请求server，如果server被tripped了，则跳过 | 
RandomRule | 随机选择一个server | 
ResponseTimeWeightedRule | ~~已废弃~~ |
RetryRule | 对选定的负载策略机上重试机制，在一个配置时间段内当选择server不成功,则一直尝试使用subRule的方式选择一个可用的server | 
RoundRobbinRule | 轮询选择,轮询index，选择index对应的server | 
WeightedResponseTimeRule | 根据响应时间加权，响应时间越长，权重越小，被选择的可能性越小 |
ZoneAvoidanceRule | 复合判断server所zone的性能和server的可用性选择server,在没有zone的环境下，类似轮询(RoundRobinRule) | 

#### ribbon轮询算法(RoundRobbinRule)
```java
private int incrementAndGetModulo(int modulo) {
  for (;;) {
      int current = nextServerCyclicCounter.get();
      int next = (current + 1) % modulo;
      if (nextServerCyclicCounter.compareAndSet(current, next))
          return next;
  }
}
```

#### 配置文件与代码方式配置负载均衡
> 尽量使用属性配置的方式配置负载均衡规则，同一工程中尽量使用统一的方式进行配置

配置方式 | 优点 | 缺点
:-:|:-:|:-:
代码配置 | 基于代码，更加灵活 | 有小坑(父子上下文) 线上修改得重新打包，发布|
属性配置 | 易上手，配置更新直观，线上修改无需重新打包，发布，优先级更新高 | 极端情况下没有代码配置方式灵活 |

#### Feign的日志级别
> feign 默认是不打印日志

级别 | 打印内容 | 
:-:|:-:
NONE(默认) | 不打印日志 |
BASIC | 公记录请求方法，URL,响应状态码及执行时间 |
HEADERS | 记录BASIC级别的基础上，记录请求和响应的header |
FULL | 记录请求和响应的header,body和无数据 |

#### Feign的组件
接口 | 作用 | 默认值
:-:|:-:|:-:
Feign.Builder | Feign的入口 | Feign.Builder|
Client | Feign底层用什么去请求 | 和ribbon配置时:LoadBalancerFeignClient,不和ribbon配置时feign.Client.Default |
Contract | 契约，注解支持 | SpringMvcContract|
Encoder | 编码器，用于对象转换成http请求消息体 | SpringEncoder|
Decoder | 解码器，将响应消息体转换成对象 | ResponseEntityDecoder|
Logger | 日志管理器 | Slf4jLogger0|
RequestInterceptor | 用于为每个请求添加逻辑 | 无|

#### Feign构造多参数的请求
> 方法一[推荐]
```java
@FeignClient("spring-boot-user")
public interface UserFeignClient {
  @GetMapping("/get")
  public User get0(@SpringQueryMap User user);
}

```

> 方法二[推荐]
```java
@FeignClient(name = "microservice-provider-user")
public interface UserFeignClient {
  @RequestMapping(value = "/get", method = RequestMethod.GET)
  public User get1(@RequestParam("id") Long id, @RequestParam("username") String username);
}

```

> POST请求包含多个参数
```java
@FeignClient(name = "microservice-provider-user")
public interface UserFeignClient {
  @RequestMapping(value = "/post", method = RequestMethod.POST)
  public User post(@RequestBody User user);
}

```
#### 服务容错方案思想
* 超时
* 限流
* 仓壁模式(线程隔离)
* 断路器模式

#### sentinel 降级
* RT
> 平均响应时间（秒级统计）超出阈值且在时间窗口内通过的请求>=5触发降级
> 时间窗口结束关闭降级