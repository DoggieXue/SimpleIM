package org.cloudxue.imServer.session;

import lombok.extern.slf4j.Slf4j;
import org.cloudxue.im.common.bean.User;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @ClassName SessionMap
 * @Description 自定义Session管理集合
 * @Author xuexiao
 * @Date 2021/12/22 上午9:23
 * @Version 1.0
 **/
@Slf4j
public class SessionMap {
    private SessionMap () {

    }

    private static SessionMap singleInstance = new SessionMap();
    //会话集合
    private ConcurrentHashMap<String, ServerSession> map = new ConcurrentHashMap<>();

    public static SessionMap inst() {
        return singleInstance;
    }

    /**
     * 增加session对象
     * @param s
     */
    public void addSession(ServerSession s) {
        map.put(s.getSessionId(), s);
        log.info("用户登录： id = 【" + s.getUser().getUid() + "】，在线总数： " + map.size());
    }

    /**
     * 根据sessionId，获取session对象
     * @param sessionId
     * @return
     */
    public ServerSession getSession(String sessionId) {
        if(map.containsKey(sessionId)) {
            return map.get(sessionId);
        }else {
            return null;
        }
    }

    /**
     * 根据用户ID，获取session对象
     * @param userId
     * @return
     */
    public List<ServerSession> getSessionBy (String userId) {
        List<ServerSession> list = map.values()
                .stream()
                .filter(s -> s.getUser().getUid().equals(userId))
                .collect(Collectors.toList());
        return list;
    }

    /**
     * 删除session
     * @param sessionId
     */
    public void removeSession(String sessionId) {
        if (!map.containsKey(sessionId)) {
            return;
        }
        ServerSession serverSession = map.get(sessionId);
        map.remove(sessionId);
        log.info("用户下线： id = " + serverSession.getUser().getUid() + "在线总数： " + map.size());
    }

    /**
     * 判断当前用户是否登录
     * 判断规则：相同平台登录的同一个用户号
     * @param user
     * @return
     */
    public boolean hasLogin(User user) {
        Iterator<Map.Entry<String, ServerSession>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ServerSession> next = iterator.next();
            User u = next.getValue().getUser();
            if (u.getUid().equals(user.getUid()) && u.getPlatform().equals(user.getPlatform())) {
                return true;
            }
        }
        return false;
    }
}
