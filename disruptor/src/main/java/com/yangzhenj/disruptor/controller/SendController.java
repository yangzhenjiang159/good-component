package com.yangzhenj.disruptor.controller;

import com.yangzhenj.disruptor.base.DisruptorEvent;
import com.yangzhenj.disruptor.business.SendMsgDisruptor;
import com.yangzhenj.disruptor.dto.MsgDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/send")
public class SendController {
    @Resource
    private SendMsgDisruptor sendMsgDisruptor;

    @GetMapping("/byNumber")
    public String sendByNumber(String msg) {
        sendMsgDisruptor.onData(initDisruptorEvent(msg));
        return "ok";
    }

    private DisruptorEvent<MsgDTO> initDisruptorEvent(String msg) {
        MsgDTO msgDTO = new MsgDTO(msg);
        DisruptorEvent<MsgDTO> disruptorEvent = new DisruptorEvent();
        disruptorEvent.setSource(msgDTO);
        return disruptorEvent;
    }
}
