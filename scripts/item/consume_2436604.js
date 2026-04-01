importPackage(java.sql);
importPackage(java.lang);
importPackage(Packages.database);
importPackage(Packages.handling.world);
importPackage(Packages.constants);
importPackage(java.util);
importPackage(java.io);
importPackage(Packages.client.inventory);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet);

asd = Randomizer.rand(1,5);
esc = "#fc0xFF4C4C4C#";
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
   if (status == 0) {
     cm.gainItem(2048717,asd);
     cm.gainItem(2436604,-1)
     cm.sendOk("#fs15##e"+asd+"[sss]#k#n\r\n\r\n#i2048717# #b#z2048717##k #r"+asd+"as# kd #n");
     cm.dispose();
   }
}