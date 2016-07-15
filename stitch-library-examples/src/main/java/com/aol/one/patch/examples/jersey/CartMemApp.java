// Copyright 2015 AOL Platforms.

package com.aol.one.patch.examples.jersey;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * @author Madhu Ramanna <madhu.ramanna@advertising.com> dt 2/5/16.
 */

@ApplicationPath("/cart-mem")
public class CartMemApp extends ResourceConfig {

  public CartMemApp() {
    // Register resources and providers using package-scanning.
    packages("com.aol.one.patch.examples.jersey");

    // Register my custom provider - not needed if it's in my.package.
    // register(SecurityRequestFilter.class);
    // Register an instance of LoggingFilter.
    // register(new LoggingFilter(LOGGER, true));

    // Enable Tracing support.
    //property(ServerProperties.TRACING, "ALL");

  }

}
