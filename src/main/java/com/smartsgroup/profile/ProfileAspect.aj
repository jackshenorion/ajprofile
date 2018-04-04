package com.smartsgroup.profile;

public aspect ProfileAspect {
    pointcut methodCall(): @annotation(ProfileMethod) && call(* *(..)) && !within(MyAspectJDemo);

    Object around(): methodCall() {
        long start = System.currentTimeMillis();
        Object o = proceed();
        long end = System.currentTimeMillis();
        ProfilingProcessor.onMethod(thisJoinPoint.getStaticPart().getSignature().toString(), start, end);
        return o;
    }
}