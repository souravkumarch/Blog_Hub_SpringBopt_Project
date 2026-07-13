package in.scalive.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = ResourceAlreadyExistException.class)
	public ResponseEntity<ErrorResponse> handleResourceAlreadyExistException(ResourceAlreadyExistException ex) {
		ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(value=MethodArgumentNotValidException.class)
	 public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
	 BindingResult bs =ex.getBindingResult();
	 List<FieldError> ferror = bs.getFieldErrors();
	 StringBuffer sb = new StringBuffer();
	 for(FieldError fe:ferror) {
		 sb.append(fe.getField()+":");
		 sb.append(fe.getDefaultMessage()+";");
	 }
	 ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),sb.toString());
	 return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
	 }
	
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex) {
		ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
