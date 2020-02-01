package cn.itcast.jvm.t1.direct;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 禁用显式回收对直接内存的影响
 */
public class Demo1_26 {
    static int _1Gb = 1024 * 1024 * 1024;

    /*
     * 在做jvm的调优时，我们常常会见到这样一个参数：
     * -XX:+DisableExplicitGC
     * Explicit是显式的意思，DisableExplicitGC就是禁用显示的GC，其实就是让代码中的System.gc();无效
     *
     */
    public static void main(String[] args) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(_1Gb);
        System.out.println("分配完毕...");
        System.in.read();
        System.out.println("开始释放...");
        byteBuffer = null;
        System.gc(); // 这句代码是一种显式的垃圾回收，触发的是Full GC

        //Full GC是一种比较影响性能的gc，不光要回收新生代，还要回收老年代，会导致程序暂停时间比较长
        //所以为了防止某些程序员不小心在自己代码里经常写System.gc()，触发这种显式的垃圾回收
        //我们在虚拟机调优的时候，经常会加上这么一个虚拟机参数:-XX:+DisableExplicitGC，禁用这种显式的垃圾回收
        // 但是加上这个参数后，可能就会影响直接内存的回收机制
        //因为禁用了System.gc()后，调用这个方法就是无效的，也就不会触发垃圾回收，由于此时内存很充裕
        //那这个bytebuffer对象此时还存活着，既然它存活着，那么它所对应的直接内存也没有被回收掉
        //一旦我们禁用了System.gc()，可能会发现其他代码没什么问题，但是对直接内存的使用还是有问题的
        //我们不通过显示的代码（System.gc()）回收掉bytebuffer对象，那么这个bytebuffer对象只有等到触发
        //真正的垃圾回收的时候，才会被清理，从而它所对应的直接内存才会被释放，导致直接内存占用的时间过长
        //我们都知道真正释放内存，其实不用Java的垃圾回收，不用cleaner，不用referencehandler都可以
        //你可以用unsafet对象，直接调用freememory方法直接释放直接内存(见Demo1_27.java)

        System.in.read();
    }
}
