### gateway介绍
> http://www.imooc.com/article/290804

* Rote (路由)
> spring cloud gateway的基础元素,可简单理解成一我要的不多转发的规则.包含：ID,目标URL,Predicate集合以及Filter集合
* Predicate (谓词)
> 即java.util.function.Predicate,Spring cloud gateway使用predicate实现路由的匹配条件。
* Filter (过滤器)
> 修改请求以及响应

##### 路由谓词工厂
* 时间相关
    1. AfterRoutePredicateFactory
    2. BeforeRoutePredicateFactory
    3. BetweenRoutePredicateFactory
* Cookie相关
    1. CookieRoutePredicateFactory
* Hander相关
    1. HeaderRoutePredicateFactory
    2. HostRoutePredicateFactory
* 请求相关
    1. MethodRoutePredicateFactory
    2. PathRoutePredicateFactory
    3. QueryRoutePredicateFactory
    4. RemoteAddrRoutePredicateFactory
    
##### 过滤器
> http://www.imooc.com/article/290816
> AbstractGatewayFilterFactory ,AbstractNameValueGateFilterFactory
* pre
> gateway转发请求之前
* post
> gateway转发请求之后

API相关
* exchange.getRequest().mutate().XXX //修改request
* exchange.mutate().XXX //修改exchange
* chain.filter(exchange) // 传递给下一个过滤器处理
* exchange.getResponse() //拿到响应

##### 全局过滤器
> http://www.imooc.com/article/290821

##### actuator端点
ID | Http Method | 描述
:-:|:-:|:-:
globalfilters | GET | 展示所有的全局过滤器 |
routefilters | GET | 展示所有的过滤器工厂(GatewayFilterfacoties) |
refresh | POST(无消息体) | 清空路由缓存 |
routes | GET | 展示路由列表 |
routes/{id} | GET | 展示指定ID的路由信息 |
routes/{id} | POST | 新增（修改）一个路由 |
routes/{id} | DELETE(无消息体) | 删除一个路由 |


