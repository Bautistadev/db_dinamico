package com.example.persona.demo.Notations.Operations;

import com.example.persona.demo.Configuration.Datasource.DataSourceContextHolder;
import com.example.persona.demo.Logs.AppLogger;
import com.example.persona.demo.Notations.SelectDB;
import jakarta.annotation.PostConstruct;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
@Aspect
@Component
public class SelectDBOperation {

    private static final AppLogger log = AppLogger.getLogger(SelectDBOperation.class);

    @Before("@annotation(com.example.persona.demo.Notations.SelectDB)")
    public void setDB(JoinPoint joinPoint) throws NoSuchMethodException {
        Method metodo = ((MethodSignature) joinPoint.getSignature()).getMethod();
        SelectDB anotacion = metodo.getAnnotation(SelectDB.class);
        log.info("DB CONTEXT: "+anotacion.value());
        DataSourceContextHolder.setDataSource(anotacion.value());
    }

    @After("@annotation(com.example.persona.demo.Notations.SelectDB)")
    public void clearDataSource(JoinPoint joinPoint){
        DataSourceContextHolder.clearDataSource();
        log.info("CLEAR DATASOURCE CONTEXT");
    }
}
