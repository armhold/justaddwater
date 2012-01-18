package net.ftlines.blog.cdidemo.cdi;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.interceptor.InterceptorBinding;

/**
 * NB: this file is derived from the open-source project hosted at https://github.com/42Lines/blog-cdidemo
 */
@Target({ METHOD, TYPE })
@Retention(RUNTIME)
@Documented
@InterceptorBinding
public @interface Conversational {

}
