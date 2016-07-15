// Copyright 2015 AOL Platforms.

package com.aol.one.patch.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author Madhu Ramanna <madhu.ramanna@advertising.com> dt 2/22/16.
 */

@Provider
public class AppExceptionMapper implements ExceptionMapper<AppException> {

  Logger LOGGER = LoggerFactory.getLogger(AppExceptionMapper.class);

  @Override
  public Response toResponse(AppException exception) {

    LOGGER.warn("{}", exception);
    return Response.status(Response.Status.BAD_REQUEST)
        .entity(new ErrorResponse(Response.Status.BAD_REQUEST.getStatusCode(),
                                  exception.getMessage()))
        .type(MediaType.APPLICATION_JSON)
        .build();
  }
}
