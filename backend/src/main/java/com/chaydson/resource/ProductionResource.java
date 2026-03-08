package com.chaydson.resource;


import com.chaydson.dto.ProductionPlanDTO;
import com.chaydson.service.ProductionService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/production")
@Produces(MediaType.APPLICATION_JSON)
public class ProductionResource {

    private final ProductionService service;

    public ProductionResource(ProductionService service) {
        this.service = service;
    }

    @GET
    @Path("/optimize")
    public Response getOptimization() {
        ProductionPlanDTO plan = service.calculateOptimization();
        return Response.ok(plan).build();
    }
}