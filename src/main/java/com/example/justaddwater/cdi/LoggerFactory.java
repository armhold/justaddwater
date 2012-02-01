/*
 *  Copyright 2012 George Armhold
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
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
