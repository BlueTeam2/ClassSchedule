package com.softserve.exception.handler;

import com.softserve.exception.*;
import com.softserve.exception.apierror.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

import static org.springframework.http.HttpStatus.*;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    //Handles FieldAlreadyExistsException. Triggered when entities field has conflict with already existed field.
    @ExceptionHandler(FieldAlreadyExistsException.class)
    protected ResponseEntity<Object> handleEntityFieldAlreadyExistsException(
            FieldAlreadyExistsException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(ex.getShortMessage());
        apiError.setDebugMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * Handles IncorrectTimeException, IncorrectPasswordException, ScheduleConflictException,
     * PeriodConflictException, EntityAlreadyExistsException, IncorrectEmailException, UsedEntityException.
     * Triggered when:
     * time in period / password, entered during registration by User, are incorrect;
     * schedule / period have conflicts with already existed entities;
     * object already exists in another class.
     *
     * @param ex exception
     * @return ApiError with exception data
     */
    @ExceptionHandler({IncorrectTimeException.class, IncorrectPasswordException.class,
            ScheduleConflictException.class, PeriodConflictException.class, EntityAlreadyExistsException.class,
            IncorrectEmailException.class, UsedEntityException.class, ParseFileException.class})
    protected ResponseEntity<Object> handleIncorrectFieldExceptions(
            RuntimeException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        apiError.setDebugMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    //Handles AccessDeniedException. Triggered when access for User is denied.
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDeniedException(
            AccessDeniedException ex) {
        ApiError apiError = new ApiError(FORBIDDEN);
        apiError.setMessage("Access is denied");
        apiError.setDebugMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    //Handles AccessDeniedException. Triggered when the credentials are invalid,
    // the account is neither locked nor disabled (when it' thrown).
    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<Object> handleBadCredentialsException(
            BadCredentialsException ex) {
        ApiError apiError = new ApiError(UNAUTHORIZED);
        apiError.setMessage("Invalid password or email");
        apiError.setDebugMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    // Handles EntityNotFoundException. Created to encapsulate errors with
    // more detail than javax.persistence.EntityNotFoundException.
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(
            EntityNotFoundException ex) {
        ApiError apiError = new ApiError(NOT_FOUND);
        apiError.setMessage(ex.getShortMessage());
        apiError.setDebugMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(SortOrderNotExistsException.class)
    protected ResponseEntity<Object> handleSortOrderNotExists(SortOrderNotExistsException exception) {
        ApiError apiError = new ApiError(NOT_FOUND, exception);
        apiError.setMessage(exception.getShortMessage());
        log.error(exception.getMessage());
        return buildResponseEntity(apiError);
    }

    // Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails.
    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(
            javax.validation.ConstraintViolationException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage("Validation error");
        apiError.addValidationErrors(ex.getConstraintViolations());
        log.error(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    // Handle DataIntegrityViolationException, inspects the cause for different DB causes.
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handlePersistenceException(final DataIntegrityViolationException ex) {
        ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR);
        apiError.setMessage(ex.getLocalizedMessage());
        apiError.setDebugMessage(ex.getLocalizedMessage());
        log.error(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    // Handle DeleteDisabledException. Triggered when an object requested for deleting is still referenced by another object.
    @ExceptionHandler(DeleteDisabledException.class)
    protected ResponseEntity<Object> handleDeleteDisabledException(
            DeleteDisabledException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        apiError.setDebugMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    // Handle MessageNotSendException. Triggered when we cannot send message on email.
    @ExceptionHandler(MessageNotSendException.class)
    protected ResponseEntity<Object> handleMessageNotSendException(MessageNotSendException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        apiError.setDebugMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    //Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        log.info("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());
        String error = "Malformed JSON request";
        log.error(ex.getMessage());
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }

    // Handle MissingServletRequestParameterException. Triggered when a 'required' request parameter is missing.
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";
        log.error(ex.getMessage());
        return buildResponseEntity(new ApiError(BAD_REQUEST, error, ex));
    }

    // Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid as well.
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        log.error(ex.getMessage());
        return buildResponseEntity(new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), ex));
    }

    // Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage("Validation error");
        apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
        apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
        log.error(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    // Handle HttpMessageNotWritableException.
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        String error = "Error writing JSON output";
        log.error(ex.getMessage());
        return buildResponseEntity(new ApiError(INTERNAL_SERVER_ERROR, error, ex));
    }

    // Handle NoHandlerFoundException.
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage("Could not find the " + ex.getHttpMethod() + " method for URL " + ex.getRequestURL());
        apiError.setDebugMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    // Handle javax.persistence.EntityNotFoundException
    @ExceptionHandler(javax.persistence.EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(javax.persistence.EntityNotFoundException ex) {
        log.error(ex.getMessage());
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, ex));
    }

    // Handle Exception, handle generic Exception.class
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage("The parameter " + ex.getName() + " of value " + ex.getValue()
                + " could not be converted to type " + Objects.requireNonNull(ex.getRequiredType()).getSimpleName());
        apiError.setDebugMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    //Handles AuthenticationException. Triggered when password is wrong.
    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {
        ApiError apiError = new ApiError(UNAUTHORIZED);
        apiError.setMessage("Invalid password or email");
        apiError.setDebugMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    //Handles FileDownloadException. Triggered when downloading of file failed.
    @ExceptionHandler(FileDownloadException.class)
    protected ResponseEntity<Object> handleFileDownloadException(FileDownloadException ex) {
        ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR);
        apiError.setMessage(ex.getMessage());
        apiError.setDebugMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    // Handle Exception in case, other handlers dod not handle it
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleOtherExceptions(Exception ex) {
        ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR);
        apiError.setMessage("Unexpected error occurred, please refer to the logs for more information");
        apiError.setDebugMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    //Response builder
    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }
}
