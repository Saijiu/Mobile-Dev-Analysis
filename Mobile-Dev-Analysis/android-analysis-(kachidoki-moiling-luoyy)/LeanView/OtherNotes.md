#Android  View
读郭霖《带你一步步深入了解View》笔记


-------------------
##Andorid LayoutInflater


> 用来加载布局的，我们经常用的setContentView()方法的内容也是使用LayoutInflater来加载布局的。

-------------------
###LayoutInflater 基本用法

-------------------
####步骤

-------------------
#####1.获取LayoutInflater的实例

> 获取LayoutInflater的实例有两种方法，第一种方法是Android给我们封装后的简单写法：

```java
LayoutInflater layoutInflater = LayoutInflater.from(context);
```

>原本的写法也是同样的效果：

```java
LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
```
-------------------
#####2.调用inflate()方法加载布局


```java
layoutInflater.inflate(resourceId, root);
```

> inflate()方法一般接受两个参数：
>  - 第一个参数为加载的布局id，
>  - 第二个参数是指给该布局的外部在嵌套一层父布局，如果不需要就null。
>
> 这样就成功创建了一个布局的实例，之后再将它们添加到制定的位置即可。

-------------------
####例子
> activity_main.xml：

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
	android:id="@+id/main_layout"
	android:layout_width="match_parent"
	android:layout_height="match_parent">
</LinearLayout>
```

> 一个空的LinearLayout，里面什么控件都没有，因此界面上不会显示任何东西。那么接下来我们再定义一个布局，给它取名为button_layout.xml：

```xml
<Button xmlns:android="http://schemas.android.com/apk/res/android"  
    android:layout_width="wrap_content"  
    android:layout_height="wrap_content"  
    android:text="Button" >  
  
</Button>  
```

> 这个布局文件也非常简单，只有一个button按钮。现在就要通过LayoutInflater来将button_layout这个布局添加到主布局文件的LinearLayout中。修改MainActivity中的代码：

```java
public class MainActivity extends Activity {
	private LinearLayout mainLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mainLayout = (LinearLayout) findViewById(R.id.main_layout);
		LayoutInflater layoutInfalter = LayoutInflater.from(this);
		View buttonLayout = layoutInflater.inflater(R.layout.button_layout, null);
		mainLayout.addView(buttonLayout)		
	}
}
```

> 可以看到，这里先是获取到了LayoutInflater的实例，然后调用它的inflate()方法来加载button_layout这个布局，最后调用LinearLayout的addView()方法将它添加到LinearLayout中。
>我们跑一下虚拟机：
>
![image](https://github.com/moiling/mAndroidViewLeaning/blob/master/LeanView/art/1.png)
>
>成功将Button显示出来了
>LayoutInflater技术广泛应用于需要动态添加View的时候，比如在ScrollView和ListView中，经常都可以看到LayoutInflater的身影。

--------------
####从源码看LayoutInflater工作

> 不管你使用的是哪个inflate()方法的重载，最终都会辗转调用到LayoutInflater的如下代码中：

```java
public View inflate(XmlPullParser parser, ViewGroup root, boolean attachToRoot) {
	synchronized (mConstructorArgs) {
		final AttributeSet attrs = Xml.asAttributeSet(parser);
		mConstructorArgs[0] = mContext;
		View result = root;
		try {
			int type;
			while ((type = parser.next()) != XmlPullParser.STAR_TAG && type != XmlPullParser.END_DOCUMENT)
		}
		if (type != XmlPullParser.START_TAG) {
			throw new InflateException(parser.getPositionDescription() + ": No start tag found!");
		}
		final String name = parser.getName();
		if (TAG_MERGE.equals(name)) {
			if (root == null || !attachToRoot) {
				throw new InflateException("merge can be used only with a valid" + "ViewGroup root and attachToRoot=true");
			}
			rInflate(parser, root, attrs);
		} else {
			View temp = createViewFromTag(name, attrs);
			ViewGroup.LayoutParams params = null;
			if (root != null) {
				params = root.generateLayoutParams(attrs);
				if (!attachToRoot) {
					temp.setLayoutParams(params);
				}
			}
			rInflate(parser, temp, attrs);
			if (root != null && attachToRoot) {
				root.addView(temp, params);
			}
			if (root == null || !attachToRoot) {
				result =temp;
			}
		}
	} catch (XmlPullParserExceptin e) {
		InflateException ex = new InflateException(e.getMessage());
		ex.initCause(e);
		throw ex;
	} catch (IOException e) {
		InflateException ex = new InflateException(parser.getPositionDescription() + ":" + e.getMessage());
		ex.initCause(e);
		throw ex;
	}
	return result;
}
```

> 从这里我们就可以清楚地看出，LayoutInflater其实就是使用Android提供的pull解析方法来解析布局文件的。**难怪我看不懂！**这里我们看到 View temp = createViewFromTag(name, attrs);  这一句，调用了createViewFromTag()这个方法，并把节点名和参数传了进去。它是用来根据节点名来创建View对象的。在createViewFromTag()方法的内部又会去调用createView()方法，然后使用反射的方式创建出View的实例并返回。
> 当然，这里只是创建出了一个根布局的实例而已，接下来会调用rInflate()方法来循环遍历这个根布局下的子元素，代码如下所示：

```java
private void rInflate(XmlPullParser parser, View parent, final AttributeSet attrs)  
        throws XmlPullParserException, IOException {  
    final int depth = parser.getDepth();  
    int type;  
    while (((type = parser.next()) != XmlPullParser.END_TAG ||  
            parser.getDepth() > depth) && type != XmlPullParser.END_DOCUMENT) {  
        if (type != XmlPullParser.START_TAG) {  
            continue;  
        }  
        final String name = parser.getName();  
        if (TAG_REQUEST_FOCUS.equals(name)) {  
            parseRequestFocus(parser, parent);  
        } else if (TAG_INCLUDE.equals(name)) {  
            if (parser.getDepth() == 0) {  
                throw new InflateException("<include /> cannot be the root element");  
            }  
            parseInclude(parser, parent, attrs);  
        } else if (TAG_MERGE.equals(name)) {  
            throw new InflateException("<merge /> must be the root element");  
        } else {  
            final View view = createViewFromTag(name, attrs);  
            final ViewGroup viewGroup = (ViewGroup) parent;  
            final ViewGroup.LayoutParams params = viewGroup.generateLayoutParams(attrs);  
            rInflate(parser, view, attrs);  
            viewGroup.addView(view, params);  
        }  
    }  
    parent.onFinishInflate();  
}  
```

> 可以看到，同样是createViewFromTag()方法来创建View实例，然后还会递归调用rInflate()方法来查找这个View下的子元素，每次递归完成后则将这个View添加到父布局当中。
> 这样的话，把整个布局文件都解析完成后，就形成了一个完整的DOM结构，最终会把最底层的根布局返回，至此inflate()过程全部结束。

--------------------------------
#####inflate()三个参数的方法重载

```java
inflate(int resource, ViewGroup root,boolean attachToRoot)
```
> 1.如果root为null，attachToRoot将失去作用，设置任何值都没有意义。
> 2.如果root不为null，attachToRoot设为true，则会在加载的布局文件的最外层再嵌套一层root布局。
> 3.如果root不为null，attachToRoot设为false，则root参数失去作用。
> 4.在不设置attachToRoot参数的情况下，如果root部位null，attachToRoot参数默认为true。

---------------------------------
####更改例子中button的大小
> 如果按照我们常规的方法，就直接修改button_layout.xml中的代码：

```xml
<Button xmlns:android="http://schemas.android.com/apk/res/android"  
    android:layout_width="300dp"  
    android:layout_height="80dp"  
    android:text="Button" >  
  
</Button>  
```

>这里我们设置了button的宽度和高度，重新运行一下，没有任何变化。
>因为这两个值现在已经完全失去了作用。平时我们经常使用layout_width和layout_height来设置View的大小，并且一直都能正常工作，就好像这两个属性确实是用于设置View的大小的。而实际上则不然，**它们其实是用于设置View在布局中的大小的**，也就是说，首先**View必须存在与一个布局中**，之后再根据数值来设定。这也是为什这两个属性叫做layout_width和layout_height，而不是width和height。
>而我们button_layout.xml中的Button目前不存在于任何布局当中，所以要让这两个属性起作用，最简单的方法就是**在Button的外面再嵌套一层布局**。

```xml
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"  
    android:layout_width="match_parent"  
    android:layout_height="match_parent" >  
  
    <Button  
        android:layout_width="300dp"  
        android:layout_height="80dp"  
        android:text="Button" >  
    </Button>  
  
</RelativeLayout>  
```

> 可以看到，这里我们又加入了一个RelativeLayout，此时的Button存在于RelativeLayout之中，两个属性开始起作用了。当然，处于**最外层的RelativeLayout，它的layout_width和layout_height则会失去作用**。
> 那为什么平时在Activity中指定布局文件的时候，最外层的那个布局是可以指定大小的？
> 因为在setContentView()方法中，Android会自动在布局文件的最外层再嵌套一个FrameLayout，所以layout_width和layout_height属性才会有效果。
> 任何一个Activity中显示的界面其实主要都由两部分组成，标题栏和内容布局。标题栏就是在很多界面顶部显示的那部分内容，比如刚刚我们的那个例子当中就有标题栏，可以在代码中控制让它是否显示。而内容布局就是一个FrameLayout，这个布局的id叫作content，我们调用setContentView()方法时所传入的布局其实就是放到这个FrameLayout中的，这也是为什么这个方法名叫作setContentView()，而不是叫setView()。
