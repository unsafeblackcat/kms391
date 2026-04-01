importPackage(Packages.server);
importPackage(Packages.client.inventory);

var status;
var enter = "\r\n";
var grade = "安卓機器人"
var ringList = [1662131];
var allstat = 300;
var atk = 300;

function start() {
    status = -1;
    action(1, 1, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }
    var msg = "#fs15#";
    if (status == 0) {
        msg += "恭喜 #r#h0##k#l先生獲得了 " + grade + " 請確認背包." + enter + enter;
        msg += "將發放以下物品！" + enter + enter;
        for (i = 0; i < ringList.length; i++) {
            msg += "#i" + ringList[i] + "# #z" + ringList[i] + "#" + enter + "#i" + [1672020] + "# #z" + [1672020] + "#" + enter;
            citem = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(ringList[i]);
            citem.setStr(allstat);
            citem.setDex(allstat);
            citem.setInt(allstat);
            citem.setLuk(allstat);
            citem.setWatk(atk);
            citem.setMatk(atk);
            Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), citem, false);
		}
        cm.gainItem(2437718, -1);
        cm.gainItem(1672020, 1);		
        cm.sendOk(msg);
        cm.dispose();
    }
}
