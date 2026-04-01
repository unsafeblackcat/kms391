var enter = "\r\n";
var seld = -1;


var itemid = 4310248;
var qty = 200;
var max = 0;
var tocoin = 2433019, toqty = 1;

function start() {
	status = -1;
	action(1, 0, 0);
}
function action(mode, type, sel) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
    	}
	if (status == 0) {
	    var chat = "학교 과제 준비물 크레파스가 필요한데 .. 혹시 가지고 있어?" + enter;
	    chat += "#b#i" + itemid + "# #z" + itemid +"# " + qty + "개#k를 주신다면 저에게 큰 도움이 될겁니다. 대신 제가 #b#i" +tocoin +"# #z" + tocoin +"##k 1개를 드리겠습니다.";

	    cm.sendNext(chat);
    } else if (status == 1) {
        max = Math.floor(cm.getPlayer().getItemQuantity(itemid, false) / 200);
        var chat = "당신은 최대 #b" + max + "#k번 교환할수 있어 몇번을 교환할래?" +enter;
        chat += "#r한번에 교환할수 있는 최대횟수는 100번이야 " + enter;
        cm.sendGetNumber(chat, 0, 1, 1);
    } else if (status == 2) {
        if (max <= 0) {
            cm.sendOk("장난치지마");
            cm.dispose();
            return;
        }
        if (sel > max) {
            cm.sendOk("음.. 그정도로 가지고 있진 않은거 같아!");
            cm.dispose();
            return;
        }

        if (sel > 100) {
            cm.sendOk("한번에 최대 교환 가능한 횟수는 100번이야!");
            cm.dispose();
            return;
        } else {
            var removeQty = (sel * 100 * 2);
            var howGive = sel;

            cm.sendOk("고마워..! 인벤토리 창을 확인 해보면 내가 준 선물이 있을거야 다음에도 꼭 도와줘!");
            cm.getPlayer().removeItem(itemid, -removeQty);
            cm.getPlayer().gainItem(tocoin, sel);
            cm.dispose();
        }
    }
}