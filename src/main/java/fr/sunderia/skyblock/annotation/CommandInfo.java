package fr.sunderia.skyblock.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandInfo{

    String name();
    boolean needsPlayer() default true;
    String[] aliases();
    String permission() default "";

}
