package resource;

import dto.MainInfoDTO;
import org.eclipse.microprofile.jwt.JsonWebToken;
import service.UserService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.constraints.Email;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.OK;
import static utils.StaticRole.ADMIN_ROLE;
import static utils.StaticRole.READER_ROLE;

@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private final JsonWebToken jsonWebToken;
    private final UserService userService;

    @Inject
    public UserResource(JsonWebToken jsonWebToken,
                        UserService userService) {
        this.jsonWebToken = jsonWebToken;
        this.userService = userService;
    }

    @GET
    @Path("/main-info/{id}")
    @RolesAllowed({ADMIN_ROLE, READER_ROLE})
    public Response findMainInfo(@PathParam("id") Long id) {
        MainInfoDTO response = userService.findMainInfo(id);

        return Response.ok(response).status(OK).build();
    }

    @PUT
    @Path("/invite/{email}")
    @RolesAllowed(ADMIN_ROLE)
    public Response inviteUser(@PathParam("email") @Email String email) {
        userService.inviteUser(email);

        return Response.ok().status(OK).build();
    }
}
