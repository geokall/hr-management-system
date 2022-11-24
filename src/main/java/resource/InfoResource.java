package resource;

import dto.BonusDTO;
import org.eclipse.microprofile.jwt.JsonWebToken;
import service.InfoService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.OK;
import static utils.StaticRole.ADMIN_ROLE;
import static utils.StaticRole.READER_ROLE;

@Path("/info")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class InfoResource {

    private final JsonWebToken jsonWebToken;
    private final InfoService infoService;

    @Inject
    public InfoResource(JsonWebToken jsonWebToken, InfoService infoService) {
        this.jsonWebToken = jsonWebToken;
        this.infoService = infoService;
    }

    @PUT
    @Path("/update-bonus/{id}")
    @RolesAllowed({ADMIN_ROLE, READER_ROLE})
    public Response updateBonus(@PathParam("id") Long id, BonusDTO dto) {
        infoService.updateBonus(id, dto);

        return Response.ok().status(OK).build();
    }


}
