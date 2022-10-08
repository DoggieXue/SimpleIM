package org.cloudxue.im.common.bean;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.cloudxue.im.common.bean.msg.ProtoMsg;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName User
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/12/20 下午4:49
 * @Version 1.0
 **/
@Slf4j
@Data
public class User {
    private static final AtomicInteger NO =  new AtomicInteger(1);
    String uid = String.valueOf(NO.getAndIncrement());
    String deviceId = UUID.randomUUID().toString();
    String token = UUID.randomUUID().toString();
    String nickName = "nikeName-" + uid;
    PLATTYPE platform = PLATTYPE.MAC;

    public enum PLATTYPE {
        WINDOWS,MAC,ANDROID,IOS,WEB,OTHER;
    }

    private String sessionId;

    public void setPlatform(int platform) {
        PLATTYPE[] values = PLATTYPE.values();
        Arrays.asList(values).forEach(i -> {
            if (i.ordinal() == platform) {
                this.platform = i;
            }
        });
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", token='" + token + '\'' +
                ", nickName='" + nickName + '\'' +
                ", platform=" + platform +
                '}';
    }

    public static User fromMsg(ProtoMsg.LoginRequest info) {
        User user = new User();
        user.uid = new String(info.getUid());
        user.deviceId = new String(info.getDeviceId());
        user.token = new String(info.getToken());
        user.setPlatform(info.getPlatform());
        log.info("登录中：{}", user.toString());
        return user;
    }
}
