package org.cloudxue.im.common.bean;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * @ClassName ChatMsg
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/12/20 下午4:50
 * @Version 1.0
 **/
@Data
public class ChatMsg {
    /**
     * 消息类型：1-文本；2-音频；3-视频；4-地理位置；5-其他
     */
    public enum MSGTYPE {
        TEXT,
        AUDIO,
        VIDEO,
        POS,
        OTHER;
    }

    private User user;

    private long msgId;
    private String from;
    private String to;
    private long time;
    private MSGTYPE msgType;
    private String content;
    private String url;         //多媒体地址
    private String property;    //附加属性
    private String fromNick;    //发送者昵称
    private String json;        //附加的json串

    public ChatMsg (User user) {
        if (null == user) {
            return;
        }
        this.user = user;
        this.setTime(System.currentTimeMillis());
        this.setFrom(user.getUid());
        this.setFromNick(user.getNickName());
    }

    public void fillMsg (ProtoMsg.MessageRequest.Builder cb) {
        if (msgId > 0) {
            cb.setMsgId(msgId);
        }
        if (StringUtils.isNotEmpty(from)) {
            cb.setFrom(from);
        }
        if (StringUtils.isNotEmpty(to)) {
            cb.setTo(to);
        }
        if (time > 0) {
            cb.setTime(time);
        }
        if (msgType != null) {
            cb.setMsgType(msgType.ordinal());
        }
        if (StringUtils.isNotEmpty(content)) {
            cb.setContent(content);
        }
        if (StringUtils.isNotEmpty(url)) {
            cb.setUrl(url);
        }
        if (StringUtils.isNotEmpty(property)) {
            cb.setProperty(property);
        }
        if (StringUtils.isNotEmpty(fromNick)) {
            cb.setFromNick(fromNick);
        }
        if (StringUtils.isNotEmpty(json)) {
            cb.setJson(json);
        }
    }
}
