function firm() {
	if(document.getElementById) {
		document.getElementById('juridical_login').style.display = 'none';
	}
	
}
function show(toshow) {
	if(document.getElementById) {
		var tohide = '';
		//alert(toshow);
		if(toshow == 'physical_login') tohide='juridical_login';
		if(toshow == 'juridical_login') tohide='physical_login';
		document.getElementById(toshow).style.display = 'block';
		document.getElementById(tohide).style.display = 'none';
	}
	
}