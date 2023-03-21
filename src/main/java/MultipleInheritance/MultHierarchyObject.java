package MultipleInheritance;

import MultInheritanceExceptions.AmbiguousMethodException;
import MultInheritanceExceptions.InstantiationFailedException;
import MultInheritanceExceptions.MethodInvocationFailedException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class MultHierarchyObject {
    List<Object> parents;

    static private void initializeVirtualAncestors(MultExtends[] extensions) throws InstantiationFailedException {
        if (extensions.length <= 0) return;
        for (MultExtends extension : extensions) {
            initializeVirtualAncestors(extension.value().getAnnotationsByType(MultExtends.class));
        }
    }

    public MultHierarchyObject() throws InstantiationFailedException {
        parents = new ArrayList<>();
        Class<?> thisClass = this.getClass();
        MultExtends[] extensions = thisClass.getAnnotationsByType(MultExtends.class);
        if (extensions.length > 0) {
            try {
                initializeVirtualAncestors(extensions);

                for (MultExtends e : extensions) {
                    //otherwise, create instance.
                    Constructor<?> parentConstructor = e.value().getDeclaredConstructor();
                    if (Modifier.isPrivate(parentConstructor.getModifiers())) {
                        throw new InstantiationFailedException();
                    }
                    parentConstructor.setAccessible(true);
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

    public boolean multExtends(Class<?> c) {
        for (Object o : parents) {
            if (o.getClass() == c ||
                    (o instanceof MultHierarchyObject && ((MultHierarchyObject) o).multExtends(c)) ||
                    (!(o instanceof MultHierarchyObject) && c.isAssignableFrom(o.getClass())))
                return true;
        }
        return false;
    }

    public Object invokeMethod(String name, Object... callArgs) throws MethodInvocationFailedException, NoSuchMethodException, AmbiguousMethodException {
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

    public Object findDefiningClass(String name, Class<?>... argTypes) throws NoSuchMethodException, AmbiguousMethodException {
        List<Object> nextLayer = new ArrayList<>();
        List<Object> currentLayer = new ArrayList<>(parents);
        if (isMethodDefinedInClass(this.getClass(), name, argTypes)) return this;

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
}
