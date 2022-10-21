package exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class HuaConflictExceptionHandler implements ExceptionMapper<HuaConflictException> {

    @Override
    public Response toResponse(HuaConflictException e) {
        return Response.status(Response.Status.CONFLICT)
                .entity(new HuaErrorMessage(e.getMessage()))
                .build();
    }
}
