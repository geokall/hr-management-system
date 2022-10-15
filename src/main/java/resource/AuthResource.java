package resource;

import dto.LoginDTO;
import dto.UserDTO;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import security.JwtResponseDTO;
import service.AuthService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Set;

import static javax.ws.rs.core.Response.Status.OK;
import static utils.HuaRoles.READER_ROLE;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    private final JsonWebToken jsonWebToken;
    private final AuthService authService;

    @Inject
    public AuthResource(JsonWebToken jsonWebToken, AuthService authService) {
        this.jsonWebToken = jsonWebToken;
        this.authService = authService;
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
    public Response login(LoginDTO dto) {
        JwtResponseDTO response = authService.login(dto);

        return Response.ok(response)
                .status(OK)
                .build();
    }

    @GET
    @Path("/reader")
    @RolesAllowed(READER_ROLE)
    public UserDTO readerTest() {
        Set<String> role = jsonWebToken.getClaim(Claims.groups);
        String oneRole = role.stream().findFirst().orElse(null);
        String name = jsonWebToken.getName();

        UserDTO test = new UserDTO();
        test.setName(name);
        test.setRole(oneRole);

        return test;
    }
}
