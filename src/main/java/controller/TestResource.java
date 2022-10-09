package controller;

import entity.HuaUser;
import repository.HuaUserRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TestResource {

    private final HuaUserRepository huaUserRepository;

    public TestResource(HuaUserRepository huaUserRepository) {
        this.huaUserRepository = huaUserRepository;
    }

    @POST
    @Path("/create")
    public Response create() {
        HuaUser huaUser = new HuaUser();
        huaUser.setName("geokall");

        HuaUser save = huaUserRepository.save(huaUser);
        return Response.ok(save).build();
    }

    @GET
    @Path("/test")
    public Response list() {
        return Response.ok(List.of("test")).build();
    }
}
