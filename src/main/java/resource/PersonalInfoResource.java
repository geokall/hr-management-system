package resource;

import dto.InformationDTO;
import org.eclipse.microprofile.jwt.JsonWebToken;
import service.PersonalInfoService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.OK;
import static utils.StaticRole.ADMIN_ROLE;
import static utils.StaticRole.READER_ROLE;

@Path("/personal")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PersonalInfoResource {

    private final JsonWebToken jsonWebToken;
    private final PersonalInfoService personalInfoService;

    @Inject
    public PersonalInfoResource(JsonWebToken jsonWebToken,
                                PersonalInfoService personalInfoService) {
        this.jsonWebToken = jsonWebToken;
        this.personalInfoService = personalInfoService;
    }

    @GET
    @Path("/fetch-info/{id}")
    @RolesAllowed({ADMIN_ROLE, READER_ROLE})
    public Response findPersonalInfo(@PathParam("id") Long id) {
        InformationDTO response = personalInfoService.findPersonalInfo(id);

        return Response.ok(response).status(OK).build();
    }

    @PUT
    @Path("/update-info/{id}")
    @RolesAllowed({ADMIN_ROLE, READER_ROLE})
    public Response updateUserInfo(@PathParam("id") Long id, InformationDTO dto) {
        personalInfoService.updateBasicInformation(id, dto);

        return Response.ok().status(OK).build();
    }
}
