var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {

        cm.getPlayer().setKeyValue(1477, "count", "18888");
        cm.sendOk("共發送了18888核心碎片");
        cm.dispose();
    }
