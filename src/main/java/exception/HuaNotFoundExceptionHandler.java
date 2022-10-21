package exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class HuaNotFoundExceptionHandler implements ExceptionMapper<HuaNotFoundException> {

    @Override
    public Response toResponse(HuaNotFoundException e) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new HuaErrorMessage(e.getMessage()))
                .build();
    }
}
