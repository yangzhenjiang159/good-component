package com.yangzhenj.disruptor.business;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import com.yangzhenj.disruptor.base.BaseDisruptor;
import com.yangzhenj.disruptor.base.DisruptorEvent;
import com.yangzhenj.disruptor.dto.MsgDTO;
import com.yangzhenj.disruptor.handler.SendMsgHandler;
import com.yangzhenj.disruptor.service.SendService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SendMsgDisruptor extends BaseDisruptor<MsgDTO> {
    private static final int WORKER_NUMBERS = 50;

    @Resource
    private SendService sendService;

    SendMsgDisruptor() {
        super.name = "sendMsg";
    }

    /**
     * 是否使用工作者池处理
     */
    @Override
    public boolean isHandleWithWorkerPool() {return true;}

    @Override
    public WorkHandler<DisruptorEvent<MsgDTO>>[] getWorkHandlers() {
        WorkHandler<DisruptorEvent<MsgDTO>>[] workHandlers = new WorkHandler[WORKER_NUMBERS];

        for (int i = 0; i < workHandlers.length; i++) {
            workHandlers[i] = new SendMsgHandler(sendService);
        }

        return workHandlers;
    }

    @Override
    public EventHandler<DisruptorEvent<MsgDTO>>[] getEventHandlers() {
        return null;
    }
}
