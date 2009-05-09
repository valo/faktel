function init(opened) {
	if(document.getElementById) {
		var temp = document.getElementById('outboxsmslist').getElementsByTagName('a');
		//alert(temp.length);
		for(var i = 0; i < temp.length; i++) {
			if(temp[i].className == 'contact') {
				//alert(temp[i].className);
				temp[i].onclick=function(){navigate(this); return false;};
			}
		}
		
		var divs = document.getElementById('outboxsmslist').getElementsByTagName('div');
		//alert(temp.length);
		for(var j = 0; j < divs.length; j++) {
			if (opened==1) {
				divs[j].style.display = 'block';
			}
			else if (opened==0) {
				divs[j].style.display = 'none';
			}
		}
	}
	
}

function navigate(o) {
		//alert(o.nextSibling.tagName + " " + o.nextSibling.className);
		var td = o.parentNode;
		//alert(td.tagName);
		//alert(td.childNodes.length);
		for(var i = 0; i < td.childNodes.length; i++) {
			//alert(td.childNodes[i].tagName);
			if(td.childNodes[i].tagName == 'DIV') {
				//alert(td.childNodes[i].style.display);
				
				if((/none/.test(td.childNodes[i].style.display))) {
					td.childNodes[i].style.display = 'block';
					//alert(td.childNodes[i].style.display);
				} else {
					td.childNodes[i].style.display = 'none';	
					//alert(td.childNodes[i].style.display);
				}
			}
		}
		
		var divs = document.getElementsByTagName('div');
		for(var j = 0; j < divs.length; j++) {
			if(divs[j].className == 'hr') {
				//alert(divs[j].parentNode.offsetHeight);	
				divs[j].style.top = divs[j].parentNode.offsetHeight + 'px';
			}
		}
}