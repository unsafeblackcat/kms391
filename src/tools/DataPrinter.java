package tools;
 
import java.text.SimpleDateFormat; 
import java.util.Date; 
 
/**
 * 游戏日志记录和系统消息打印工具 
 * 支持多种日志类型分类输出 
 */
public class DataPrinter {
    // 日志类型常量
    public static final int CASHSHOP = 0;
    public static final int EXCEPTION_CAUGHT = 1;
    public static final int TRADE = 2;
    public static final int CHAT = 3;
    public static final int COMMAND = 4;
    
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat readableFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
 
    /**
     * 获取当前可读格式时间 
     */
    public static String CurrentReadable_Date() {
        return readableFormat.format(new  Date());
    }
 
    /**
     * 发送分类日志
     * @param type 日志类型 (CASHSHOP/EXCEPTION_CAUGHT等)
     * @param message 日志内容
     */
    public static void send(int type, Object message) {
        String prefix;
        switch (type) {
            case CASHSHOP:
                prefix = "[CashShop]";
                break;
            case EXCEPTION_CAUGHT:
                prefix = "[Exception]";
                break;
            case TRADE:
                prefix = "[Trade]";
                break;
            case CHAT:
                prefix = "[Chat]";
                break;
            case COMMAND:
                prefix = "[Command]";
                break;
            default:
                prefix = "[System]";
        }
        
        String timestamp = dateFormat.format(new  Date());
        System.out.println(timestamp  + " " + prefix + " " + message.toString()); 
        
        // 异常情况特殊处理
        if (type == EXCEPTION_CAUGHT && message instanceof Exception) {
            ((Exception) message).printStackTrace();
        }
    }
 
    /**
     * 带错误码的系统消息
     */
    public static void sendError(int errorCode, String message) {
        String timestamp = dateFormat.format(new  Date());
        System.err.println(timestamp  + " [Error:" + errorCode + "] " + message);
    }
 
    /**
     * 调试信息输出
     */
    public static void debugPrint(String module, Object data) {
    	if (Boolean.getBoolean("debug.mode"))  {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            System.out.println(timestamp  + " [Debug][" + module + "] " + data);
    	}
    }
}