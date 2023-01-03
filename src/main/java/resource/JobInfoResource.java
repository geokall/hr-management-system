package resource;

import dto.*;
import org.eclipse.microprofile.jwt.JsonWebToken;
import service.JobInfoService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

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

    @GET
    @Path("/fetch-information/{id}")
    @RolesAllowed({ADMIN_ROLE, READER_ROLE})
    public Response fetchJobInformationInfo(@PathParam("id") Long id) {
        JobInformationDTO response = jobInfoService.fetchJobInformation(id);

        return Response.ok(response).status(OK).build();
    }

    @GET
    @Path("/fetch-last-effective-work-info/{id}")
    @RolesAllowed({ADMIN_ROLE, READER_ROLE})
    public Response fetchLastEffectiveWorkInfo(@PathParam("id") Long id) {
        IdNameDTO response = jobInfoService.fetchLastEffectiveWorkInfo(id);

        return Response.ok(response).status(OK).build();
    }

    @GET
    @Path("/fetch-bonus/{id}")
    @RolesAllowed({ADMIN_ROLE, READER_ROLE})
    public Response fetchBonus(@PathParam("id") Long id) {
        List<BonusDTO> response = jobInfoService.fetchBonus(id);

        return Response.ok(response).status(OK).build();
    }

    @PUT
    @Path("/update-info/{id}")
    @RolesAllowed({ADMIN_ROLE, READER_ROLE})
    public Response updateJobInfo(@PathParam("id") Long id, JobInformationDTO dto) {
        jobInfoService.updateJobInfo(id, dto);

        return Response.ok().status(OK).build();
    }

    @GET
    @Path("/fetch-work-information/{id}")
    @RolesAllowed({ADMIN_ROLE, READER_ROLE})
    public Response fetchWorkInformation(@PathParam("id") Long id) {
        List<WorkInformationDTO> response = jobInfoService.fetchWorkInformations(id);

        return Response.ok(response).status(OK).build();
    }

    @POST
    @Path("/create-work-information/{id}")
    @RolesAllowed({ADMIN_ROLE, READER_ROLE})
    public Response createWorkInformation(@PathParam("id") Long id, WorkInformationDTO dto) {
        jobInfoService.createWorkInformation(id, dto);

        return Response.ok().status(OK).build();
    }

    @PUT
    @Path("/update-work-information/{id}")
    @RolesAllowed({ADMIN_ROLE, READER_ROLE})
    public Response updateWorkInformation(@PathParam("id") Long id, WorkInformationDTO dto) {
        jobInfoService.updateWorkInformation(id, dto);

        return Response.ok().status(OK).build();
    }

    @DELETE
    @Path("/delete-work-information/{id}")
    @RolesAllowed({ADMIN_ROLE, READER_ROLE})
    public Response deleteWorkInformation(@PathParam("id") Long id) {
        jobInfoService.deleteWorkInformation(id);

        return Response.ok().status(OK).build();
    }

    @GET
    @Path("/fetch-compensations/{id}")
    @RolesAllowed({ADMIN_ROLE, READER_ROLE})
    public Response fetchCompensations(@PathParam("id") Long id) {
        List<CompensationDTO> response = jobInfoService.fetchCompensations(id);

        return Response.ok(response).status(OK).build();
    }

    @POST
    @Path("/create-compensation/{id}")
    @RolesAllowed({ADMIN_ROLE, READER_ROLE})
    public Response createCompensation(@PathParam("id") Long id, CompensationDTO dto) {
        jobInfoService.createCompensation(id, dto);

        return Response.ok().status(OK).build();
    }

    @PUT
    @Path("/update-compensation/{id}")
    @RolesAllowed({ADMIN_ROLE, READER_ROLE})
    public Response updateCompensation(@PathParam("id") Long id, CompensationDTO dto) {
        jobInfoService.updateCompensation(id, dto);

        return Response.ok().status(OK).build();
    }

    @DELETE
    @Path("/delete-compensation/{id}")
    @RolesAllowed({ADMIN_ROLE, READER_ROLE})
    public Response deleteCompensation(@PathParam("id") Long id) {
        jobInfoService.deleteCompensation(id);

        return Response.ok().status(OK).build();
    }
}
