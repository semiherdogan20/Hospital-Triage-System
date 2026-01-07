package exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ErrorDTO> handleLockingException(Exception exception){
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage("Bu kayıt başka bir kullanıcı tarafından güncellendi.Lütfen sayfayı yenileyip tekrar deneyin.");
        errorDTO.setTimeStamp(System.currentTimeMillis());
        errorDTO.setStatus(HttpStatus.CONFLICT.value());

        return new ResponseEntity<ErrorDTO>(errorDTO,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleException(Exception exception){
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(exception.getMessage());
        errorDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorDTO.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<ErrorDTO>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
