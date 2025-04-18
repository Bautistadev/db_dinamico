package com.example.persona.demo.Exceptions.Custom;

public class BadRequestException extends RuntimeException{
    public BadRequestException(){
        super();
    }
    public BadRequestException(String message){
        super(message);
    }
}
