package com.yangzhenj.disruptor.dto;

public class MsgDTO {
    private String msg;

    public MsgDTO(String msg) {
        this.msg = msg;
    }

    public MsgDTO() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
