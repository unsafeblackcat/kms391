/* 마피아 맵 전용 엔피시 입니다. */
importPackage(Packages.launch.world);
importPackage(Packages.packet.creators);
importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.launch.world);
importPackage(Packages.handling.world);
importPackage(Packages.packet.creators);
importPackage(Packages.tools.packet);
importPackage(java.util);
importPackage(java.lang);

var status = -1;

function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0 && status == 0) {
    cm.dispose();
    return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
    if(cm.getPlayer().getMapId() != 910000000) {
    cm.dispose(); return;
    }
    map = cm.getClient().getChannelServer().getMapFactory().getMap(910000000);
    names = map.names.split(",");
        if(cm.getPlayer().isDead) {
            cm.dispose(); return;
        }
            if(cm.getPlayer().isMapiaVote) {
                    for(a=0;a<names;a++) {
                     try {
                        if(cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(names[a]).getmapiavote == 1) {
                        cm.sendOk("已有隊員進行指認，無法再進行指認。"); cm.dispose(); return;
                        }
                                        }catch(e){
                    }
                    }
                var text = "請選擇要暗殺的對象。\r\n";
                    for(i=0;i< names.length; i++) {
                    try {
                        if(cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(names[i]).mapiajob != "마피아") {
                            text += "#L"+i+"##b"+names[i]+"#k";
                        }
                        if(cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(names[i]).isDead) {
                            text += "[사망]#l\r\n";
                        } else {
                            text += "#l\r\n";
                        }
                    }catch(e){
                    }
                    }
            } else {
                cm.sendOk("您已進行過指認，無法再次指認。");
                cm.dispose(); return;
            }
            cm.sendSimple(text);
    } else if(status == 1) {
    sel = selection;
    if(cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(names[sel]).isDead) {
        cm.sendOk("已死亡的玩家無法被指認。"); cm.dispose(); return;
    } else {
        cm.sendYesNo("您確定要指認 #b"+names[sel]+"#k玩家嗎？一旦指認，其他隊員將無法再進行指認。");
    }
    } else if(status == 2) {
    if(cm.getPlayer().isMapiaVote) {
        cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(names[sel]).getmapiavote = 1;
        cm.getPlayer().isMapiaVote = false;
        map.broadcastMessage(CWvsContext.serverNotice(11, cm.getPlayer().getName()+"玩家指認了 "+names[sel]+"玩家。"));
        cm.sendOk("指認已完成。");
        cm.dispose(); return;
    } else {
        cm.sendOk("您無法進行指認。");
        cm.dispose(); return;
    }
    }
}