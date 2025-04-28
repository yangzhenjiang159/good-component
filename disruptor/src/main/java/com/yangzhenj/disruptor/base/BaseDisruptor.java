package com.yangzhenj.disruptor.base;

import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadFactory;

@Data
public abstract class BaseDisruptor<T> {
    private static final Logger log = LoggerFactory.getLogger(BaseDisruptor.class);

    private Disruptor<DisruptorEvent<T>> disruptor;
    private RingBuffer<DisruptorEvent<T>> ringBuffer;
    private int Buffer_size = 1024;
    public String name;

    protected int getRingBufferSize() {return this.Buffer_size;}
    protected EventFactory<DisruptorEvent<T>> initEventFactory() {return new DefaultEventFactory<>();}

    /**
     * 是否使用工作池处理
     * @return
     */
    public abstract boolean isHandleWithWorkerPool();
    public abstract WorkHandler<DisruptorEvent<T>>[] getWorkHandlers();
    public abstract EventHandler<DisruptorEvent<T>>[] getEventHandlers();

    protected ThreadFactory initThreadFactory() {
        return (new ThreadFactoryBuilder()).setNameFormat("disruptor_thread_" + (String) Optional.ofNullable(this.name).orElse("") + "_%d").build();
    }

    protected ProducerType initProducerType() {
        return ProducerType.SINGLE;
    }

    protected WaitStrategy initWaitStrategy() {
        return new BlockingWaitStrategy();
    }

    public void onData(DisruptorEvent<T> event) {
        log.info("produce data :{}", JSON.toJSONString(event));
        if (Objects.isNull(this.disruptor)) {
            this.init();
        }

        long sequeue;
        while(true) {
            try {
                sequeue = this.ringBuffer.tryNext();
                break;
            } catch (Exception e) {
                log.info("produce ondata error:{}", e.getMessage());

                try {
                    Thread.sleep(800L);
                } catch (Exception var11) {
                }
            }
        }

        try {
            DisruptorEvent<T> current = (DisruptorEvent)this.ringBuffer.get(sequeue);
            BeanUtils.copyProperties(event, current);
        } finally {
            this.ringBuffer.publish(sequeue);
        }

    }

    private void init() {
        int size = this.getRingBufferSize();
        this.disruptor = new Disruptor(this.initEventFactory(), size, this.initThreadFactory(), this.initProducerType(), this.initWaitStrategy());
        if (this.isHandleWithWorkerPool()) {
            this.disruptor.handleEventsWithWorkerPool(this.getWorkHandlers());
        } else {
            this.disruptor.handleEventsWith(this.getEventHandlers());
        }

        this.ringBuffer = this.disruptor.start();
    }

    public void shutDown() {
        if (!Objects.isNull(this.disruptor)) {
            this.disruptor.shutdown();
        }

    }

    public void halt() {
        if (!Objects.isNull(this.disruptor)) {
            this.disruptor.halt();
        }

    }
}
