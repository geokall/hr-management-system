package resource;

import dto.BonusDTO;
import dto.JobInfoDTO;
import org.eclipse.microprofile.jwt.JsonWebToken;
import service.JobInfoService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.OK;
import static utils.StaticRole.ADMIN_ROLE;
import static utils.StaticRole.READER_ROLE;

@Path("/job")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class JobInfoResource {

    private final JsonWebToken jsonWebToken;
    private final JobInfoService jobInfoService;

    @Inject
    public JobInfoResource(JsonWebToken jsonWebToken,
                           JobInfoService jobInfoService) {
        this.jsonWebToken = jsonWebToken;
        this.jobInfoService = jobInfoService;
    }

    @PUT
    @Path("/update-bonus/{id}")
    @RolesAllowed({ADMIN_ROLE, READER_ROLE})
    public Response updateBonus(@PathParam("id") Long id, BonusDTO dto) {
        jobInfoService.updateBonus(id, dto);

        return Response.ok().status(OK).build();
    }

    @GET
    @Path("/fetch/{id}")
    @RolesAllowed({ADMIN_ROLE, READER_ROLE})
    public Response fetchInfo(@PathParam("id") Long id) {
        JobInfoDTO response = jobInfoService.fetchJobInfo(id);

        return Response.ok(response).status(OK).build();
    }
}
