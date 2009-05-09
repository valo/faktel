function showMessage(id) {
	var message = document.getElementById(id);
	message.style.display = "block";
}
function findTRs(){
	var i,trs;
	// loop through all trs of the document
	trs=document.getElementsByTagName('tr');
	for(i=0;i<trs.length;i++){
	// test if the class 'message' exists
		if(/message/.test(trs[i].className)){
			trs[i].style.display = "none";
			//alert(trs[i].childNode[0].nodeValue);
		}
	}
}