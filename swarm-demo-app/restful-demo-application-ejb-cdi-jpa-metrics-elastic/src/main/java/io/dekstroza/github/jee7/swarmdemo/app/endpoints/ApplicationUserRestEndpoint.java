package io.dekstroza.github.jee7.swarmdemo.app.endpoints;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.*;
import static javax.ws.rs.core.Response.Status.OK;

import java.util.Collection;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import io.dekstroza.github.jee7.swarmdemo.app.api.ApplicationUser;
import io.dekstroza.github.jee7.swarmdemo.app.api.NoSuchApplicationUserException;

@Stateless
@Path("v1.0.0")
public class ApplicationUserRestEndpoint {

    @PersistenceContext
    private EntityManager em;

    @Produces(APPLICATION_JSON)
    @GET
    @Path("applicationUser")
    @Asynchronous
    public void findAllApplicationUsers(final @Suspended AsyncResponse response) {
        final Collection<ApplicationUser> applicationUsers = findAllApplicationUsers();
        response.resume(status(OK).entity(applicationUsers).build());
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Asynchronous
    public void insertApplicationUser(final ApplicationUser applicationUser, final @Suspended AsyncResponse response,
                                      final @Context UriInfo uriInfo) {
        try {
            final ApplicationUser persistedApplicationUser = insertApplicationUser(applicationUser);
            final UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(Integer.toString(persistedApplicationUser.getId()));
            response.resume(created(uriBuilder.build()).build());
        } catch (final Exception e) {
            response.resume(status(Status.BAD_REQUEST).entity(e.getMessage()).build());
        }

    }

    @Produces(APPLICATION_JSON)
    @GET
    @Path("applicationUser/{id}")
    @Asynchronous
    public void findApplicationUserById(@PathParam("id") final int id, final @Suspended AsyncResponse response) {
        try {
            final ApplicationUser applicationUsers = findApplicationUserById(id);
            response.resume(status(OK).entity(applicationUsers).build());
        } catch (final NoSuchApplicationUserException nae) {
            response.resume(status(Status.BAD_REQUEST).entity(nae.getMessage()).build());
        }
    }

    Collection<ApplicationUser> findAllApplicationUsers() {
        Collection<ApplicationUser> applicationUsers = em.createQuery("SELECT au FROM ApplicationUser au", ApplicationUser.class).getResultList();
        return applicationUsers;
    }

    ApplicationUser insertApplicationUser(final ApplicationUser applicationUser) {
        em.persist(applicationUser);
        return applicationUser;
    }

    ApplicationUser findApplicationUserById(int id) throws NoSuchApplicationUserException {
        try {
            return em.find(ApplicationUser.class, id);
        } catch (final NoResultException nre) {
            throw new NoSuchApplicationUserException(new StringBuilder("Application user with id=").append(id).append(" does not exist.").toString());
        }

    }
}