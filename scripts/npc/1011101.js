function start() {
    cm.sendYesNo("是否要重置所有每日任务？这将允许你重新完成今天的任务。");
}

function action(mode, type, selection) {
    if (mode == 1 && selection == 1) {
        time = new Date();
        year = time.getFullYear();
        month = time.getMonth() + 1;
        date2 = time.getDate() < 10 ? "0" + time.getDate() : time.getDate();
        date = year + "" + month + "" + date2;
        
        // 重置任务2的记录
        qnum = 2;
        cm.getPlayer().addKV("arcane_quest_" + qnum + "_" + date, -1);
        cm.getPlayer().getClient().setKeyValue("check_" + qnum + "_" + date, "0");
        cm.getPlayer().getClient().setKeyValue("compensation_" + qnum + "_" + date, "0");
        
        // 重置所有怪物的任务记录
        questlist = [
    [8642000, 3, "mob"],//鳳梨鹿兒 
    [8642001, 3, "mob"],//大角鳳梨鹿兒
    [8642002, 3, "mob"],//猶娜娜
    [8642003, 3, "mob"],//雷娜娜
    [8642004, 3, "mob"],//普利溫
    [8642005, 3, "mob"],//生氣的普利溫
    [8642006, 3, "mob"],//幼年烏普洛
    [8642007, 3, "mob"],//成熟的烏普洛
    [8642008, 3, "mob"],//綠鯰魚
    [8642009, 3, "mob"],//藍鯰魚
    [8642010, 3, "mob"],//拉伊托特
    [8642011, 3, "mob"],//隊長拉伊托特
    [8642012, 3, "mob"],//克利拉
    [8642013, 3, "mob"],//族長克利拉
    [8642014, 3, "mob"],//巴薩克
    [8642015, 3, "mob"],//族長巴薩克

        ];
        
        for (i = 0; i < questlist.length; i++) {
            clearquest(questlist[i][0]);
        }
        
        cm.sendOk("已重置所有每日任务，你现在可以重新完成它们了。");
    }
    cm.dispose();
}

function clearquest(paramint) {
    cm.getPlayer().addKV("river_" + paramint + "_count", -1);
    cm.getPlayer().addKV("river_" + paramint + "_mobq", -1);
    cm.getPlayer().addKV("river_" + paramint + "_itemq", -1);
    cm.getPlayer().addKV("river_" + paramint + "_isclear", -1);
}