package com.wind.config.exception;

import com.wind.base.MessageLocaleService;
import com.wind.dto.ApiErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageLocaleService messageLocaleService;

    public RestExceptionHandler(MessageLocaleService messageLocaleService) {
        this.messageLocaleService = messageLocaleService;
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleInternalServerException(Exception ex, WebRequest request) {
        String traceId = this.generateTraceId();

        ApiErrorDto apiErrorDto = new ApiErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage());
        apiErrorDto.setTraceId(traceId);

        this.extractRequest(traceId, HttpStatus.INTERNAL_SERVER_ERROR, request);
        log.error("[TraceDetail]: {}", ExceptionUtils.getRootCauseMessage(ex));
        return new ResponseEntity<>(apiErrorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ServerApiException.class)
    public ResponseEntity<ApiErrorDto> handlerServerApiException(ServerApiException ex, WebRequest request) {
        String message = this.buildMessage(ex.getErrorCode(), ex.getParams());
        String traceId = this.generateTraceId();

        ApiErrorDto apiErrorDto = new ApiErrorDto(HttpStatus.UNPROCESSABLE_ENTITY, message);
        apiErrorDto.setTraceId(this.generateTraceId());

        this.extractRequest(traceId, HttpStatus.UNPROCESSABLE_ENTITY, request);
        log.error("[TraceDetail]: {}", ExceptionUtils.getStackTrace(ex));
        return new ResponseEntity<>(apiErrorDto, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ValidateException.class)
    public ResponseEntity<ApiErrorDto> handlerValidateException(ValidateException ex, WebRequest request) {
        String message = this.buildMessage(ex.getErrorCode(), ex.getParams());
        String traceId = this.generateTraceId();

        ApiErrorDto apiErrorDto = new ApiErrorDto(HttpStatus.BAD_REQUEST, message);
        apiErrorDto.setTraceId(this.generateTraceId());

        this.extractRequest(traceId, HttpStatus.BAD_REQUEST, request);
        return new ResponseEntity<>(apiErrorDto, HttpStatus.BAD_REQUEST);
    }


    private String buildMessage(String messageCode, Object... params) {
        return messageLocaleService.getMessage(messageCode, params);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers,
                                                                         HttpStatus status, WebRequest request) {
        String traceId = this.generateTraceId();

        Set<Object> params = new HashSet<>();
        params.add(ex.getMethod());
        if (ex.getSupportedHttpMethods() != null) {
            params.addAll(ex.getSupportedHttpMethods());
        }

        String message = messageLocaleService.getMessage("error.method_not_support", params.toArray());

        ApiErrorDto apiErrorDto = new ApiErrorDto(status, message);
        apiErrorDto.setTraceId(this.generateTraceId());

        this.extractRequest(traceId, HttpStatus.METHOD_NOT_ALLOWED, request);
        return new ResponseEntity<>(apiErrorDto, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers,
                                                                     HttpStatus status, WebRequest request) {
        String traceId = this.generateTraceId();
        Set<Object> params = new HashSet<>();

        params.add(ex.getContentType());
        params.addAll(ex.getSupportedMediaTypes());
        String message = messageLocaleService.getMessage("error.media_type_not_supported", params.toArray());

        ApiErrorDto apiErrorDto = new ApiErrorDto(status, message);
        apiErrorDto.setTraceId(this.generateTraceId());

        this.extractRequest(traceId, status, request);
        return new ResponseEntity<>(apiErrorDto, status);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
                                                                   HttpStatus status, WebRequest request) {
        String error = messageLocaleService.getMessage("error.api_not_found", ex.getHttpMethod(), ex.getRequestURL());
        ApiErrorDto apiErrorDto = new ApiErrorDto(status, error);
        return new ResponseEntity<>(apiErrorDto, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        StringBuilder errors = new StringBuilder();
        String traceId = this.generateTraceId();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.append(error.getField()).append(": ").append(error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.append(error.getObjectName()).append(": ").append(error.getDefaultMessage());
        }

        ApiErrorDto apiErrorDto = new ApiErrorDto(status, errors.toString());
        apiErrorDto.setTraceId(this.generateTraceId());

        this.extractRequest(traceId, status, request);
        return handleExceptionInternal(ex, apiErrorDto, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatus status,
                                                                          WebRequest request) {
        String traceId = this.generateTraceId();
        String error = messageLocaleService.getMessage("error.missing_parameter", ex.getParameterName());
        ApiErrorDto apiErrorDto = new ApiErrorDto(HttpStatus.BAD_REQUEST, error);
        apiErrorDto.setTraceId(traceId);

        this.extractRequest(traceId, HttpStatus.BAD_REQUEST, request);
        return new ResponseEntity<>(apiErrorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                   WebRequest request) {
        String traceId = this.generateTraceId();
        String error = messageLocaleService.getMessage("error.method_argument_type_mismatch", ex.getName(),
                Objects.requireNonNull(ex.getRequiredType()).getName());

        ApiErrorDto apiErrorDto = new ApiErrorDto(HttpStatus.BAD_REQUEST, error);
        apiErrorDto.setTraceId(traceId);

        this.extractRequest(traceId, HttpStatus.BAD_REQUEST, request);
        return new ResponseEntity<>(apiErrorDto, HttpStatus.BAD_REQUEST);
    }

    private void extractRequest(String traceId, HttpStatus httpStatus, WebRequest request) {
        log.info("================================== {} ==================================", httpStatus.toString());
        ServletWebRequest servletWebRequest = ((ServletWebRequest) request);
        HttpServletRequest httpServletRequest = servletWebRequest.getRequest();

        log.info("[TraceId]: {}", traceId);
        log.info("[API]: {}", httpServletRequest.getRequestURI());
        log.info("[Method]: {}", httpServletRequest.getMethod());
        log.info("[Status]: {}", httpStatus.value());
        Iterator<String> headerNames = request.getHeaderNames();

        while (headerNames.hasNext()) {
            String headerName = headerNames.next();
            String headerValue = request.getHeader(headerName);
            log.info("Header {}: {}", headerName, headerValue);
        }

        Map<String, String[]> parameters = request.getParameterMap();
        parameters.forEach((param, values) -> log.info("Param {}: {}", param, Arrays.toString(values)));

        String body = this.extractBody(httpServletRequest);
        if (StringUtils.isNotBlank(body)) {
            log.info("[Body]: {}", body);
        }
    }

    /**
     * That’s because the body is realized as an InputStream which gets consumed on the first read.
     * Instead, it is necessary to wrap the body and copy the content of the stream while reading.
     * Therefore, it is only possible to access the body content AFTER the actual processing of the request was done.
     * <p>
     * The request wrapped before in first filter and allows this content to be retrieved via a byte array.
     *
     * @param request the servlet request
     * @return the body payload
     * @see com.wind.config.AuthenticationFilter
     * <p>
     * So, we need wrap request to make sure we can read the body of the request
     * (otherwise it will be consumed by the actual request handler)
     */
    private String extractBody(ServletRequest request) {
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        String payload = "[Empty]";
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {

                try {
                    payload = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
                } catch (UnsupportedEncodingException ex) {
                    payload = "[unknown]";
                }
            }
        }
        return payload;
    }

    private String generateTraceId() {
        return RandomStringUtils.randomNumeric(4) +
                RandomStringUtils.randomAlphanumeric(8);
    }

}
