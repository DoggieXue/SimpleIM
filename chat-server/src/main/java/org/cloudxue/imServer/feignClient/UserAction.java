package org.cloudxue.imServer.feignClient;

import feign.Param;
import feign.RequestLine;

/**
 * @ClassName UserAction
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/12/21 下午5:29
 * @Version 1.0
 **/
public interface UserAction {
    @RequestLine("GET /login/{username}/{password}")
    public String loginAction(@Param("username") String username,
                              @Param("password") String password);

    @RequestLine("GET /{userId}")
    public String getById(@Param("userId") Integer userId);
}
