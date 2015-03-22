### 在真正的了解View之前，我希望先做一个自定义的View（EasyView）来进入对View的学习。
###读http://blog.csdn.net/guolin_blog/article/details/16330267 总结。
###一：定义EasyView extends View 并复写构造函数,每一个视图的绘制过程都必须经历三个最主要的阶段，即onMeasure()、onLayout()和onDraw().

     1.onMeasure():一个View的开始是从ViewRoot.performTraversals()方法中开始。
     其中onMeasure（）方法接受两个参数，分别是widthMeasureSpec和heightMeasureSpec。而这个
     参数又是由specSize和specMode共同组成的，一但specMode为WRAP或者MATCH的话，就不需要specSize。onMeasure（）有默认和自己设置两种。
     2.onlayout():在onMeasure（）后接着就会执行onlayout（）,
     onLayout()过程是为了确定视图在布局中所在的位置，而这个操作应该是由布局来完成的，即父视图决定子视图的显示位置。
     3.onDraw():在Canvas上画。

###二：View与attr.xml的联系：
     1.在res/values/attr.xml中添加
     <?xml version="1.0" encoding="utf-8"?>
     <resources>
         <declare-styleable name="MyView">   ------申明是哪个View的。
             <attr name="textColor" format="color" />   ------采用名字和属性连接起来。
             <attr name="textSize" format="dimension" />
         </declare-styleable>
     </resources>
     2.在自定义View的构造函数中获取并设置属性。
