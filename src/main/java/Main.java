import MultipleInheritance.InstantiationFailedException;
import MultipleInheritance.MethodInvocationFailedException;
import MultipleInheritance.MultExtends;
import MultipleInheritance.MultHierarchyObject;

public class Main {
    public static void main(String[] args) throws InstantiationFailedException, MethodInvocationFailedException, NoSuchMethodException {
        D d;
        d = new D();
        String res1 = (String) d.invokeMethod("method");
        String res2 = (String) d.callNextMethod("method");
        String res3 = (String) d.invokeMethod("cMethod");
        String res4 = (String) d.invokeMethod("rootMethod");

        System.out.println("Result of calling method from D: " + res1);
        System.out.println("Result of call-next-method \"method\" from D: " + res2);
        System.out.println("Result of calling cMethod from D: " + res3);
        System.out.println("Result of calling rootMethod from D: " + res4);
    }

    /*
                                A (method, rootMethod)
                              /   \
                    (method) B     C (method, cMethod)
                              \   /
                                D (method)
                     */

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

    @MultExtends(A.class)
    public static class B extends MultHierarchyObject {
        public B() throws InstantiationFailedException {
        }

        public String method() {
            return "B";
        }
    }

    @MultExtends(A.class)
    public static class C extends MultHierarchyObject {
        public C() throws InstantiationFailedException {
        }

        public String method() {
            return "C";
        }

        public String cMethod() {
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
}
