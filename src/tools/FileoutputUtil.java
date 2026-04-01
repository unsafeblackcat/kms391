package tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FileoutputUtil {

    public static final String Acc_Stuck = "Log_AccountStuck.txt";

    public static final String Login_Error = "Log_Login_Error.txt";

    public static final String Pinkbean_Log = "Log_Pinkbean.txt";

    public static final String Cooldown_Log = "Log_Cooldown.txt";

    public static final String ScriptEx_Log = "Log_Script_Except.txt";

    public static final String PacketEx_Log = "Log_Packet_Except.rtf";

    public static final String Donator_Log = "Log_Donator.rtf";

    public static final String ForceAtom_Log = "Log_ForceAtom.rtf";

    public static final String Buff_Error_Log = "Log_BuffError.rtf";

    public static final String Hacker_Log = "Log_Hacker.rtf";

    public static final String Movement_Log = "Log_Movement.rtf";

    public static final String CommandEx_Log = "Log_Command_Except.rtf";

    public static final String Attack_Log = "Log_Attack.txt";

    public static final String Kill_Log = "Log_Kill.txt";
    
    public static final String 進程日誌 = "Log/LogFile/進程日誌/" + getDCurrentTime() + ".txt";

    public static final String 連接器日誌 = "Log/LogFile/連接器日誌/" + getDCurrentTime() + ".txt";

    public static final String 偵測日誌 = "Log/LogFile/偵測日誌/" + getDCurrentTime() + ".txt";

    public static final String 聊天日誌 = "Log/LogFile/聊天日誌/" + getDCurrentTime() + ".txt";

    public static final String 怪物等級範圍日誌 = "Log/LogFile/怪物日誌/" + getDCurrentTime() + ".txt";

    public static final String 計時器日誌 = "Log/LogFile/計時器日誌/" + getDCurrentTime() + ".txt";

    public static final String 真相之房日誌 = "Log/LogFile/漏洞使用日誌/真相之房/" + getDCurrentTime() + ".txt";

    public static final String Attack_Dealy = "Log/LogFile/漏洞使用日誌/延遲/" + getDCurrentTime() + ".txt";

    public static final String 拍賣場購買日誌 = "Log/LogFile/拍賣場/購買日誌/" + getDCurrentTime() + ".txt";

    public static final String 拍賣場款項領取日誌 = "Log/LogFile/拍賣場/款項領取日誌/" + getDCurrentTime() + ".txt";

    public static final String 拍賣場物品返還日誌 = "Log/LogFile/拍賣場/物品返還日誌/" + getDCurrentTime() + ".txt";

    public static final String 拍賣場入場日誌 = "Log/LogFile/拍賣場/入場日誌/" + getDCurrentTime() + ".txt";

    public static final String 拍賣場離場日誌 = "Log/LogFile/拍賣場/離場日誌/" + getDCurrentTime() + ".txt";

    public static final String 拍賣場銷售登記日誌 = "Log/LogFile/拍賣場/銷售登記日誌/" + getDCurrentTime() + ".txt";

    public static final String 拍賣場銷售中止日誌 = "Log/LogFile/拍賣場/銷售中止日誌/" + getDCurrentTime() + ".txt";

    public static final String 拍賣場搜索日誌 = "Log/LogFile/拍賣場/搜索日誌/" + getDCurrentTime() + ".txt";

    public static final String 帳號登入日誌 = "Log/LogFile/帳號/登入" + getDCurrentTime() + ".txt";

    public static final String 交換楓幣日誌 = "Log/LogFile/交換/楓幣" + getDCurrentTime() + ".txt";

    public static final String 交換物品日誌 = "Log/LogFile/交換/物品" + getDCurrentTime() + ".txt";

    public static final String 首領入場日誌 = "Log/LogFile/首領/入場" + getDCurrentTime() + ".txt";

    public static final String 首領通關日誌 = "Log/LogFile/首領/通關" + getDCurrentTime() + ".txt";

    public static final String 首領獲得物品日誌 = "Log/LogFile/首領/獲得物品" + getDCurrentTime() + ".txt";

    public static final String 商城入場日誌 = "Log/LogFile/商城/入場日誌/" + getDCurrentTime() + ".txt";

    public static final String 商城離場日誌 = "Log/LogFile/商城/離場日誌/" + getDCurrentTime() + ".txt";

    public static final String 商城購買日誌 = "Log/LogFile/商城/購買日誌/" + getDCurrentTime() + ".txt";

    public static final String 商城倉庫日誌 = "Log/LogFile/商城/倉庫日誌/" + getDCurrentTime() + ".txt";

    public static final String 物品掉落日誌 = "Log/LogFile/物品/物品掉落日誌/" + getDCurrentTime() + ".txt";

    public static final String 物品使用日誌 = "Log/LogFile/物品/物品使用日誌/" + getDCurrentTime() + ".txt";

    public static final String 腳本物品使用日誌 = "Log/LogFile/物品/腳本物品/物品使用/" + getDCurrentTime() + ".txt";

    public static final String 物品獲得日誌 = "Log/LogFile/物品/物品獲得日誌/" + getDCurrentTime() + ".txt";

    public static final String NPC對話日誌 = "Log/LogFile/NPC/對話/" + getDCurrentTime() + ".txt";

    public static final String NPC商店日誌 = "Log/LogFile/NPC/商店/" + getDCurrentTime() + ".txt";

    public static final String NPC商店購買日誌 = "Log/LogFile/NPC/商店購買/" + getDCurrentTime() + ".txt";

    public static final String NPC商店銷售日誌 = "Log/LogFile/NPC/商店銷售/" + getDCurrentTime() + ".txt";

    public static final String 全體聊天日誌 = "Log/LogFile/聊天/全體聊天/" + getDCurrentTime() + ".txt";

    public static final String 廣播聊天日誌 = "Log/LogFile/聊天/廣播聊天/" + getDCurrentTime() + ".txt";

    public static final String 一般聊天日誌 = "Log/LogFile/聊天/一般聊天/" + getDCurrentTime() + ".txt";

    public static final String 好友聊天日誌 = "Log/LogFile/聊天/好友聊天/" + getDCurrentTime() + ".txt";

    public static final String 公會聊天日誌 = "Log/LogFile/聊天/公會聊天/" + getDCurrentTime() + ".txt";

    public static final String 組隊聊天日誌 = "Log/LogFile/聊天/組隊聊天/" + getDCurrentTime() + ".txt";

    public static final String 密語聊天日誌 = "Log/LogFile/聊天/密語聊天/" + getDCurrentTime() + ".txt";

    public static final String 聯盟聊天日誌 = "Log/LogFile/聊天/聯盟聊天/" + getDCurrentTime() + ".txt";

    public static final String 即時通聊天日誌 = "Log/LogFile/聊天/即時通聊天/" + getDCurrentTime() + ".txt";

    public static final String 交易聊天日誌 = "Log/LogFile/聊天/交易聊天/" + getDCurrentTime() + ".txt";

    public static final String 小遊戲聊天日誌 = "Log/LogFile/聊天/小遊戲聊天/" + getDCurrentTime() + ".txt";

    public static final String 升級日誌 = "Log/LogFile/角色/升級/" + getDCurrentTime() + ".txt";

    public static final String 登入日誌 = "Log/LogFile/角色/登入/" + getDCurrentTime() + ".txt";

    public static final String 登出日誌 = "Log/LogFile/角色/登出/" + getDCurrentTime() + ".txt";

    public static final String 指令日誌 = "Log/LogFile/指令/" + getDCurrentTime() + ".txt";

    public static final String 倉庫存入日誌 = "Log/LogFile/倉庫/存入/" + getDCurrentTime() + ".txt";

    public static final String 倉庫取出日誌 = "Log/LogFile/倉庫/取出/" + getDCurrentTime() + ".txt";

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final SimpleDateFormat sdfGMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final SimpleDateFormat sdf_ = new SimpleDateFormat("yyyy-MM-dd");

    static {
        sdfGMT.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public static String getDCurrentTime() {
        Calendar calz = Calendar.getInstance(TimeZone.getTimeZone("KST"), Locale.KOREAN);
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time = simpleTimeFormat.format(calz.getTime());
        return time;
    }

    private static void ensureDirectoryExists(String file) {
        File logFile = new File(file);
        File parentDir = logFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
    }

    public static void log(String file, String msg) {//记录普通的日志信息
        ensureDirectoryExists(file);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file, true);
            out.write(("\n------------------------ " + CurrentReadable_Time() + " ------------------------\n\r\n")
                    .getBytes());
            out.write(msg.getBytes());
        } catch (IOException iOException) {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException iOException1) {
            }
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException iOException) {
            }
        }
    }

    public static void outputFileError(String file, Throwable t) {//記錄异常的堆栈跟踪信息写入指定的文件
        ensureDirectoryExists(file);
        try (FileOutputStream out = new FileOutputStream(file, true)) {
            String separator = System.lineSeparator();
            out.write(("------------------------ " + CurrentReadable_Time() + " ------------------------" + separator).getBytes("UTF-8"));
            out.write(getString(t).getBytes("UTF-8"));
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public static String CurrentReadable_Date() {
        return sdf_.format(Calendar.getInstance().getTime());
    }

    public static String CurrentReadable_Time() {
        return sdf.format(Calendar.getInstance().getTime());
    }

    public static String CurrentReadable_TimeGMT() {
        return sdfGMT.format(new Date());
    }

    public static String getString(Throwable e) {
        String retValue = null;
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            retValue = sw.toString();
        } finally {
            try {
                if (pw != null) {
                    pw.close();
                }
                if (sw != null) {
                    sw.close();
                }
            } catch (IOException iOException) {
            }
        }
        return retValue;
    }
}