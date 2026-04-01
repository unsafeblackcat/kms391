﻿importPackage(Packages.server);
importPackage(Packages.database);
importPackage(Packages.client);
importPackage(java.lang);

var enter = "\r\n";
var seld = -1;
var seldreward = -1;
var seld2 = -1;
var seld3 = -1;

var name, comment;
var youtube, blog, etc;

var year, month, date2, date, day

var grade = [
    [0, "普通"],
    [1, "新星"],
    [2, "躍升昇星"],
    [3, "飛翔之星"],
    [4, "閃耀之星"],
    [5, "偶像之星"],
    [6, "超級之星"]
]

var shop = [{
        'n': 0,
        'items': [

            {
                'itemid': 5152301,
                'qty': 1,
                'atk': -1,
                'allstat': -1,
                'price': 1000,
                'special': -1
            },
            {
                'itemid': 5152300,
                'qty': 1,
                'atk': -1,
                'allstat': -1,
                'price': 4000,
                'special': -1
            },
            {
                'itemid': 1113055,
                'qty': 1,
                'atk': -1,
                'allstat': -1,
                'price': 50000,
                'special': -1
            },
        ]
    },
    {
        'n': 1,
        'items': [{
                'itemid': 2450124,
                'qty': 10,
                'atk': -1,
                'allstat': -1,
                'price': 3000,
                'special': -1
            },
            {
                'itemid': 4034803,
                'qty': 1,
                'atk': -1,
                'allstat': -1,
                'price': 50000,
                'special': -1
            },
            {
                'itemid': 5060048,
                'qty': 1,
                'atk': -1,
                'allstat': -1,
                'price': 5000,
                'special': -1
            },
            {
                'itemid': 5060048,
                'qty': 10,
                'atk': -1,
                'allstat': -1,
                'price': 45000,
                'special': -1
            },
        ]
    },
    {
        'n': 2,
        'items': [{
                'itemid': 2450064,
                'qty': 1,
                'atk': -1,
                'allstat': -1,
                'price': 3000,
                'special': -1
            },
            {
                'itemid': 1112763,
                'qty': 1,
                'atk': -1,
                'allstat': -1,
                'price': 10000,
                'special': 5
            },
            {
                'itemid': 1112767,
                'qty': 1,
                'atk': -1,
                'allstat': -1,
                'price': 10000,
                'special': 5
            },
            {
                'itemid': 1112771,
                'qty': 1,
                'atk': -1,
                'allstat': -1,
                'price': 10000,
                'special': 5
            },
            {
                'itemid': 1112775,
                'qty': 1,
                'atk': -1,
                'allstat': -1,
                'price': 10000,
                'special': 5
            },
        ]
    },
    {
        'n': 3,
        'items': [{
                'itemid': 1113070,
                'qty': 1,
                'atk': 30,
                'allstat': 50,
                'price': 100000,
                'special': 5
            },
            {
                'itemid': 1152155,
                'qty': 1,
                'atk': 30,
                'allstat': 50,
                'price': 100000,
                'special': 5
            },
            {
                'itemid': 1032216,
                'qty': 1,
                'atk': 30,
                'allstat': 50,
                'price': 100000,
                'special': 5
            },
        ]
    },
    {
        'n': 4,
        'items': [{
            'itemid': 1002140,
            'qty': 1,
            'atk': -1,
            'allstat': -1,
            'price': 1000,
            'special': -1
        }, ]
    },
    {
        'n': 5,
        'items': [{
                'itemid': 1002140,
                'qty': 1,
                'atk': -1,
                'allstat': -1,
                'price': 40000,
                'special': -1
            },

        ]
    },
    {
        'n': 6,
        'items': [{
                'itemid': 1002140,
                'qty': 1,
                'atk': -1,
                'allstat': -1,
                'price': 1000,
                'special': -1
            },
            {
                'itemid': 1113055,
                'qty': 1,
                'atk': 50,
                'allstat': 100,
                'price': 200000,
                'special': 10
            },
        ]
    },
]

var NumReward = [

    {
        'n': 45,
        'items': [
            //	{'itemid' : 1147004, 'qty' : 1, 'atk' : 150, 'allstat' : 60, 'select' : false},
            //	{'itemid' : 2430025, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'select' : false},
            //	{'itemid' : 2430058, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'select' : false},
            //	{'itemid' : 2438687, 'qty' : 15,  'atk' : -1, 'allstat' : -1, 'select' : false},
            //{'itemid' : 2438695, 'qty' : 10,  'atk' : -1, 'allstat' : -1, 'select' : false},
            //{'itemid' : 2438692, 'qty' : 5,  'atk' : -1, 'allstat' : -1, 'select' : false},
            {
                //'itemid': [4000000, 4000001, 4000002],
                'qty': 10,
                'atk': -1,
                'allstat': -1,
                'select': true
            },
        ]
    },
    {
        'n': 60,
        'items': [
            //	{'itemid' : [1147009, 1147010], 'qty' : 1, 'atk' : 250, 'allstat' : 100, 'select' : true},
            //	{'itemid' : 2430058, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'select' : false},
            //	{'itemid' : 2430025, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'select' : false},
            //	{'itemid' : 2438687, 'qty' : 15,  'atk' : -1, 'allstat' : -1, 'select' : false},
            //	{'itemid' : 2438695, 'qty' : 10,  'atk' : -1, 'allstat' : -1, 'select' : false},
            //	{'itemid' : 2438692, 'qty' : 5,  'atk' : -1, 'allstat' : -1, 'select' : false},
            //	{'itemid' : 3010091, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'select' : false},
            //	{'itemid' : 3010093, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'select' : false},
        ]
    },
    {
        'n': 75,
        'items': [
            //	{'itemid' : [1146990, 1146991, 1146992, 1147005, 1147006, 1147007, 1147008], 'qty' : 1, 'atk' : 300, 'allstat' : 150, 'select' : true},
            //	{'itemid' : 2430025, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'select' : false},
            //	{'itemid' : 2438687, 'qty' : 15,  'atk' : -1, 'allstat' : -1, 'select' : false},
            //	{'itemid' : 2438695, 'qty' : 10,  'atk' : -1, 'allstat' : -1, 'select' : false},
            //	{'itemid' : 2438692, 'qty' : 5,  'atk' : -1, 'allstat' : -1, 'select' : false},
        ]
    },
    {
        'n': 100,
        'items': [
            //	{'itemid' : 1147004, 'qty' : 1, 'atk' : 150, 'allstat' : 60, 'select' : false},
            //	{'itemid' : 2430025, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'select' : false},
            //	{'itemid' : 2430058, 'qty' : 1,  'atk' : -1, 'allstat' : -1, 'select' : false},
            //	{'itemid' : 2438687, 'qty' : 15,  'atk' : -1, 'allstat' : -1, 'select' : false},
            //	{'itemid' : 2438695, 'qty' : 10,  'atk' : -1, 'allstat' : -1, 'select' : false},
            //	{'itemid' : 2438692, 'qty' : 5,  'atk' : -1, 'allstat' : -1, 'select' : false},
            //	{'itemid' : 5060048, 'qty' : 10,  'atk' : -1, 'allstat' : -1, 'select' : false},
        ]
    },
]

var reward = 0;

var modify = "";
var modifychr;

var max = 0;

var g = -1;
var k = -1;

var hasSelect = false;

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
        getData();
        if (cm.getClient().getKeyValue("pGrade") == null) {
            cm.getClient().setKeyValue("pGrade", "0");
        }
        if (cm.getPlayer().getHPoint() < 0) {
            cm.getPlayer().setHPoint(0);
        }
        var msg = "#fs15# 我是 #eFANCY#n 推廣積分發放助手.\r\n " + enter;
        msg += "當前 #b#h ##k的推廣積分 : #b" + cm.getPlayer().getHPoint() + "P#k" + enter;
        msg += "當前 #b#h ##k推廣等級 : #b" + cm.getPlayer().getPgrades() + "#k#b" + enter;
        msg += "當前 #b#h ##k的累計次數 : #b" + cm.getPlayer().getClient().getKeyValue("PNumber") + "次#k#b" + enter + enter;
        msg += "#L1#領取推廣積分" + enter;
        msg += "#L2#確認推廣積分排名"+enter;
        msg += "#L5#推廣積分商店" + enter;
        msg += "#L9#推廣積分商店2"+enter;
		msg += "#L8#推廣技能商店" + enter;
        msg += "#L7#領取累計次數奬勵"+enter;
        if (cm.getPlayer().isGM()) {
            msg += "#L3#發放推廣積分" + enter;
            msg += "#L4#推廣用戶管理" + enter;
            msg += "#L6#修改累計次數";
        }
        cm.sendSimple(msg);
    } else if (status == 1) {
        seld = sel;
        switch (sel) {
            case 1:
                cm.sendSimple(getList());
                break;
            case 2:
                cm.sendOk(getRanking(10));
                cm.dispose();
                break;
            case 3:
                if (!cm.getPlayer().isGM())
                    return;
                cm.sendGetText("#fs15#請寫下支付對象的名稱.");
                break;
            case 4:
                if (!cm.getPlayer().isGM())
                    return;
                var msg = "#fs15#請選擇您想要的項目.#b" + enter;
                msg += "#L2##fs15#調整用戶的推廣等級";
                cm.sendSimple(msg);
                break;
            case 5:
                /*				var msg = "구매하실 항목을 선택해주세요."+enter;
                				msg += "현재 #b#h ##k님의 홍보 포인트 : #b"+cm.getPlayer().getHPoint()+"P#k"+enter;
                				msg += "현재 #b#h ##k님의 홍보　 등급 : #b"+cm.getPlayer().getPgrades()+"#k"+enter
                				msg += "현재 #b#h ##k님의 누적   횟수 : #b"+cm.getPlayer().getClient().getKeyValue("PNumber")+"#k#b"+enter;
                				for (i = 0; i < shop.length; i++)
                					msg += "#L"+i+"##i"+shop[i]['itemid']+"##z"+shop[i]['itemid']+"# "+shop[i]['qty']+"개 #e("+shop[i]['price']+"P)#n"+enter;

                				cm.sendSimple(msg);*/
                /*
				var msg = "구매하실 항목을 선택해주세요."+enter;
				msg += "홍보 등급에 따라 구매 가능한 물품이 달라집니다." + enter;
				msg += "#h 0#님의 홍보 등급 : #b"+cm.getPlayer().getPgrades()+"#k#b"+enter;
				a = 0;
				for (i = 0; i < shop.length; i++) {
					if (shop[i]['n'] <= cm.getPlayer().getPgrade()) {
						msg += enter + "#e#d" + grade[shop[i]['n']][1] + " 이상 등급#k#n이 구매가능한 아이템 목록입니다." + enter;
						for (j = 0; j < shop[i]['items'].length; ++j) {
							msg += "#L"+a+"##i"+shop[i]['items'][j]['itemid']+"##z"+shop[i]['items'][j]['itemid']+"# "+shop[i]['items'][j]['qty']+"개 #e("+shop[i]['items'][j]['price']+"P)#n#l"+enter;
							a++;
						}
					}
				}
				cm.sendSimple(msg);
                                            */
                cm.dispose();
                cm.openNpc(1540100);
                break;
            case 6:
                if (!cm.getPlayer().isGM())
                    return;
                cm.sendGetText("#fs15#請寫下修改對象的名稱.");
                break;
            case 7:
                for (i = 0; i < NumReward.length; i++) {
                    if (cm.getClient().getKeyValue("pnd_" + NumReward[i]['n']) == null && Integer.parseInt(cm.getPlayer().getClient().getKeyValue("PNumber")) >= NumReward[i]['n']) {
                        g = i;
                        break;
                    }
                }
                if (g == -1) {
                    cm.sendOk("#fs15#無法獲得累計奬勵.");
                    cm.dispose();
                    return;
                }
                if (Integer.parseInt(cm.getPlayer().getClient().getKeyValue("PNumber")) < NumReward[g]['n']) {
                    cm.sendOk("#fs15#無法獲得等級奬勵.");
                    cm.dispose();
                    return;
                }
                if (cm.getClient().getKeyValue("pnd_" + NumReward[g]['n']) != null) {
                    cm.sendOk("#fs15#無法獲得等級奬勵.");
                    cm.dispose();
                    return;
                }
                var msg = "#fs15##b累計推廣 祝賀您完成第" + NumReward[g]['n'] + "#k次!" + enter;
                msg += "從低等級奬勵開始依次獲得." + enter;
                msg += "現時獲得的奬勵是 #b推廣 " + NumReward[g]['n'] + "場次，楓葉裏 " + grade[g + 3][1] + " 等級#k的奬勵." + enter + enter;
                msg += "#r※警告！該奬勵每個帳號可獲得一次." + enter;
                msg += "如果真的想在這個角色上收到，請點擊'是'.";
                cm.sendYesNo(msg);
                break;
				case 8:
                cm.dispose();
                cm.openNpc(9062612);
                break;
                case 9:
                cm.dispose();
                cm.openNpcCustom(cm.getClient(), 9063144, "supportshop");
                break;
        }
    } else if (status == 2) {
        switch (seld) {
            case 1:
                getList2(getsex(sel));
                break;
            case 3:
                if (!cm.getPlayer().isGM())
                    return;
                name = cm.getText();
                var msg = "#fs15#支付對象的名稱 : #b" + name + "#k" + enter;
                msg += "#fs15##fUI/StatusBar.img/BtClaim/normal/0#要支付多少流媒體奬勵?";
                cm.sendGetNumber(msg, 0, 0, 100000);
                break;
            case 4:
                seld3 = sel;
                if (!cm.getPlayer().isGM())
                    return;
                switch (seld3) {
                    case 2:
                        cm.sendGetText("#fs15#請寫下要調整等級的用戶的名稱.");
                        break;
                }
                break;
            case 5:
                seld2 = sel;
                a = 0;
                itx = null;
                for (i = 0; i < shop.length; i++) {
                    if (shop[i]['n'] <= cm.getPlayer().getPgrade()) {
                        for (j = 0; j < shop[i]['items'].length; ++j) {
                            if (a == seld2) {
                                itx = shop[i]['items'][j];
                                break;
                            } else {
                                a++;
                            }
                        }
                    }
                    if (itx != null) {
                        break;
                    }
                }

                if (itx == null) {
                    cm.sendOk("#fs15#發生錯誤。 請與運營者聯系.");
                    cm.dispose();
                    return;
                }

                msg = "#fs15#選擇的物品 : #b#i" + itx['itemid'] + "##z" + itx['itemid'] + "##k" + enter;
                msg += "支付的數量 : #d" + itx['qty'] + "個#k" + enter;
                if (itx['allstat'] > 0) {
                    msg += "#fs15#其他全自動調節器選項 : #b+" + itx['allstat'] + "#k" + enter;
                }
                if (itx['atk'] > 0) {
                    msg += "#fs15#追加攻擊力/魔力選項 : #b+" + itx['atk'] + "#k" + enter;
                }
                if (itx['special'] > 0) {
                    msg += "#fs15#BOSS攻擊力/防禦力無視/傷害 : #b+" + itx['special'] + "%#k" + enter + enter;
                }
                if (itx['allstat'] > 0 || itx['atk'] > 0 || itx['special'] > 0) {
                    msg += "#fs15##e該内容使用卷軸時無法被恢復.#n" + enter;
                }
                msg += enter + enter;
                msg += "#fs15#如果您要購買的物品資訊正確，請按 #e是#n." + enter;
                msg += "#fs15#推廣積分 减少到P#r#e" + itx['price'] + "P#k#n.";

                cm.sendYesNo(msg);
                break;
            case 6:
                if (!cm.getPlayer().isGM())
                    return;
                name = cm.getText();
                modifychr = cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(name);
                var msg = "#fs15#要將#b" + name + "#k的推廣等級設定為多少次?" + enter;
                if (modifychr == null) {
                    cm.sendOk("#fs15#當前頻道 #b" + name + "#k不存在. 請在同一頻道進行.");
                    cm.dispose();
                    return;
                }
                cm.sendGetNumber(msg, 0, 0, 5000);
                break;
            case 7:
                if (g == -1) {
                    cm.sendOk("#fs15#沒有可獲得的累計奬勵.");
                    cm.dispose();
                    return;
                }
                if (Integer.parseInt(cm.getPlayer().getClient().getKeyValue("PNumber")) < NumReward[g]['n']) {
                    cm.sendOk("#fs15#無法獲得等級奬勵.");
                    cm.dispose();
                    return;
                }
                if (cm.getClient().getKeyValue("pnd_" + NumReward[g]['n']) != null) {
                    cm.sendOk("#fs15#無法獲得等級奬勵.");
                    cm.dispose();
                    return;
                }

                for (j = 0; j < NumReward[g]['items'].length; j++) {
                    if (NumReward[g]['items'][j]['select']) {
                        hasSelect = true;
                    }
                }

                if (hasSelect) {
                    var k = -1;
                    var msg = "#fs15#存在可供選擇的物品!" + enter;
                    for (j = 0; j < NumReward[g]['items'].length; j++) {
                        if (NumReward[g]['items'][j]['select']) {
                            k = j;
                        }
                    }
                    if (k == -1) {
                        cm.sendOk("-1");
                        cm.dispose();
                        return;
                    }

                    for (i = 0; i < NumReward[g]['items'][k]['itemid'].length; i++) {
                        msg += "#L" + i + "##i" + NumReward[g]['items'][k]['itemid'][i] + "##z" + NumReward[g]['items'][k]['itemid'][i] + "#" + enter;
                    }
                    cm.sendSimple(msg);
                } else {
                    for (j = 0; j < NumReward[g]['items'].length; j++) {
                        if (Math.floor(NumReward[g]['items'][j]['itemid'] / 10000) == 1)
                            gainItemS(NumReward[g]['items'][j]['itemid'], NumReward[g]['items'][j]['qty'], NumReward[g]['items'][j]['allstat'], NumReward[g]['items'][j]['atk'])
                        else
                            cm.gainItem(NumReward[g]['items'][j]['itemid'], NumReward[g]['items'][j]['qty']);
                    }
                    cm.getPlayer().getClient().setKeyValue("pnd_" + NumReward[g]['n'], "1");
                    cm.getPlayer().setPgrade(g + 3);
                    cm.sendOk("#fs15#支付已完成.");
                    cm.dispose();
                    return;
                }
                break;
        }
    } else if (status == 3) {
        switch (seld) {
            case 1:
                seldreward = sel;
                reward = getQ(seldreward);
                cm.sendYesNo("#fs15#確定要得到相應的奬勵碼?\r\n共 獲得#b" + reward + " 推廣積分#k.");
                break;
            case 3:
                if (!cm.getPlayer().isGM())
                    return;
                youtube = sel;
                var msg = "#fs15#支付對象的名稱 : #b" + name + "#k" + enter;
                msg += "#fs15#流媒體奬勵 : #b" + youtube + "#k" + enter;
                msg += "#fs15##fUI/StatusBar.img/BtClaim/normal/0#要支付多少發帖奬勵?";
                cm.sendGetNumber(msg, 0, 0, 100000);
                break;
            case 4:
                seld2 = sel;
                switch (seld3) {
                    case 2:
                        modify = cm.getText();
                        modifychr = cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(modify);
                        var msg = "#fs15#要將#b" + modify + "#k的推廣等級更改為什麼?" + enter;
                        if (modifychr == null)
                            msg += enter + "#fs15#當前頻道中不存在 #b" + modify + "#k. #b如果確定沒有連接 #b請繼續進行.#k 如果正在連接中，請與該角色在 #b#e同一頻道#k#n進行." + enter;
                        for (i = 0; i < grade.length; i++) {
                            msg += "#L" + i + "#FANCY " + grade[i][1] + enter;
                        }
                        cm.sendSimple(msg);
                        break;
                }
                break;
            case 5:
                if (cm.getPlayer().getHPoint() < itx['price']) {
                    cm.sendOk("#fs15#推廣積分不足.");
                    cm.dispose();
                    return;
                }

                if (!cm.canHold(itx['itemid'], itx['qty'])) {
                    cm.sendOk("#fs15#庫存空間不足或無法再擁有的物品.");
                    cm.dispose();
                    return;
                }

                cm.getPlayer().gainHPoint(-itx['price']);

                if (Math.floor(itx['itemid'] / 1000000) == 1)
                    gainItemT(itx['itemid'], itx['allstat'], itx['atk'], itx['special']);
                else
                    cm.gainItem(itx['itemid'], itx['qty']);

                msg = "#fs15#選擇的物品 : #b#i" + itx['itemid'] + "##z" + itx['itemid'] + "##k" + enter;
                msg += "#fs15##h 0#正常向您支付了 #d" + itx['qty'] + "個#k." + enter + enter;
                msg += "#fs15#現時 #h 0#的剩餘推廣點 : #e#b" + cm.getPlayer().getHPoint() + "P#k#n";
                cm.sendOk(msg);
                cm.dispose();
                break;
            case 6:
                if (!cm.getPlayer().isGM())
                    return;
                modifychr.getClient().setKeyValue("PNumber", "" + sel);
                cm.sendOk("#fs15#設定已完成.");
                cm.dispose();
                break;
            case 7:
                for (j = 0; j < NumReward[g]['items'].length; j++) {
                    if (NumReward[g]['items'][j]['select']) {
                        if (Math.floor(NumReward[g]['items'][j]['itemid'][sel] / 10000) == 1)
                            gainItemS(NumReward[g]['items'][j]['itemid'][sel], NumReward[g]['items'][j]['qty'], NumReward[g]['items'][j]['allstat'], NumReward[g]['items'][j]['atk'])
                        else
                            cm.gainItem(NumReward[g]['items'][j]['itemid'][sel], NumReward[g]['items'][j]['qty']);
                    } else {
                        if (Math.floor(NumReward[g]['items'][j]['itemid'] / 10000) == 1)
                            gainItemS(NumReward[g]['items'][j]['itemid'], NumReward[g]['items'][j]['qty'], NumReward[g]['items'][j]['allstat'], NumReward[g]['items'][j]['atk'])
                        else
                            cm.gainItem(NumReward[g]['items'][j]['itemid'], NumReward[g]['items'][j]['qty']);
                    }
                }
                cm.getPlayer().getClient().setKeyValue("pnd_" + NumReward[g]['n'], "1");
                cm.getPlayer().setPgrade(g + 3);
                cm.sendOk("#fs15#支付已完成.");
                cm.dispose();
                break;
        }
    } else if (status == 4) {
        switch (seld) {
            case 1:
                /*
                	수령 (지급이 완료되었습니다)
                */
                getReward(seldreward);
                cm.getPlayer().gainHPoint(reward);
	    cm.getPlayer().saveToDB(true, false);
                cm.sendOk("#fs15#領取完畢.");
                cm.dispose();
                break;
            case 3:
                if (!cm.getPlayer().isGM())
                    return;
                blog = sel;
                var msg = "#fs15#支付對象的名稱 : #b" + name + "#k" + enter;
                msg += "#fs15#流媒體奬勵 : #b" + youtube + "#k" + enter;
                msg += "#fs15#發帖奬勵 : #b" + blog + "#k" + enter;
                msg += "#fs15##fUI/StatusBar.img/BtClaim/normal/0#想支付多少其他補償?";
                cm.sendGetNumber(msg, 0, 0, 100000);
                break;
            case 4:
                switch (seld3) {
                    case 2:
                        if (modifychr != null) {
                            modifychr.setPgrade(sel);
                        } else {
                            var con = DatabaseConnection.getConnection();
                            asdid = getAccId(modify);
                            var ps = con.prepareStatement("UPDATE acckeyvalue SET `value` = ? WHERE `id` = ? and `key` = ?");
                            ps.setString(1, sel);
                            ps.setInt(2, asdid);
                            ps.setString(3, "pGrade");
                            ps.executeUpdate();
                            ps.close();
                        }
                        cm.sendOk("#fs15#更改已完成.");
                        cm.dispose();
                        break;
                }
                break;
        }
    } else if (status == 5) {
        if (!cm.getPlayer().isGM())
            return;
        etc = sel;
        var msg = "#fs15#支付對象的名稱 : #b" + name + "#k" + enter;
        msg += "#fs15#流媒體奬勵 : #b" + youtube + "#k" + enter;
        msg += "#fs15#發帖奬勵 : #b" + blog + "#k" + enter;
        msg += "#fs15#其他奬勵 : #b" + etc + "#k" + enter;
        msg += "#fs15##b最後！ 請寫下備註.#k";
        cm.sendGetText(msg);
    } else if (status == 6) {
        if (!cm.getPlayer().isGM())
            return;
        comment = cm.getText();
        var msg = "#fs15#支付對象的名稱 : #b" + name + "#k" + enter;
        msg += "#fs15#流補償 : #b" + youtube + "#k" + enter;
        msg += "#fs15#發帖奬勵 : #b" + blog + "#k" + enter;
        msg += "#fs15#其他奬勵 : #b" + etc + "#k" + enter;
        msg += "#fs15#備註 : #b" + comment + "#k" + enter;
        msg += "#fs15##b確定要支付碼?";
        cm.sendYesNo(msg);
    } else if (status == 7) {
        if (!cm.getPlayer().isGM())
            return;
        send(name, youtube, blog, etc, comment);
        cm.sendOk("#fs15#支付完畢");
        cm.dispose();
    }
}

function getData() {
    time = new Date();
    year = time.getFullYear();
    month = time.getMonth() + 1;
    if (month < 10) {
        month = "0" + month;
    }
    date2 = time.getDate() < 10 ? "0" + time.getDate() : time.getDate();
    date = year + "." + month + "." + date2;
    day = time.getDay();
}

function getRanking(asd) {
    var ret = "#fs15#排名最多只顯示" + asd + "人數.\r\n";
    var as = 0;
    var names = [];

    var con = DatabaseConnection.getConnection();
    var ps = con.prepareStatement("SELECT * FROM kms.acckeyvalue WHERE `key` = 'nHpoint' ORDER BY CAST(`value` AS UNSIGNED INT) DESC LIMIT " + asd);
    var rs = ps.executeQuery();

    while (rs.next()) {
        as += 1;
        var ps2 = con.prepareStatement("SELECT `name` FROM kms.characters WHERE `accountid` = ? ORDER BY `level` DESC LIMIT 1");
        ps2.setInt(1, rs.getInt("id"));
        rs2 = ps2.executeQuery();
        var name = "";
        if (rs2.next()) {
            name = rs2.getString("name");
        }
        rs2.close();
        ps2.close();

        ret += as + "#fs15#. #b" + name + "#k 累計推廣積分 : #d" + rs.getString("value") + "#k\r\n";
    }
    rs.close();
    ps.close();

    if (ret.equals("")) return "#fs15#沒有排名.";
    return ret;
}

function send(a, b, c, d, e) {
    var con = DatabaseConnection.getConnection();
    var ps = con.prepareStatement("INSERT INTO hongbo (name, youtube, blog, etc, comment, date, cid) VALUES (?, ?, ?, ?, ?, ?, ?)");
    ps.setString(1, a);
    ps.setInt(2, b);
    ps.setInt(3, c);
    ps.setInt(4, d);
    ps.setString(5, e);
    ps.setString(6, date);
    ps.setInt(7, MapleCharacterUtil.getIdByName(a));
    ps.executeUpdate();
    ps.close();
}

function getList() {
    var ret = "";
    var names = [];

    var con = DatabaseConnection.getConnection();
    var ps = con.prepareStatement("SELECT * FROM hongbo WHERE `check` = 0");
    var rs = ps.executeQuery();

    while (rs.next()) {
        if (names.indexOf(rs.getString("name")) == -1) {
            names.push(rs.getString("name"));
            ret += "#L" + rs.getInt("id") + "##b" + rs.getString("name");
            if (rs.getInt("cid") == cm.getPlayer().getId() || rs.getString("name").equals(cm.getPlayer().getName()))
                ret += "#fs15# (支付對象)\r\n";
            else
                ret += "#fs15#\r\n";
        }
    }
    rs.close();
    ps.close();

    if (ret.equals("")) return "#fs15#現時沒有可領取的推廣奬勵.";
    return ret;
}

function getsex(sad) {
    var ret = "";
    var names = [];

    var con = DatabaseConnection.getConnection();
    var ps = con.prepareStatement("SELECT * FROM hongbo WHERE `check` = 0 AND `id` = ?");
    ps.setInt(1, sad);
    var rs = ps.executeQuery();

    while (rs.next()) {
        ret = rs.getString("name");
    }
    rs.close();
    ps.close();

    if (ret.equals("")) return "SSSSSSSSS";
    return ret;
}

function getList2(n) {
    var mine = false;
    var ret = "#fs15##fUI/StatusBar.img/BtClaim/normal/0#收到了來自管理員的奬勵?\r\n----------------------------------------------\r\n";
    var con = DatabaseConnection.getConnection();
    var ps = con.prepareStatement("SELECT * FROM hongbo WHERE `check` = 0 AND `name` = ?");
    ps.setString(1, n);
    var rs = ps.executeQuery();
    while (rs.next()) {
        if (rs.getInt("cid") == cm.getPlayer().getId() || rs.getString("name").equals(cm.getPlayer().getName())) {
            ret += "#L" + (rs.getInt("id")) + "#";
            mine = true;
        }
        ret += "#fs15#名稱 : #b" + rs.getString("name") + "#k #fs15#支付日期 : #d" + rs.getString("date") + "#k \r\n";
        ret += "#fs15#直播次數 : #b" + rs.getInt("youtube") + "#k / 發文次數 : #b" + rs.getInt("blog") + "#k / 其他 : #b" + rs.getInt("etc") + "#k\r\n";
        ret += "#fs15#備註 : #d" + rs.getString("comment") + "#k#l#fs15#\r\n\r\n----------------------------------------------\r\n";
    }
    rs.close();
    ps.close();
    if (mine) {
        cm.sendSimple(ret);
    } else {
        cm.sendOk(ret);
        cm.dispose();
        return;
    }
}

function getQ(id) {
    var ret = 0;
    var con = DatabaseConnection.getConnection();
    var ps = con.prepareStatement("SELECT * FROM hongbo WHERE `check` = 0 AND `id` = ? AND (`name` = ? or `cid` = ?)");
    ps.setInt(1, id);
    ps.setString(2, cm.getPlayer().getName());
    ps.setInt(3, cm.getPlayer().getId());
    var rs = ps.executeQuery();
    while (rs.next()) {
        ret += rs.getInt("youtube");
        ret += rs.getInt("blog");
        ret += rs.getInt("etc");
    }
    rs.close();
    ps.close();
    if (ret.equals("")) return "#fs15#沒有推廣奬勵.";
    return ret;
}

function getAccId(a) {
    var ret = -1;
    var con = DatabaseConnection.getConnection();
    var ps = con.prepareStatement("SELECT * FROM characters WHERE `name` = ?");
    ps.setString(1, a);
    var rs = ps.executeQuery();

    while (rs.next()) {
        ret = rs.getInt("accountid");
    }
    rs.close();
    ps.close();
    return ret;
}

function getReward(id) {
    var con = DatabaseConnection.getConnection();
    var ps = con.prepareStatement("UPDATE hongbo SET `check` = 1 WHERE `id` = ? AND (`name` = ? or `cid` = ?)");
    ps.setInt(1, id);
    ps.setString(2, cm.getPlayer().getName());
    ps.setInt(3, cm.getPlayer().getId());
    ps.executeUpdate();
    ps.close();
}

function gainItemT(id, as, atk, spe) {
    item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(id);
    if (as > -1) {
        item.setStr(as);
        item.setDex(as);
        item.setInt(as);
        item.setLuk(as);
    }
    if (atk > -1) {
        item.setWatk(atk);
        item.setMatk(atk);
    }
    if (spe > -1) {
        item.addBossDamage(spe);
        item.addIgnoreWdef(spe);
        item.addTotalDamage(spe);
    }
    Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), item, false);
}

function gainItemS(id, as, atk) {
    item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(id);
    if (as > -1) {
        item.setStr(as);
        item.setDex(as);
        item.setInt(as);
        item.setLuk(as);
    }
    if (atk > -1) {
        item.setWatk(atk);
        item.setMatk(atk);
    }
    Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), item, false);
}