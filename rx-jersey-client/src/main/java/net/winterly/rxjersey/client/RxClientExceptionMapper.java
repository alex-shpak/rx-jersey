package net.winterly.rxjersey.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Exception mapper to bypass response from client as server response<br>
 * For example if client will return {@code 404 Not Found} error then server response will be same including content
 */
public class RxClientExceptionMapper implements ExceptionMapper<ResponseProcessingException> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Response toResponse(ResponseProcessingException exception) {
        logger.error("Client error", exception);

        ClientErrorException clientErrorException = (ClientErrorException) exception.getCause();
        return clientErrorException.getResponse();
    }
}
