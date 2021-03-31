package com.talkdesk.tdx.nlp.platform.filters;

import io.vertx.core.http.HttpServerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

@Provider
public class LoggingFilter implements ContainerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Context
    private HttpServerRequest request;

    @Override
    public void filter(ContainerRequestContext context) {
        final String method = context.getMethod();
        final String path = context.getUriInfo().getPath();
        final String address = request.remoteAddress().toString();
        System.out.println(">>>>>" + this);

        logger.info("[m:{}][p:{}][client:{}] incoming request.", method, path, address);
    }
}

