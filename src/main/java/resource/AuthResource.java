package resource;

import dto.LoginDTO;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import security.JwtResponseDTO;
import service.AuthService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

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
    public String readerTest() {
        String claim = jwt.getClaim(Claims.birthdate.toString());
        String name1 = jwt.getName();

        return name1 + claim;
    }
}
