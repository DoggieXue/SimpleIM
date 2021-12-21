package org.cloudxue.im.common;

/**
 * @ClassName ProtoInstant
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/12/20 下午3:38
 * @Version 1.0
 **/
public class ProtoInstant {
    /**
     * 魔数
     */
    public static final short MAGIC_CODE = 0x86;
    /**
     * 版本号
     */
    public static final short VERSION_CODE = 0x01;
    /**
     * 客户端平台
     */
    public interface Platform {
        /**
         * Windows
         */
        public static final int WINDOWS = 1;
        /**
         * Mac
         */
        public static final int MAC = 2;
        /**
         * Android端
         */
        public static final int ANDROID = 3;
        /**
         * IOS端
         */
        public static final int IOS = 4;
        /**
         * Web端
         */
        public static final int WEB = 5;
        /**
         * 其他未知
         */
        public static final int UNKNOW = 6;
    }

    public enum ResultCodeEnum {
        SUCCESS(0, "交易成功"),
        AUTH_FAILED(1, "登录失败"),
        NO_TOKEN(2, "没有授权码"),
        UNKNOW_ERROR(3, "未知错误"),
        ;
        private Integer code;
        private String desc;

        ResultCodeEnum(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        public Integer getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }

}
