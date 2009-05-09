     
    function redirect(page) {
		location.replace(page);
	}

	function dispatch(direction) {
		document.location.href=direction;
	}

	function dispatch_url(url) {
	    if( url.indexOf("http://") > 0 ){
            //window.open( url.substring( url.indexOf("http://") , url.indexOf("?") ) );
            window.open( url.substring( url.indexOf("http://")  ) );
	    } else {
            document.location.href=url;
        }
	}

    
    
    function URLencode(sStr) {
        return escape(sStr).
                 replace(/\+/g, '%2B').
                    replace(/\"/g,'%22').
                       replace(/\'/g, '%27').
                         replace(/\//g,'%2F');
    }
    

	function tab_logoff(url,disp) {

		document.location.href=disp+'myglobul.portal?action=logoff&url='+url;
	}

    function logoff(url) {
		document.location.href='/einvoice/logoff.jsp?url='+url;
	}

	function login_submit(){
	    if(document.forms[0].username.value == ""){
	        alert("Моля , въведе Вашето потрбителско име !");
	        document.forms[0].username.focus();
	        return false;
	    }

	    if(document.forms[0].password.value == ""){
	        alert("Моля , въведе Вашетa парола !");
	        document.forms[0].password.focus();
	        return false;
	    }
	    document.forms[0].submit();
	}

	function standart_submit(aUrl){
        document.forms[0].submit;
	}

	function open_file( file_type , file_name , context_path, lower_bound, upper_bound , inv_no , bill_acc_id , custnum){
        if(! inv_no)    inv_no=0;
        if(! bill_acc_id) bill_acc_id=0;
        if(! custnum)   custnum = "";
       
        var url = context_path + "/einvoice/ExportServlet"+
                 "?file_name="+ file_name +
                 "&file_type="+ file_type +
                 "&lower_bound="+ lower_bound +
                 "&upper_bound="+ upper_bound +
                 "&inv_no="+ inv_no +
                 "&bill_acc_id="+bill_acc_id+
                 "&custnum="+custnum;
	     window.open(url);
	}

	function open_pdf_file( file_name , context_path, bill_acc_id , custnum){
       
	   
    var  url= context_path + "/einvoice/ExportServlet"+
                  "?file_name="+ file_name +
                  "&file_type=pdf"+
                  "&lower_bound=0"+
                  "&upper_bound=0"+
                  "&bill_acc_id="+ bill_acc_id+
                  "&custnum="+ custnum+
                  "&inv_no=0";
     window.open(url);
	}

	function export_period_OnChange(contextpath){
        urllaction = contextpath+ "/myglobul.ei?action=exports&pkey=127&jkey=129";
	    document.main.action = urllaction;
	    document.main.submit();
	}

	function bill_accounts_OnChange(){
	    document.main.action = "einvoice/exports/exports_main.jsp";
	    document.main.submit();
	}

	function chkAll_OnClick(flag){
	    if(document.forms[2].chkFiles){
            if(document.forms[2].chkFiles.length){
	            for(i=0; i<document.forms[2].chkFiles.length; i++){
                    document.forms[2].chkFiles[i].checked = flag;
	            }
            }else{
	            document.forms[2].chkFiles.checked = flag;
	        }
	    }
	}

	function cmdExportNumbers_OnClick(url_action){

	    if(! isChekedFile()){
	        alert("Внимание !\nЗа да изпълните операция експорт, трябва да е избран поне един файл !")
	        return;
	    }



	    document.forms[2].action = url_action;
	    document.forms[2].submit();
	}
    
	function export_one_number(contextpath, num){
        window.open(contextpath+"/myglobul.ei?action=export_by_msisdn&chkFiles="+ num);
	}

	function export_one_number_by_details( contextpath, val, num ){
        
        if(val==0){
            alert("Моля, изберете телефонен номер !");
            return;
        }

        num = '35989' + num.substring(4);
        export_one_number(contextpath, num);
    }
    
    function export_one_number_by_details( contextpath, val, num, cust_acc_id, custcode10, bill_acc_id, custnum ){
        if(val==0){
            alert("Моля, изберете телефонен номер !");
            return;
        }
        num = '35989' + num.substring(4);
        window.open(contextpath+"/myglobul.ei?action=export_by_msisdn&chkFiles="+ num + "&msisdn_id=" + val +  "&cust_acc_id=" + cust_acc_id + "&custcode10=" + custcode10 + "&bill_acc_id="+bill_acc_id + "&custnum=" +custnum );
    }
    
	function isChekedFile(){
	    if(!document.forms[2].chkFiles) return false;
        if(!document.forms[2].chkFiles.length) return document.forms[2].chkFiles.checked
        for(i=0; i<document.forms[2].chkFiles.length; i++){
            if(document.forms[2].chkFiles[i].checked == true){
                return true;
            }
        }
        return false;
	}

	function link_submit(billacc,inv_no){
	    document.main.action  = "invoice.jsp";
	    document.main.billacc.value = billacc;
	    document.main.inv_no.value = inv_no;
	    document.main.submit();
	}

    function RedirectBack(path,pkey, jkey){
     var newlocation= path + "?pkey=" + pkey + "&jkey="+jkey;
     document.location.href=newlocation;
     return true;
  }
    function RedirectAction(path){
     var newlocation= path;
     document.location.href=newlocation;
     return false;
  }
     function Redirect2Help(path,pkey, jkey,faqID){
     var newlocation;
     if(path==("/mg/myglobul.portal?action=faq"))
     {
         newlocation= path + "&pkey=" + pkey + "&jkey="+jkey+"&faqID="+faqID;
     }else{
          newlocation= path + "?pkey=" + pkey + "&jkey="+jkey+"&faqID="+faqID;
     }
     document.location.href=newlocation;
     return true;
  }
    function cmdChData_onclick(){
        /* REMOVED - MNP 10.2007
        if(document.main.gsm.value != "" && (document.main.gsm.value.replace(" ","").length < 7 || isNaN(document.main.gsm.value)) ){
		    alert("Моля, въведете седемте цифри след код 089 на телефонния номер !\r")
		    document.main.gsm.select();
		    document.main.gsm.focus();
		    return false ;
	    }
        if(document.main.gsm.value != "" && 
            (document.main.gsm.value.charAt(0).search("4") == -1) &&
            (document.main.gsm.value.charAt(0).search("5") == -1) &&
            (document.main.gsm.value.charAt(0).search("6") == -1) &&
	        (document.main.gsm.value.charAt(0).search("7") == -1) &&
		    (document.main.gsm.value.charAt(0).search("8") == -1) &&
		    (document.main.gsm.value.charAt(0).search("9") == -1) ){
	   	    alert("Моля, въведете валиден телефонен номер !\r")
		    document.main.gsm.select();
		    document.main.gsm.focus();
		    return false ;
	    }
        */
	
        if(document.main.email.value != "" && echeck(document.main.email.value) == false){
            alert("Моля, въведете валиден адрес !\r")
		    document.main.email.select();
		    document.main.email.focus();
		    return false;
	    }


	    if( document.main.gsmnotif.checked == true &&  document.main.gsm.value==""  ){
            alert("Моля, въведете телефонен номер на който да получите съобщение !\r")
		    document.main.gsm.focus();
		    return false;
	    }

	    if( document.main.emailnotif.checked == true &&  document.main.email.value==""  ){
            alert("Моля, въведете адрес за елетронна поща на който да получите съобщение !\r")
            document.main.email.focus();
            return false;
	    }
        
	    return true;
    }

    function echeck(str) {

        var at="@"
        var dot="."
        var lat=str.indexOf(at)
        var lstr=str.length
        var ldot=str.indexOf(dot)
        if (str.indexOf(at)==-1){
        //alert("Invalid E-mail ID")
        return false
        }

        if (str.indexOf(at)==-1 || str.indexOf(at)==0 || str.indexOf(at)==lstr){
        //alert("Invalid E-mail ID")
        return false
        }

        if (str.indexOf(dot)==-1 || str.indexOf(dot)==0 || str.indexOf(dot)==lstr){
        //alert("Invalid E-mail ID")
        return false
        }

        if (str.indexOf(at,(lat+1))!=-1){
        //alert("Invalid E-mail ID")
        return false
        }

        if (str.substring(lat-1,lat)==dot || str.substring(lat+1,lat+2)==dot){
        //alert("Invalid E-mail ID")
        return false
        }

        if (str.indexOf(dot,(lat+2))==-1){
        //alert("Invalid E-mail ID")
        return false
        }

        if (str.indexOf(" ")!=-1){
        //alert("Invalid E-mail ID")
        return false
        }

        return true
    }

    function cmdCreateMsisdnUser(flag){
        if ( flag == "add" ) {

            if(document.main.username.value == "" ) {
                alert("Моля , попълнете полето !");
                document.main.username.focus();
                return false;
            }

            if(document.main.username.value.length < 6 ) {
                alert("Потребителското трябва да бъде не по-малко от шест символа !");
                document.main.username.select();
                document.main.username.focus();
                return false;
            }

            if(document.main.pasw1.value == document.main.username.value) {
                alert("Потребителското име и паролата трябва да са различни !");
                document.main.pasw1.value="";
                document.main.pasw2.value="";
                document.main.pasw1.focus();
                return false;
            }
	    }

	    if(document.main.admin_pasw){
            if( document.main.admin_pasw.value == "") {
                alert("Моля , попълнете полето !");
                document.main.admin_pasw.focus();
                return false;
            }
	    }

	    if(document.main.pasw1.value == "") {
	        alert("Моля , попълнете полето !");
	        document.main.pasw1.focus();
	        return false;
	    }

	    if(document.main.pasw1.value.length < 6) {
	        alert("Паролата трябва да бъде не по-малка от шест символа !");
	        document.main.pasw1.select();
	        document.main.pasw2.value="";
	        document.main.pasw1.focus();
	        return false;
	    }

        if(document.main.pasw2.value == "") {
            alert("Моля , попълнете полето !");
        	document.main.pasw2.focus();
        	return false;
        }

        if(document.main.pasw1.value != document.main.pasw2.value) {
	        alert("Моля , въведете една и съща парола в двете полета !");
	        document.main.pasw1.value="";
	        document.main.pasw2.value="";
	        document.main.pasw1.focus();
	        return false;
	    }


        return true;

    }

	function cmdChPass_onclick(){
	    if(document.main.oldpass.value == "") {
	        alert("Моля , попълнете полето !");
	        document.main.oldpass.focus();
	        return false;
	    }

	    if(document.main.pasw1.value == "") {
	        alert("Моля , попълнете полето !");
	        document.main.pasw1.focus();
	        return false;
	    }

	    if(document.main.pasw1.value.length < 6) {
	        alert("Паролата трябва да бъде не по-малка от шест символа !");
	        document.main.pasw1.select();
	        document.main.pasw1.focus();
	        return false;
	    }

        if(document.main.pasw2.value == "") {
            alert("Моля , попълнете полето !");
        	document.main.pasw2.focus();
        	return false;
        }

        if(document.main.pasw1.value != document.main.pasw2.value) {
	        alert("Моля , въведете една и съща парола в двете полета !");
	        document.main.pasw2.value="";
	        document.main.pasw1.value="";
	        document.main.pasw1.focus();
	        return false;
	    }

        return true;
	}

	function call_type_OnChange(id,subs_id){
	    document.location.href = "cdetails2.jsp?subs="+subs_id+"&call_type="+id;

	}

	function filter_period_OnChange(http_vars, submit){
        // alert(http_vars);
        if( submit==1 ){
	        document.forms[0].action = http_vars;
	        document.forms[0].submit();
	    }else{
            document.location.href = http_vars;
	    }
    }

    function selector_OnChange(val){


        if( document.filters.period ) {
            val += "?period=" + document.filters.period.value;
            if( document.filters.billacc ) val += "&billacc=" + document.filters.billacc.value;
        } else {
            if( document.filters.billacc ) val += "?billacc=" + document.filters.billacc.value;
        }
        document.location.href = val ;
    }

    function billacc_numbers(billacc , inv_no){
        document.main.action = "numbers.jsp";
        document.main.billacc.value = billacc;
        document.main.inv_no.value = inv_no;
        document.main.submit();
    }
    
    function number_bill(billacc , subs_id , msisdn){
        
        //REMOVED MNP 10.2007 
        //msisdn                =   "35989" + msisdn.substr(4);
        
        document.location.href  =   "numberbill.jsp?billacc="+ billacc +
                                    "&subs="+ subs_id +
                                    "&msisdn="+ msisdn;
    }

    function number_bill2( aHref, billacc , subs_id , msisdn){
        
        //REMOVED MNP 10.2007        
        //msisdn                =   "35989" + msisdn.substr(4);
        
        document.location.href  =   aHref   + 
                                    "&billacc="+ billacc +
                                    "&subs="+ subs_id +
                                    "&msisdn="+ msisdn;
    }

    function chkBillAcc_onclick(val , child_chk ){
        if(child_chk.length){
            for(j=0; j< child_chk.length ; ++j){
                child_chk[j].checked = val;
            }
        } else {
            child_chk.checked = val;
        }
        if(val==false){
            document.main.chkall.checked = false;
        }
    }

    function chkMsisdn_onclick(val,parent_chk){
        if(val==false){
            parent_chk.checked=false;
            document.main.chkall.checked = false;
        }
    }

    function chkall_onclick(val , fform){
        for(i=0; i<fform.elements.length ; ++i){
            if(fform.elements[i].type == "checkbox" && fform.elements[i].name != "chkall"){
               fform.elements[i].checked = val;
            }
        }
    }

    /*
    function chkCustCode_onclick(val , child_chk ){
        if(child_chk.length){
            for(j=0; j< child_chk.length ; ++j){
                child_chk[j].click();
                child_chk[j].checked = val;

            }
        } else {
            child_chk.click();
            child_chk.checked = val;


        }
    }
    */


    function create_user( url, bill_acc_id, msisdn_id , msisdn ){
        document.main.action = url;
        document.main.bill_acc_id.value = bill_acc_id;
        document.main.msisdn_id.value = msisdn_id;
        document.main.msisdn.value = msisdn;
        document.main.submit();
    }

    function status_user( url, user_id ){
        document.main.action = url;
        document.main.user_id.value = user_id;
        document.main.submit();
    }

    function edit_user( url, cust_acc_id, bill_acc_id, msisdn_id , msisdn ){
        var tempurl;
        tempurl=url+"&cust_acc_id="+cust_acc_id+"&bill_acc_id="+bill_acc_id+"&msisdn_id="+msisdn_id+"&msisdn="+msisdn;
        document.location.href=tempurl;
    }

    function ch_pass( url, bill_acc_id, msisdn_id , msisdn , user_id ){
        document.main.action=url;
        document.main.bill_acc_id.value = bill_acc_id;
        document.main.msisdn_id.value = msisdn_id;
        document.main.msisdn.value = msisdn;
        document.main.user_id.value = user_id;
        document.main.submit();
    }

    function rem_user( url , fullmsisdn ){
        if( confirm("Сигурни ли сте че искате да изтриете акаунта ?") ){
            document.main.action = url;
            document.main.fullmsisdn.value = fullmsisdn;
            document.main.submit();
        }
    }

    // EDIT USER RIGHTS
    function chkViewAll_onclick(val , fform ){
        for(i=0; i<fform.elements.length ; ++i){
            if(fform.elements[i].type == "checkbox" && fform.elements[i].name != "chkViewAll" && fform.elements[i].name.substr(0,7) == "chkView" && fform.elements[i].disabled==false){
               fform.elements[i].checked = val;
            }
        }
    }

    function chkAdminAll_onclick(val , fform ){
         if(!fform.elements){
        }else{  
        for(i=0; i<fform.elements.length ; ++i){
            if(fform.elements[i].type == "checkbox" && fform.elements[i].name != "chkAdminAll" && fform.elements[i].name.substr(0,8) == "chkAdmin" && fform.elements[i].disabled==false){
               fform.elements[i].checked = val;
            }
        }
        }
    }

    // VIEW CUST_CODE
    function chkCustCode_onclick( val , fform, id, chkName ){
        if(fform.elements){
            for(i=0; i<fform.elements.length ; ++i){
                if( fform.elements[i].type == "checkbox" && 
                    fform.elements[i].name.indexOf(chkName) == 0 &&
                    fform.elements[i].name.indexOf(id) >=0 ){
                
                    fform.elements[i].checked = val;
                    fform.elements[i].disabled= val;
                }
            }
        }
    }
    
    // VIEW OR ADMIN BILL_ACC
    function chkView_AdminBillAcc_onclick(val , child_chk ){
        if(child_chk){
            if(child_chk.length){
                for(j=0; j< child_chk.length ; ++j){
                    child_chk[j].checked = val;
                    child_chk[j].disabled = val;
                }
            } else {
                child_chk.checked = val;
                child_chk.disabled = val;
            }
        }
    }

    function cmdSaveRigths_onclick(fform){
        
        for(i=0; i<fform.elements.length ; ++i){
            if(fform.elements[i].type == "checkbox"){
                if( fform.elements[i].checked==true && fform.elements[i].disabled==false && fform.elements[i].name.substring(0,13) == "chkViewMsisdn" ){
                    if(fform.eu_view_msisdn.value != "")    fform.eu_view_msisdn.value = fform.eu_view_msisdn.value + "," + fform.elements[i].value;
                    else fform.eu_view_msisdn.value = fform.elements[i].value;
                } else if( fform.elements[i].checked==true && fform.elements[i].disabled==false && fform.elements[i].name.substring(0,14) == "chkAdminMsisdn" ){
                    if(fform.eu_admin_msisdn.value != "")   fform.eu_admin_msisdn.value = fform.eu_admin_msisdn.value + "," + fform.elements[i].value;
                    else fform.eu_admin_msisdn.value = fform.elements[i].value;
                }
                
                if( fform.elements[i].checked==true && 
                    fform.elements[i].disabled==false && 
                    fform.elements[i].name.substring(0,14) == "chkViewBillAcc" ){
                    
                    if(fform.eu_view_billacc.value != "")    
                        fform.eu_view_billacc.value = fform.eu_view_billacc.value + "," + fform.elements[i].value;
                    else 
                        fform.eu_view_billacc.value = fform.elements[i].value;
                        
                } else if( fform.elements[i].checked==true && 
                           fform.elements[i].disabled==false && 
                           fform.elements[i].name.substring(0,15) == "chkAdminBillAcc" ){
                           
                    if(fform.eu_admin_msisdn.value != "")   
                        fform.eu_admin_billacc.value = fform.eu_admin_billacc.value + "," + fform.elements[i].value;
                    else 
                        fform.eu_admin_billacc.value = fform.elements[i].value;
                }
                
            }
        }
        // REMOVED - 01.2008 ( Execusion caused redirect error on production servers !!!)
        //urllaction = "myglobul.ei?action=save_priv";
        //fform.action = urllaction; 
        fform.submit();
    }

    function cmdSaveHotRigths_onclick(){
        var flag = false;
        for(i=0; i<document.main.elements.length ; ++i){
            if(document.main.elements[i].type == "checkbox"){
                if( document.main.elements[i].checked==true && document.main.elements[i].disabled==false && document.main.elements[i].name.substring(0,9) == "chkMsisdn" ){
                    if(document.main.eu_msisdn.value != "")    document.main.eu_msisdn.value = document.main.eu_msisdn.value + "," + document.main.elements[i].value;
                    else document.main.eu_msisdn.value = document.main.elements[i].value;
                    flag=true;
                }
            }
        }

        if(!flag || document.main.hotkeys.value==0){
            alert("Моля, изберете операция за задаване на превилегии и поне един телефонен номер !");
            return false;
        }

        if(document.main.hotkeys.value==4 || document.main.hotkeys.value==7){
            if(confirm("Внимание!\nОперацията ще предизвика отнемане на права!\nСигурни ли сте, че желаете да продължите?")){
                document.main.action.value="save_hot_priv";
                document.main.submit();
            }else{
                return false;
            }
        } else {
            document.main.action.value="save_hot_priv";
            document.main.submit();
        }
    }

    function show_help(aHTML){

        var aHTMLTitle = "<table border='0' cellspacing='0' cellpadding='0' width='100%'>"+
                            "<tr>"+
                                "<td align='left'>Електронна фактура - помощ</td>"+
                                "<td align='right'><a href='javascript:hide_help();'>[x]&nbsp;&nbsp;</td>"+
                            "</tr>"
                        "</table>";

        overlib_pagedefaults(WIDTH,300,FGCOLOR,'#ffffcc',BGCOLOR,'#666666');


        aHTML = "<table border='0' cellspacing='5' cellpadding='5'>"+
				    "<tr>"+
					    "<td align='center'>Съдържание</td>"+
					"</tr>"+
					"<tr>"+
					    "<td align='right'></td>"+
					"</tr>"
                "</table>";


        overlib(aHTML , CAPTION, aHTMLTitle , TEXTSIZE, '9px' , WIDTH , '450', HEIGHT , '300' , FIXX , '0' , FIXY, '0' , TEXTFONT, 'Verdana'); //Times Roman

    }

    function hide_help(){
        nd();
    }

    // DE/SELEKTIRANE NA WSICHKI CHEKBOX kontroli of forma
    function select_all(chk_item, chk_array ){
        if( chk_array ){
            if(chk_array.length){
                for(i=0; i < chk_array.length; ++i){
                    chk_array[i].checked = chk_item.checked;
                }
            } else {
                    chk_array.checked = chk_item.checked;
            }
        }
    }

    function select_one(chk_item, chk_array ){
        var d = true;
        if( chk_array ){
            if(chk_array.length){
                for(i=0; i < chk_array.length; ++i){
                   d = d && chk_array[i].checked;
                }
                chk_item.checked = d;
            } else {
                    chk_item.checked = chk_array.checked;
            }
        }
    }



    function MM_openBrWindow(theURL,winName,features) { //v2.0
        remote = window.open(theURL,winName,features);
        if (remote.opener == null) remote.opener = window;
            remote.opener.name = "opener";
    }
    
    function MM_openBrWindow_max(theURL,winName,features) { //v2.0
        w=screen.availWidth;
        h=screen.availHeight;
        window.open(theURL,winName,'width='+w+',height='+h+',left=0,top=0,'+features);
    }
    
    var myWin = null;
    function open_media( file, name, media, level ){
                    
//        if( media.match("r") == null && media.match("p") == null ) {
//            return;
//        }
                
        //if( null!=myWin ){
        //    myWin.location.href = level + '../media/listen.php?file='+ file +'&media='+ media +'&name='+ name;
        //    myWin.focus();
        //} else {
        
        myWin = window.open( level + '../media/listen.php?file='+ file +'&media='+ media +'&name='+ name,'x','width=260,height=137,status=no,titlebar=no,scrollbars=no,resizable=no'); 
        myWin.focus();
        
        //}     
    }
    
