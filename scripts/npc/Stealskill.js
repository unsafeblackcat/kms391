/**
 * 제작 : sh
 */

importPackage(Packages.constants);
importPackage(Packages.client);
importPackage(Packages.server.Luna);
importPackage(Packages.main.world);
importPackage(Packages.packet.creators);
importPackage(Packages.launch.world);
 
 var GameConstants = Packages.constants.GameConstants;
 
 var enter = '\r\n';
 var reset = '#l#k';
 var IS_DEBUGGING = false;
 
 var status = -1;
 var selectedStealSkillSlot;
 var selectedJobIndex;
 var selectedSkillIndex;
 var selectedSkills;
 var chat
 var jobList = [];
 
 var Job = function (name, id) {
	 this.name = name
	 this.id = id;
 }
 var stealSkills = [];
 stealSkills[0] = [
	 1001005, //검사
	 2001008, 2001002, //매지션
	 3001004, //아처
	 3011004, //패파
	 4001334, 4001344, 4001005, 4001003, // 로그
	 5001002, 5001003, //해적
	 5011000, 5011001 //캐슈    
 ];
 
 stealSkills[1] = [
	 1101011, 1101006, //파이터
	 1201011, 1201012, 1201013, //페이지
	 1301011, 1301012, 1301006, 1301007, //스피어맨
	 2301005, 2301002, 2301004, //클레릭
	 2101004, 2101005, 2101001, 2100010, //불독
	 2201008, 2201005, 2201001, //썬콜
	 3101005, //헌터
	 3201005, 3201008, //사수
	 3301003, //패파
	 4101008, 4101010, //어쌔신
	 4201012, 4201004, 4201011, //시프
	 4301004, 4301003, //세미듀어러
	 4311002, 4311003, //듀어러
	 5201001, //건슬링거
	 5101012, 5101004, //인파이터
	 5301000, 5301001, 5301003 //캐논슈터
 ];
 
 stealSkills[2] = [
	 1111010, 1111012, 1111008, //크루세이더
	 1211008, 1211010, 1211012, 1211013, 1211014, 1211011, //나이트
	 1311011, 1311012, 1311015, //버서커
	 2311004, 2311011, 2311012, 2311002, 2311001, 2311003, 2311009, //프리스트
	 2111002, 2111003, 2111011, 2111008, //불독
	 2211002, 2211010, 2211012, 2211008, //썬콜
	 3111013, 3111003, //레인저
	 3211009, 3211008, 3211011, 3211012, //저격수
	 3311012, //패파
	 4111010, 4111015, 4111003, //허밋
	 4211011, 4211002, //시프마스터
	 4321006, 4321004, 4321002, //듀얼마스터
	 4331000, 4331011, 4331006, //슬래셔
	 5211008, 5211010, 5211007, //발키리
	 5111009, 5111012, 5111007, //버커니어? 누꼬
	 5311000, 5311010, 5311002, 5311004, 5311005 //캐논블래스터
 ];
 
 stealSkills[3] = [
	 1121016, //히어로
	 1221009, 1221004, 1221014, 1221011, 1221016, //팔라딘
	 1321013, 1321014, 1321012, //다크나이트
	 2321007, 2321008, 2321006, 2321005, //비숍
	 2121006, 2121003, 2121007, 2121011, //불독
	 2221006, 2221011, 2221007, 2221012, //썬콜
	 3121020, 3121014, 3121015, 3121002, //보우마스터 3121007 없는스킬
	 3221017, 3221007, 3221014, 3221002, //신궁 3221006 없는스킬
	 3321022, //패파
	 4121013, 4121017, 4121016, 4121015, //나로
	 4221014, //섀도어
	 4341002, 4341011, //듀블
	 5221004, 5221016, 5221015, 5221017, 5221013, 5221021, 5221018, //캡틴
	 5121007, 5121013, 5121015, 5121010, 5121009, //바퍼
	 5321000, 5321012, 5321001, 5321010 //캐논마스터
 ];
 
 stealSkills[4] = [
	 1121054, //히어로
	 1221054, //팔라딘
	 1321054, //다크나이트
	 2321054, //비숍
	 2121054, //불독
	 2221054, //썬콜   
	 3121054, //보마 
	 3221054, //신궁
	 3321034, //패파 
	 4121054, //나로
	 4221054, //섀도어
	 5221054, //캡틴
	 5121054, //바퍼
 ]
 
 
 var jobList = [];
 
 
 function start() {
	 //if(!cm.getPlayer().isGM()) return;
	 status = -1;
	 action(1, 0, 0);
 }
 
 function action(mode, type, selection) {
	 if (mode == 1) {
		 status++;
	 } else if (mode == -1 || mode == 0) {
		 cm.dispose();
		 return;
	 } else {
		 cm.dispose();
		 return;
	 }
 
	 chat = '#fs15#'
	 if (status == 0) {
		 chat += 'Hai！ 我是服務器\r\n幻影的鋼鐵技能管理！\r\n\r\n#r※ 部分無法或無法佩戴的技能請上報'
		 cm.sendSimple(chat)
	 } else if (status == 1) {
		 if (GameConstants.isPhantom(cm.getPlayer().getJob())) {
			 for (var i = 0; i < 5; i++) {
				 chat += '#L' + i + '#';
				 chat += getStealSkillSlotName(i);
				 chat += enter;
			 }
 
			 cm.sendSimple(chat)
		 } else {
			 chat += '只有幻影才能使用的功能。';
			 cm.dispose()
			 cm.sendOk(chat)
		 }
 
	 } else if (status == 2) { //직업출력        
		 if (selectedStealSkillSlot == null) {
			 selectedStealSkillSlot = selection;
		 }
 
		 chat += '請選擇想要劇照的職業群。' + enter
 
		 for (var i = 0; i < stealSkills[selectedStealSkillSlot].length; i++) {
			 var skillId = stealSkills[selectedStealSkillSlot][i];
			 var jobId = Math.floor(skillId / 10000);
			 if (jobList[jobList.length - 1] != jobId) {
				 chat += '#L' + jobList.length + '#'
				 chat += getJobNameById(jobId);
				 chat += enter;
 
				 jobList.push(jobId);
			 }
		 }
		 cm.sendSimple(chat)
	 } else if (status == 3) { //스킬목록출력
		 if (selectedJobIndex == null) {
			 selectedJobIndex = selection;
		 }
 
		 chat += '請選擇想要劇照的技能' + enter
 
		 var selectedJobId = jobList[selectedJobIndex];
		 for (var i = 0; i < stealSkills[selectedStealSkillSlot].length; i++) {
			 var skillId = stealSkills[selectedStealSkillSlot][i];
			 var jobId = Math.floor(skillId / 10000);
			 if (jobId == selectedJobId) {
				 chat += '#L' + i + '#';
				 chat += '#s' + skillId + '#'; //스킬 이미지
				 chat += SkillFactory.getSkillName(skillId); //스킬이름
				 chat += enter;
			 }
		 }
		 cm.sendSimple(chat);
	 } else if (status == 4) { //스킬넣을 슬롯선택
		 if (selectedSkillIndex == null) {
			 selectedSkillIndex = selection;
		 }
		 chat += '請選擇加入該技能的格' + enter;
		 var stolenSkillArray = getStoleanSkillArray();
		 for (var i = 0; i < GameConstants.getNumSteal(selectedStealSkillSlot + 1); i++) {
			 var stolenSkill = stolenSkillArray[selectedStealSkillSlot][i]; //.left, .right를 갖고있는 일종의 튜플
			 chat += '#L' + i + '#';
			 chat += (i + 1) + '第一格: '
			 if (stolenSkill != null) {
				 chat += '#s' + stolenSkill.left + '#'; //이미지출력
				 chat += SkillFactory.getSkillName(stolenSkill.left);
 
				 if (stolenSkill.right) {
					 chat += '(裝備中的技能)';
				 }
 
			 } else {
				 chat += '空';
			 }
			 chat += enter;
 
		 }
 
		 cm.sendSimple(chat)
 
	 } else if (status == 5) {
		 var skill = SkillFactory.getSkill(stealSkills[selectedStealSkillSlot][selectedSkillIndex]);
		 var tupleSlotSkill = getStoleanSkillArray()[selectedStealSkillSlot][selection]
		 var isUpdate = false;
		 var skillLevel = skill.getMaxLevel()
 
		 if (tupleSlotSkill == null || !tupleSlotSkill.right) {
			 var isreplaceSkill = tupleSlotSkill == null ? false : true;
 
			 chat += getStealSkillSlotName(selectedStealSkillSlot)
			 chat += ' ' + (selection + 1) + '在第1個插槽中 ' + enter
 
			 //대사처리
			 if (isreplaceSkill) {
				 chat += '#s' + tupleSlotSkill.left + '#';
				 chat += SkillFactory.getSkillName(tupleSlotSkill.left) + '已成功 ' + enter
			 }
 
			 chat += '#s' + skill.getId() + '#';
			 chat += SkillFactory.getSkillName(skill.getId());
			 chat += (isreplaceSkill ? '已更改為' : '已添加。');
 
			 //스킬추가부분
			 if (isreplaceSkill) {
				 cm.getPlayer().removeStolenSkill(tupleSlotSkill.left);
			 }
			 cm.getPlayer().addStolenSkill(skill.getId(), skillLevel);
			 isUpdate = true;
		 } else {
			 chat += '佩戴中的技能無法變更。'
	 cm.sendOk(chat)
		 }
 
		 if (isUpdate) {
			 cm.getPlayer().reloadChar()
		 }
 
		 //cm.sendOk(chat)
		 cm.dispose();
	 }
 
 }
 
 function getStoleanSkillArray() {
	 var stolenSkills = cm.getPlayer().getStolenSkills();
	 var stolenSkillIterator = stolenSkills.iterator();
	 var stolenSkillArray = [];
 
	 for (var i = 0; i < 5; i++) {
		 stolenSkillArray[i] = [];
	 }
 
	 while (stolenSkillIterator.hasNext()) {
		 var stolenSkill = stolenSkillIterator.next();
		 var skillJobLevel = GameConstants.getJobNumber(stolenSkill.left) - 1;
 
		 stolenSkillArray[skillJobLevel].push(stolenSkill);
	 }
	 return stolenSkillArray;
 }
 
 function getStealSkillSlotName(index) {
	 var name = ''
	 if (index == 4) {
		 name += '超技能';
	 } else {
		 name += (index + 1) + '搶奪技能';
	 }
	 return name;
 }
 
 
 ////////////
 function Color(a, r, g, b) {
	 var hexcode = ''
	 var alpha = 'FF';
	 var red;
	 var green;
	 var blue;
 
	 if (b != null) {
		 alpha = formattedHex(a);
		 red = formattedHex(r);
		 green = formattedHex(g);
		 blue = formattedHex(b);
 
	 } else if (g != null) {
		 red = formattedHex(a);
		 green = formattedHex(r);
		 blue = formattedHex(g);
	 }
 
	 if (red == null) {
		 hexcode = a;
	 } else {
		 hexcode = alpha + red + green + blue
	 }
 
	 return '#fc0x' + hexcode + '#'
 }
 
 function formattedHex(c) {
	 var hex = c.toString(16);
	 return hex.length == 1 ? '0' + hex : hex;
 }
 
 function FFColor(hexcode) {
	 return Color('FF' + hexcode);
 }
 
 function formattedMeso(meso) {
	 //억단위
	 var upperMeso = Math.floor(meso / 100000000);
	 var upperLeftMeso = meso % 100000000;
	 //1억 5천이면 아래에 5천이 남아있음 
	 //천만단위
	 var lowerMeso = Math.floor(upperLeftMeso / 10000);
	 var lowerLeftMeso = upperLeftMeso % 10000;
 
	 var mesoString = '';
 
	 if (upperMeso >= 1)
		 mesoString += upperMeso + '억';
	 if (lowerMeso > 0)
		 mesoString += lowerMeso + '만';
	 if (lowerLeftMeso > 0)
		 mesoString += lowerLeftMeso;
 
	 mesoString += '메소';
 
 
	 return mesoString;
 }
 
 function getIcon(name) {
	 var itemMap = new newMap();
	 itemMap.put('호감도', 3800452)
	 itemMap.put('쥐', 3801286)
	 itemMap.put('소', 3801287)
	 itemMap.put('호랑이', 3801288)
	 itemMap.put('토끼', 3801289)
	 itemMap.put('용', 3801290)
	 itemMap.put('말', 3801292)
	 itemMap.put('양', 3801293)
	 itemMap.put('원숭이', 3801294)
	 itemMap.put('닭', 3801295)
	 itemMap.put('개', 3801296)
	 itemMap.put('돼지', 3801297)
	 itemMap.put('메소', 3801305)
	 itemMap.put('위화살표', 3801306)
	 itemMap.put('아래화살표', 3801307)
	 itemMap.put('단풍잎', 3801309)
	 itemMap.put('코젬', 3801310)
	 itemMap.put('훈장', 3801311)
	 itemMap.put('주황버섯', 3801312)
	 itemMap.put('슬라임', 3801313)
	 itemMap.put('핑크빈', 3801314)
	 itemMap.put('예티', 3801315)
	 itemMap.put('페페', 3801316)
	 itemMap.put('돌의정령', 3801317)
 
	 return '#i' + itemMap.get(name) + '#'
 }
 
 
 
 function newMap() {
	 var map = {};
	 map.value = {};
	 map.getKey = function (id) {
		 return 'k_' + id;
	 };
	 map.put = function (id, value) {
		 var key = map.getKey(id);
		 map.value[key] = value;
	 };
	 map.contains = function (id) {
		 var key = map.getKey(id);
		 if (map.value[key]) {
			 return true;
		 } else {
			 return false;
		 }
	 };
	 map.get = function (id) {
		 var key = map.getKey(id);
		 if (map.value[key]) {
			 return map.value[key];
		 }
		 return null;
	 };
	 map.remove = function (id) {
		 var key = map.getKey(id);
		 if (map.contains(id)) {
			 map.value[key] = undefined;
		 }
	 };
 
	 map.getList = function () {
		 return map.value;
	 }
 
	 return map;
 }
 
 
 function sleep(delay) {
	 var start = new Date().getTime();
	 while (new Date().getTime() < start + delay);
 }
 
 function print(text) {
	 java.lang.System.out.println(text)
 }
 
 function getMonsterImage(mobId) {
	 return '#fMob/' + mobId + '.img/stand/0#'
 }
 
 function numberToKorean(number) {
	 var inputNumber = number < 0 ? false : number;
	 var unitWords = ['', '萬', '億', '趙', '京'];
	 var splitUnit = 10000;
	 var splitCount = unitWords.length;
	 var resultArray = [];
	 var resultString = '';
 
	 for (var i = 0; i < splitCount; i++) {
		 var unitResult = (inputNumber % Math.pow(splitUnit, i + 1)) / Math.pow(splitUnit, i);
		 unitResult = Math.floor(unitResult);
		 if (unitResult > 0) {
			 resultArray[i] = unitResult;
		 }
	 }
 
	 for (var i = 0; i < resultArray.length; i++) {
		 if (!resultArray[i]) continue;
		 resultString = String(resultArray[i]) + unitWords[i] + resultString;
	 }
 
	 return resultString;
 }
 
 function getDate() {
	 var data = new Date();
	 var month = data.getMonth() < 10 ? '0' + (data.getMonth() + 1) : (data.getMonth() + 1) + '';
	 var day = data.getDate() < 10 ? '0' + data.getDate() : data.getDate() + ''
	 var date = (data.getYear() + 1900) + '' + month + '' + day;
	 return date;
 }
 
 function getFamilarJobCode(jobCode) {
	 var jobList = [];
	 var jobCodeLength = jobCode.toString().length;
	 var baseJobCode = Math.floor(jobCode / 100) * 100;
	 var subJobCode = 1;
	 var minorJobCode = 2;
 
	 if (GameConstants.isDualBlade(jobCode)) {
		 minorJobCode = 4;
	 } else if (GameConstants.isEvan(jobCode)) {
		 minorJobCode = 8;
	 }
 
	 if (jobCodeLength == 3) {
		 subJobCode = parseInt(jobCode.toString()[1]);
	 } else {
		 jobList.push(baseJobCode);
	 }
 
	 jobList.push(baseJobCode)
	 jobList.push(baseJobCode + (subJobCode * 10));
	 for (var i = 1; i <= minorJobCode; i++) {
		 jobList.push(baseJobCode + (subJobCode * 10) + i);
	 }
 
	 return jobList;
 }
 
 function getJobNameById(job) {
	 switch (job) {
		 case 0:
			 return "초보자";
		 case 100:
			 return "검사";
		 case 110:
			 return "파이터";
		 case 111:
			 return "크루세이더";
		 case 112:
			 return "히어로";
		 case 120:
			 return "페이지";
		 case 121:
			 return "나이트";
		 case 122:
			 return "팔라딘";
		 case 130:
			 return "스피어맨";
		 case 131:
			 return "버서커";
		 case 132:
			 return "다크나이트";
		 case 200:
			 return "마법사";
		 case 210:
			 return "위자드(불,독)";
		 case 211:
			 return "메이지(불,독)";
		 case 212:
			 return "아크메이지(불,독)";
		 case 220:
			 return "위자드(썬,콜)";
		 case 221:
			 return "메이지(썬,콜)";
		 case 222:
			 return "아크메이지(썬,콜)";
		 case 230:
			 return "클레릭";
		 case 231:
			 return "프리스트";
		 case 232:
			 return "비숍";
		 case 300:
			 return "아처";
		 case 310:
			 return "헌터";
		 case 311:
			 return "레인저";
		 case 312:
			 return "보우마스터";
		 case 320:
			 return "사수";
		 case 321:
			 return "저격수";
		 case 322:
			 return "신궁";
		 case 400:
			 return "로그";
		 case 410:
			 return "어쌔신";
		 case 411:
			 return "허밋";
		 case 412:
			 return "나이트로드";
		 case 420:
			 return "시프";
		 case 421:
			 return "시프마스터";
		 case 422:
			 return "섀도어";
		 case 430:
			 return "세미듀어러";
		 case 431:
			 return "듀어러";
		 case 432:
			 return "듀얼마스터";
		 case 433:
			 return "슬래셔";
		 case 434:
			 return "듀얼블레이더";
		 case 500:
			 return "해적";
		 case 510:
			 return "인파이터";
		 case 511:
			 return "버커니어";
		 case 512:
			 return "바이퍼";
		 case 520:
			 return "건슬링거";
		 case 521:
			 return "발키리";
		 case 522:
			 return "캡틴";
		 case 800:
			 return "매니저";
		 case 900:
			 return "운영자";
		 case 1000:
			 return "노블레스";
		 case 1100:
		 case 1110:
		 case 1111:
		 case 1112:
			 return "소울마스터";
		 case 1200:
		 case 1210:
		 case 1211:
		 case 1212:
			 return "플레임위자드";
		 case 1300:
		 case 1310:
		 case 1311:
		 case 1312:
			 return "윈드브레이커";
		 case 1400:
		 case 1410:
		 case 1411:
		 case 1412:
			 return "나이트워커";
		 case 1500:
		 case 1510:
		 case 1511:
		 case 1512:
			 return "스트라이커";
		 case 2000:
			 return "레전드";
		 case 2100:
		 case 2110:
		 case 2111:
		 case 2112:
			 return "아란";
		 case 2001:
		 case 2200:
		 case 2210:
		 case 2211:
		 case 2212:
		 case 2213:
		 case 2214:
		 case 2215:
		 case 2216:
		 case 2217:
		 case 2218:
			 return "에반";
		 case 3000:
			 return "시티즌";
		 case 3200:
		 case 3210:
		 case 3211:
		 case 3212:
			 return "배틀메이지";
		 case 3300:
		 case 3310:
		 case 3311:
		 case 3312:
			 return "와일드헌터";
		 case 3500:
		 case 3510:
		 case 3511:
		 case 3512:
			 return "메카닉";
		 case 501:
			 return "해적(캐논슈터)";
		 case 530:
			 return "캐논슈터";
		 case 531:
			 return "캐논블래스터";
		 case 532:
			 return "캐논마스터";
		 case 2002:
		 case 2300:
		 case 2310:
		 case 2311:
		 case 2312:
			 return "메르세데스";
		 case 3001:
		 case 3100:
		 case 3110:
		 case 3111:
		 case 3112:
			 return "데몬슬레이어";
		 case 2003:
		 case 2400:
		 case 2410:
		 case 2411:
		 case 2412:
			 return "팬텀";
		 case 2004:
		 case 2700:
		 case 2710:
		 case 2711:
		 case 2712:
			 return "루미너스";
		 case 5000:
		 case 5100:
		 case 5110:
		 case 5111:
		 case 5112:
			 return "미하일";
		 case 6000:
		 case 6100:
		 case 6110:
		 case 6111:
		 case 6112:
			 return "카이저";
		 case 6001:
		 case 6500:
		 case 6510:
		 case 6511:
		 case 6512:
			 return "엔젤릭버스터";
		 case 3101:
		 case 3120:
		 case 3121:
		 case 3122:
			 return "데몬어벤져";
		 case 3002:
		 case 3600:
		 case 3610:
		 case 3611:
		 case 3612:
			 return "제논";
		 case 10000:
			 return "제로JR";
		 case 10100:
			 return "제로10100";
		 case 10110:
			 return "제로10110";
		 case 10111:
			 return "제로10111";
		 case 10112:
			 return "제로";
		 case 2005:
			 return "???";
		 case 2500:
		 case 2510:
		 case 2511:
		 case 2512:
			 return "은월";
		 case 14000:
		 case 14200:
		 case 14210:
		 case 14211:
		 case 14212:
			 return "키네시스";
		 case 15000:
		 case 15200:
		 case 15210:
		 case 15211:
		 case 15212:
			 return "일리움";
		 case 15001:
		 case 15500:
		 case 15510:
		 case 15511:
		 case 15512:
			 return "아크";
 
		 case 301:
		 case 330:
		 case 331:
		 case 332:
			 return "패스 파인더";
		 case 16000:
		 case 16400:
		 case 16410:
		 case 16411:
		 case 16412:
			 return "호영";
 
		 case 15002:
		 case 15100:
		 case 15110:
		 case 15111:
		 case 15112:
			 return "아델";
 
		 default:
			 return "알수없음";
	 }
 }