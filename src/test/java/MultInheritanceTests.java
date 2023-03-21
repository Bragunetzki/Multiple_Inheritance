import MultInheritanceExceptions.AmbiguousMethodException;
import MultInheritanceExceptions.InstantiationFailedException;
import MultInheritanceExceptions.MethodInvocationFailedException;
import MultipleInheritance.MultExtends;
import MultipleInheritance.MultHierarchyObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MultInheritanceTests {
        /*
           F     A (method, rootMethod)
            \  /   \
    (method) B     C (method, cUnique)
              \   /
               D (method), E (no method)
     */

    @Test
    void test1() throws InstantiationFailedException, MethodInvocationFailedException, NoSuchMethodException, AmbiguousMethodException {
        D d = new D();
        Assertions.assertEquals("D", d.invokeMethod("method"));
    }

    @Test
    void test2() throws MethodInvocationFailedException, NoSuchMethodException, AmbiguousMethodException, InstantiationFailedException {
        E e = new E();
        Assertions.assertEquals("B", e.invokeMethod("method"));
    }

    @Test
    void test3() throws MethodInvocationFailedException, NoSuchMethodException, AmbiguousMethodException, InstantiationFailedException {
        E e = new E();
        Assertions.assertEquals("A", e.invokeMethod("rootMethod"));
    }

    @Test
    void test4() throws InstantiationFailedException, MethodInvocationFailedException, NoSuchMethodException, AmbiguousMethodException {
        D d = new D();
        Assertions.assertEquals("C", d.invokeMethod("cUnique"));
    }

    @Test
    void test5() throws InstantiationFailedException, MethodInvocationFailedException, NoSuchMethodException, AmbiguousMethodException {
        D d = new D();
        Assertions.assertEquals("F", d.invokeMethod("fMethod"));
    }

    @Test
    void test6() throws InstantiationFailedException {
        D d = new D();
        Assertions.assertThrows(NoSuchMethodException.class, () -> d.invokeMethod("amogus"));
    }

    public static class A extends MultHierarchyObject {
        public A() throws InstantiationFailedException {
        }

        public String method() {
            return "A";
        }

        public String rootMethod() {
            return "A";
        }
    }

    @MultExtends(value = F.class)
    @MultExtends(value=A.class)
    public static class B extends MultHierarchyObject {
        public B() throws InstantiationFailedException {
        }

        public String method() {
            return "B";
        }
    }

    @MultExtends(value=A.class)
    public static class C extends MultHierarchyObject {
        public C() throws InstantiationFailedException {
        }

        public String method() {
            return "C";
        }

        public String cUnique() {
            return "C";
        }
    }

    @MultExtends(B.class)
    @MultExtends(C.class)
    public static class D extends MultHierarchyObject {
        public D() throws InstantiationFailedException {
        }

        public String method() {
            return "D";
        }
    }

    @MultExtends(B.class)
    @MultExtends(C.class)
    public static class E extends MultHierarchyObject {
        public E() throws InstantiationFailedException {
        }
    }

    public static class F extends MultHierarchyObject {
        public F() throws InstantiationFailedException {
        }

        public String fMethod() {
            return "F";
        }
    }
}
