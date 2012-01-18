package com.example.justaddwater.cdi;

import org.slf4j.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 * Produces an injectable logger with name based on the injecting class.
 * Wherever you need a logger, just do:
 *
 *     @Inject
 *     Logger log;
 *
 * @author George Armhold armhold@gmail.com
 */
public class LoggerFactory
{
    @Produces
    Logger createLogger(InjectionPoint injectionPoint)
    {
        String name = injectionPoint.getMember().getDeclaringClass().getName();
        return org.slf4j.LoggerFactory.getLogger(name);
    }

}
