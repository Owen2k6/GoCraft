package minecraft.net.minecraft.src;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

// 
// Decompiled by Procyon v0.5.36
// 

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface MLProp {
    String name() default "";
    
    String info() default "";
    
    double min() default Double.NEGATIVE_INFINITY;
    
    double max() default Double.POSITIVE_INFINITY;
}
