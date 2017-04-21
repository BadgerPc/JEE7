package com.github.dekstroza.hopsfactory.inventoryservice;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.TEXT;

import com.jayway.restassured.http.ContentType;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class HealthzEndpointIT {

    public static final String HEALTHZ_URL = Constants.INVENTORY_SERVICE_BASE_URL + "/healthz";

    @Test
    public void testHealthCheck__RETURNS_200() {
        given().get(HEALTHZ_URL).then().assertThat().statusCode(200).and().contentType(ContentType.TEXT);
    }
}