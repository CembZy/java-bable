#spring-interceptor
拦截器顺序：<br>
两个为例<br>
prehandler1---prehandler2---执行controller
---posthandler2---posthandler1---aftercompletion2
---agtercompletion1

使用场景：<br>
解决乱码问题<br>
对登陆进行判断

filter依赖于servlet容器，基于回调函数，过滤范围大<br>
interceptor依赖于框架，基于反射机制，只过滤请求<br>

注意！<br>
对象里的属性全为null，对象也不为null