package com.yangzhenj.disruptor.base;

import com.lmax.disruptor.EventFactory;

public class DefaultEventFactory<T> implements EventFactory<DisruptorEvent<T>> {
    public DisruptorEvent<T> newInstance() {
        return new DisruptorEvent<>();
    }
}
