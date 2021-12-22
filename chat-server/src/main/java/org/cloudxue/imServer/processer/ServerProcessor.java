package org.cloudxue.imServer.processer;

import org.cloudxue.im.common.bean.msg.ProtoMsg;
import org.cloudxue.imServer.session.ServerSession;

/**
 * 顶层操作接口
 */
public interface ServerProcessor {
    ProtoMsg.HeadType type();

    boolean action(ServerSession session, ProtoMsg.Message proto);
}
