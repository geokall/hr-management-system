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

import java.util.Set;

import static utils.HuaRoles.READER_ROLE;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    JsonWebToken jwt;

    @Inject
    AuthService authService;

//    @POST
//    @Path("/register")
//    @Transactional
//    public User register(User user) {
//        user.persist(); //super simplified registration, no checks of uniqueness
//        return user;
//    }

    @POST
    @Path("/login")
    public JwtResponseDTO login(LoginDTO dto) {
        JwtResponseDTO response = authService.login(dto);

        return response;
    }

    @GET
    @Path("/reader")
    @RolesAllowed(READER_ROLE)
    public UserDTO readerTest() {
        Set<String> role = jwt.getClaim(Claims.groups);
        String oneRole = role.stream().findFirst().orElse(null);
        String name = jwt.getName();

        UserDTO test = new UserDTO();
        test.setName(name);
        test.setRole(oneRole);

        return test;
    }
}
