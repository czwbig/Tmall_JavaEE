# Tmall_JavaEE

技术栈 Servlet + Jsp + Tomcat , 是Java Web入门非常好的练手项目  
##### 效果展示：  
[模仿天猫前台](http://how2j.cn/tmall?p=55563)    

### 项目简介

`关联项目`  
**[github - 天猫 JavaEE 项目](https://github.com/czwbig/Tmall_JavaEE)**  
**[github - 天猫 SSH 项目](https://github.com/czwbig/Tmall_SSH)**  
**[github - 天猫 SSM 项目](https://github.com/czwbig/Tmall_SSM)**  

本项目为Java EE入门练手项目，没有使用 SSH , SSM 框架，而是使用 JavaEE 整套技术来作为解决方案，实现模仿天猫网站的各种业务场景。 之所以不使用框架，就是为了借助这个项目夯实 JavaEE 基础，并且在项目中借助反射等技术。


> 项目用到的技术如下：  
 **Java：`Java SE基础`  
 前端： `HTML` ,  `CSS` ,  `JavaScript` ,  `jQuery`  
 J2EE： `Tomcat` ,  `Servlet` ,  `JSP` ,  `Filter`  
数据库： `MySQL`**  
>

### 表结构

[建表sql](https://github.com/czwbig/Tmall_JavaEE/blob/master/sql/tmall.sql)  已经放在 Github 项目的 /sql 文件夹下

|表名 |中文含义 |介绍 |
| - |:-:| -|
| Category           |分类表         |存放分类信息，如女装，平板电视，沙发等 
| Property            |属性表         |存放属性信息，如颜色，重量，品牌，厂商，型号等 
| Product             |产品表         |存放产品信息，如LED40EC平板电视机，海尔EC6005热水器 
| PropertyValue   |属性值表     |存放属性值信息，如重量是900g,颜色是粉红色 
| ProductImage   |产品图片表   |存放产品图片信息，如产品页显示的5个图片 
| Review              |评论表           |存放评论信息，如买回来的蜡烛很好用，么么哒 
| User                   |用户表         |存放用户信息，如斩手狗，千手小粉红 
| Order                 |订单表         |存放订单信息，包括邮寄地址，电话号码等信息 
| OrderItem         |订单项表       | 存放订单项信息，包括购买产品种类，数量等

![表关系](https://upload-images.jianshu.io/upload_images/14923529-8645ee131490206f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

|一|多|
|-|-|
| Category-分类	|Product-产品
| Category-分类	|Property-属性
| Property-属性	|PropertyValue-属性值
| Product-产品	      |PropertyValue-属性值
| Product-产品	      | ProductImage-产品图片
| Product-产品	     | Review-评价
| User-用户	    | Order-订单
| Product-产品	    | OrderItem-订单项
| User-用户	    | OrderItem-订单项
| Order-订单	    | OrderItem-订单项
| User-用户	    | User-评价

以上直接看可能暂时无法完全理解，结合后面具体到项目的业务流程就明白了。

----

### 实体类设计
所谓的实体类，就是对于数据库中的表的互相映射的类。 
这是一种 ORM 的设计思想，即一个对象，对应数据库里的一条记录
举个例子，对于 `评价 / review` 的 实体类 和 表结构 设计如下：

![](https://upload-images.jianshu.io/upload_images/14923529-b9559ad1693c9faf.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

已省略对应的 getter/setter 方法

----

### DAO 类设计
DAO 是 Data Access Object 的缩写，专门用于进行数据库访问的操作。
首先看一下数据库工具类 
##### DBUtil 

![](https://upload-images.jianshu.io/upload_images/14923529-a51316228f3f328e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

这个类的作用是初始化驱动，并且提供一个 getConnection 用于获取连接，统一管理连接参数，方便后续操作。

##### CategoryDAO
利用 DBUtil 获取 Connectoion ，再获取对应的 Statement，利用 JDBC 从数据库取出数据，并构造成 bean 对象返回。
![CategoryDAO.list](https://upload-images.jianshu.io/upload_images/14923529-037388bb26575584.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

----
### Service 类
作为J2EE web 应用，一般会按照如图所示的设计流程进行
Servlet -> Service（业务类） -> DAO -> database 

![](https://upload-images.jianshu.io/upload_images/14923529-47005c4231fc678c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

在本模仿天猫整站 JavaEE 版本中，不使用 Service 这一层。 原因是在 DAO 进行了比较详细的设计，已经提供了很好的支持业务的方法。如果在 DAO 上包裹一层 Service 业务类，不过是在直接调用 DAO 设计好的方法罢了。另外一个使用框架的项目会用到 Service 层。

---

### Filter 配合 Servlet
后台在系统设计的时候，并不是简单的每个功能对应一个 Servlet ，而是使用了反射的技术，结合过滤器Filter 进行了封装，使得开发配置以及维护成本降低了很多。
##### 一个路径对应一个 Servlet 的弊端
这里以分类进行举例：
分类管理需要：增加，删除，编辑，修改，查询 5 个功能，按照传统的在 web.xml 中配置 Servlet 的思路，那么就需要 5 个 Servlet 类，而后台需要做分类，产品，属性，产品图，用户，订单 6 中管理，就一共需要30 个 Servlet，还要配置 web.xml 就会变得很乱。
解决的方法是把所有分类操作放在同一个 Servlet ，对应不同的方法。
让我们来分析，如何做到访问 admin_category_list 的时候，CategoryServlet 的 list() 方法会被调用：

>1. 假设访问路径是 /admin_category_x
>2. 过滤器 BackServletFilter 进行拦截，判断访问的地址是否以/admin_开头
>3. 如果是，那么做如下操作  
  3.1 取出两个下划线之间的值 category  
  3.2 取出最后一个下划线之后的值 x  
  3.3 然后根据这个值，服务端跳转到 categoryServlet，并且把 x 这个值传递过去  
>4. categoryServlet 继承了 BaseBackServlet，其 service 方法会被调用。 在 service 中，借助反射技术，根据传递过来的值 x，调用对应 categoryServlet 中的方法 x()
>5. 这样就实现了当访问的路径是 admin_category_list 的时候，就会调用 categoryServlet.x() 方法这样一个效果

换句话说:
如果访问的路径是 admin_category_add，就会调用 categoryServlet.add() 方法
如果访问的路径是 admin_category_delete，就会调用 categoryServlet.delete() 方法

##### BackServletFilter
[Github-BackServletFilter 完整代码](https://github.com/czwbig/Tmall_JavaEE/blob/master/src/tmall/filter/BackServletFilter.java)

![](https://upload-images.jianshu.io/upload_images/14923529-794a86555266a1e0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

###### BaseBackServlet
[Github-BaseBackServlet 完整代码](https://github.com/czwbig/Tmall_JavaEE/blob/master/src/tmall/servlet/BaseBackServlet.java)
BaseBackServlet 继承了 HttpServlet 并重写了 service 方法，其核心代码如下：

![BaseBackServlet .service()](https://upload-images.jianshu.io/upload_images/14923529-e2bf10f6d2c2afdc.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

流程图

![](https://upload-images.jianshu.io/upload_images/14923529-f4707d399173e59f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

另外还定义了增删查改等基础抽象方法，初始化了所有 DAO 对象
上图已经注释的很清楚了，项目全部代码都放在 github 上了，欢迎查看。

##### CategoryServlet
[Github-CategoryServlet 完整代码](https://github.com/czwbig/Tmall_JavaEE/blob/master/src/tmall/servlet/CategoryServlet.java)
1. 首先 CategoryServlet 继承了 BaseBackServlet，而 BaseBackServlet 又继承了 HttpServlet
2. 服务端跳转过来之后，会访问 CategoryServlet 会访问 service() 方法
3. 父类 BaseBackServlet中重写了 service() 方法，所以流程就进入到了 service() 中  
  3.1 在 service() 方法中根据反射访问对应的方法  
  3.2 根据对应方法的返回值，进行服务端跳转、客户端跳转、或者直接输出字符串。  
4.  取到从 BackServletFilter 中 request.setAttribute() 传递过来的值 list
5.  根据这个值 list，借助反射机制调用 CategoryServlet 类中的 list() 方法，这样就达到了CategoryServlet.list()方法被调用的效果

![CategoryServlet.list()](https://upload-images.jianshu.io/upload_images/14923529-198c215e4819cdab.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

而后，list 方法使用 categoryDAO.list 查询出 category 对象集合，并跳转到 listCategory.jsp 显示

![listCategory.jsp 部分 ](https://upload-images.jianshu.io/upload_images/14923529-b4e8280491d3b335.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![localhost/admin_category_list 访问效果](https://upload-images.jianshu.io/upload_images/14923529-767eeb3d143ed003.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

[完整版的 listCategory.jsp](https://github.com/czwbig/Tmall_JavaEE/blob/master/web/admin/listCategory.jsp) 还包含4个公共文件，分别是 头部，导航，行业，页脚。
分类管理还有增加，编辑，修改，删除，分页，另外后台其他管理页面，前台页面。具体的需要浏览代码，篇幅原因就不展开了。

##### 页面展示
![前台首页](https://upload-images.jianshu.io/upload_images/14923529-6be73ccdb1dec779.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![产品页](https://upload-images.jianshu.io/upload_images/14923529-d7a5bfc9f920b5e8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

本篇博客所讲不足整个项目的 1/10 ，有兴趣的朋友请移步 [github 项目的地址](https://github.com/czwbig/Tmall_JavaEE) 。


### 参考
**[天猫整站学习教程](http://how2j.cn/k/tmall-j2ee/tmall-j2ee-894/894.html?p=55563)** 里面除了本项目，还有 Java 基础，前端，Tomcat 及其他中间件等教程， 可以注册一个账户，能保存学习记录。
