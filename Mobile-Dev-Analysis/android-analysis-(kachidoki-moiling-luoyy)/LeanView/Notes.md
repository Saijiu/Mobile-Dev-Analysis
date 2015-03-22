# 3/22/2015 18:21

也不是感叹自己差多少的时候了，既然与人差距很大，那我就重头开始！<br> 
由于寒假一直在看郭大神的书和博客，所以这次对view的探索，我也选择了郭大神博客的文章<br> 
[Android LayoutInflater原理分析，带你一步步深入了解View(一)](http://blog.csdn.net/guolin_blog/article/details/12921889 "快戳我！")<br> 
对于这篇文章的笔记我也把它放在`OtherNotes.md`里面了，<br>
虽然几乎一样，但也是我纯手打的，至少算过了一遍的见证吧！废话就不多说了，进入正题！<br>
<br>
该篇主要讲了`LayoutInflater`，在之前程序中的fragment里出现过很多次了，尤记部长的demo里面还注释了一句“注意这个的用法！"

##LayoutInflater基本用法
* 1.获取LayoutInflater的实例
```java
LayoutInflater layoutInflater = LayoutInflater.from(context);
```
* 2.调用inflate()方法加载布局
```java
layoutInflater.inflate(resourceId, root);
```
> inflate()方法一般接受两个参数：
>  - 第一个参数为加载的`布局id`，
>  - 第二个参数是指给该布局的`外部在嵌套一层父布局`，如果不需要就`null`。

* 3.inflate()三个参数的方法重载
```java
inflate(int resource, ViewGroup root,boolean attachToRoot)
```
>  - 1.如果root为null，attachToRoot将失去作用，设置任何值都没有意义。
>  - 2.如果root不为null，attachToRoot设为true，则会在加载的布局文件的最外层再嵌套一层root布局。
>  - 3.如果root不为null，attachToRoot设为false，则root参数失去作用。
>  - 4.在不设置attachToRoot参数的情况下，如果root部位null，attachToRoot参数默认为true。

##关于布局世界的新大门

通过郭神的例子，我们知道了`layout_width`和`layout_height`其实是用于设置View在布局中的大小的，
如果这个View外面没有父布局了，**该方法会失效**！
而我们平时Activity最外层的布局可以指定大小是因为在`setContentView()`方法中，Andorid会自动
在布局文件的**最外层再嵌套**一个`FrameLayout`!<br>

    而这个神奇的FrameLayout的id就是所谓的content！
