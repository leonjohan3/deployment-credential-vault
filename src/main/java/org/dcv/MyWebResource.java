package org.dcv;

import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import static java.lang.String.format;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

@Slf4j
@Path("/v1")
public class MyWebResource {

    @GET
    @Path("/hello")
    @Produces(TEXT_PLAIN)
    public String hello(@QueryParam("name") @DefaultValue("leon") final String name) {
        log.info("starting hello, name: {}", name);
        return format("hello %s", name);
    }
}
