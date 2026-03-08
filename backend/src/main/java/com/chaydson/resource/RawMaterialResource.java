package com.chaydson.resource;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.Produces;

import com.chaydson.dto.RawMaterialDTO;
import com.chaydson.service.RawMaterialService;

import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/api/raw-materials")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RawMaterialResource {
    private final RawMaterialService service;

    public RawMaterialResource(RawMaterialService service) {
        this.service = service;
    }

    @POST
    public Response create(@Valid RawMaterialDTO.Request request) {
        RawMaterialDTO.Response response = service.create(request);
        
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    public Response listAll() {
        return Response.ok(service.listAll()).build();
    }
}
