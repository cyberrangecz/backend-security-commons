package cz.muni.ics.kypo.commons.rest;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import cz.muni.ics.kypo.commons.rest.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.UrlPathHelper;


/**
 * 
 * @author Pavel Seda (441048)
 *
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class CustomRestExceptionHandlerCommons extends ResponseEntityExceptionHandler {

  private static final Logger LOG = LoggerFactory.getLogger(CustomRestExceptionHandlerCommons.class);
  protected static final UrlPathHelper URLHELPER = new UrlPathHelper();

  // 400
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status,
      final WebRequest request) {
    LOG.debug("handleMethodArgumentNotValid({}, {}, {}, {})", new Object[] {ex, headers, status, request});

    final List<String> errors = new ArrayList<String>();
    for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.add(error.getField() + ": " + error.getDefaultMessage());
    }
    for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
      errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
    }
    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage()).setErrors(errors).setPath(request.getContextPath()).build();

    return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
  }

  @Override
  protected ResponseEntity<Object> handleBindException(final BindException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    LOG.debug("handleBindException({}, {}, {}, {})", new Object[] {ex, headers, status, request});

    final List<String> errors = new ArrayList<String>();
    for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.add(error.getField() + ": " + error.getDefaultMessage());
    }
    for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
      errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
    }
    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage()).setErrors(errors).setPath(request.getContextPath()).build();
    return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
  }

  @Override
  protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex, final HttpHeaders headers, final HttpStatus status,
      final WebRequest request) {
    LOG.debug("handleTypeMismatch({}, {}, {}, {})", new Object[] {ex, headers, status, request});

    final String error = ex.getValue() + " value for " + ex.getPropertyName() + " should be of type " + ex.getRequiredType();

    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage()).setError(error).setPath(request.getContextPath()).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestPart(final MissingServletRequestPartException ex, final HttpHeaders headers,
      final HttpStatus status, final WebRequest request) {
    LOG.debug("handleMissingServletRequestPart({}, {}, {}, {})", new Object[] {ex, headers, status, request});

    final String error = ex.getRequestPartName() + " part is missing";
    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage()).setError(error).setPath(request.getContextPath()).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException ex, final HttpHeaders headers,
      final HttpStatus status, final WebRequest request) {
    LOG.debug("handleMissingServletRequestParameter({}, {}, {}, {})", new Object[] {ex, headers, status, request});

    final String error = ex.getParameterName() + " parameter is missing";
    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage()).setError(error).setPath(request.getContextPath()).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  // 404
  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex, final HttpHeaders headers, final HttpStatus status,
      final WebRequest request) {
    LOG.debug("handleNoHandlerFoundException({}, {}, {}, {})", new Object[] {ex, headers, status, request});

    final String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(HttpStatus.NOT_FOUND, ex.getLocalizedMessage()).setError(error).setPath(request.getContextPath()).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  // 405
  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers,
      final HttpStatus status, final WebRequest request) {
    LOG.debug("handleHttpRequestMethodNotSupported({}, {}, {}, {})", new Object[] {ex, headers, status, request});

    final StringBuilder builder = new StringBuilder();
    builder.append(ex.getMethod());
    builder.append(" method is not supported for this request. Supported methods are ");
    ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));

    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(HttpStatus.METHOD_NOT_ALLOWED, ex.getLocalizedMessage()).setPath(request.getContextPath()).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  // 415
  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex, final HttpHeaders headers,
      final HttpStatus status, final WebRequest request) {
    LOG.debug("handleHttpMediaTypeNotSupported({}, {}, {}, {})", new Object[] {ex, headers, status, request});

    final StringBuilder builder = new StringBuilder();
    builder.append(ex.getContentType());
    builder.append(" media type is not supported. Supported media types are ");
    ex.getSupportedMediaTypes().forEach(t -> builder.append(t + " "));

    final ApiErrorSecurity apiError = new ApiErrorSecurity.ApiErrorBuilder(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2))
        .setPath(request.getContextPath()).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  // Custom methods for handling own exceptions

  @ExceptionHandler(BadGatewayException.class)
  public ResponseEntity<Object> handleBadGatewayException(final BadGatewayException ex, final WebRequest request, HttpServletRequest req) {
    LOG.debug("handleBadGatewayException({}, {}, {})", new Object[] {ex, request, req});

    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(BadGatewayException.class.getAnnotation(ResponseStatus.class).value(), ex.getLocalizedMessage())
            .setError(BadGatewayException.class.getAnnotation(ResponseStatus.class).reason()).setPath(URLHELPER.getRequestUri(req)).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<Object> handleBadRequestException(final BadRequestException ex, final WebRequest request, HttpServletRequest req) {
    LOG.debug("handleBadRequestException({}, {}, {})", new Object[] {ex, request, req});

    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(BadRequestException.class.getAnnotation(ResponseStatus.class).value(), ex.getLocalizedMessage())
            .setError(BadRequestException.class.getAnnotation(ResponseStatus.class).reason()).setPath(URLHELPER.getRequestUri(req)).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  // @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<Object> handleForbiddenException(final ForbiddenException ex, final WebRequest request, HttpServletRequest req) {
    LOG.debug("handleForbiddenException({}, {}, {})", new Object[] {ex, request, req});

    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(ForbiddenException.class.getAnnotation(ResponseStatus.class).value(), ex.getLocalizedMessage())
            .setError(ForbiddenException.class.getAnnotation(ResponseStatus.class).reason()).setPath(URLHELPER.getRequestUri(req)).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler(GatewayTimeoutException.class)
  public ResponseEntity<Object> handleGatewayTimeoutException(final GatewayTimeoutException ex, final WebRequest request, HttpServletRequest req) {
    LOG.debug("handleGatewayTimeoutException({}, {}, {})", new Object[] {ex, request, req});

    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(GatewayTimeoutException.class.getAnnotation(ResponseStatus.class).value(), ex.getLocalizedMessage())
            .setError(GatewayTimeoutException.class.getAnnotation(ResponseStatus.class).reason()).setPath(URLHELPER.getRequestUri(req)).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler(HTTPVersionNotSupportedException.class)
  public ResponseEntity<Object> handleHTTPVersionNotSupportedException(final HTTPVersionNotSupportedException ex, final WebRequest request,
      HttpServletRequest req) {
    LOG.debug("handleHTTPVersionNotSupportedException({}, {}, {})", new Object[] {ex, request, req});

    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(HTTPVersionNotSupportedException.class.getAnnotation(ResponseStatus.class).value(), ex.getLocalizedMessage())
            .setError(HTTPVersionNotSupportedException.class.getAnnotation(ResponseStatus.class).reason()).setPath(URLHELPER.getRequestUri(req)).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler(InsufficientStorageException.class)
  public ResponseEntity<Object> handleInsufficientStorageException(final InsufficientStorageException ex, final WebRequest request, HttpServletRequest req) {
    LOG.debug("handleInsufficientStorageException({}, {}, {})", new Object[] {ex, request, req});

    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(InsufficientStorageException.class.getAnnotation(ResponseStatus.class).value(), ex.getLocalizedMessage())
            .setError(InsufficientStorageException.class.getAnnotation(ResponseStatus.class).reason()).setPath(URLHELPER.getRequestUri(req)).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler(InternalServerErrorException.class)
  public ResponseEntity<Object> handleInternalServerErrorException(final InternalServerErrorException ex, final WebRequest request, HttpServletRequest req) {
    LOG.debug("handleInternalServerErrorException({}, {}, {})", new Object[] {ex, request, req});

    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(InternalServerErrorException.class.getAnnotation(ResponseStatus.class).value(), ex.getLocalizedMessage())
            .setError(InternalServerErrorException.class.getAnnotation(ResponseStatus.class).reason()).setPath(URLHELPER.getRequestUri(req)).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler(InvalidParameterException.class)
  public ResponseEntity<Object> handleInvalidParameterException(final InvalidParameterException ex, final WebRequest request, HttpServletRequest req) {
    LOG.debug("handleInvalidParameterException({}, {}, {})", new Object[] {ex, request, req});

    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(InvalidParameterException.class.getAnnotation(ResponseStatus.class).value(), ex.getLocalizedMessage())
            .setError(InvalidParameterException.class.getAnnotation(ResponseStatus.class).reason()).setPath(URLHELPER.getRequestUri(req)).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler(LoopDetectedException.class)
  public ResponseEntity<Object> handleLoopDetectedException(final LoopDetectedException ex, final WebRequest request, HttpServletRequest req) {
    LOG.debug("handleLoopDetectedException({}, {}, {})", new Object[] {ex, request, req});

    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(LoopDetectedException.class.getAnnotation(ResponseStatus.class).value(), ex.getLocalizedMessage())
            .setError(LoopDetectedException.class.getAnnotation(ResponseStatus.class).reason()).setPath(URLHELPER.getRequestUri(req)).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler(MethodNotAllowedException.class)
  public ResponseEntity<Object> handleMethodNotAllowedException(final MethodNotAllowedException ex, final WebRequest request, HttpServletRequest req) {
    LOG.debug("handleMethodNotAllowedException({}, {}, {})", new Object[] {ex, request, req});

    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(MethodNotAllowedException.class.getAnnotation(ResponseStatus.class).value(), ex.getLocalizedMessage())
            .setError(MethodNotAllowedException.class.getAnnotation(ResponseStatus.class).reason()).setPath(URLHELPER.getRequestUri(req)).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler(MovedPermanentlyException.class)
  public ResponseEntity<Object> handleMovedPermanentlyException(final MovedPermanentlyException ex, final WebRequest request, HttpServletRequest req) {
    LOG.debug("handleMovedPermanentlyException({}, {}, {})", new Object[] {ex, request, req});

    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(MovedPermanentlyException.class.getAnnotation(ResponseStatus.class).value(), ex.getLocalizedMessage())
            .setError(MovedPermanentlyException.class.getAnnotation(ResponseStatus.class).reason()).setPath(URLHELPER.getRequestUri(req)).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler(NetworkAuthenticationRequiredException.class)
  public ResponseEntity<Object> handleNetworkAuthenticationRequiredException(final NetworkAuthenticationRequiredException ex, final WebRequest request,
      HttpServletRequest req) {
    LOG.debug("handleNetworkAuthenticationRequiredException({}, {}, {})", new Object[] {ex, request, req});

    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(NetworkAuthenticationRequiredException.class.getAnnotation(ResponseStatus.class).value(), ex.getLocalizedMessage())
            .setError(NetworkAuthenticationRequiredException.class.getAnnotation(ResponseStatus.class).reason()).setPath(URLHELPER.getRequestUri(req)).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler(NoContentException.class)
  public ResponseEntity<Object> handleNoContentException(final NoContentException ex, final WebRequest request, HttpServletRequest req) {
    LOG.debug("handleNoContentException({}, {}, {})", new Object[] {ex, request, req});

    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(NoContentException.class.getAnnotation(ResponseStatus.class).value(), ex.getLocalizedMessage())
            .setError(NoContentException.class.getAnnotation(ResponseStatus.class).reason()).setPath(URLHELPER.getRequestUri(req)).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler(NotExtendedException.class)
  public ResponseEntity<Object> handleNotExtendedException(final NotExtendedException ex, final WebRequest request, HttpServletRequest req) {
    LOG.debug("handleNotExtendedException({}, {}, {})", new Object[] {ex, request, req});

    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(NotExtendedException.class.getAnnotation(ResponseStatus.class).value(), ex.getLocalizedMessage())
            .setError(NotExtendedException.class.getAnnotation(ResponseStatus.class).reason()).setPath(URLHELPER.getRequestUri(req)).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler(NotImplementedException.class)
  public ResponseEntity<Object> handleNotImplementedException(final NotImplementedException ex, final WebRequest request, HttpServletRequest req) {
    LOG.debug("handleNotImplementedException({}, {}, {})", new Object[] {ex, request, req});

    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(NotImplementedException.class.getAnnotation(ResponseStatus.class).value(), ex.getLocalizedMessage())
            .setError(NotImplementedException.class.getAnnotation(ResponseStatus.class).reason()).setPath(URLHELPER.getRequestUri(req)).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler(PayloadTooLargeException.class)
  public ResponseEntity<Object> handlePayloadTooLargeException(final PayloadTooLargeException ex, final WebRequest request, HttpServletRequest req) {
    LOG.debug("handlePayloadTooLargeException({}, {}, {})", new Object[] {ex, request, req});

    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(PayloadTooLargeException.class.getAnnotation(ResponseStatus.class).value(), ex.getLocalizedMessage())
            .setError(PayloadTooLargeException.class.getAnnotation(ResponseStatus.class).reason()).setPath(URLHELPER.getRequestUri(req)).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler(ProxyAuthenticationRequiredException.class)
  public ResponseEntity<Object> handleProxyAuthenticationRequiredException(final ProxyAuthenticationRequiredException ex, final WebRequest request,
      HttpServletRequest req) {
    LOG.debug("handleProxyAuthenticationRequiredException({}, {}, {})", new Object[] {ex, request, req});

    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(ProxyAuthenticationRequiredException.class.getAnnotation(ResponseStatus.class).value(), ex.getLocalizedMessage())
            .setError(ProxyAuthenticationRequiredException.class.getAnnotation(ResponseStatus.class).reason()).setPath(URLHELPER.getRequestUri(req)).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler(RangeNotSatisfiableException.class)
  public ResponseEntity<Object> handleRangeNotSatisfiableException(final RangeNotSatisfiableException ex, final WebRequest request, HttpServletRequest req) {
    LOG.debug("handleRangeNotSatisfiableException({}, {}, {})", new Object[] {ex, request, req});

    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(RangeNotSatisfiableException.class.getAnnotation(ResponseStatus.class).value(), ex.getLocalizedMessage())
            .setError(RangeNotSatisfiableException.class.getAnnotation(ResponseStatus.class).reason()).setPath(URLHELPER.getRequestUri(req)).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler(RequestTimeoutException.class)
  public ResponseEntity<Object> handleRequestTimeoutException(final RequestTimeoutException ex, final WebRequest request, HttpServletRequest req) {
    LOG.debug("handleRequestTimeoutException({}, {}, {})", new Object[] {ex, request, req});

    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(RequestTimeoutException.class.getAnnotation(ResponseStatus.class).value(), ex.getLocalizedMessage())
            .setError(RequestTimeoutException.class.getAnnotation(ResponseStatus.class).reason()).setPath(URLHELPER.getRequestUri(req)).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler(ResourceAlreadyExistingException.class)
  public ResponseEntity<Object> handleResourceAlreadyExistingException(final ResourceAlreadyExistingException ex, final WebRequest request,
      HttpServletRequest req) {
    LOG.debug("handleResourceAlreadyExistingException({}, {}, {})", new Object[] {ex, request, req});

    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(ResourceAlreadyExistingException.class.getAnnotation(ResponseStatus.class).value(), ex.getLocalizedMessage())
            .setError(ResourceAlreadyExistingException.class.getAnnotation(ResponseStatus.class).reason()).setPath(URLHELPER.getRequestUri(req)).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  // resource not created exception
  @ExceptionHandler({ResourceNotCreatedException.class})
  public ResponseEntity<Object> handleResourceNotCreatedException(final ResourceNotCreatedException ex, final WebRequest request, HttpServletRequest req) {
    LOG.debug("handleResourceNotCreatedException({}, {}, {})", new Object[] {ex, request, req});

    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(ResourceNotCreatedException.class.getAnnotation(ResponseStatus.class).value(), ex.getLocalizedMessage())
            .setError(ResourceNotCreatedException.class.getAnnotation(ResponseStatus.class).reason()).setPath(URLHELPER.getRequestUri(req)).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  // handle resource not found exceptions e.g. ~/{id} which does not exists
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Object> handleResourceNotFoundException(final ResourceNotFoundException ex, final WebRequest request, HttpServletRequest req) {
    LOG.debug("handleResourceNotFoundException({}, {}, {})", new Object[] {ex, request, req});

    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(ResourceNotFoundException.class.getAnnotation(ResponseStatus.class).value(), ex.getLocalizedMessage())
            .setError(ResourceNotFoundException.class.getAnnotation(ResponseStatus.class).reason()).setPath(URLHELPER.getRequestUri(req)).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  // resource not created exception
  @ExceptionHandler({ResourceNotModifiedException.class})
  public ResponseEntity<Object> handleResourceNotModifiedException(final ResourceNotModifiedException ex, final WebRequest request, HttpServletRequest req) {
    LOG.debug("handleResourceNotModifiedException({}, {}, {})", new Object[] {ex, request, req});

    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(ResourceNotModifiedException.class.getAnnotation(ResponseStatus.class).value(), ex.getLocalizedMessage())
            .setError(ResourceNotModifiedException.class.getAnnotation(ResponseStatus.class).reason()).setPath(URLHELPER.getRequestUri(req)).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler({ServiceUnavailableException.class})
  public ResponseEntity<Object> handleServiceUnavailableException(final ServiceUnavailableException ex, final WebRequest request, HttpServletRequest req) {
    LOG.debug("handleServiceUnavailableException({}, {}, {})", new Object[] {ex, request, req});

    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(ServiceUnavailableException.class.getAnnotation(ResponseStatus.class).value(), ex.getLocalizedMessage())
            .setError(ServiceUnavailableException.class.getAnnotation(ResponseStatus.class).reason()).setPath(URLHELPER.getRequestUri(req)).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler({TooManyRequestsException.class})
  public ResponseEntity<Object> handleTooManyRequestsException(final TooManyRequestsException ex, final WebRequest request, HttpServletRequest req) {
    LOG.debug("handleTooManyRequestsException({}, {}, {})", new Object[] {ex, request, req});

    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(TooManyRequestsException.class.getAnnotation(ResponseStatus.class).value(), ex.getLocalizedMessage())
            .setError(TooManyRequestsException.class.getAnnotation(ResponseStatus.class).reason()).setPath(URLHELPER.getRequestUri(req)).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler({UnauthorizedException.class})
  public ResponseEntity<Object> handleUnauthorizedException(final UnauthorizedException ex, final WebRequest request, HttpServletRequest req) {
    LOG.debug("handleUnauthorizedException({}, {}, {})", new Object[] {ex, request, req});

    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(UnauthorizedException.class.getAnnotation(ResponseStatus.class).value(), ex.getLocalizedMessage())
            .setError(UnauthorizedException.class.getAnnotation(ResponseStatus.class).reason()).setPath(URLHELPER.getRequestUri(req)).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler({UnprocessableEntityException.class})
  public ResponseEntity<Object> handleUnprocessableEntityException(final UnprocessableEntityException ex, final WebRequest request, HttpServletRequest req) {
    LOG.debug("handleUnprocessableEntityException({}, {}, {})", new Object[] {ex, request, req});

    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(UnprocessableEntityException.class.getAnnotation(ResponseStatus.class).value(), ex.getLocalizedMessage())
            .setError(UnprocessableEntityException.class.getAnnotation(ResponseStatus.class).reason()).setPath(URLHELPER.getRequestUri(req)).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler({UnsupportedMediaTypeException.class})
  public ResponseEntity<Object> handleUnsupportedMediaTypeException(final UnsupportedMediaTypeException ex, final WebRequest request, HttpServletRequest req) {
    LOG.debug("handleUnsupportedMediaTypeException({}, {}, {})", new Object[] {ex, request, req});

    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(UnsupportedMediaTypeException.class.getAnnotation(ResponseStatus.class).value(), ex.getLocalizedMessage())
            .setError(UnsupportedMediaTypeException.class.getAnnotation(ResponseStatus.class).reason()).setPath(URLHELPER.getRequestUri(req)).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler({URITooLongException.class})
  public ResponseEntity<Object> handleURITooLongException(final URITooLongException ex, final WebRequest request, HttpServletRequest req) {
    LOG.debug("handleURITooLongException({}, {}, {})", new Object[] {ex, request, req});

    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(URITooLongException.class.getAnnotation(ResponseStatus.class).value(), ex.getLocalizedMessage())
            .setError(URITooLongException.class.getAnnotation(ResponseStatus.class).reason()).setPath(URLHELPER.getRequestUri(req)).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler({VariantAlsoNegotiatesException.class})
  public ResponseEntity<Object> handleVariantAlsoNegotiatesException(final VariantAlsoNegotiatesException ex, final WebRequest request,
      HttpServletRequest req) {
    LOG.debug("handleVariantAlsoNegotiatesException({}, {}, {})", new Object[] {ex, request, req});

    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(VariantAlsoNegotiatesException.class.getAnnotation(ResponseStatus.class).value(), ex.getLocalizedMessage())
            .setError(VariantAlsoNegotiatesException.class.getAnnotation(ResponseStatus.class).reason()).setPath(URLHELPER.getRequestUri(req)).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  // Existing Java Exceptions

  // access denied
  @ExceptionHandler({AccessDeniedException.class})
  public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
    LOG.debug("handleAccessDeniedException({}, {})", new Object[] {ex, request});

    return new ResponseEntity<Object>("Access denied message here", new HttpHeaders(), HttpStatus.FORBIDDEN);
  }

  // handle illegal argument exceptions e.g. given payload is not valid against
  // draft-v4 schema
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Object> handleIllegalArgumentException(final IllegalArgumentException ex, final WebRequest request, HttpServletRequest req) {
    LOG.debug("handleIllegalArgumentException({}, {}, {})", new Object[] {ex, request, req});

    final ApiErrorSecurity apiError = new ApiErrorSecurity.ApiErrorBuilder(HttpStatus.NOT_ACCEPTABLE, ex.getLocalizedMessage()).setError("Illegal Argument")
        .setPath(URLHELPER.getRequestUri(req)).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  public ResponseEntity<Object> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex, final WebRequest request,
      HttpServletRequest req) {
    LOG.debug("handleMethodArgumentTypeMismatch({}, {}, {})", new Object[] {ex, request, req});

    final String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();
    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage()).setError(error).setPath(URLHELPER.getRequestUri(req)).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler({ConstraintViolationException.class})
  public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex, final WebRequest request, HttpServletRequest req) {
    LOG.debug("handleConstraintViolation({}, {}, {})", new Object[] {ex, request, req});

    final List<String> errors = new ArrayList<String>();
    for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": " + violation.getMessage());
    }

    final ApiErrorSecurity apiError =
        new ApiErrorSecurity.ApiErrorBuilder(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage()).setErrors(errors).setPath(URLHELPER.getRequestUri(req)).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  // 500
  @ExceptionHandler({Exception.class})
  public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request, HttpServletRequest req) {
    LOG.debug("handleAll({}, {}, {})", new Object[] {ex, request, req});

    final ApiErrorSecurity apiError = new ApiErrorSecurity.ApiErrorBuilder(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage())
        .setError("error occurred").setPath(URLHELPER.getRequestUri(req)).build();
    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }


}