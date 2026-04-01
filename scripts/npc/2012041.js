/* 公會地下水道 NPC 
941000000 入口
941000001 祭壇出口
941000100 通往祭壇之路 
941000200 祭壇
*/
importPackage(Packages.tools.packet); 
importPackage(Packages.database); 
function start() {
    St = -1;
    action(1, 0, 0);
}
 
var se = -1;
var EtcTimer = Packages.server.Timer.EtcTimer; 
var higher = false;
var score = 0;
 
function action(M, T, S) {
    if(M == -1) {
        cm.dispose(); 
        return;
    }
 
    if(M == 0) {
        if(St == 1 && se == 0){
            cm.sendOk(" 明白了。準備好調查時請再次嘗試。");
            cm.dispose(); 
            return;
        }
        if(St == 0){
            cm.sendOk(" 那麼請整備完畢後再試。");
            cm.dispose(); 
            return;
        }
        St--;
    }
 
    if(M == 1)
        St++;
 
    if(St == 0) {
        if(cm.getPlayer().getClient().getGuildSulo().getSuloWeekPoint()  == -1 || cm.getPlayer().getClient().getGuildSulo().getSuloPoint()  == -1){
            cm.getPlayer().getClient().getGuildSulo().setSuloPoint(0); 
            cm.getPlayer().getClient().getGuildSulo().setSuloWeekPoint(0); 
        }
        if (cm.getPlayerStat("GID")  > 0) {
            if(cm.getPlayer().getMapId()  != 941000001 && cm.getPlayer().getMapId()  != 941000100) {
                if(cm.getPlayer().getMapId()  == 941000000){
                    對話 = "遺跡正散發不祥的氣息。現在要調查地下水道嗎？\r\n\r\n";
                    對話 += "- #e本週最高分數 : #r"+cm.getPlayer().getClient().getGuildSulo().getSuloWeekPoint()+"#k#n\r\n"; 
                } else {
                    對話 = "要挑戰夏萊尼安的惡魔阿爾卡努斯嗎？\r\n";
                }
                對話 += "#L1##b進入夏萊尼安地下水道#l\r\n";
                對話 += "#L2##b什麼是夏萊尼安地下水道？#l\r\n";
                對話 += "#L3##b我想獲得公會點數#l\r\n";
                cm.sendSimple( 對話);
            } else if(cm.getPlayer().getMapId()  == 941000100) {
                cm.sendYesNo(" 若準備完畢，將引導您前進。\r\n\r\n#b#e現在要前往祭壇嗎？#k#n");
            } else if(cm.getPlayer().getMapId()  == 941000001) {
                var txt = "祭壇的不祥氣息似乎減弱了些。\r\n將引導您返回地下水道入口。\r\n\r\n";
                higher = parseInt(cm.getPlayer().getClient().getGuildSulo().getSuloPoint())  > parseInt(+cm.getPlayer().getClient().getGuildSulo().getSuloWeekPoint()); 
                txt += "#e- 獲得分數: #n#b#e" + cm.getPlayer().getClient().getGuildSulo().getSuloPoint()  + "分#n#k"
                if (higher) {
                    txt += " #b#e(本週新紀錄!)#n#k"
                }
                txt += "\r\n"
                txt += "#e- 本週最高分: #n#r#e" + cm.getPlayer().getClient().getGuildSulo().getSuloWeekPoint()  + "分#n#k\r\n\r\n"
                txt +="#L3##b返回地下水道入口\r\n"
                txt +="#L4#結束對話#k"
                cm.sendSimple(txt); 
            }
        } else {
            cm.sendOk(" 需加入公會才能挑戰。");
            cm.dispose(); 
            return;
        }
    } else if(St == 1) {
        se = S;
        if(S == 1){
            if(cm.getPlayer().getMapId()  == 941000000){
                cm.sendAcceptDecline("#fs15# 進入地下水道時，\r\n#fs16##b#e所有增益效果將被解除#k#n#fs15#。\r\n\r\n確定現在挑戰嗎？");
            } else {
                cm.warp(941000000,  2);
                cm.dispose(); 
            }
        } else if(S == 2){
            cm.sendNext("#r#e 夏萊尼安地下水道#k#n的相關說明");
        } else if(S == 3){
            cm.getPlayer().getGuildScore(); 
            higher = true;
            if (higher) {
                cm.getPlayer().getClient().getGuildSulo().setSuloWeekPoint(cm.getPlayer().getClient().getGuildSulo().getSuloPoint()); 
                score += cm.getPlayer().getClient().getGuildSulo().getSuloPoint(); 
                cm.getPlayer().setGuildScore(cm.getPlayer().getGuildId(),  score);
            }
            cm.warp(941000000); 
            cm.dispose(); 
            return;
        } else if(S == 4){
            cm.dispose(); 
            return;
        } else {
            cm.EnterSulo();
            cm.dispose(); 
            return;
        }
    } else if(St == 2) {
        if(se == 1){
            cm.TimeMoveMap(941000100, 941000000, 125);
            cm.cancelSkillsbuff() 
            cm.getPlayer().getClient().getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(2012041,  1000, "因祭壇的不祥氣息，#r所有增益效果已解除#k。", ""));
            nextMsg();
            cm.dispose(); 
            return;
        }
        cm.sendNextPrev(" 在遙遠的過去...曾有名叫夏萊尼安的古代王國。\r\n雖因可怕事件導致整個王國滅亡。");
    } else if(St == 3) {
        cm.sendNextPrev(" 雖多次有人嘗試探索夏萊尼安遺跡，\r\n但都不容易，最終通往該處的路徑被人遺忘。\r\n\r\n#r#e直到不久前...#k#n");
    } else if(St == 4) {
        cm.sendNextPrev(" 我一直在調查夏萊尼安遺跡，因意外事故在無人涉足之處發現了#b通往夏萊尼安地下水道的路徑#k。");
    } else if(St == 5) {
        cm.sendNextPrev(" 雖有無數入口如迷宮般交錯，但每條路最終都能通往沉眠著不祥氣息的祭壇。");
    } else if(St == 6) {
        cm.sendNextPrev(" 公會總部決定調查地下水道。在地水道入口與我對話開啟通往夏萊尼安地下水道的門，即可前往充滿不祥氣息的祭壇通道。");
    } else if(St == 7) {
        cm.sendNextPrev(" 調查只能獨自進行，中途退出公會或被驅逐將無法獲得副本點數。此外，退出或被驅逐時會從公會總點數中扣除，請注意。");
    } else if(St == 8) {
        cm.sendNextPrev(" 調查可無限次進行。但因地水道的不祥氣息，進入時所有增益效果將解除，請謹慎決定。");
    } else if(St == 9) {
        cm.sendNextPrev(" 在通往祭壇的通道中可暫時使用增益技能和消耗品，為即將到來的試煉做準備。\r\n但若未在一定時間內進入祭壇，將被傳送出地下水道，請注意。");
    } else if(St == 10) {
        cm.sendNextPrev(" 在水道最深處，蟄伏於不祥氣息祭壇中的存在即將甦醒。那就是\r\n\r\n"
        +"#r夏萊尼安的惡魔，阿爾卡努斯#k\r\n\r\n"
        +"。");
    } else if(St == 11) {
        cm.sendNextPrev(" 他是未知的存在...受到攻擊會逐漸變強，請小心。若以最強狀態重生，無論受到多強攻擊都不會倒下。\r\n若被阿爾卡努斯擊敗，將立即被傳送至祭壇外，請注意。");
    } else if(St == 12) {
        cm.sendNextPrev(" 根據對水道中敵人造成的傷害比例獲得副本點數，每週一0點公會總部會根據達成目標發放貴族SP獎勵。\r\n但需公會總副本點數達一定標準才能領取。");
    } else if(St == 13) {
        cm.sendNextPrev(" 另可依點數排名獲得額外貴族SP。退出地下水道後需經過一定時間才會結算點數。\r\n"
        +"#r各公會成員的副本點數匯總需要時間，排名更新前請勿慌張，稍後再確認。#k");
    } else if(St == 14) {
        var msg ="#b#e第1名 : 40點SP\r\n";
        msg+="第2名 : 38點SP\r\n";
        msg+="第3名 : 36點SP\r\n";
        msg+="第4~5名 : 34點SP\r\n";
        msg+="第6~10名 : 32點SP\r\n";
        msg+="達500點以上 : 10點SP\r\n\r\n";
        msg+="#r※未達500點者即使排名內也無法領取獎勵。\r\n";
        cm.sendNextPrev(msg); 
    } else if(St == 15) {
        cm.sendPrev(" 我會陪同前往水道，不必擔心迷路。請開始挑戰吧。");
    } else if(St == 16) {
        cm.dispose(); 
    }
}
 
function nextMsg() {
    var tick = 0;
    schedule = EtcTimer.getInstance().register(function  () {
    if(tick == 2){
        cm.getPlayer().getClient().getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(2012041,  2500, "請為與#r夏萊尼安的惡魔#k的決戰做準備", ""));
        schedule.cancel(true); 
    }
        tick++;
    }, 1000);
}