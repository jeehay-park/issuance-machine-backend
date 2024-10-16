package com.ictk.issuance.common.annotations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;


@Slf4j
@Aspect
@Configuration
public class InjectSequenceValueAOP {

    @PersistenceContext
    private EntityManager em;


    @Before("execution(* org.springframework.data.repository.CrudRepository.save(*))")
    public void generateSequence(JoinPoint joinPoint) {
        Object [] args = joinPoint.getArgs();

        Arrays.stream(args).forEach(obj -> {
            AtomicReference<String> sequenceName = new AtomicReference<>();
            AtomicReference<String> tableName = new AtomicReference<>();
            AtomicReference<String> subId = new AtomicReference<>();
            AtomicReference<String> subSequenceName = new AtomicReference<>();
            AtomicReference<String> subIdName = new AtomicReference<>();
            AtomicReference<String> subTableName = new AtomicReference<>();

            Arrays.stream(obj.getClass().getDeclaredFields()).forEach(field -> {
                Arrays.stream(field.getAnnotations()).forEach(annotation -> {
                    if (annotation.annotationType().equals( InjectSequenceValue.class)) {
                        // log.info("******************** field annotation {}", annotation);
                        sequenceName.set(field.getAnnotation(InjectSequenceValue.class).sequencename());
                        tableName.set(field.getAnnotation(InjectSequenceValue.class).tablename());
                    }
                    if (annotation.annotationType().equals( InjectSubSequenceValue.class)) {
                        // log.info("******************** field annotation {}", annotation);
                        subSequenceName.set(field.getAnnotation(InjectSubSequenceValue.class).sequencename());
                        subIdName.set(field.getAnnotation(InjectSubSequenceValue.class).subidname());
                        subTableName.set(field.getAnnotation(InjectSubSequenceValue.class).tablename());
                    }
                    if (annotation.annotationType().equals( InjectSubIdValue.class)) {
                        String methodName = field.getAnnotation(InjectSubIdValue.class).methodname();
                        try {
                            Method method = obj.getClass().getMethod(methodName);
                            // log.debug("{}", method.getName());
                            // log.debug("----------------> {}", method.invoke(obj));
                            subId.set(""+method.invoke(obj));
                        } catch (Exception nse) {
                            log.error("error ***** {}", nse.getMessage());
                        }
                    }
                });
            });

            // log.info(" 1st sequence {}, {}", sequenceName.get(), tableName.get());
            // log.info(" sub sequence {}, {}, {}, {} ", subSequenceName.get(), subIdName.get(), subId.get(), subTableName.get());
            Arrays.stream(obj.getClass().getDeclaredFields()).forEach(field -> {

                Arrays.stream(field.getAnnotations()).forEach(annotation -> {
                    if (annotation.annotationType().equals( InjectSequenceValue.class)) {
                        try {
                            long nextval=getNextValue(sequenceName.get(), tableName.get());
                            // log.info("1st *************** Next value : {} {}", field.getName(), nextval);
                            field.set(obj, nextval);
                        } catch (Exception e) {
                            log.error("error ***** {}", e.getMessage());
                        }
                    }
                    if (annotation.annotationType().equals( InjectSubSequenceValue.class)) {
                        try {
                            long nextval=getSubNextValue(subSequenceName.get(), subIdName.get(), subId.get(), subTableName.get());
                            // log.info("2nd *************** Next value : {} {}", field.getName(), nextval);
                            field.set(obj, nextval);
                        } catch (Exception e) {
                            log.error("error ***** {}", e.getMessage());
                        }
                    }
                });
            });
        });
    }


    /**
     * This method fetches the next value from sequence
     */
    public long getNextValue(String sequence, String table) {
        long sequenceNextVal=0L;

        String nativeQuery = "SELECT coalesce(max("+sequence+" ),0)+1 FROM "+table;

        Number num = (Number)em.createNativeQuery("SELECT coalesce(max("+sequence+" ),0)+1 FROM "+table).getSingleResult();
        sequenceNextVal = num.longValue();

        log.info("getNextValue {} --> {}", nativeQuery, sequenceNextVal);

        return  sequenceNextVal;
    }

    public long getSubNextValue(String sequence, String subIdName, String subIdValue, String table) {
        long sequenceNextVal=0L;

        String nativeQuery = "SELECT coalesce(max("+sequence+" ),0)+1 FROM "+table+" WHERE "+subIdName+" = '"+subIdValue+"'";

        Number num = (Number)em.createNativeQuery( nativeQuery ).getSingleResult();
        sequenceNextVal = num.longValue();

        log.info("getSubNextValue {} --> {}", nativeQuery, sequenceNextVal);

        return  sequenceNextVal;
    }

}
