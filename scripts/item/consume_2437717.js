importPackage(Packages.server);
importPackage(Packages.client.inventory);

var status;
var enter = "\r\n";
var grade = "안드로이드"
var ringList = [1662130];
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
        msg += "안녕하세요. #h0#님! " + grade + " 축하드립니다." + enter + enter;
        msg += "아래와 같은 아이템이 지급됩니다!" + enter + enter;
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
			cm.gainItem(1672020, 1);
        }
        cm.gainItem(2437717, -1);
		
        cm.sendOk(msg);
        cm.dispose();
    }
}
