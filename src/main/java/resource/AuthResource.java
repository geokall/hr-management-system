package resource;

import dto.LoginDTO;
import entity.HuaUser;
import repository.HuaUserRepository;
import security.TokenService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import static utils.HuaRoles.READER_ROLE;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    TokenService service;

    @Context
    SecurityContext securityContext;

    private final HuaUserRepository huaUserRepository;

    public AuthResource(HuaUserRepository huaUserRepository) {
        this.huaUserRepository = huaUserRepository;
    }

//    @POST
//    @Path("/register")
//    @Transactional
//    public User register(User user) {
//        user.persist(); //super simplified registration, no checks of uniqueness
//        return user;
//    }

    @POST
    @Path("/login")
    public String login(LoginDTO dto) {

        HuaUser huaUser = huaUserRepository.findById(1L).orElse(null);

        if (huaUser == null) {
            throw new WebApplicationException(Response.status(404)
                    .entity("No user found or password is incorrect")
                    .build());

        }

        return service.generateReaderToken(huaUser.getEmail(), dto.getPassword());
    }

    @GET
    @Path("/reader")
    @RolesAllowed(READER_ROLE)
    public String readerTest() {
        String name = securityContext.getUserPrincipal().getName();
        return name;
    }
}
