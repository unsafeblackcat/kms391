var importer = JavaImporter(Packages.scripting);
var status = -1;
var text;
var helper;

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
        cm.dispose();
	return;
    }
    if (mode == 1) {
        status++;
    }
    with(importer) {
		if (status == 0) {
			if (cm.getPlayer().hasGmLevel(6)) {
				helper = new Packages.scripting.NPCAutoWriterHelper(cm.getNpc(), cm.getClient());
				if (helper.checkFileExist()) {
					cm.sendOk("현재 선택하신 엔피시는 #b#e"+cm.getNpc()+"#k#r 입니다. 스크립트 문법에 오류가 생긴것으로 추정됩니다.");
					cm.dispose();
					return;
				}
				cm.sendYesNo("현재 선택하신 엔피시는 #b"+cm.getNpc()+"#k 입니다. 현재 스크립트가 존재하지 않으므로 단문장 엔피시를 만들 수 있습니다. 지금 만들어 보시겠어요?");
			} else {
				cm.dispose();
				return;
			}
		} else if (status == 1) {
			cm.sendGetText("엔피시를 클릭시 말하게 될 내용을 입력하세요. 도움이 되는 내용은 다음과 같습니다. \r\n\r\n#e#b엔피시 코드 : "+ cm.getNpc() +"\r\n"+helper.addInfo(cm.getNpc()));
		} else if (status == 2) {
			text = cm.getText();
			cm.sendYesNo("입력하신 내용은 다음과 같습니다. \r\n\r\n#b#e"+text+"#k#n\r\n\r\n정말 위와 같이 엔피시 대화를 설정하시겠습니까?");
		} else if (status == 3) {
			try {
				helper.doMain();
				helper.newLine();
				helper.newLine();
				helper.newLine();
				helper.writeLine("/*");
				helper.newLine();
				helper.newLine();
				helper.writeLine("	* 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.");
				helper.newLine();
				helper.newLine();
				helper.writeLine("	* (Guardian Project Development Source Script)");
				helper.newLine();
				helper.newLine();
				helper.writeLine("	"+cm.getPlayer().getName()+" 에 의해 만들어 졌습니다.");
				helper.newLine();
				helper.newLine();
				helper.writeLine("	엔피시아이디 : "+cm.getNpc()+"");
				helper.newLine();
				helper.newLine();
				helper.writeLine("	엔피시 이름 : "+helper.getNpcName());
				helper.newLine();
				helper.newLine();
				helper.writeLine("	엔피시가 있는 맵 : "+cm.getPlayer().getMap().getStreetName() + " : " + cm.getPlayer().getMap().getMapName() + " ("+cm.getPlayer().getMapId()+")");
				helper.newLine();
				helper.newLine();
				helper.writeLine("	엔피시 설명 : "+helper.getNpcFunc());
				helper.newLine();
				helper.newLine();
				helper.newLine();
				helper.writeLine("*/");
				helper.newLine();
				helper.newLine();
				helper.writeLine("var status = -1;");
				helper.newLine();
				helper.newLine();
				helper.writeLine("function start() {");
				helper.newLine();
				helper.writeLine("    status = -1;");
				helper.newLine();
				helper.writeLine("    action (1, 0, 0);");
				helper.newLine();
				helper.writeLine("}");
				helper.newLine();
				helper.newLine();
				helper.writeLine("function action(mode, type, selection) {");
				helper.newLine();
				helper.newLine();
				helper.writeLine("    if (mode == -1) {");
				helper.newLine();
				helper.writeLine("        cm.dispose();");
				helper.newLine();
				helper.writeLine("        return;");
				helper.newLine();
				helper.writeLine("    }");
				helper.newLine();
				helper.writeLine("    if (mode == 0) {");
				helper.newLine();
				helper.writeLine("        status --;");
				helper.newLine();
				helper.writeLine("    }");
				helper.newLine();
				helper.writeLine("    if (mode == 1) {");
				helper.newLine();
				helper.writeLine("        status++;");
				helper.newLine();
				helper.writeLine("    }");
				helper.newLine();
				helper.newLine();
				helper.writeLine("    if (status == 0) {");
				helper.newLine();
				helper.writeLine("        cm.sendOk(\""+text+"\");");
				helper.newLine();
				helper.writeLine("        cm.dispose();");
				helper.newLine();
				helper.writeLine("        return;");
				helper.newLine();
				helper.writeLine("    }");
				helper.newLine();
				helper.writeLine("}");
				helper.newLine();
				helper.closeFile();
				cm.sendOk("작업이 완료되었습니다.");
				cm.dispose();
			} catch (err) {
				cm.sendOk("작업에 실패했습니다!.."+err);
				cm.dispose();
			}
		}
        return;
    }
}

