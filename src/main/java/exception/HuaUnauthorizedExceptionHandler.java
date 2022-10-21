package exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class HuaUnauthorizedExceptionHandler implements ExceptionMapper<HuaUnauthorizedException> {

    @Override
    public Response toResponse(HuaUnauthorizedException e) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(new HuaErrorMessage(e.getMessage()))
                .build();
    }
}
