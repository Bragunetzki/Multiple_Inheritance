import MultipleInheritance.InstantiationFailedException;
import MultipleInheritance.MethodInvocationFailedException;
import MultipleInheritance.MultExtends;
import MultipleInheritance.MultHierarchyObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MultInheritanceTests {
                        /*
                           F    A (method, rootMethod)
                            \ /   \
    (method, nextTestMethod) B     C (method, nextTestMethod, cUnique)
                              \   /
                                D (method), E (no method)
                     */

    @Test
    void testInheritanceMethod() throws InstantiationFailedException, MethodInvocationFailedException, NoSuchMethodException {
        D d = new D();
        Assertions.assertEquals("D", d.invokeMethod("method"));
    }

    @Test
    void testInheritanceMethod1() throws MethodInvocationFailedException, NoSuchMethodException, InstantiationFailedException {
        E e = new E();
        Assertions.assertEquals("B", e.invokeMethod("method"));
    }

    @Test
    void testInheritanceMethod2() throws MethodInvocationFailedException, NoSuchMethodException, InstantiationFailedException {
        E e = new E();
        Assertions.assertEquals("A", e.invokeMethod("rootMethod"));
    }

    @Test
    void testInheritanceMethod3() throws InstantiationFailedException, MethodInvocationFailedException, NoSuchMethodException {
        D d = new D();
        Assertions.assertEquals("C", d.invokeMethod("cUnique"));
    }

    @Test
    void testInheritanceMethod4() throws InstantiationFailedException, MethodInvocationFailedException, NoSuchMethodException {
        D d = new D();
        Assertions.assertEquals("F", d.invokeMethod("fMethod"));
    }

    @Test
    void testNoSuchMethod() throws InstantiationFailedException {
        D d = new D();
        Assertions.assertThrows(NoSuchMethodException.class, () -> d.invokeMethod("amogus"));
    }

    @Test
    void testPrivateMethod() throws InstantiationFailedException {
        D d = new D();
        Assertions.assertThrows(NoSuchMethodException.class, () -> d.invokeMethod("privateMethodB"));
    }

    @Test
    void testNextMethod() throws InstantiationFailedException, MethodInvocationFailedException, NoSuchMethodException {
        D d = new D();
        Assertions.assertEquals("B", d.callNextMethod("method"));
        Assertions.assertEquals("C", d.callNextMethod("nextTestMethod"));
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
    @MultExtends(value = A.class)
    public static class B extends MultHierarchyObject {
        public B() throws InstantiationFailedException {
        }

        public String method() {
            return "B";
        }

        public String nextTestMethod() { return "B"; }

        private String privateMethodB() {
            return "B";
        }
    }

    @MultExtends(value = A.class)
    public static class C extends MultHierarchyObject {
        public C() throws InstantiationFailedException {
        }

        public String method() {
            return "C";
        }

        public String nextTestMethod() { return "C"; }

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
