package io.dekstroza.github.jee7.swarmdemo.app.endpoints;

import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.OK;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Stateless
@Path("v1.0.0")
public class HealthzEndpoint {

    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    @GET
    @Path("healthz")
    public Response healthCheck() {
        return status(OK).entity("OK").build();
    }
}