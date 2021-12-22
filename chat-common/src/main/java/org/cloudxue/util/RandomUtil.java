package org.cloudxue.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @ClassName RandomUtil
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/12/20 下午5:03
 * @Version 1.0
 **/
public class RandomUtil {
    /**
     * 按照范围，生成随机的整数
     * @param mod
     * @return [1, mod]之间的整数
     */
    public static int randInMod(int mod) {
        return Math.abs(ThreadLocalRandom.current().nextInt(mod) + 1);
    }

    /**
     * 按照上下限范围，生成随机的整数
     * @param low  下限
     * @param high 上限
     * @return  [low, high]之间的整数
     */
    public static int randInRange(int low, int high) {
        return randInMod(high - low) + low ;
    }
}
