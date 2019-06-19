package com.sun.redisson.aop;

import com.sun.redisson.aop.anno.SyncLock;
import com.sun.redisson.aop.anno.SyncLockKey;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;

@Aspect
@Component
public class SyncLockAspect {

    @Autowired
    private RedissonClient redissonClient;

    @Pointcut("@annotation(com.sun.redisson.aop.anno.SyncLock)")
    public void syncLockAspect() {
    }

    @Around(value = "syncLockAspect()")
    public void around(ProceedingJoinPoint point) throws Throwable {
        Method method = ((MethodSignature) point.getSignature()).getMethod();

        // TODO 注解信息缓存
        SyncLock anno = method.getAnnotation(SyncLock.class);

        SyncLockKey[] keys = anno.keys();
        if (keys != null) {
            int length = keys.length;
            Lock allLock = null;
            if (length > 0) {
                Map<String, Object> parameterMap = getParameterMap(point);

                boolean isLock = false;
                try {
                    if (keys.length == 1) {
                        SyncLockKey key = keys[0];
                        // 获取锁
                        RLock lock = redissonClient.getLock(readExpr(key.value(), parameterMap));
                        // 尝试锁定
                        isLock = lock.tryLock(anno.maxWaitTime(), anno.expireTime(), anno.timeUnit());
                        allLock = lock;
                    } else {
                        RLock[] locks = new RLock[keys.length];
                        // 遍历获取锁
                        for (int i = 0; i < keys.length; i++) {
                            RLock lock = redissonClient.getLock(readExpr(keys[i].value(), parameterMap));
                            locks[i] = lock;
                        }
                        // 多锁合并
                        RedissonMultiLock multiLock = new RedissonMultiLock(locks);
                        // 尝试锁定
                        isLock = multiLock.tryLock(anno.maxWaitTime(), anno.expireTime(), anno.timeUnit());
                        allLock = multiLock;
                    }

                    // 锁定成功 执行业务逻辑
                    if (isLock) {
                        point.proceed(point.getArgs());
                    }
                } catch (InterruptedException e) {
                    // TODO 锁失败
                    e.printStackTrace();
                } finally {
                    unLock(isLock, allLock, anno);
                }
            }
        }
        // 没有锁，直接执行逻辑
        else {
            point.proceed(point.getArgs());
        }
    }

    /**
     * 解锁
     *
     * @param isLock
     * @param allLock
     * @param anno
     */
    private void unLock(boolean isLock, Lock allLock, SyncLock anno) {
        if (isLock) {
            if (anno.isTransactional() && TransactionSynchronizationManager.isActualTransactionActive()) {
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                    @Override
                    public void afterCompletion(int status) {
                        allLock.unlock();
                    }
                });
            } else {
                allLock.unlock();
            }
        }
        // 抛出自定义错误
        else {
            throw new RuntimeException("获取锁失败");
        }
    }

    /**
     * 获取参数map
     *
     * @param point
     * @return
     */
    private Map<String, Object> getParameterMap(ProceedingJoinPoint point) {
        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] parameterNames = methodSignature.getParameterNames();

        Object[] args = point.getArgs();

        Map<String, Object> parameterMap = new HashMap<>();
        if (ArrayUtils.isNotEmpty(parameterNames) && ArrayUtils.isNotEmpty(args)) {
            if (parameterNames.length != args.length) {
                throw new IllegalArgumentException("参数和参数名称不匹配");
            }
            for (int i = 0; i < args.length; i++) {
                parameterMap.put(parameterNames[i], args[i]);
            }
        }
        return parameterMap;
    }


    /**
     * 注入多个变量
     *
     * @param expr #{#shopId}.#{#typeId}.#{#paging.page}
     * @param map
     * @return
     */
    public static String readExpr(String expr, Map<String, Object> map) {
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(expr);
        EvaluationContext context = new StandardEvaluationContext();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());
        }
        return expression.getValue(context, String.class);
    }
}
