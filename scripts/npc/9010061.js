var status = -1;
var enter = "\r\n";
var talkType = 0x86;
var seld = -1;
function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
		cm.dispose();
		cm.openNpcCustom(cm.getClient(), 9010061, "SerenityPassInfo");
		}
}