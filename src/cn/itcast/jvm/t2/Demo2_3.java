package cn.itcast.jvm.t2;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 演示软引用
 * -Xmx20m
 * -XX:+PrintGCDetails -verbose:gc  打印垃圾回收的详细信息
 */
public class Demo2_3 {

    private static final int _4MB = 4 * 1024 * 1024;



    public static void main(String[] args) throws IOException {

        // 在跑这段注释的代码前，已经预先设置了-Xmx20m（堆内存大小设置为20M）
//        List<byte[]> list = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            list.add(new byte[_4MB]);
//        }
        // 在执行完这个循环后，肯定会超过堆内存大小
        // 执行这段注释的代码的话，就会报一个OutOfMemoryError：Java heap space的错误
        // 在现实开发中，这样的应用场景还是比较多的
        // 比如说这里的byte数组可能是用于读取网络上的一些图片，这些图片显示的时候
        // 需要把它放到一个list集合里去进行显示，但是这些图片资源并不属于核心业务资源
        // 假如图片过多，我们使用强引用来引用这些图片的话，就容易造成前面提到的内存溢出问题
        //像这样不太重要的资源，能不能在内存紧张的时候，把它占用的内存释放掉
        // 等到以后再用到这些图片的时候，再读取一遍图片资源不就行了吗
        // 那强引用就不适合这种场景了，这时候可以考虑软引用了
//
//        System.in.read();


        soft();


    }

    public static void soft() {
        // 之前的做法是
        // list --> byte[] 两者之间是强引用
        // 而这段代码不是使用list直接去引用byte数组了，而是在它们之间加了一个softReference
        // list --> SoftReference --> byte[]
        // list和SoftReference之间是强引用，SoftReference和byte[]之间是软引用
        // List<SoftReference<byte[]>>：List里放的是软引用SoftReference，软引用里放的才是byte数组
        List<SoftReference<byte[]>> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            //创建一个软引用对象new SoftReference，参数是软引用要指向的类型
            SoftReference<byte[]> ref = new SoftReference<>(new byte[_4MB]);
            //加了打印，运行后发现，这里循环的时候，软引用指向的五个byte数组都是存在的
            System.out.println(ref.get());
            list.add(ref);
            System.out.println(list.size());

        }
        System.out.println("循环结束：" + list.size());
        for (SoftReference<byte[]> ref : list) {
            //加了打印，运行后发现，这里循环的时候，软引用指向的五个byte数组，只有最后一个被保留下来了，其他四个byte数组被回收了
            System.out.println(ref.get());
        }
    }
}
