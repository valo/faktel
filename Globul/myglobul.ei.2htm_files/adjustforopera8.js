// JavaScript Document
function operaAdjustment() {
	var ua = navigator.userAgent.toLowerCase();
	if(/opera/.test(ua)) {
		var ind = ua.indexOf('opera');
		var theOpera = ua.substr(ind,ind+10);
		var mVersion = theOpera.match(/\d/g);
		if(mVersion[0] < 9) {
			var allDivs = document.getElementsByTagName('div');
			var hrs = new Array();
			var tools = new Array();
			for(var i = 0; i < allDivs.length; i++) {
				if(/hr/.test(allDivs[i].className)) {
					hrs.push(allDivs[i]);
				} else if(/tools/.test(allDivs[i].className)) {
					tools.push(allDivs[i]);
				}
			}
			if(hrs.length > 0) {
				for(i = 0; i < hrs.length; i++)	{
					hrs[i].style.top = hrs[i].parentNode.offsetHeight+'px';
				}
			}
			if(tools.length > 0) {
				for(i = 0; i < tools.length; i++)	{
					tools[i].style.top = tools[i].parentNode.offsetHeight+'px';
				}
			}
		}
	}
}