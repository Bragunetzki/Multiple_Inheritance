package MultipleInheritance;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(MultExtensions.class)
public @interface MultExtends {
    Class<?> value();
}
