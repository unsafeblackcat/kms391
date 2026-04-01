importPackage(java.lang);
importPackage(Packages.constants);
importPackage(Packages.client);
importPackage(Packages.tools.packet);

var status = 0;
var sel = -1;
var index = 0;

var Hero = [// 히어로 스킬 1~4차
    [1001005, 1001008, 1001011, 1001010],
    [1101011, 1101014, 1101013, 1101006],
    [1111010, 1111016, 1111012, 1111003],
    [1121008, 1121015, 1121016, 1121010, 1121000, 1121011],
    [1121052, 1121053, 1121054],
];

var Paladin = [// 팔라딘 스킬 1~4차
    [1001005, 1001008, 1001011, 1001010],
    [1201015, 1201013],
    [1211018, 1211010, 1211012, 1211013, 1211014, 1211011],
    [1221009, 1221014, 1221011, 1221015, 1221000, 1221012, 1221016],
    [1221052, 1221053, 1221054],
];

var BowMaster = [// 보우마스터 스킬 1~4차
    [3001004],
    [3101005, 3101008, 3101009],
    [3111013, 3111015, 3111005, 3111017],
    [3121020, 3121015, 3121002, 3121009, 3121000],
    [3121052, 3121053, 3121054],
];

var WindBreaker = [// 윈드브레이커 1~4차
    [13001020, 13001021, 13001022],
    [13101021, 13101029, 13101022],
    [13111020, 13111021, 13111024],
    [13121016, 13121001, 13121017, 13121005],
    [13121052, 13121053, 13121055],
];

var NightRod = [// 나이트로드 1~4차
    [4001334, 4001344, 4001003],
    [4101013, 4101010, 4101011, 4101015, 4101016],
    [4111010, 4111015, 4111007, 4111002],
    [4121013, 4121017, 4121016, 4121015],
    [4121052, 4121053, 4121054],
];

var NightWalker = [// 나이트워커 1~4차
    [14001020, 14001021, 14001027, 14001023, 14001024],
    [14101020, 14101028],
    [14111020, 14111024, 14111030],
    [14121001, 14121003, 14121004, 14121016],
    [14121052, 14121053, 14121054],
];

var bulldog = [// 불독 1~4차
    [2100009, 2101005],
    [2111013, 2111014, 2111003, 2100010, 2111002, 2101010, ],
    [2121003, 2121006],
];

function start() {
    status = -1;
    action(1, 0, 0);
}
function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }
    if (status == 0) {
        if (!cm.getPlayer().isGM()) {
            return;
        }
        if (cm.getPlayer().getJob() == 112) {
            var msg = "#fs15#현재 당신의 직업은 #e<하야토>#n 입니다.\r\n하야토는 팔라딘의 스킬을 계승받을수 있습니다.\r\n\r\n";
            for (i = 0; i < Paladin.length; i++) {
                if (i == 4) {
                    msg += "#e<하이퍼>#n\r\n";
                } else {
                    msg += "#e<" + (i + 1) + "차>#n\r\n";
                }
                for (a = 0; a < Paladin[i].length; a++) {
                    msg += "#b#L" + Paladin[i][a] + "# #s" + Paladin[i][a] + "# #q" + Paladin[i][a] + "##l#k\r\n";
                }
                msg += "\r\n\r\n";
            }
        } else if (cm.getPlayer().getJob() == 6112) {
            var msg = "#fs15#현재 당신의 직업은 #e<팔라딘>#n 입니다.\r\n팔라딘은 히어로의 스킬을 계승받을수 있습니다.\r\n\r\n";
            for (i = 0; i < Hero.length; i++) {
                if (i == 4) {
                    msg += "#e<하이퍼>#n\r\n";
                } else {
                    msg += "#e<" + (i + 1) + "차>#n\r\n";
                }
                for (a = 0; a < Hero[i].length; a++) {
                    msg += "#b#L" + Hero[i][a] + "# #s" + Hero[i][a] + "# #q" + Hero[i][a] + "##k#l\r\n";
                }
                msg += "\r\n\r\n";
            }
        } else if (cm.getPlayer().getJob() == 312) {
            var msg = "현재 당신의 직업은 #e<보우마스터>#n 입니다.\r\n보우마스터는 윈드브레이커의 스킬을 계승받을수 있습니다.\r\n\r\n";
            for (i = 0; i < WindBreaker.length; i++) {
                if (i == 4) {
                    msg += "#e<하이퍼>#n\r\n";
                } else {
                    msg += "#e<" + (i + 1) + "차>#n\r\n";
                }
                for (a = 0; a < WindBreaker[i].length; a++) {
                    msg += "#L" + WindBreaker[i][a] + "# #s" + WindBreaker[i][a] + "# #q" + WindBreaker[i][a] + "##l\r\n";
                }
                msg += "\r\n\r\n";
            }
        } else if (cm.getPlayer().getJob() == 1312) {
            var msg = "현재 당신의 직업은 #e<윈드브레이커>#n 입니다.\r\n윈드브레이커는 보우마스터의 스킬을 계승받을수 있습니다.\r\n\r\n";
            for (i = 0; i < BowMaster.length; i++) {
                if (i == 4) {
                    msg += "#e<하이퍼>#n\r\n";
                } else {
                    msg += "#e<" + (i + 1) + "차>#n\r\n";
                }
                for (a = 0; a < BowMaster[i].length; a++) {
                    msg += "#L" + BowMaster[i][a] + "# #s" + BowMaster[i][a] + "# #q" + BowMaster[i][a] + "##l\r\n";
                }
                msg += "\r\n\r\n";
            }
        } else if (cm.getPlayer().getJob() == 412) {
            var msg = "현재 당신의 직업은 #e<나이트로드>#n 입니다.\r\n나이트로드는 나이트워커의 스킬을 계승받을수 있습니다.\r\n\r\n";
            for (i = 0; i < NightWalker.length; i++) {
                if (i == 4) {
                    msg += "#e<하이퍼>#n\r\n";
                } else {
                    msg += "#e<" + (i + 1) + "차>#n\r\n";
                }
                for (a = 0; a < NightWalker[i].length; a++) {
                    msg += "#L" + NightWalker[i][a] + "# #s" + NightWalker[i][a] + "# #q" + NightWalker[i][a] + "##l\r\n";
                }
                msg += "\r\n\r\n";
            }
        } else if (cm.getPlayer().getJob() == 312) {
            var msg = "현재 당신의 직업은 #e<보우마스터>#n 입니다.\r\n나이트로드는 나이트워커의 스킬을 계승받을수 있습니다.\r\n\r\n";
            for (i = 0; i < NightWalker.length; i++) {
                if (i == 4) {
                    msg += "#e<하이퍼>#n\r\n";
                } else {
                    msg += "#e<" + (i + 1) + "차>#n\r\n";
                }
                for (a = 0; a < NightWalker[i].length; a++) {
                    msg += "#L" + NightWalker[i][a] + "# #s" + NightWalker[i][a] + "# #q" + NightWalker[i][a] + "##l\r\n";
                }
                msg += "\r\n\r\n";
            }
        } else if (cm.getPlayer().getJob() == 1412) {
            var msg = "#fs15#현재 당신의 직업은 #e<나이트워커>#n 입니다.\r\n나이트워커는 나이트로드의 스킬을 계승받을수 있습니다.\r\n\r\n";
            for (i = 0; i < NightRod.length; i++) {
                if (i == 4) {
                    msg += "#e<하이퍼>#n\r\n";
                } else {
                    msg += "#e<" + (i + 1) + "차>#n\r\n";
                }
                for (a = 0; a < NightRod[i].length; a++) {
                    msg += "#L" + NightRod[i][a] + "# #b#s" + NightRod[i][a] + "# #q" + NightRod[i][a] + "##k#l\r\n";
                }
                msg += "\r\n\r\n";
            }
        } else if (cm.getPlayer().getJob() == 222) {
            var msg = "#fs15#현재 당신의 직업은 #e<썬콜>#n 입니다.\r\썬콜(아크메이지는) 불독의 스킬을 계승받을수 있습니다.\r\n\r\n";
            for (i = 0; i < bulldog.length; i++) {
                if (i == 4) {
                    msg += "#e<하이퍼>#n\r\n";
                } else {
                    msg += "#e<" + (i + 1) + "차>#n\r\n";
                }
                for (a = 0; a < bulldog[i].length; a++) {
                    msg += "#L" + bulldog[i][a] + "# #b#s" + bulldog[i][a] + "# #q" + bulldog[i][a] + "##k#l\r\n";
                }
                msg += "\r\n\r\n";
            }
        } else {
            cm.dispose();
            msg = "해당 직업은 스킬 계승이 불가능합니다.";
        }
        cm.sendSimple(msg);
    } else if (status == 1) {
        cm.dispose();
        cm.getPlayer().changeSkillLevel(selection, 30, 30);
        cm.getPlayer().changeKeybinding(13, 1, selection);
        cm.getClient().getSession().writeAndFlush(CField.getKeymap(cm.getPlayer().getKeyLayout()));
        cm.sendOk("#s" + selection + "# #b[#q" + selection + "#]#k 스킬을 획득하셨습니다.\r\n모든 계승받은 스킬들은 #e[= 단축키]#n에 저장됩니다.");
    }
}