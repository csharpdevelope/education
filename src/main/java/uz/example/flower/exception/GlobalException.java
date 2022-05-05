package uz.example.flower.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uz.example.flower.payload.ApiResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> error(HttpServletRequest request, Exception e) {
        ApiResponse<String> response = new ApiResponse<>();
        response.setPath(request.getRequestURI());
        response.setStatus(500);
        response.setMethod(request.getMethod());
        response.setMessage(e.getMessage());
        response.setTimestamp(new Date());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFound(HttpServletRequest request, NotFoundException exception) {
        ApiResponse<String> response = new ApiResponse<>();
        response.setTimestamp(new Date());
        response.setStatus(404);
        response.setMethod(request.getMethod());
        response.setPath(request.getRequestURI());
        response.setMessage(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
