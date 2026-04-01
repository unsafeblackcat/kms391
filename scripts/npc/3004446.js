var status = -1;
var i0 = "#fs15##fUI/UIWindow1.img/Dinsey/contents/0#"; // 이벤트
var i1 = "#fs15##fUI/UIWindow1.img/Dinsey/contents/1#"; // 게임
var i2 = "#fs15##fUI/UIWindow1.img/Dinsey/contents/2#"; // 핫타임
var i3 = "#fs15##fUI/UIWindow1.img/Dinsey/contents/3#"; // 퀘스트
var ii = "#fMap/MapHelper.img/minimap/match#";
var ij = "#fItem/Install/0380.img/03801312/Info/icon#";
var is = "#fItem/Install/0380.img/03801313/Info/icon#";
var enter = "\r\n";

importPackage(Packages.constants);

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        var text = ii +"#h0#님 안녕하세요? 블랙월드에 방문한걸 환영합니다!" + enter
        text +=  ii +"저희 제작시스템에 원하시는게 있으면 맘껏 이용해보세요!" + enter + enter
        text += "#L0#" + "#b#i3801312# 각성 장신구#k" + "#l"
        text += "#L1#" + "#r#i3801313# 각성 방어구#k" + "#l"
		        cm.sendSimple("#fs15#" + text);
    } else if (status == 1) {
        var text = "#fs15#"
        text += "#h0#님 안녕하세요? 블랙월드에 방문한걸 환영합니다!" + enter
        text += "저희 제작시스템에 원하시는게 있으면 맘껏 이용해보세요!" + enter + enter
        if (selection == 0) {
            cm.dispose();
            cm.openNpc(cm.getClient(), 1052238);
        } else if (selection == 1) {
            cm.dispose();
            cm.openNpc(cm.getClient(), 3004445);
		}
	}
}