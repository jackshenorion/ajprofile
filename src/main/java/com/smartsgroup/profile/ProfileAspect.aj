package com.smartsgroup.profile;

public aspect ProfileAspect {
    pointcut methodCall(): @annotation(ProfileMethod) && call(* *(..)) && !within(ProfileAspect);
    pointcut finishMethodCall(): @annotation(FinishMethod) && execution(* *(..)) && !within(ProfileAspect);

    Object around(): methodCall() {
        long startTime = System.currentTimeMillis();
        try {
            return proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            ProfilingProcessor.onMethod(thisJoinPoint.getStaticPart().getSignature().toString(), startTime, endTime);
        }
    }

    Object around(): finishMethodCall() {
        long startTime = System.currentTimeMillis();
        try {
            return proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            ProfilingProcessor.onMethod(thisJoinPoint.getStaticPart().getSignature().toString(), startTime, endTime, true);
            ProfilingProcessor.logResult();
        }
    }

}