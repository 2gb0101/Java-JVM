package cn.itcast.jvm.t1.direct;

import sun.misc.Unsafe;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * 直接内存分配的底层原理：Unsafe
 * 可以利用windows的任务管理器来看看直接内存的分配与释放
 * 这个程序说明了验证了直接内存的释放和管理是通过unsafe对象来管理的，而不是通过垃圾回收机制来管理的
 */
public class Demo1_27 {
    static int _1Gb = 1024 * 1024 * 1024;

    public static void main(String[] args) throws IOException {
        Unsafe unsafe = getUnsafe();
        // 分配内存
        long base = unsafe.allocateMemory(_1Gb);    //参数是要分配的内存大小
        //allocateMemory方法返回的base变量，就代表刚刚分配的直接内存的地址，以后可以根据这个地址来释放分配的内存
        //allocateMemory得和setMemory配合使用
        unsafe.setMemory(base, _1Gb, (byte) 0);
        System.in.read();

        // 释放内存
        //Java中针对无用对象的垃圾回收，是自动的，不需要我们手动来调用任何方法，但直接内存必须由我们调用unsafe对象的freememory方法才能释放内存
        unsafe.freeMemory(base);
        System.in.read();
    }

    /**
     * 为了演示工作流程，所以这里想办法获取一个unsafe对象
     * @return
     */
    public static Unsafe getUnsafe() {
        try {
            //unsafe对象不能直接获取，所以利用了反射来获取一个静态成员变量
            //这个静态成员变量就是后面要使用的unsafe对象
            //拿到unsafe对象后，可以利用这个对象的一些方法来分配和释放内存
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            Unsafe unsafe = (Unsafe) f.get(null);
            return unsafe;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
