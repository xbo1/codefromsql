### Code From SQL
create template code from SQL for php.

#### dependency
1. [druid](https://github.com/alibaba/druid)
2. [Spring Boot](http://projects.spring.io/spring-boot/) include web,thymeleaf

#### 参考
1. spring-boot使用, http://www.cnblogs.com/xxt19970908/p/6736547.html
2. sqlparse使用，http://www.cnblogs.com/etangyushan/p/5490183.html

#### 部署
在centos上装好openjdk8
执行
```
nohup  java -Dserver.port=8088 -jar codefromsql-0.0.1-SNAPSHOT.jar >temp.text &
```