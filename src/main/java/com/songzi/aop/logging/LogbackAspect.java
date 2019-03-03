package com.songzi.aop.logging;

import com.songzi.domain.LogBackup;
import com.songzi.domain.enumeration.Level;
import com.songzi.domain.enumeration.LogType;
import com.songzi.security.SecurityUtils;
import com.songzi.service.LogBackupService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Map;

@Aspect
public class LogbackAspect {

    @Autowired
    private LogBackupService logBackupService;

    @Autowired
    private LogbackProperties logbackProperties;

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void springBeanPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * Pointcut that matches all Spring beans in the application's main packages.
     */
    @Pointcut("within(com.songzi.service..*)")
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * Advice that logs methods throwing exceptions.
     *
     * @param joinPoint join point for advice
     * @param e exception
     */
    @AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        String[] classNameArray = joinPoint.getSignature().getDeclaringTypeName().split("\\.");
        String className = classNameArray[classNameArray.length-1];
        String methodName = joinPoint.getSignature().getName();
        Map<String, Map<String,String>> needLogbakmethod = logbackProperties.getNeedlogbakmethod();
        if(needLogbakmethod != null && needLogbakmethod.containsKey(className)){
            Map<String,String> methodList = needLogbakmethod.get(className);
            if(methodList.containsKey(methodName)){
                //获取输入输出参数
                Object[] objects = joinPoint.getArgs();

                LogBackup logBackup = new LogBackup();
                logBackup.setCreatedTime(Instant.now());
                logBackup.setCreatedBy(SecurityUtils.getCurrentUserLogin().get());
                logBackup.setDescription(methodList.get(methodName)+":"+e.getMessage());
                logBackup.setSize(0);
                logBackup.setLevel(Level.INFO);
                logBackup.setAuthority("ROLE_ADMIN");
                logBackup.setLogType(LogType.SECURITY);
                logBackupService.insert(logBackup);
            }
        }
    }

    /**
     * Advice that logs when a method is entered and exited.
     *
     * @param joinPoint join point for advice
     * @return result
     * @throws Throwable throws IllegalArgumentException
     */
    @Around("applicationPackagePointcut() && springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String[] classNameArray = joinPoint.getSignature().getDeclaringTypeName().split("\\.");
        String className = classNameArray[classNameArray.length-1];
        String methodName = joinPoint.getSignature().getName();
        Map<String, Map<String,String>> needLogbakmethod = logbackProperties.getNeedlogbakmethod();
        Object object = joinPoint.proceed();
        if(needLogbakmethod != null && needLogbakmethod.containsKey(className)){
            Map<String,String> methodList = needLogbakmethod.get(className);
            if(methodList.containsKey(methodName)){
                Object[] objects = joinPoint.getArgs();
                String primaryKey = "";
                if(object == null){
                    primaryKey = objects[0] + "";
                }else{
                    Class clazz = object.getClass();
//                    Field[] fields = object.getClass().getDeclaredFields();//获得属性
//                    for (Field field : fields) {
//                    }
                    PropertyDescriptor pd = new PropertyDescriptor("id",
                        clazz);
                    Method getMethod = pd.getReadMethod();//获得get方法
                    Object o = getMethod.invoke(object);//执行get方法返回一个Object
                    primaryKey = o +"";
                }

                LogBackup logBackup = new LogBackup();
                logBackup.setCreatedTime(Instant.now());
                logBackup.setCreatedBy(SecurityUtils.getCurrentUserLogin().get());
                logBackup.setDescription(methodList.get(methodName)+":"+primaryKey);
                logBackup.setSize(0);
                logBackup.setLevel(Level.INFO);
                logBackup.setAuthority("ROLE_ADMIN");
                logBackup.setLogType(LogType.SECURITY);
                logBackupService.insert(logBackup);
            }
        }
        return object;
    }
}
