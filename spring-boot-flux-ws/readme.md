--- web socket 消息发送---
1. 登录:  返回ws.token
> ws.token:   ip:port:uid:accesstoken

- ws握手时: 验证accesstoken有效性
- onlineNum:     redis只记录在线数量 
- 每台机器连接量: 在每台机器上记录连接数.
- 每台机器记录客户端建立连接的对应session信息
 
2. 发送消息
> from:token,to:token,msg:""
- 根据totoken,知道接收者连接在哪台机器ip:port,把消息转发到这台机器.

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

