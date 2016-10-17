package com.tsystems.javaschool.util;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;

@Aspect
@Component
public class LoggingHandler {

    private Logger log = Logger.getLogger("jslog");

//    @Pointcut("within(@org.springframework.stereotype.Controller *)")
/*    @Pointcut("within(com.tsystems.javaschool.controllers.*)")
    public void controller() {
    }*/

    //    @Pointcut("execution(* *.*(..))")
    @Pointcut("execution(* com.tsystems.javaschool.controllers.rest..*(..))")
    protected void controller() {
    }

    @Pointcut("execution(* com.tsystems.javaschool.controllers.GlobalControllerExceptionHandler..*(..))")
    private void controllerAdvice() {
    }

    @Before("controllerAdvice() && args(ex, ..)")
    public void logException(JoinPoint joinPoint, Exception ex) {
        log.error("An exception handled by " + joinPoint.getSignature().getName() + "");
        log.error("Error description", ex);
        log.debug("-------------------------------------------------");
    }

    @AfterReturning(pointcut = "controllerAdvice()", returning = "result")
    public void logAdviceReturn(JoinPoint joinPoint, Object result) {
        log.debug("Controller advice return : " + result.toString());
        log.debug("-------------------------------------------------");
    }

    //before -> Any resource annotated with @Controller annotation
    //and all method and function taking HttpServletRequest as first parameter
    @Before("controller() && args(..,request)")
    public void logBefore(JoinPoint joinPoint, HttpServletRequest request) {

        log.debug("Entering in Method :  " + joinPoint.getSignature().getName());
        log.debug("Class Name :  " + joinPoint.getSignature().getDeclaringTypeName());
        log.debug("Arguments :  " + Arrays.toString(joinPoint.getArgs()));
        log.debug("Target class : " + joinPoint.getTarget().getClass().getName());

        if (null != request) {
            log.debug("Start Header Section of request ");
            log.debug("Method Type : " + request.getMethod());
            Enumeration headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = (String) headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                log.debug("Header Name: " + headerName + " Header Value : " + headerValue);
            }
            log.debug("Request Path info :" + request.getServletPath());
            log.debug("End Header Section of request ");
        }
        log.debug("-------------------------------------------------");
    }

    //After -> All method within resource annotated with @Controller annotation
    // and return a  value
    @AfterReturning(pointcut = "controller() ", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        String returnValue = this.getValue(result);
        log.debug("Method Return value : " + returnValue);
        log.debug("-------------------------------------------------");
    }

    //After -> Any method within resource annotated with @Controller annotation
    // throws an exception ...Log it
    @AfterThrowing(pointcut = "controller() ", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        log.error("An exception has been thrown in " + joinPoint.getSignature().getName() + " ()");
        log.error("Cause : " + exception.getCause());
        log.error("Message : " + exception.getMessage());
        log.error("-------------------------------------------------");
    }

    //Around -> Any method within resource annotated with @Controller annotation
    @Around("controller()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();
        try {
            String className = joinPoint.getSignature().getDeclaringTypeName();
            String methodName = joinPoint.getSignature().getName();
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - start;
            log.debug("Method " + className + "." + methodName + " ()" + " execution time : "
                    + elapsedTime + " ms");

            log.debug("-------------------------------------------------");
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument " + Arrays.toString(joinPoint.getArgs()) + " in "
                    + joinPoint.getSignature().getName() + "()");
            log.error("-------------------------------------------------");
            throw e;
        }
    }

    private String getValue(Object result) {
        String returnValue = null;
        if (null != result) {
            if (result.toString().endsWith("@" + Integer.toHexString(result.hashCode()))) {
                returnValue = ReflectionToStringBuilder.toString(result);
            } else {
                returnValue = result.toString();
            }
        }
        return returnValue;
    }
}
