package MultipleInheritance;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class MultHierarchyObject {
    List<Object> parents;

    public MultHierarchyObject() throws InstantiationFailedException {
        parents = new ArrayList<>();
        Class<?> thisClass = this.getClass();
        MultExtends[] extensions = thisClass.getAnnotationsByType(MultExtends.class);
        if (extensions.length > 0) {
            try {
                for (MultExtends e : extensions) {
                    //otherwise, create instance.
                    Constructor<?> parentConstructor = e.value().getDeclaredConstructor();
                    if (Modifier.isPrivate(parentConstructor.getModifiers())) {
                        throw new InstantiationFailedException();
                    }
                    parents.add(parentConstructor.newInstance());
                }
            } catch (Exception e) {
                throw new InstantiationFailedException();
            }
        }
    }

    static boolean isMethodDefinedInClass(Class<?> c, String name, Class<?>... argTypes) {
        try {
            c.getMethod(name, argTypes);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Object invokeMethod(String name, Object... callArgs) throws MethodInvocationFailedException, NoSuchMethodException {
        Class<?>[] argTypes = new Class<?>[callArgs.length];
        for (int i = 0; i < callArgs.length; i++) {
            argTypes[i] = callArgs[i].getClass();
        }

        Object o = findDefiningClass(name, argTypes);

        try {
            Method m = o.getClass().getMethod(name, argTypes);
            return m.invoke(o, callArgs);
        } catch (Exception e) {
            throw new MethodInvocationFailedException();
        }
    }

    public Object callNextMethod(String name, Object... callArgs) throws NoSuchMethodException, MethodInvocationFailedException {
        Class<?>[] argTypes = new Class<?>[callArgs.length];
        for (int i = 0; i < callArgs.length; i++) {
            argTypes[i] = callArgs[i].getClass();
        }

        Object o = findNextDefiningClass(name, argTypes);

        try {
            Method m = o.getClass().getMethod(name, argTypes);
            return m.invoke(o, callArgs);
        } catch (Exception e) {
            throw new MethodInvocationFailedException();
        }
    }

    public Object findDefiningClass(String name, Class<?>... argTypes) throws NoSuchMethodException {
        if (isMethodDefinedInClass(this.getClass(), name, argTypes)) return this;

        List<Object> nextLayer = new ArrayList<>();
        List<Object> currentLayer = new ArrayList<>(parents);

        while (!currentLayer.isEmpty()) {
            for (Object p : currentLayer) {
                if (isMethodDefinedInClass(p.getClass(), name, argTypes)) return p;
                List<Object> parents = ((MultHierarchyObject) p).parents;
                for (Object p1 : parents) {
                    if (!nextLayer.contains(p1))
                        nextLayer.add(p1);
                }
            }
            currentLayer.clear();
            currentLayer.addAll(nextLayer);
            nextLayer.clear();
        }
        throw new NoSuchMethodException();
    }

    public Object findNextDefiningClass(String name, Class<?>... argTypes) throws NoSuchMethodException {
        List<Object> nextLayer = new ArrayList<>();
        List<Object> currentLayer = new ArrayList<>(parents);
        boolean firstFound = isMethodDefinedInClass(this.getClass(), name, argTypes);

        while (!currentLayer.isEmpty()) {
            for (Object p : currentLayer) {
                if (isMethodDefinedInClass(p.getClass(), name, argTypes)) {
                    if (firstFound)
                        return p;
                    else firstFound = true;
                }
                List<Object> parents = ((MultHierarchyObject) p).parents;
                for (Object p1 : parents) {
                    if (!nextLayer.contains(p1))
                        nextLayer.add(p1);
                }
            }
            currentLayer.clear();
            currentLayer.addAll(nextLayer);
            nextLayer.clear();
        }
        throw new NoSuchMethodException();
    }
}
