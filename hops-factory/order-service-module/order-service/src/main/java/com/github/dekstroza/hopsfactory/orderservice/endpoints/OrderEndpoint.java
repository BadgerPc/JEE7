package com.github.dekstroza.hopsfactory.orderservice.endpoints;

import static com.github.dekstroza.hopsfactory.orderservice.OrderServiceApplication.APPLICATION_ORDER_SERVICE_V1_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.*;

import java.util.Optional;
import java.util.UUID;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dekstroza.hopsfactory.commons.rest.ExposeLogControl;
import com.github.dekstroza.hopsfactory.orderservice.domain.Order;

@Transactional
@Path("order")
@RequestScoped
@ExposeLogControl
public class OrderEndpoint {

    private static final Logger log = LoggerFactory.getLogger(OrderEndpoint.class);

    @Resource(lookup = "jboss/datasources/OrderDS")
    private DataSource dataSource;

    @PersistenceContext(unitName = "OrderPU")
    private EntityManager entityManager;

    @POST
    @Produces(APPLICATION_JSON)
    public void insertNewOrder(Order order, @Suspended AsyncResponse response) {
        log.info("Creating new order:{}", order);
        try {
            entityManager.persist(order);
            entityManager.flush();
            entityManager.refresh(order);
            log.info("Order created:{}", order);
            response.resume(status(CREATED).entity(order).build());
        } catch (Exception e) {
            response.resume(status(BAD_REQUEST).entity(e).build());

        }
    }

    @GET
    @Consumes({ APPLICATION_JSON, APPLICATION_ORDER_SERVICE_V1_JSON, TEXT_PLAIN })
    @Produces(APPLICATION_JSON)
    @Path("{id}")
    public void getOrderById(@PathParam("id") UUID id, @Suspended AsyncResponse response) {
        try {
            Optional.of(entityManager.find(Order.class, id)).ifPresent(x -> response.resume(status(OK).entity(x).build()));
            response.resume(status(NOT_FOUND).build());
        } catch (Exception e) {
            response.resume(status(INTERNAL_SERVER_ERROR).entity(e).build());

        }
    }

    @GET
    @Consumes({ APPLICATION_JSON, APPLICATION_ORDER_SERVICE_V1_JSON, TEXT_PLAIN })
    @Produces(APPLICATION_JSON)
    public void getAllOrders(@Suspended AsyncResponse response) {
        try {
            response.resume(status(OK).entity(entityManager.createQuery("SELECT o FROM order o", Order.class).getResultList()));
        } catch (Exception e) {
            response.resume(status(INTERNAL_SERVER_ERROR).entity(e).build());
        }
    }

    @PUT
    @Consumes({ APPLICATION_JSON, APPLICATION_ORDER_SERVICE_V1_JSON })
    @Produces({ APPLICATION_JSON, APPLICATION_ORDER_SERVICE_V1_JSON })
    public void updateOrder(Order order, @Suspended AsyncResponse response) {
        try {
            Optional.of(entityManager.find(Order.class, order.getId())).ifPresent(x -> response.resume(status(OK)
                       .entity(entityManager.merge(x.copyFrom(order))).build()));
            response.resume(status(NOT_FOUND).entity("No such order.").build());
        } catch (Exception e) {
            response.resume(status(INTERNAL_SERVER_ERROR).entity(e).build());
        }
    }

    @DELETE
    @Consumes({ APPLICATION_JSON, APPLICATION_ORDER_SERVICE_V1_JSON })
    @Produces(APPLICATION_JSON)
    public void deleteOrder(Order order, @Suspended AsyncResponse response) {
        try {
            Optional.of(entityManager.find(Order.class, order.getId())).ifPresent(x -> {
                entityManager.remove(x);
                response.resume(status(NO_CONTENT).build());
            });
            response.resume(status(NOT_FOUND).entity("No such order.").build());
        } catch (Exception e) {
            response.resume(status(INTERNAL_SERVER_ERROR).entity(e).build());
        }
    }

    @javax.enterprise.inject.Produces
    public DataSource getDataSource() {
        return dataSource;
    }

}
