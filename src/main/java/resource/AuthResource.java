package resource;

import dto.InformationDTO;
import dto.LoginDTO;
import dto.PersonalInformationDTO;
import dto.RegisterDTO;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import security.JwtResponseDTO;
import service.AuthService;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

import static javax.ws.rs.core.Response.Status.OK;
import static utils.StaticRole.ADMIN_ROLE;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    private final JsonWebToken jsonWebToken;
    private final AuthService authService;

    @Inject
    public AuthResource(JsonWebToken jsonWebToken,
                        AuthService authService) {
        this.jsonWebToken = jsonWebToken;
        this.authService = authService;
    }

    @POST
    @Path("/register")
    @PermitAll
    public Response register(@QueryParam("role") String role,
                             @Valid RegisterDTO dto) {
        Long response = authService.register(role, dto);

        return Response.ok(response).status(OK).build();
    }

    @POST
    @Path("/login")
    @PermitAll
    public Response login(@Valid LoginDTO dto) {
        JwtResponseDTO response = authService.login(dto);

        return Response.ok(response).status(OK).build();
    }

    @GET
    @Path("/reader")
    @RolesAllowed(ADMIN_ROLE)
    public InformationDTO readerTest() {
        Set<String> role = jsonWebToken.getClaim(Claims.groups);
        String oneRole = role.stream().findFirst().orElse(null);
        String name = jsonWebToken.getName();

        InformationDTO test = new InformationDTO();

        PersonalInformationDTO personalInformationDTO = new PersonalInformationDTO();
        personalInformationDTO.setName(name);

        test.setPersonalInformation(personalInformationDTO);

        return test;
    }
}
