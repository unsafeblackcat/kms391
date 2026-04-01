var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status--;
    } else {
        status++;
    }

    if (status == 0) {
        // 获取角色当前血量和最大血量
        var player = cm.getPlayer();
        var hp = player.getStat().getHp();
        var maxHp = player.getStat().getMaxHp();
        
        // 计算血量百分比
        var hpPercent = Math.round((hp / maxHp) * 100);
        
        // 根据血量百分比生成不同的提示信息
        var statusMessage = "";
        if (hpPercent >= 90) {
            statusMessage = "状态极佳！";
        } else if (hpPercent >= 70) {
            statusMessage = "状态良好。";
        } else if (hpPercent >= 50) {
            statusMessage = "状态一般。";
        } else if (hpPercent >= 30) {
            statusMessage = "状态不佳，建议使用药水。";
        } else if (hpPercent >= 10) {
            statusMessage = "⚠️ 血量危急！请立即治疗！";
        } else {
            statusMessage = "⚠️ 生命垂危！马上使用急救药水！";
        }
        
        // 构建完整的消息并发送给玩家
        var message = "帳號.#b" + hp + "/" + maxHp + " (" + hpPercent + "%)#k。\r\n" + statusMessage;
        //cm.getPlayer().setKeyValue(1477, "count", "18888");
        cm.sendOk(message);
        cm.dispose();
    }
}