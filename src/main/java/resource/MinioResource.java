package resource;

import dto.FileDTO;
import org.eclipse.microprofile.jwt.JsonWebToken;
import service.MinioService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.OK;
import static utils.StaticRole.ADMIN_ROLE;
import static utils.StaticRole.READER_ROLE;

@Path("/minio")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MinioResource {

    private final JsonWebToken jsonWebToken;
    private final MinioService minioService;

    @Inject
    public MinioResource(JsonWebToken jsonWebToken, MinioService minioService) {
        this.jsonWebToken = jsonWebToken;
        this.minioService = minioService;
    }

    @POST
    @Path("/update-bucket")
    @RolesAllowed({ADMIN_ROLE, READER_ROLE})
    public Response createBonus(FileDTO fileDTO) {
        String bucketName = jsonWebToken.getName();

        minioService.updateBucketWithFileBy(bucketName, fileDTO);

        return Response.ok().status(OK).build();
    }
}
