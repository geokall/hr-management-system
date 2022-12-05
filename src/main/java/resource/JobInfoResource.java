package resource;

import dto.BonusDTO;
import dto.EducationDTO;
import dto.JobInformationDTO;
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

    @POST
    @Path("/create-bonus/{id}")
    @RolesAllowed({ADMIN_ROLE, READER_ROLE})
    public Response createBonus(@PathParam("id") Long id, BonusDTO dto) {
        jobInfoService.createBonus(id, dto);

        return Response.ok().status(OK).build();
    }

    @PUT
    @Path("/update-bonus/{id}")
    @RolesAllowed({ADMIN_ROLE, READER_ROLE})
    public Response updateBonus(@PathParam("id") Long id, BonusDTO dto) {
        jobInfoService.updateBonus(id, dto);

        return Response.ok().status(OK).build();
    }

    @DELETE
    @Path("/delete-bonus/{id}")
    @RolesAllowed({ADMIN_ROLE, READER_ROLE})
    public Response deleteBonus(@PathParam("id") Long id) {
        jobInfoService.deleteBonus(id);

        return Response.ok().status(OK).build();
    }

    @POST
    @Path("/create-education/{id}")
    @RolesAllowed({ADMIN_ROLE, READER_ROLE})
    public Response createEducation(@PathParam("id") Long id, EducationDTO dto) {
        jobInfoService.createEducation(id, dto);

        return Response.ok().status(OK).build();
    }

    @PUT
    @Path("/update-education/{id}")
    @RolesAllowed({ADMIN_ROLE, READER_ROLE})
    public Response updateEducation(@PathParam("id") Long id, EducationDTO dto) {
        jobInfoService.updateEducation(id, dto);

        return Response.ok().status(OK).build();
    }

    @DELETE
    @Path("/delete-education/{id}")
    @RolesAllowed({ADMIN_ROLE, READER_ROLE})
    public Response deleteEducation(@PathParam("id") Long id) {
        jobInfoService.deleteEducation(id);

        return Response.ok().status(OK).build();
    }

    @GET
    @Path("/fetch-information/{id}")
    @RolesAllowed({ADMIN_ROLE, READER_ROLE})
    public Response fetchJobInformationInfo(@PathParam("id") Long id) {
        JobInformationDTO response = jobInfoService.fetchJobInformation(id);

        return Response.ok(response).status(OK).build();
    }

    @PUT
    @Path("/update-info/{id}")
    @RolesAllowed({ADMIN_ROLE, READER_ROLE})
    public Response updateJobInfo(@PathParam("id") Long id, JobInformationDTO dto) {
        jobInfoService.updateJobInfo(id, dto);

        return Response.ok().status(OK).build();
    }
}
