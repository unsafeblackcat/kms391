
/* 
검색성형, 검색헤어
*/

importPackage(java.util);
importPackage(java.lang);
importPackage(java.io);

importPackage(Packages.provider);
importPackage(Packages.tools);
importPackage(Packages.client);
importPackage(Packages.server);

var status = -1;
var choice;
var code;

var block = [0, 0]; //성형, 헤어 변경을 금지할 성형, 헤어코드 입력

function blockCheck(itemid) {
    for (var i = 0; i < block.length; i++) {
        if (itemid == block[i]) {
            return true;
        }
    }
    return false;
}

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode != 1) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        cm.sendSimple("#fs15#안녕하세요? 성형 헤어 검색에 오신걸 환영합니다!\r\n\r\n#b#L1#검색 헤어#l#L2#검색 성형#l");
    } else if (status == 1) {
        choice = selection;
        var text;
        if (choice == 1) {
            text = "헤어";
        } else if (choice == 2) {
            text = "성형";
        } else {
            cm.dispose();
            return;
        }
        cm.sendGetText("#fs15#검색#n#k 하고자 하는 #b" + text + "#n#k 이름#n#k 혹은 그 이름#n#k의 일부#n#k를 입력#n#k해 주세요.\r\n\r\n");
    } else if (status == 2) {
        if (cm.getText().length() < 2) {
            cm.sendOk("#e#r검색어#n#k가 #e#d너무#n#k 짧습니다. #e#r2자 이상#n#k으로 #e#b검색#n#k하시기 바랍니다.");
            cm.dispose();
            return;
        }           
        if (choice == 1) {
            if (cm.getText().contains("헤어") || cm.getText().contains("머리")) {
                cm.sendOk("#fs15##r#e헤어, 머리#n#k가 들어간 검색어는 검색#n#k할 수 없습니다.\r\n\r\n" +
                        "#e#dex) : (토벤 머리 -> 토벤) 으로 입력해주세요.");
                cm.dispose();
                return;
            }
            var text = "";
            var codysearch = MapleItemInformationProvider.getInstance().getAllItems().iterator();
            while(codysearch.hasNext()) {
                codystring = codysearch.next();
                //#fCharacter/Hair/000" + codystring.getLeft() + ".img/default/hair#   
                if (codystring.getRight().contains(cm.getText()) && parseInt(codystring.getLeft()%10) == 0 && 
                ((codystring.getLeft() >= 30000 && codystring.getLeft() < 50000) || (codystring.getLeft() >= 60000 && codystring.getLeft() < 70000))) {
                    text += "#e#b#L" + codystring.getLeft() + "#" + codystring.getRight() + "#l\r\n";
                }
            }
            if (text == "") {
                cm.sendOk("#e#b검색#n#k된 #e#r헤어#n#k가 없습니다.");
                cm.dispose();
                return;
            } else {
                cm.sendSimple("   #e#b검색#n#k된 #e#r헤어#n#k은 다음과 같습니다.\r\n" + text);
            }
        } else if (choice == 2) {
            if (cm.getText().contains("성형") || cm.getText().contains("얼굴")) {
                cm.sendOk("#fs15##r#e성형, 얼굴#n#k이 들어간 검색어는 검색#n#k할 수 없습니다.\r\n\r\n" +
                        "#e#dex) : (깜찍 얼굴 -> 깜찍) 으로 입력해주세요.");
                cm.dispose();
                return;
            }
            var text = "";
            var codysearch = MapleItemInformationProvider.getInstance().getAllItems().iterator();
            while(codysearch.hasNext()) {
                codystring = codysearch.next();
                //#fCharacter/Face/000" + codystring.getLeft() + ".img/default/face#   
                if (codystring.getRight().contains(cm.getText()) && parseInt(codystring.getLeft()%1000) < 100 && 
                ((codystring.getLeft() >= 20000 && codystring.getLeft() < 30000) || (codystring.getLeft() >= 50000 && codystring.getLeft() < 60000))) {
                    text += "#e#b#L" + codystring.getLeft() + "#" + codystring.getRight() + "#l\r\n";
                }
            }
            if (text == "") {
                cm.sendOk("#e#b검색#n#k된 #e#r성형#n#k이 없습니다.");
                cm.dispose();
                return;
            } else {
                cm.sendSimple("   #e#b검색#n#k된 #e#r성형#n#k은 다음과 같습니다.\r\n" + text);
            }
        } else {
            cm.dispose();
            return;
        }
    } else if(status == 3) {
        code = [selection];
        if (blockCheck(selection) == true) {
            cm.sendOk("해당 #e#r성형#n#k, #e#b헤어#n#k는 #e#d사용#n#k할 수 없습니다.");
            cm.dispose();
            return;
        }
        if (choice == 1) {
            cm.sendStyle("원하시는 헤어가 맞으면 확인을 눌러주세요", code);
        } else if (choice == 2) {
            cm.sendStyle("원하시는 성형이 맞으면 확인을 눌러주세요", code);
        } else {
            cm.dispose();
            return;  
        } 
    } else if (status == 4) {
        if (choice == 1) {
            cm.setHair(code[selection]);
            cm.dispose();
            return;
        } else if (choice == 2) {
            cm.setFace(code[selection]);
            cm.dispose();
            return;
        } else {
            cm.dispose();
            return;
        }
    } else {
        cm.dispose();
        return;
    }
}