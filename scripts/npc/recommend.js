importPackage(Packages.packet.creators); 
importPackage(Packages.client.items); 
importPackage(Packages.server.items); 
importPackage(Packages.launch.world); 
importPackage(Packages.main.world); 
importPackage(Packages.database); 
importPackage(java.lang); 
importPackage(Packages.server); 
importPackage(Packages.handling.world); 
importPackage(Packages.tools.packet); 
 
/* (可選)發送給推薦人的獎勵道具 */
var item = 1000;
 
/* 成功推薦時的獎勵道具 */
var item3 = new Array(2000005, 500); // 藥水500個 
var status = -1;
var seld = -1, seld2 = -1, a = -1;
 
var 獎勵道具 = 1122148;
var 全屬性 = 0, 攻魔 = 0;
 
var 額外獎勵 = 2630281, 獎勵數量 = 10;
var 推薦獎勵 = [[1122148, 1]]; 
var 階級獎勵 = [
    {'goal' : 10, 'item' : [[5068301, 5]]},
    {'goal' : 50, 'item' : [[5068301, 20]]},
    {'goal' : 100, 'item' : [[5068301, 40]]}
]
 
/* 檢查重複推薦 */
function 檢查重複推薦(name, name2) {
    var conn = DatabaseConnection.getConnection(); 
    var stmt = conn.prepareStatement("SELECT  * FROM recom_log WHERE name LIKE '"+name+"%'");
    var rs = stmt.executeQuery(); 
    
    var 重複 = true;
    if (!rs.next())  重複 = false;
    
    rs.close(); 
    stmt.close(); 
    return 重複;
}
 
/* 檢查角色是否存在 */
function 角色是否存在(name) {
    var conn = DatabaseConnection.getConnection(); 
    var stmt = conn.prepareStatement("SELECT  * FROM characters WHERE name LIKE '"+name+"%'");
    var rs = stmt.executeQuery(); 
    
    var 存在 = true;
    if (!rs.next())  存在 = false;
    
    rs.close(); 
    stmt.close(); 
    return 存在;
}
 
/* 獲取帳號ID */
function 取得帳號ID(name) {
    var conn = DatabaseConnection.getConnection(); 
    var stmt = conn.prepareStatement("SELECT  * FROM characters WHERE name LIKE '" + name + "%'");
    var rs = stmt.executeQuery(); 
    
    var 帳號ID = -1;
    if (rs.next())  {
        帳號ID = rs.getInt("accountid"); 
    }
    
    rs.close(); 
    stmt.close(); 
    return 帳號ID;
}
 
/* 計算累積推薦數 */
function 計算累積推薦(name) {
    var 計數 = 0;
    var conn = DatabaseConnection.getConnection(); 
    var stmt = conn.prepareStatement("SELECT  * FROM recom_log WHERE recom LIKE '" + name + "'");
    var rs = stmt.executeQuery(); 
    
    while(rs.next())  {
        if (rs.getString("recom").equals(name))  {
            計數++;
        }
    }
    
    rs.close(); 
    stmt.close(); 
    return 計數;
}
 
/* 註冊推薦關係 */
function 註冊推薦(name, name2, 推薦人) {
    var conn = DatabaseConnection.getConnection(); 
    var stmt = conn.prepareStatement("INSERT  INTO recom_log(name, recom, state, date) VALUES(?,?,?,now())");
    stmt.setString(1,  name+"%"+name2);
    stmt.setString(2,  推薦人);
    stmt.setString(3,  "0");
    stmt.executeUpdate(); 
    stmt.close(); 
}
 
/* 生成推薦排行榜 */
function 生成推薦榜() {
    var 文字 = new StringBuilder();
    var conn = DatabaseConnection.getConnection(); 
    var stmt = conn.prepareStatement("SELECT  log.id,  log.recom,  count(*) AS player FROM recom_log AS log LEFT JOIN characters AS ch ON log.recom  = ch.name  WHERE ch.gm  <= 0 GROUP BY recom ORDER BY player DESC");
    var rs = stmt.executeQuery(); 
    
    var 排名 = 0;
    while(rs.next())  {
        文字.append("#L"+rs.getInt("id")+"#") 
           .append(排名 == 0 ? "#r " : 排名 == 1 ? "#b " : "#k ")
           .append("推薦代碼 #k: ").append(rs.getString("recom")).append("  | ")
           .append("推薦數 #k: #e").append(rs.getString("player")).append("#n\r\n"); 
        排名++;
    }
    
    rs.close(); 
    stmt.close(); 
    return 文字.toString();
}
 
/* 獲取推薦名單 */
function 取得推薦列表(id) {
    var 文字 = new StringBuilder();
    var conn = DatabaseConnection.getConnection(); 
    
    // 先取得推薦人ID 
    var idStmt = conn.prepareStatement("SELECT  * FROM recom_log WHERE id = '"+id+"'");
    var idRs = idStmt.executeQuery(); 
    idRs.next(); 
    var 推薦人 = idRs.getString("recom"); 
    idRs.close(); 
    
    // 查詢該推薦人的所有被推薦者 
    var stmt = conn.prepareStatement("SELECT  * FROM recom_log WHERE recom = '"+推薦人+"'");
    var rs = stmt.executeQuery(); 
    
    文字.append(推薦人+"的推薦名單：\r\n\r\n");
    while(rs.next())  {
        var 名稱 = rs.getString("name").split("%"); 
        文字.append("角色名 : #e").append(名稱[1]).append("#n | ")
           .append("日期 : ").append(rs.getDate("date")+"  "+rs.getTime("date")).append("\r\n"); 
    }
    
    rs.close(); 
    stmt.close(); 
    return 文字.toString();
}
 
/* 計算推薦數量 */
function 計算推薦數(name) {
    var conn = DatabaseConnection.getConnection(); 
    var stmt = conn.prepareStatement("SELECT  COUNT(*) AS player FROM recom_log WHERE recom = '"+name+"' and state = 0");
    var rs = stmt.executeQuery(); 
    rs.next(); 
    var 數量 = rs.getString("player"); 
    rs.close(); 
    stmt.close(); 
    return 數量;
}
 
/* 取得推薦者名單 */
function 取得推薦者(name) {
    var 文字 = new StringBuilder();
    var conn = DatabaseConnection.getConnection(); 
    var stmt = conn.prepareStatement("SELECT  * FROM recom_log WHERE recom = '"+name+"' and state = 0");
    var rs = stmt.executeQuery(); 
 
    while(rs.next())  {
        var 名稱 = rs.getString("name").split("%"); 
        文字.append("#b["+名稱[1]+"] ");
    }
    
    rs.close(); 
    stmt.close(); 
    return 文字.toString();
}
 
function start() {
    action(1, 0, 0);
}
 
function action(mode, type, selection) {
    if (mode == 1) { 
        status++;
    } else { 
        cm.dispose();  
        return; 
    }
 
    /* 主介面 */
    if (status == 0) {
        cm.sendSimple("#fc0xFFFF3300##fs15#"+cm.getPlayer().getName()+"#fc0xFF000000# ，歡迎來到#[ZERO]#fc0xFF000000#世界\r\n#r(推薦系統需角色等級200以上且每個帳號限用一次)#k\r\n\r\n#fUI/UIWindow.img/UtilDlgEx/list1#\r\n#L0##b 註冊推薦人#k\r\n#L1##b查看推薦排行榜#l\r\n\r\n\r\n#fUI/UIWindow.img/UtilDlgEx/list0#\r\n#L2##b 查詢我的推薦#l\r\n");
 
    } else if (status == 1) {
        if (selection == 0) {
            if (!檢查重複推薦(cm.getClient().getAccID(),  cm.getPlayer().getName()))  {
                if (cm.getPlayer().getLevel()  >= 200) {
                    cm.sendGetText("#b#fs15#"+cm.getPlayer().getName()+"#k ，請輸入將您引薦至本服的玩家角色名\r\n注意：#r推薦關係一經註冊無法更改#k");
                } else {
                    cm.sendOk("#fs15##r 等級未達200#k無法使用推薦系統");
                    cm.dispose(); 
                }
            } else {
                cm.sendOk("#fs15# 每個帳號僅能註冊一次推薦人");
                cm.dispose(); 
            }
 
        } else if (selection == 1) {
            cm.sendSimple("#fs15# 這是當前伺服器的推薦達人排行榜\r\n#b"+cm.getPlayer().getName()+"#k 只要努力推薦也有機會上榜哦\r\n"+生成推薦榜());
            status = 2;
 
        } else if (selection == 2) {
            var 推薦數 = 計算推薦數(cm.getPlayer().getName()); 
            if (推薦數 == 0) {
                cm.sendOk("#fs15# 很遺憾 #b"+cm.getPlayer().getName()+"#k 目前還沒有任何推薦記錄\r\n多多宣傳本服就能獲得推薦獎勵哦");
                cm.dispose(); 
            } else {
                cm.sendOk(" 不愧是#b"+cm.getPlayer().getName()+"#k ！您目前已有"+推薦數+"位推薦玩家："+取得推薦者(cm.getPlayer().getName())+"#k"); 
                
                // 更新推薦狀態為已領取 
                var conn = DatabaseConnection.getConnection(); 
                conn.prepareStatement("UPDATE  recom_log SET state = 1 WHERE recom = '"+cm.getPlayer().getName()+"'").executeUpdate(); 
                conn.close(); 
                
                cm.dispose(); 
            }
        } 
 
    } else if (status == 2) {
        if (seld == 3) {
            seld2 = selection;
            var 獎勵內容 = "這是累積"+階級獎勵[seld2]['goal']+"人推薦的獎勵：#b#fs15#\r\n";
            for (i = 0; i < 階級獎勵[seld2]['item'].length; i++) {
                獎勵內容 += "#i"+階級獎勵[seld2]['item'][i][0]+"##z"+階級獎勵[seld2]['item'][i][0]+"# "+階級獎勵[seld2]['item'][i][1]+"個\r\n";
            }
            
            var 累積數 = 計算累積推薦(cm.getPlayer().getName()); 
            if (階級獎勵[seld2]['goal'] <= 累積數 && cm.getPlayer().getKeyValue(201821,  "recom_"+階級獎勵[seld2]['goal']) == -1) {
                // 發放獎勵 
                for (i = 0; i < 階級獎勵[seld2]['item'].length; i++) {
                    cm.gainItem( 階級獎勵[seld2]['item'][i][0], 階級獎勵[seld2]['item'][i][1]);
                }
                
                cm.getClient().setKeyValue(201821,  "recom_"+階級獎勵[seld2]['goal'], "1");
                cm.sendOk( 獎勵內容);
            } else {
                cm.sendOk( 獎勵內容);
            }
            cm.dispose(); 
            
        } else {
            // 處理推薦註冊 
            if (!角色是否存在(cm.getText()))  {
                cm.sendOk(" 該角色不存在");
                cm.dispose(); 
                return;
            }
            
            if (cm.getText().equals("")  || cm.getText().equals(cm.getPlayer().getName())  || 取得帳號ID(cm.getText())  == 取得帳號ID(cm.getPlayer().getName()))  {
                cm.sendOk(cm.getText().equals("")  ? "輸入格式錯誤" : "不能將自己設為推薦人");
                cm.dispose(); 
            } else {
                // 註冊推薦關係 
                註冊推薦(cm.getClient().getAccID(),  cm.getPlayer().getName(),  cm.getText()); 
                
                // 發放獎勵 
                cm.gainItem( 額外獎勵, 獎勵數量);
                for (i = 0; i < 推薦獎勵.length; i++) {
                    cm.gainItem( 推薦獎勵[i][0], 推薦獎勵[i][1]);
                }
                
                cm.sendOk("#fs15# 這是給#b"+cm.getPlayer().getName()+"#k 的小禮物，祝您遊戲愉快");
                
                // 全服公告 
                World.Broadcast.broadcastMessage(CWvsContext.serverNotice(11,  "", "[公告] "+ cm.getPlayer().getName()+"  已將 "+cm.getText()+"  設為推薦人"));
                
                cm.dispose(); 
            }
        }
    }
}