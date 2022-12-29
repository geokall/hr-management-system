package resource;

import dto.UserDTO;
import org.eclipse.microprofile.jwt.JsonWebToken;
import service.SearchService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.core.Response.Status.OK;
import static utils.StaticRole.ADMIN_ROLE;
import static utils.StaticRole.READER_ROLE;

@Path("/people")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SearchResource {

    private final JsonWebToken jsonWebToken;
    private final SearchService searchService;

    @Inject
    public SearchResource(JsonWebToken jsonWebToken,
                          SearchService searchService) {
        this.jsonWebToken = jsonWebToken;
        this.searchService = searchService;
    }

    @GET
    @Path("/search-all")
    @RolesAllowed({ADMIN_ROLE, READER_ROLE})
    public Response searchAllPeople() {
        List<UserDTO> response = searchService.findAllUsers();

        return Response.ok(response).status(OK).build();
    }
}
