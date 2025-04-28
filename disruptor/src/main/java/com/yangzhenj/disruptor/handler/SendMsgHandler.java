package com.yangzhenj.disruptor.handler;

import com.lmax.disruptor.WorkHandler;
import com.yangzhenj.disruptor.base.DisruptorEvent;
import com.yangzhenj.disruptor.dto.MsgDTO;
import com.yangzhenj.disruptor.service.SendService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Data
@Component
@Slf4j
public class SendMsgHandler implements WorkHandler<DisruptorEvent<MsgDTO>> {
    private SendService sendService;

    public SendMsgHandler(SendService sendService) {
        this.sendService = sendService;
    }

    @Override
    public void onEvent(DisruptorEvent<MsgDTO> msgDTODisruptorEvent) {
        MsgDTO source = msgDTODisruptorEvent.getSource();
        this.sendService.sendMsg(source.getMsg());
    }
}
