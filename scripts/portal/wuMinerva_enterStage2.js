var check = [4001822, 4001823, 4001824, 4001825, 4001826, 4001827, 4001828];
var box;

function enter(pi) {
    em = pi.getEventManager("MinervaPQ");
    if(check.length == 0){
        check = [4001822, 4001823, 4001824, 4001825, 4001826, 4001827, 4001828];
    }
        for(var i = 0; i<7; i++){
            box = Math.floor(Math.random() * check.length);
            em.setProperty("stage1r_q"+i, ""+check[box]);
            check.splice(box, 1);
        }
    em.setProperty("stage1r_clear", "0");
    pi.resetMap(933032000);
    pi.warpParty(933032000);
}