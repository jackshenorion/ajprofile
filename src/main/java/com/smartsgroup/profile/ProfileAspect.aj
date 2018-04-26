package com.smartsgroup.profile;

public aspect ProfileAspect {
    pointcut methodCall(): @annotation(ProfileMethod) && execution(* *(..)) && !within(ProfileAspect);
    pointcut finishMethodCall(): @annotation(FinishMethod) && execution(* *(..)) && !within(ProfileAspect);

    Object around(): methodCall() {
        long startTime = System.currentTimeMillis();
        try {
            return proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            ProfilingProcessor.onMethod(thisJoinPoint.getStaticPart().getSignature(), startTime, endTime);
        }
    }

    Object around(): finishMethodCall() {
        long startTime = System.currentTimeMillis();
        try {
            return proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            ProfilingProcessor.onMethod(thisJoinPoint.getStaticPart().getSignature(), startTime, endTime, true);
            ProfilingProcessor.logResult();
        }
    }

}