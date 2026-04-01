importPackage(Packages.constants);
importPackage(Packages.tools.packet);
importPackage(Packages.tools.packet);
importPackage(Packages.constants.programs);
importPackage(Packages.database);
importPackage(java.lang);
importPackage(Packages.handling.world)
importPackage(java.sql);
importPackage(java.util);
importPackage(java.lang);
importPackage(java.io);
importPackage(java.awt);
importPackage(Packages.database);
importPackage(Packages.constants);
importPackage(Packages.client.items);
importPackage(Packages.client.inventory);
importPackage(Packages.server.items);
importPackage(Packages.server);
importPackage(Packages.tools);
importPackage(Packages.server.Luna);
importPackage(Packages.packet.creators);
importPackage(Packages.client.items);
importPackage(Packages.server.items);
importPackage(Packages.launch.world);
importPackage(Packages.main.world);
importPackage(Packages.database.hikari);
importPackage(java.lang);
importPackage(Packages.handling.world);

var enter = "\r\n";
var 훈장 = [1140002, 1141002, 1142000, 1142001, 1142002, 1142003, 1142004, 1142009, 1142010, 1142011, 1142012, 1142013, 1142014, 1142015, 1142016, 1142017, 1142018, 1142019, 1142020, 1142021, 1142022, 1142023, 1142024, 1142025, 1142026, 1142027, 1142028, 1142029, 1142030, 1142031, 1142032, 1142033, 1142034, 1142035, 1142036, 1142037, 1142038, 1142039, 1142040, 1142041, 1142042, 1142043, 1142044, 1142045, 1142046, 1142047, 1142048, 1142049, 1142050, 1142051, 1142052, 1142053, 1142054, 1142055, 1142056, 1142057, 1142058, 1142059, 1142060, 1142061, 1142062, 1142063, 1142064, 1142065, 1142066, 1142067, 1142068, 1142069, 1142070, 1142071, 1142072, 1142073, 1142074, 1142075, 1142076, 1142077, 1142078, 1142079, 1142080, 1142081, 1142082, 1142083, 1142084, 1142085, 1142086, 1142087, 1142088, 1142089, 1142090, 1142091, 1142092, 1142093, 1142094, 1142095, 1142096, 1142097, 1142098, 1142099, 1142100, 1142101, 1142107, 1142108, 1142109, 1142110, 1142111, 1142112, 1142113, 1142114, 1142115, 1142116, 1142117, 1142118, 1142119, 1142120, 1142122, 1142123, 1142126, 1142134, 1142135, 1142136, 1142137, 1142138, 1142139, 1142140, 1142141, 1142142, 1142143, 1142149, 1142150, 1142151, 1142166, 1142187, 1142190, 1142191, 1142217, 1142218, 1142295, 1142296, 1142297, 1142298, 1142299, 1142300, 1142301, 1142305, 1142306, 1142307, 1142329, 1142334, 1142335, 1142360, 1142373, 1142406, 1142408, 1142441, 1142442, 1142443, 1142457, 1142511, 1142512, 1142543, 1142569, 1142573, 1142627, 1142656];
var 개수 = 20;

function start() {
    status = -1;
    action(1, 1, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
            var text = "#fs15#" + enter
            text += "#r#L1##d어떤 아이템들이 나오나요?!#k#l"
            text += "#r#L0##d네오 스톤 " + 개수 + "개로 훈장 뽑기#k#l"
            cm.sendSimple(text)
        } else if (status == 1) {
            if (selection == 1) {
                var amed = "#fs15#" + enter
                for (var i = 0; i < 훈장.length; i++) {
                    amed += "#i" + 훈장[i] + "# #z" + 훈장[i] + "#\r\n";
                }
                cm.sendOk(amed);
                cm.dispose();
            } else {
                if (cm.getPlayer().getNeoStone() < 개수) {
                    cm.sendOkS("#fs15##i4310300# #z4310300# " + (개수 - cm.getPlayer().getNeoStone()) + "개가 부족 합니다.", 2);
                    cm.dispose();
                } else {
                    cm.getPlayer().gainNeoStone(-개수);
                    획득 = "#fs15#" + enter + enter + "축하드립니다~! #h #님! 아래와 같은 아이템이 나왔어요!" + enter + enter + "#fUI/UIWindow2.img/QuestIcon/4/0#" + enter
                    훈장 = 훈장[Math.floor(Math.random() * 훈장.length)];
                    ItemInfo = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(훈장);
                    Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), ItemInfo, true);
                    cm.sendOk("#fs15#" + 획득 + "#i" + 훈장 + "# #z" + 훈장 + "#")
                    cm.dispose();
                }
            }
        }
    }
}