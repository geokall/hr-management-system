package resource;

import dto.LoginDTO;
import entity.HuaUser;
import io.smallrye.jwt.auth.principal.DefaultJWTCallerPrincipal;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import repository.HuaUserRepository;
import security.TokenService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import java.security.Principal;

import static utils.HuaRoles.READER_ROLE;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    TokenService service;

    @Inject
    JsonWebToken jwt;

    @Inject
    HuaUserRepository huaUserRepository;

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

        HuaUser huaUser = huaUserRepository.findByUsernameAndPassword(dto.getUsername(), dto.getPassword())
                .orElseThrow(() -> {
                    throw new WebApplicationException(Response.status(404)
                            .entity("User not found")
                            .build());
                });

        return service.generateReaderToken(huaUser.getUsername(), huaUser.getBirthDate());
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
