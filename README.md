# 项目说明
这是一个使用Spring+SpringMVC+MyBatis框架编写的网上商城。<br><br>
前台用户可以注册、登录，添加购物车，下单购买物品（非真实购买），添加购买商品的评论。前台商品支持搜索，也可以按照价格、上架时间、销量、评论数、综合（销量*评论数）进行排序，且支持分页显示。<br><br>
**注意**：由于每个商品分类下只添加了5个商品，为了展示分页效果只设置了每页显示2个商品。<br>
可以在"**com.ltr.mymall.controller.ForeController**"中的"**foreCategory**"方法下修改"**productCount**"的值来改变每页显示的商品数量<br><br>
后台管理员可以注册(注册码ltr)、登录，更改各种商品及其分类信息，查看用户、订单，安排发货。<br><br>

在解决数据一致性与超卖问题方面，考虑到并发访问量不是很高，多数情况下不存在争用，所以使用了声明式事务与乐观锁。<br><br>

## 项目使用到的工具
- Eclipse IDE for Enterprise Java Developers,Version: 2019-03 (4.11.0)<br>
- MySQL,Version: 8.0.15  
- Spring,Version: 4.1.3  
- MyBatis,Version: 3.1.1  
- TomCat, Version: 8.0  

## 项目结构

```
.
├── pom.xml
├── README.md
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── ltr
│   │   │           └── mymall
│   │   │               ├── comparator  前台商品排序比较器
│   │   │               │   
│   │   │               ├── controller  SpringMVC——Controller
│   │   │               │
│   │   │               ├── dto         对创建订单事务操作结果的封装
│   │   │               │ 
│   │   │               ├── exception   自定义Exception
│   │   │               │   ├── ConcurrentChangeException.java
│   │   │               │   └── OutOfStockException.java 
│   │   │               │ 
│   │   │               ├── interceptor SpringMVC拦截器，用于拦截前后台登录
│   │   │               │  
│   │   │               ├── mapper      DAO
│   │   │               │   
│   │   │               ├── pojo        Entity
│   │   │               │   
│   │   │               ├── service     Web service
│   │   │               │   │
│   │   │               │   └── impl    Service实现类
│   │   │               │     
│   │   │               └── util
│   │   │                   ├── ImageUtil.java              对上传的图片进行处理(更改为.jpg，更改图片大小)
│   │   │                   ├── MybatisGenerator.java       MyBatis逆向工程的执行类
│   │   │                   ├── OverIsMergeablePlugin.java  MyBatis逆向工程的插件，防止生成的.xml出现重复内容
│   │   │                   ├── Page.java                   实现分页的工具类，可以在里面更改每页显示数据的数量（默认显示8个）
│   │   │                   └── UploadedImageFile.java      接受上传文件的注入
│   │   ├── resources
│   │   │   ├── applicationContext.xml  Spring-Mybatis配置文件
│   │   │   ├── generatorConfig.xml     MyBatis逆向工程配置文件
│   │   │   ├── jdbc.properties         数据库连接相关信息，若使用5.x版本的MySQL，需要更改URL格式
│   │   │   ├── log4j.properties        日志的配置文件，可以配置输出信息
│   │   │   ├── mapper                  与DAO对应的.xml文件   
│   │   │   ├── springMVC.xml           SpringMVC配置文件
│   │   │   └── SQL
│   │   │       ├── CreateDataBase.sql  建数据库、键表语句
│   │   │       └── mymall.sql          展示网站效果的基本数据（静态资源在后面给出）
│   │   └── webapp
│   │       ├── admin
│   │       │   └── index.jsp
│   │       ├── css
│   │       │   │
│   │       │   └── fore
│   │       │       └── style.css
│   │       ├── img                    静态资源的存放位置
│   │       │   ├── category           存放分类图片
│   │       │   │   
│   │       │   ├── lunbo              首页轮播图片
│   │       │   │   
│   │       │   ├── productDetail      商品详情页图片（用于介绍商品）
│   │       │   │   
│   │       │   ├── productSingle      单个产品图片（用于分类页的产品图片）
│   │       │   │   
│   │       │   ├── productSingle_middle
│   │       │   │   
│   │       │   ├── productSingle_small
│   │       │   │   
│   │       │   └── site
│   │       │       
│   │       ├── index.jsp
│   │       ├── js
│   │       │   ├── bootstrap
│   │       │   │   └── 3.3.6
│   │       │   │       ├── bootstrap.js
│   │       │   │       ├── bootstrap.min.js
│   │       │   │       └── npm.js
│   │       │   └── jquery
│   │       │       └── 2.0.0
│   │       │           └── jquery.min.js
│   │       └── WEB-INF
│   │           ├── jsp
│   │           │   ├── admin  后台所有页面
│   │           │   │
│   │           │   ├── fore   前台所有页面
│   │           │   │
│   │           │   └── include   公共页面
│   │           │       ├── admin 后台公共页面
│   │           │       │   ├── adminFooter.jsp
│   │           │       │   ├── adminHeader.jsp
│   │           │       │   ├── adminNavigator.jsp
│   │           │       │   └── adminPage.jsp
│   │           │       └── fore  前台公共页面
│   │           │           ├── buyPage.jsp
│   │           │           ├── cart
│   │           │           │   ├── alipayPage.jsp
│   │           │           │   ├── boughtPage.jsp
│   │           │           │   ├── cartPage.jsp
│   │           │           │   ├── confirmPayPage.jsp
│   │           │           │   ├── nostockPage.jsp
│   │           │           │   ├── orderConfirmedPage.jsp
│   │           │           │   ├── payedPage.jsp
│   │           │           │   └── reviewPage.jsp
│   │           │           ├── category
│   │           │           │   ├── categoryPage.jsp
│   │           │           │   ├── productsByCategory.jsp
│   │           │           │   └── sortBar.jsp
│   │           │           ├── footer.jsp
│   │           │           ├── forePage.jsp
│   │           │           ├── header.jsp
│   │           │           ├── home
│   │           │           │   ├── carousel.jsp
│   │           │           │   ├── categoryAndcarousel.jsp
│   │           │           │   ├── categoryMenu.jsp
│   │           │           │   ├── homepageCategoryProducts.jsp
│   │           │           │   ├── homePage.jsp
│   │           │           │   └── productsAsideCategorys.jsp
│   │           │           ├── loginPage.jsp
│   │           │           ├── modal.jsp
│   │           │           ├── product
│   │           │           │   ├── imgAndInfo.jsp
│   │           │           │   ├── productDetail.jsp
│   │           │           │   ├── productPage.jsp
│   │           │           │   └── productReview.jsp
│   │           │           ├── productsBySearch.jsp
│   │           │           ├── registerPage.jsp
│   │           │           ├── registerSuccessPage.jsp
│   │           │           ├── search.jsp
│   │           │           ├── searchResultPage.jsp
│   │           │           ├── simpleSearch.jsp
│   │           │           └── top.jsp
│   │           └── web.xml
│   └── test
│       └── java
│           └── com
│               └── ltr
│                   └── mymall
│                       └── service
│                           └── impl
│                               ├── OrderItemServiceImplTest.java
│                               ├── OrderServiceImplTest.java
│                               └── ProductServiceImplTest.java

```

## 静态资源下载   
链接：https://pan.baidu.com/s/1uuP30YNT7EZmJ7t7Qqc0aA 提取码：diy3<br><br>
解压后放在：mymall\src\main\webapp\img中 
