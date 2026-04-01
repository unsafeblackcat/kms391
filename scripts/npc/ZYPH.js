importPackage(Packages.database);

var 모험가 = [ //4차 이상만 뜨게끔 유도
    // 冒險家 
    [112, "英雄"], [122, "聖騎士"], [132, "黑騎士"],
    [212, "火毒大魔導士"], [222, "冰雷大魔導士"], [232, "主教"], 
    [312, "箭神"], [322, "神射手"], [332, "開拓者"], 
    [412, "夜使者"], [422, "暗影神偷"], [434, "影武者"],
    [512, "拳霸"], [522, "槍神"], [532, "重砲指揮官"],
    // 皇家騎士團 
    [1112, "聖魂劍士"], [1212, "烈焰巫師"], [1312, "破風使者"], [1412, "暗夜行者"], [1512, "閃雷悍將"], [5112, "米哈逸"],
    // 英雄團 
    [2112, "狂狼勇士"], [2218, "龍魔導士"], [2312, "精靈遊俠"], [2412, "幻影俠盜"], [2512, "隱月"], [2712, "夜光"],
    // 反抗軍 & 惡魔 
    [3112, "惡魔殺手"], [3122, "惡魔復仇者"], [3212, "爆拳槍神"], [3312, "狂豹獵人"], [3512, "機甲戰神"], [3612, "傑諾"], [3712, "煉獄巫師"],
    // 諾巴族 
    [6112, "凱撒"], [6312, "卡因"], [6412, "卡蒂娜"], [6512, "天使破壞者"],
    // 雷普族 
    [15112, "阿戴爾"], [15212, "伊利恩"], [15512, "亞克"],
    // 其他 (零, 凱內西斯, 虎影)
    [10112, "零"], [14212, "凱內西斯"], [16412, "虎影"], [16212, "拉拉"]
];



var choice = 0;

var status = -1;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode != 1) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        var say = "   職業等級排行榜\r\n   請選擇您想查看的職業：\r\n\r\n";
        for (var i = 0; i < 모험가.length; i++) {
            say += "#L" + i + "#" + 모험가[i][1] + "#l\r\n";
        }
        cm.sendSimple(say);
    } else if (status == 1) {
        var con = null;
        var ps = null;
        var rs = null;
        var count = 0;
        var say = "" + 모험가[selection][1] + "  職業等級排行榜\r\n\r\n";
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM `characters` WHERE `job` = " + 모험가[selection][0] + " ORDER BY `level` DESC, `exp` DESC LIMIT 35"); //AND `gm` = 0 
            rs = ps.executeQuery();
            while (rs.next()) {
                count++;
                say += count + "등 : " + rs.getString("name") + " / 等級 : " + rs.getInt("level") + "\r\n";
            }
            rs.close();
            ps.close();
            con.close();
            cm.sendOk(say);
            cm.dispose();
            return;
        } catch (e) {
            cm.sendOk("發生錯誤，請稍後再試。\r\n\r\n錯誤訊息：" + e);
            cm.dispose();
            return;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        }
    } else {
        cm.dispose();
        return;
    }
}
