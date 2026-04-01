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
      status --;
  }

  if (mode == 1) {
      status++;
  }

  if (status == 0) {
      cm.sendOk("#fs15#你好！ 我是可以給你換名字的 #b先生#k請問，有什麼可以幫您的嗎？\r\n\r\n#b#L0# 更改角色名稱\r\n#L1# 結束對話.");
  } else if (status == 1) {
      if (selection == 0) {
        cm.sendNameChangeWindow();
        cm.dispose();
      } else if (selection == 1) {
        cm.dispose();
      }
  }
}

