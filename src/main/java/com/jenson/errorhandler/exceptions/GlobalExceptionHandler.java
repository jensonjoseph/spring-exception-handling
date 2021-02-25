package com.jenson.errorhandler.exceptions;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.log4j.Log4j2;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {
	@ExceptionHandler({UserNotFoundException.class,
							  ContentNotAllowedException.class
					  })
	@Nullable
	public final ResponseEntity<ApiError> handleException(Exception ex, WebRequest request) {
		HttpHeaders headers = new HttpHeaders();

		log.error("Handling " + ex.getClass().getSimpleName() + " due to " + ex.getMessage());

		if (ex instanceof UserNotFoundException) {
			HttpStatus status = HttpStatus.NOT_FOUND;
			UserNotFoundException unfe = (UserNotFoundException) ex;

			return handleUserNotFoundException(unfe, headers, status, request);
		}
		else if (ex instanceof ContentNotAllowedException) {
			HttpStatus status = HttpStatus.BAD_REQUEST;
			ContentNotAllowedException cnae = (ContentNotAllowedException) ex;

			return handleContentNotAllowedException(cnae, headers, status, request);
		}
		else {
			if (log.isWarnEnabled()) {
				log.warn("Unknown exception type: " + ex.getClass().getName());
			}

			HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
			return handleExceptionInternal(ex, null, headers, status, request);
		}
	}

	/**
	 * Customize the response for UserNotFoundException.
	 *
	 * @param ex The exception
	 * @param headers The headers to be written to the response
	 * @param status The selected response status
	 * @return a {@code ResponseEntity} instance
	 */
	protected ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException ex,
			HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		List<String> errors = Collections.singletonList(ex.getMessage());
		return handleExceptionInternal(ex, new ApiError(errors), headers, status, request);
	}

	/**
	 * Customize the response for ContentNotAllowedException.
	 *
	 * @param ex The exception
	 * @param headers The headers to be written to the response
	 * @param status The selected response status
	 * @return a {@code ResponseEntity} instance
	 */
	protected ResponseEntity<ApiError> handleContentNotAllowedException(ContentNotAllowedException ex,
			HttpHeaders headers,
			HttpStatus status,
			WebRequest request) {
		List<String> errorMessages = ex.getErrors()
				.stream()
				.map(contentError -> contentError.getObjectName() + " " + contentError.getDefaultMessage())
				.collect(Collectors.toList());

		return handleExceptionInternal(ex, new ApiError(errorMessages), headers, status, request);
	}

	/**
	 * A single place to customize the response body of all Exception types.
	 *
	 * <p>The default implementation sets the {@link WebUtils#ERROR_EXCEPTION_ATTRIBUTE}
	 * request attribute and creates a {@link ResponseEntity} from the given
	 * body, headers, and status.
	 *
	 * @param ex The exception
	 * @param body The body for the response
	 * @param headers The headers for the response
	 * @param status The response status
	 * @param request The current request
	 */
	protected ResponseEntity<ApiError> handleExceptionInternal(Exception ex, @Nullable ApiError body,
			HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
			request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
		}

		return new ResponseEntity<>(body, headers, status);
	}

}
