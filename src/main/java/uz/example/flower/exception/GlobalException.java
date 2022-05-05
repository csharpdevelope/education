package uz.example.flower.exception;

import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uz.example.flower.model.JSend;
import uz.example.flower.payload.ApiResponse;

@ControllerAdvice
public class GlobalException {

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> error(Exception e, HttpRequest request) {
//        ApiResponse<String> response = new ApiResponse<>();
//        response.setPath(request.getURI().getPath());
//        response.setStatus(500);
//        response.setMessage(e.getMessage());
//        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFound(NotFoundException exception) {
        JSend response = JSend.notFound(404, exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }
}
