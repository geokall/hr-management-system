package resource;

import dto.IdNameDTO;
import org.eclipse.microprofile.jwt.JsonWebToken;
import service.DataService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.core.Response.Status.OK;
import static utils.StaticRole.ADMIN_ROLE;
import static utils.StaticRole.READER_ROLE;

@Path("/data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DataResource {

    private final JsonWebToken jsonWebToken;
    private final DataService dataService;

    @Inject
    public DataResource(JsonWebToken jsonWebToken,
                        DataService dataService) {
        this.jsonWebToken = jsonWebToken;
        this.dataService = dataService;
    }

    @GET
    @Path("/fetch-locations/{id}")
    @RolesAllowed({ADMIN_ROLE, READER_ROLE})
    public Response fetchJobInformationInfo(@PathParam("id") Long id) {
        List<IdNameDTO> response = dataService.fetchLocations();

        return Response.ok(response).status(OK).build();
    }

    @GET
    @Path("/fetch-divisions/{id}")
    @RolesAllowed({ADMIN_ROLE, READER_ROLE})
    public Response fetchDivisions(@PathParam("id") Long id) {
        List<IdNameDTO> response = dataService.fetchDivisions();

        return Response.ok(response).status(OK).build();
    }
}
