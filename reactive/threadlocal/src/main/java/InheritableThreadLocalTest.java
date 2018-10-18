import java.lang.reflect.Field;

/**
 * @author : Sun
 * @date : 2018/10/18 14:33
 */
public class InheritableThreadLocalTest {


    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

        ThreadLocal<String > tl = new ThreadLocal<>();
        InheritableThreadLocal<String > itl = new InheritableThreadLocal<>();
        
         tl.set("xxxx");
         itl.set("yyyy");


        Thread thread = Thread.currentThread();
        
        // 通过反射
        Field threadLocals = Thread.class.getDeclaredField("threadLocals");
        threadLocals.setAccessible(true);
        Field inheritableThreadLocals =   Thread.class.getDeclaredField("inheritableThreadLocals");
        inheritableThreadLocals.setAccessible(true);


        String s = tl.get();
        System.out.println(s);

        Object o = threadLocals.get(thread);
        System.out.println(o);
        Object o1 = inheritableThreadLocals.get(thread);
        System.out.println(o1);
        
        
        
        Thread t2 = new Thread();

        Object o3 = threadLocals.get(t2);
        System.out.println(o3);
        Object o4 = inheritableThreadLocals.get(t2);
        System.out.println(o4);
        
        
    }
}
