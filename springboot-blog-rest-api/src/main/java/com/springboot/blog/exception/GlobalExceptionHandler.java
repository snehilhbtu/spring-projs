package com.springboot.blog.exception;

import com.springboot.blog.dto.ErrorDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
/*
This whole class is just using a wrapper dto class to feed its exception data then send that class
as response to the user (ErrorDetails) plus we have declared custom exceptions extending runtime exception also
this class is also handling that
exception thrown -> this class catches -> feeds data to ErrorDetails --> sends this class with status code to the user
 */
//this tag means global exception handler
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    //handle specific exception
    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest webRequest){
        ErrorDetails errorDetails=new ErrorDetails(new Date(),exception.getMessage(),webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(BlogApiException.class)
    ResponseEntity<ErrorDetails> handleBlogApiException(BlogApiException exception,WebRequest webRequest){
        ErrorDetails errorDetails=new ErrorDetails(new Date(),exception.getMessage(),webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        HashMap<String,String> errorList=new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error)->{
            //fielderror extends errorObject(error)
            String fieldName=((FieldError)error).getField();
            String message=error.getDefaultMessage();

            errorList.put(fieldName,message);

        });

        return new ResponseEntity<>(errorList,HttpStatus.BAD_REQUEST);

    }


    @ExceptionHandler(AuthorizationDeniedException.class)
    ResponseEntity<ErrorDetails> handleAuthorizationDeniedException(AuthorizationDeniedException exception, WebRequest webRequest){
        ErrorDetails errorDetails=new ErrorDetails(new Date(),exception.getMessage(),webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }



    //handle global exception
}


