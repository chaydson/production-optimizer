package com.chaydson.resource;

import com.chaydson.dto.ProductDTO;
import com.chaydson.service.ProductService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    private final ProductService service;

    public ProductResource(ProductService service) {
        this.service = service;
    }

    @POST
    public Response create(@Valid ProductDTO.Request request) {
        ProductDTO.Response response = service.create(request);
        
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    public Response listAll() {
        return Response.ok(service.listAll()).build();
    }
}
