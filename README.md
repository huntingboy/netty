### 学习netty sgg视频教程的笔记

1. bio介绍，写一个简单的client-server
2. nio介绍，写一个简单的client-server
3. netty介绍，写一个简单的client-server(协议：tcp, http, websocket；通信格式：StringDecoder/Protobuf, HttpServerDecoder, TextWebSocketFrame)
4. netty重要组件介绍与使用（bossGroup=NioEventLoopGroup, workerGroup=NioEventLoop, ChannelHandlerContext, (Inbound|Outbound)ChannelHandler（这一块有很多处理器，编码器/解码器/业务相关）, taskQueue, scheduledQueue, headbeat/IdleStateHandler）
5. netty整个架构的设计，重要组件的源码分析
6. rpc介绍，基于netty通信组件实习的一个简单的dubbo远程过程调用

