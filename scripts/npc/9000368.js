별 = "#fUI/FarmUI.img/objectStatus/star/whole#"
importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(Packages.handling.channel);
var status;
var questcompleted = 0;
user = 0;

function start() {
    status = -1;
    action(1, 1, 0);
}

function action(mode, type, selection) {
    questlist = [
        [4000001, "mob", 150, false, 10, 100010000],
        [4000621, "mob", 150, false, 30, 101030500],
        [4000036, "mob", 150, false, 50, 103020320],
        [4000208, "mob", 250, false, 80, 102040600],
        [4000182, "mob", 250, false, 100, 230040000],
        [4000179, "mob", 250, false, 200, 230040000],
        [4036491, "mob", 500, false, 210, 310030300],
        [4033172, "mob", 500, false, 220, 410000113],
        [4001843, "mob", 2, false, 230, 350060300],
        [4001869, "mob", 2, false, 240, 105300303],
        [4001879, "mob", 2, false, 245, 450004000],
        [250, "level", 0, false, 250, -1]
    ];
    itemlist = [
        [
            [4319999, "item", 100, 0],
            [4310248, "item", 100, 0]
        ],
        [
            [4319999, "item", 200, 0]
        ],
        [
            [4319999, "item", 300, 0]
        ],
        [
            [4319999, "item", 100, 0]
        ],
        [
            [4319999, "item", 200, 0]
        ],
        [
            [4319999, "item", 300, 0]
        ],
        [
            [4319999, "item", 200, 0]
        ],
        [
            [4319999, "item", 200, 0]
        ],
        [
            [4319999, "item", 350, 0]
        ],
        [
            [4319999, "item", 350, 0]
        ],
        [
            [4319999, "item", 100, 0]
        ],
        [
            [4319999, "item", 1000, 0]
        ]
    ];

    rewardlist = [
        [
            [4319999, 100]
        ],
        [
            [4319999, 200]
        ],
        [
            [4319999, 300]
        ],
        [
            [4319999, 100]
        ],
        [
            [4319999, 200]
        ],
        [
            [4319999, 300]
        ],
        [
            [4319999, 200]
        ],
        [
            [4319999, 200]
        ],
        [
            [4319999, 350]
        ],
        [
            [4319999, 350]
        ],
        [
            [4319999, 100]
        ],
        [
            [4319999, 1000]
        ]
    ];

    if (mode <= 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        for (i = 0; i < ChannelServer.getAllInstances().length; i++) {
            user += ChannelServer.getAllInstances().get(i).getPlayerStorage().getAllCharacters().length;
        }
        말 = "#fs15##b#e[Fancy] 培養日記,#n#k";
        말 += "#fs15##b完成 " + questlist.length + " 個#k #b#e[Fancy] 培養日記#k#n後領取講勵.\r\n#r#e#fs15#[ 點擊任務可前往該地圖. ]#k#fs15##n\r\n";
        말 += "#fs15##b【所有日記完成講勵】 : #b#i4319999# #r2000個#k\r\n";

        for (i = 0; i < questlist.length; i++) {

            if (Integer.parseInt(cm.getPlayer().getKeyValue(20190710 + i, "diary")) != -1) {

                questcompleted++;
            } else {
                말 += "#fs15##L" + i + "#"
                if (cm.getPlayer().getLevel() >= questlist[i][4]) {
                    말 += "#b"
                } else {
                    말 += "#Cgray#"
                }
                말 += getQuestType(questlist[i][0], questlist[i][1], questlist[i][2], questlist[i][3], questlist[i][4])
                말 += " #b(" + isQuestCompletable(questlist[i][0], questlist[i][1], questlist[i][2], questlist[i][4]) + ")#k#l\r\n\r\n";
                for (j = 0; j < rewardlist[i].length; j++)
                    말 += "#fs15#     #fc0xFFF781D8#獲得 : #i" + rewardlist[i][j][0] + "#" + rewardlist[i][j][1] + "個#k\r\n"
            }
        }
        if (cm.getPlayer().getKeyValue(20190710, "questallcompleted") == -1) {
            말 += "\r\n#L1000##d("
            if (questcompleted == questlist.length || cm.getPlayer().getGMLevel() >= 11) { // GM테스트
                말 += "可完成)#k#l"
            } else {
                말 += "[Fancy] 培養日記正在進行中)#r\r\n"
                말 += "[" + questcompleted + "完成了個任務。, " + (questlist.length - questcompleted) + "個任務正在進行中.]"
            }
        }
        cm.sendSimpleS(말,0x86);
    } else if (status == 1) {
        if (selection == 1000) {
            말2 = "";
            if (questcompleted == questlist.length || cm.getPlayer().getGMLevel() >= 11) {
                gift = [
                    [4319999, 2000]
                ];
                for (i = 0; i < gift.length; i++) {
                    말2 += "#i" + gift[i][0] + "# #b#z" + gift[i][0] + "# " + gift[i][1] + "個#k\r\n"
                    cm.gainItem(gift[i][0], gift[i][1]);
                }
                cm.getPlayer().setKeyValue(20190710, "questallcompleted", 1);
                말 = "完成了所有的日記啊！ 作為補償，發放了以下物品!\r\n\r\n";
                말 += 말2;
                cm.sendOk(말);
            } else {
                cm.sendOk("好像還沒完成日記。?");
                cm.dispose();
            }
        } else {
            st = selection;
            // 检查索引是否在questlist数组范围内
            if (st >= 0 && st < questlist.length) {
                qt = questlist[st];
                // 检查任务是否已经完成
                if (Integer.parseInt(cm.getPlayer().getKeyValue(20190710 + st, "diary")) == -1) {
                    if (isQuestCompletable(qt[0], qt[1], qt[2], qt[4]) == "可完成") {
                        for (i = 0; i < itemlist[st].length; i++) {
                            gainItemByType(itemlist[st][i][0], itemlist[st][i][1], itemlist[st][i][2], itemlist[st][i][3])
                        }
                        gainReqitemByType(qt[0], qt[1], qt[2]);
                        cm.getPlayer().setKeyValue(20190710 + st, "diary", 1);
                        cm.sendOk("任務完成了！ 我會給你報酬的~");
                    } else {
                        mapid = qt[5];
                        if (mapid < 0)
                            cm.sendOk("現時執行該任務的等級不足，或者任務好像沒有完成！");
                        else
                            cm.warp(qt[5]);
                    }
                } else {
                    cm.sendOk("该任务已经完成！");
                }
            } else {
                cm.sendOk("已經完成了今天的所有任務！");
            }
        }
        cm.dispose();
    }
}

function getQuestType(qid, qtype, qnum, isSecret, qlevel) {
    if (!isSecret) {
        switch (qtype) {
            case "mob":
                return "#e[Lv." + qlevel + "]#n #fc0xFFF361A6##i" + qid + "##k #r" + nf(qnum) + "個#k 收集#k";
                break;
            case "level":
                return "#e[Lv." + qlevel + "]#n #r" + qlevel + "#k等級達成#k";
                break;
            case "item":
                return "#e[Lv." + qlevel + "]#n #fc0xFFF361A6##i" + qid + "##k #r" + nf(qnum) + "個#k 持有异常#k";
                break;
            case "party":
                return "#e[Lv." + qlevel + "]#n #r" + qnum + "#k屬於名以上的隊伍#k";
                break;
            case "boss":
                return "#e[Lv." + qlevel + "]#n #r" + qid + "#k를 " + qnum + "清除次數以上#k";
                break;
            case "meso":
                return "#e[Lv." + qlevel + "]#n #r" + nf(qnum) + "#k 擁有兆以上#k";
                break;
            case "mpoint":
                return "#e[Lv." + qlevel + "]#n #r" + nf(qnum) + "#k P 擁有更多快取記憶體#k";
                break;
            case "kpoint":
                return "#e[Lv." + qlevel + "]#n #r" + nf(qnum) + "#k 擁有以上格子社區積分#k";
                break;
            case "howmany":
                return "#e[Lv." + qlevel + "]#n #k#fc0xFFF361A6#동접 #r" + nf(qnum) + "명#k 實現";
                break;
            default:
                return "錯誤.";
                break;
        }
    } else {
        return "[Lv. " + qlevel + "] 實現條件保密.";
    }
}

function isQuestCompletable(qid, qtype, qnum, qlevel) {
    if (cm.getPlayer().getLevel() >= qlevel) {
        switch (qtype) {
            case "mob":
                if (cm.itemQuantity(qid) >= qnum) {
                    return "可完成"
                } else {
                    return "正在進行中"
                }
                break;
            case "level":
                return "可完成"
                break;
            case "item":
                if (cm.itemQuantity(qid) >= qnum) {
                    return "可完成"
                } else {
                    return "正在進行中"
                }
                break;
            case "party":
                if (cm.getPlayer().getParty() != null && cm.getPartyMembers().size() >= qnum) {
                    return "可完成"
                } else {
                    return "正在進行中"
                }
                break;
            case "boss":
                if (cm.GetCount(qid, 1) >= qnum) {
                    return "可完成"
                } else {
                    return "正在進行中"
                }
                break;
            case "meso":
                if (cm.getPlayer().getMeso() >= qnum) {
                    return "可完成"
                } else {
                    return "正在進行中"
                }
                break;
            case "mpoint":
                if (cm.getPlayer().getCSPoints(1) >= qnum) {
                    return "可完成"
                } else {
                    return "正在進行中"
                }
                break;
            case "kpoint":
                break;
            case "howmany":
                if (user >= qnum) {
                    return "可完成"
                } else {
                    return "正在進行中"
                }
                break;
            default:
                return "錯誤.";
                break;
        }
    } else {
        return "等級不足"
    }
}

function gainReqitemByType(qid, qtype, qnum) {
    switch (qtype) {
        case "mob":
            cm.gainItem(qid, -qnum);
            break;
        case "meso":
            cm.gainMeso(-qnum);
            break;
        case "mpoint":
            cm.getPlayer().modifyCSPoints(2, -qnum, false);
            break;
        case "kpoint":
            cm.getPlayer().gainbounscoin(-qnum);
            break;
        default:
            break;
    }
}

function gainItemByType(iid, itype, i1, i2) {
    switch (itype) {
        case "item":
            cm.gainItem(iid, i1);
            break;
        case "meso":
            cm.gainMeso(i1);
            break;
        case "itemPeriod":
            cm.gainItemPeriod(iid, i1, i2);
            break;
        case "EqpAllStatAtk":
            break;
        case "Point":
            cm.getPlayer().AddStarDustCoin(i1);
            break;
        default:
            cm.sendOk("發生錯誤.");
            cm.dispose();
            break;
    }
}

function nf(paramint) {
    return java.text.NumberFormat.getInstance().format(paramint);
}