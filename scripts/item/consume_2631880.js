var status = -1;
var enter = "\r\n";
var talkType = 0x86;
var NoramlPass = "Serenity_Noraml_Pass_Info";
var PrimeumPass = "Serenity_Premium_Pass_Info";
var SQJY = "Yjsqjy";
var SQDS = "Yjsqds";
var MAXS = "Maxshenmi";
var MFSD = "Mfangsd";
var YJQH = "YiJianQiangHua";
var LMXT = "LianMengXiTong";
var YCLC = "Yijianyclc";
var ZZXT = "ZanZhuXiTing";
var PMXT = "YjVhxsp";
var YJHZ = "Yijianhz";
var CJXT = "ChouJiangXiTong";
var TEST = "test1";
var MAXY = "Maxyuanchu";
var YZKD = "Yijiankd";
var GRCS = "Gerenceshi";

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
        giveCharacterToEventKeyValue();
        
        var chat = "高級功能菜單" + enter;
        chat += "#L0#" + "一鍵神器經驗"+"  #L1#" + "一鍵神器點數"+"  #L11#" + "一鍵裝備修改"+"  #L3#" + "一鍵Max原初"+enter;
        chat += "#L8#" + "一鍵口袋潛能"+"  #L10#" + "一鍵胸章潛能"+"  #L9#" + "一鍵現金潛能"+"  #L2#" + "一鍵Max神秘"+enter;
        chat += "#L12#" + "一鍵魔方强化"+"  #L13#" + "一鍵設置屬性"+"  #L15#" + "一鍵核心碎片"+"  #L6#" + "開放原初六槽"+enter;
        //chat += "#L4#" + "一鍵口袋槽"+"     #L5#" + "一鍵項鍊槽"+"  #L14#" + "抽奬系統"+"     #L7#" + "美容美髮"+enter;
        chat +="\r\n#L16##r回到大廳!"
        cm.sendSimpleS(chat, talkType);
    } else if (status == 1) {
        switch (selection) {
            case 0:
                cm.openNpcCustom(cm.getClient(), 9010061, SQJY);
                break;
            case 1:
                cm.openNpcCustom(cm.getClient(), 9010061, SQDS);
                break;
            case 2:
                cm.openNpcCustom(cm.getClient(), 9010061, MAXS);//MRRW
                break;
            case 3:
                cm.openNpcCustom(cm.getClient(), 9010061, MAXY);//FLXT
                break;
            case 4:
                cm.openNpcCustom(cm.getClient(), 9010061, YJQH);
                break;
            case 5:
                cm.openNpcCustom(cm.getClient(), 9010061, LMXT);
                break;
            case 6:
                cm.openNpcCustom(cm.getClient(), 9010061, YCLC);
                break;
            case 7:
                cm.openNpcCustom(cm.getClient(), 9010061, ZZXT);
                break;
            case 8:
                cm.openNpcCustom(cm.getClient(), 9010061, YZKD);
                break;
            case 9:
                cm.dispose();
                cm.openNpc(2001);
                break;
            case 10:
                cm.openNpcCustom(cm.getClient(), 9010061, YJHZ);
                break;
            case 11:
                cm.openNpcCustom(cm.getClient(), 9010061, TEST);
                break;
            case 12:
                cm.openNpcCustom(cm.getClient(), 9010061, MFSD);
                break;
            case 13:
                cm.dispose();
                cm.openNpc(9031016);
                break;
            case 14:
                cm.openNpcCustom(cm.getClient(), 9010061, CJXT);
                break;
            case 15:
                cm.openNpcCustom(cm.getClient(), 9000069, PMXT);
                break;
            case 16:
                cm.warp(910000000);
                cm.dispose();
                break;
        }
    }
}

function FancyPassExplaination() {

}

function giveCharacterToEventKeyValue() {

}