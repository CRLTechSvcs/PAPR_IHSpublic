var view={};
var reportView = {};

var strLen = 50;

require(["dijit/TitlePane","dojo/dom","dojo/domReady!"], function(dom) {

	document.getElementById('file-input')
	  .addEventListener('change', readSingleFile, false);

	dojo.xhrGet({
	        handleAs: 'json',
	        url: "/deaccessionSummary/getDeaccessionNewView",
	        preventCache: true,
	        load: function(data) {

	        	view = data;

				    /* AJE 2016-09-26 standard1 and standard2 should appear (toggle, see IFs below) on
				        app/views/deaccession_new_deaccession.scala.html */
	        	var standard1= '<h2 style="margin-top:0px;">Minimum Requirements to Meet Withdrawal Threshold</h2> '+
			                   '<p>Issues Held of Title\'s Run: <input type="text" value="'+ view.deaccessionIthacaView.HeldOfTitle +'" class="resizedTextbox" onkeyup=" updateField(\'HeldOfTitle\', this)">%</p>'+
			                   '<p>Number Page Verified Copies: <input type="text" value="'+ view.deaccessionIthacaView.PageVerifiedCopy +'"  class="resizedTextbox" onkeyup=" updateField(\'PageVerifiedCopy\', this)"></p>'+
					           '<p>Maximum Image/Page Ratio: <input type="text" value="'+ view.deaccessionIthacaView.ImagePageRatio +'"  class="resizedTextbox" onkeyup=" updateField(\'ImagePageRatio\', this)">%</p>';

			      document.getElementById('standard1').innerHTML = standard1;

              // AJE 2016-09-26 levelList gets plugged into standard2
	        	var levelList = '<select  onchange="updateLevel(this);">';
  			 	  for (var index = 0; index < view.deaccessionCrlView.ihsVerifiedView.length;  index++) {
  			 		 levelList += '<option value="' + view.deaccessionCrlView.ihsVerifiedView[index].id + '">' + view.deaccessionCrlView.ihsVerifiedView[index].name + '</option>';
  				  }
			 	    levelList += "</select>";

			      var standard2 = '<h2 style="margin-top:0px;">Preservation Criteria</h2>'+
              '<p>Issues in Good Condition:  <input type="text" value="'+ view.deaccessionCrlView.goodCondition +'"  class="resizedTextbox" onkeyup=" updateField(\'goodCondition\', this)"> </p>'+
              '<p>Issues Verified:'+
              levelList +
              '&nbsp;<input type="text" value="'+ view.deaccessionCrlView.verfiedCopy +'"  class="resizedTextbox" onkeyup=" updateField(\'verfiedCopy\', this)"></p>';

  			    document.getElementById('standard2').innerHTML = standard2;

            /* AJE 2016-09-26 reformatted radioTxt for better display
            var radiotTxt = '<br>&nbsp;Which of the Holdings to Include: ';
            radiotTxt += '<div style="width:150px;float:right;">';
            for(var index = 0;  index < view.commitmentView.length; index++){
            radiotTxt += '<input type="radio" name="include" value="'+view.commitmentView[index].id+'" onclick="updateCommitField(this);"/>'+ view.commitmentView[index].name+' <br>';
            }
            radiotTxt += '<input type="radio" name="include" value="0" checked onclick="updateCommitField(this);"/>All</div><br>';
            */

            var radioTxt = '<div style="float:left;">';
              radioTxt += 'Holdings to Include: <br />';
              radioTxt += '<div class="admin_form_alignment">';
              radioTxt += 'All: <input type="radio" name="include" value="0" checked onclick="updateCommitField(this);"/>';
            for(var index = 0;  index < view.commitmentView.length; index++){
              radioTxt += '&nbsp;&nbsp;' + view.commitmentView[index].name + ':&nbsp;';
              radioTxt += '<input type="radio" name="include" value="'+view.commitmentView[index].id+'" onclick="updateCommitField(this);" />';
              // AJE 2016-09-26 note there may be no function updateCommitField for the onclick, though we do have 'updateCommitFiled' [sic]
            }
            radioTxt += '</div></div>'; // one for the whole group, one for just radio buttons

            document.getElementById('commit').innerHTML = radioTxt;

	        },
	        error: function(e) {
	            alert("ihs_deaccession.js, getDeaccessionNewView Error: " + e.message);
	        }
	    });
});


function  updateField(field, obj){

	var value= obj.value;

	if(!$.isNumeric(obj.value)){
		obj.value = '';
		value=0;
	}

	if(field == 'HeldOfTitle'){
		if(value>100){
				obj.value='100'; value=100;
		}else if(value<0){
			obj.value='0'; value=0;
		}

		view.deaccessionIthacaView.HeldOfTitle = value;
	}

    if(field == 'PageVerifiedCopy'){

			view.deaccessionIthacaView.PageVerifiedCopy = value;

	}

    if(field == 'ImagePageRatio'){
    	if(value>100){
			obj.value='100'; value=100;
    	}else if(value<0){
    		obj.value='0'; value=0;
    	}
    	view.deaccessionIthacaView.ImagePageRatio = value;
	}

    if(field == 'verfiedCopy'){
    	 view.deaccessionCrlView.verfiedCopy = value;
	}

    if(field == 'goodCondition'){

		view.deaccessionCrlView.goodCondition =  value;

    }

    if(field == 'updateMinDeacc'){
    	if(value>100){
			obj.value='100'; value=100;
    	}else if(value<0){
    		obj.value='0'; value=0;
    	}
    	view.minDeaccess = value;
	}


    if(field == 'fileType'){
    	view.fileType = value;
	}

}

function updateCommitField(obj){
	view.committed=obj.value;
}

function updateJobname(obj){
	view.jobName=obj.value;

}

function updateGroupFlag(obj){
	view.groupFlag=obj.value;
}

function updatefileType(obj){
	view.fileType=obj.value;
}

function updateLevel(obj){
	view.deaccessionCrlView.varificationLevel =obj.value;
}



function deaccessionStandard(e) {
	var standard1 = document.getElementById('standard1');
	var standard2 = document.getElementById('standard2');

	if (e.target.value === 'standard1') {
		standard2.style.display = "none";
		standard1.style.display = "block";
		view.standard='Ithica';
	} else {
		standard1.style.display = "none";
		standard2.style.display = "block";
		view.standard='crl';
	}
}

function drawReport(response){

	 var showFlag = false;

	 reportView = JSON.parse(response);
	 var htmlCount='<p>Number of My Holdings Examined: '+reportView.NumberOfHolding +' Issues&emsp;|&emsp;Recommended for Deaccession: '
		+reportView.NumberOfDeaccession+' Issues&emsp;|&emsp;Recommended for Donation: '+reportView.NumberOfDonation +' Issues</p>';

	 document.getElementById('count').innerHTML = htmlCount;

	 var htmlIssue = '';


	 document.getElementById('titles').innerHTML ='';
	 var table1 =  document.createElement('table');
	 document.getElementById('titles').appendChild(table1);

	 for(var index = 0; index < reportView.deaccessionTitleView.length; index++){

		 ///var table1 =  document.getElementById('table1');

	     var row = table1.insertRow(index);
	     var divLeft = document.createElement('div');
	     var divRight = document.createElement('div');
	     divLeft.style.float="Left";
	     divRight.style.float="Right";

	 	 row.appendChild(divLeft);
		 row.appendChild(divRight);

		 var htmlIssue = '';

		 if(reportView.deaccessionTitleView[index].deaccessionIssueView.length > 0 ){


			if(reportView.deaccessionTitleView[index].deaccessionIssueView[0].action == 'd'){
				htmlIssue+='<input type="radio" id="ptitleid:'+ reportView.deaccessionTitleView[index].titleId +'" name="titlecls'+  reportView.deaccessionTitleView[index].titleId +'" onclick="deaccessionttilteAction(this,\'preserve\')"   />';
				htmlIssue+='<input type="radio" id="dtitleid:'+ reportView.deaccessionTitleView[index].titleId +'" name="titlecls'+  reportView.deaccessionTitleView[index].titleId +'" onclick="deaccessionttilteAction(this,\'deaccess\')"  checked/>';
				htmlIssue+='<input type="radio" id="ntitleid:'+ reportView.deaccessionTitleView[index].titleId +'" name="titlecls'+  reportView.deaccessionTitleView[index].titleId +'" onclick="deaccessionttilteAction(this,\'donate\')"  />';
		 	}else{
		 		htmlIssue+='<input type="radio" id="ptitleid:'+ reportView.deaccessionTitleView[index].titleId +'" name="titlecls'+  reportView.deaccessionTitleView[index].titleId +'" onclick="deaccessionttilteAction(this,\'preserve\')"   />';
				htmlIssue+='<input type="radio" id="dtitleid:'+ reportView.deaccessionTitleView[index].titleId +'" name="titlecls'+  reportView.deaccessionTitleView[index].titleId +'" onclick="deaccessionttilteAction(this,\'deaccess\')"  />';
				htmlIssue+='<input type="radio" id="ntitleid:'+ reportView.deaccessionTitleView[index].titleId +'" name="titlecls'+  reportView.deaccessionTitleView[index].titleId +'" onclick="deaccessionttilteAction(this,\'donate\')"  checked/>';
		 	}
		 	divLeft.innerHTML = htmlIssue;
		 	showFlag = true;
	 	 }

		 var tmptitle = '';

		 if(reportView.deaccessionTitleView[index].title.length > strLen +1 ){
				tmptitle = reportView.deaccessionTitleView[index].title.substring(0,strLen);
			}else {
				tmptitle = reportView.deaccessionTitleView[index].title;
			}

		 var title = tmptitle + ' &nbsp;&nbsp; | &nbsp;&nbsp; ISSN: ' + reportView.deaccessionTitleView[index].issn  + '  &nbsp;&nbsp; |  &nbsp;&nbsp; Holdings:  ' +  reportView.deaccessionTitleView[index].numberOfHolding ;

		 var contentId= 'content' + reportView.deaccessionTitleView[index].titleId;
		 var tablecontentId = 'table'+contentId;

		 var divcontent = '<div id="'+ contentId +'" >'+
		 				  '<div style="height:35px;margin-top:20px;">'+
		 				  '<div class="rotate-text">'+
		 				  	'Preserve'+
		 				  '</div>'+
		 				  '<div class="rotate-text" style="left:22px;top:-16px;">'+
		 				  	'Deaccess'+
		 				  '</div>'+
		 				  '<div class="rotate-text" style="left:42px;top:-32px;">'+
		 				  	'Donate'+
		 				  '</div>'+
		 				  '</div>'+
		 				  '<table id="'+ tablecontentId +'"></table></div>';

		 var tp = new dijit.TitlePane({
	            title: title,
	            content: divcontent,
	            style: "width: 710px;",
	            open: false
	      });

		 divRight.appendChild(tp.domNode);



		 for(var index1=0; index1 < reportView.deaccessionTitleView[index].deaccessionIssueView.length; index1++){

			 var contentHTML='';
			 tmpdeaccessionIssueView = reportView.deaccessionTitleView[index].deaccessionIssueView[index1];

			 var table2 =  document.getElementById(tablecontentId);
		     var row2 = table2.insertRow(index1);
		     row2.className = "issue held";


		     var divLeft2 = document.createElement('div');

		     divLeft2.id ="divholding"+tmpdeaccessionIssueView.holdingId;

		     divLeft2.style.width="680px";

		     divLeft2.style.height="20px"

		     if(tmpdeaccessionIssueView.action == 'd' ){
		    	 divLeft2.className = "timeline timeline-wanted";
		     }
		     else {
		    	 divLeft2.className = "timeline timeline-missing";
		     }

		     row2.appendChild(divLeft2);

			 var htmlIssue1 = '';

			 if(tmpdeaccessionIssueView.action == 'd' ){
				 htmlIssue1 +='<input data-title="'+ index +'" data-holding="'+ index1 +'" type="radio" id="pholding:'+ tmpdeaccessionIssueView.holdingId +'" name="holdingcls'+  tmpdeaccessionIssueView.holdingId +'" class="holdingcls title'+ reportView.deaccessionTitleView[index].titleId +'" onclick="deaccessionHoldingAction(this,\'preserve\')"   />';
				 htmlIssue1 +='<input data-title="'+ index +'" data-holding="'+ index1 +'" type="radio" id="dholding:'+ tmpdeaccessionIssueView.holdingId +'" name="holdingcls'+  tmpdeaccessionIssueView.holdingId +'" class="holdingcls title'+ reportView.deaccessionTitleView[index].titleId +'" onclick="deaccessionHoldingAction(this,\'deaccess\')"  checked/>';
				 htmlIssue1 +='<input data-title="'+ index +'" data-holding="'+ index1 +'" type="radio" id="nholding:'+ tmpdeaccessionIssueView.holdingId +'" name="holdingcls'+  tmpdeaccessionIssueView.holdingId +'" class="holdingcls title'+ reportView.deaccessionTitleView[index].titleId +'" onclick="deaccessionHoldingAction(this,\'donate\')"  />';
			 } else{
				 htmlIssue1 +='<input data-title="'+ index +'" data-holding="'+ index1 +'" type="radio" id="pholding:'+ tmpdeaccessionIssueView.holdingId +'" name="holdingcls'+  tmpdeaccessionIssueView.holdingId +'" class="holdingcls title'+ reportView.deaccessionTitleView[index].titleId +'" onclick="deaccessionHoldingAction(this,\'preserve\')"   />';
				 htmlIssue1 +='<input data-title="'+ index +'" data-holding="'+ index1 +'" type="radio" id="dholding:'+ tmpdeaccessionIssueView.holdingId +'" name="holdingcls'+  tmpdeaccessionIssueView.holdingId +'" class="holdingcls title'+ reportView.deaccessionTitleView[index].titleId +'" onclick="deaccessionHoldingAction(this,\'deaccess\')"  />';
				 htmlIssue1 +='<input data-title="'+ index +'" data-holding="'+ index1 +'" type="radio" id="nholding:'+ tmpdeaccessionIssueView.holdingId +'" name="holdingcls'+  tmpdeaccessionIssueView.holdingId +'" class="holdingcls title'+ reportView.deaccessionTitleView[index].titleId +'" onclick="deaccessionHoldingAction(this,\'donate\')"  checked />';
			 }
			 contentHTML += reportView.deaccessionTitleView[index].deaccessionIssueView[index1].volissue;

			 contentHTML += '&nbsp;&nbsp; | &nbsp;&nbsp;';

			 contentHTML += reportView.deaccessionTitleView[index].deaccessionIssueView[index1].date;

			 divLeft2.innerHTML=htmlIssue1 + contentHTML;

			 //if(tmpdeaccessionIssueView.)

		 }

	 }


	 document.getElementById('count').style.display = "block";

	 if(showFlag){
		 document.getElementById('issues').style.display = "block";
		 document.getElementById('submitReport').style.display = "block";
	 }else {
		 document.getElementById('issues').style.display = "none";
		 document.getElementById('submitReport').style.display = "none";
	 }


	 hideWaiting();
}

function deaccessionttilteAction(obj, action){

	var res = obj.id.split(":");
	$mvar = $('.holdingcls.title'+ res[1]);

	if(action == 'preserve'){
		for (var i=0; i<$mvar.length; i++)    {
			if($mvar[i].id.charAt(0) == 'p'){
				$mvar[i].checked=true;
				 document.getElementById('divholding'+$mvar[i].id.split(":")[1]).className = "timeline timeline-held";
			}
		}
	}

	if(action == 'deaccess'){
		for (var i=0; i<$mvar.length; i++)    {
			if($mvar[i].id.charAt(0) == 'd'){
				$mvar[i].checked=true;
				 document.getElementById('divholding'+$mvar[i].id.split(":")[1]).className = "timeline timeline-wanted";
			}
		}
	}

	if(action == 'donate'){
		for (var i=0; i<$mvar.length; i++)    {
			if($mvar[i].id.charAt(0) == 'n'){
				$mvar[i].checked=true;
				 document.getElementById('divholding'+$mvar[i].id.split(":")[1]).className = "timeline timeline-missing";
			}
		}
	}


}

function deaccessionHoldingAction(obj, action){

	if(action == 'preserve'){
		if(obj.id.charAt(0) == 'p'){
			document.getElementById('divholding'+obj.id.split(":")[1]).className = "timeline timeline-held";
		}
	}

	if(action == 'deaccess'){
		if(obj.id.charAt(0) == 'd'){
			document.getElementById('divholding'+obj.id.split(":")[1]).className = "timeline timeline-wanted";
		}
	}

	if(action == 'donate'){
		if(obj.id.charAt(0) == 'n'){
			document.getElementById('divholding'+obj.id.split(":")[1]).className = "timeline timeline-missing";
		}
	}
}

function submitReport(){

	if(view.jobName == ''){
		alert("Enter a JobName");
		return;
	}

	dojo.rawXhrPost({
        url: " /deaccessionSummary/postDeaccessionReport",
        postData: dojo.toJson(view),
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        handleAs: "text",

        load: drawReport,
        error: function(error) {
        	 hideWaiting();
            alert("ihs_deaccession.js, postDeaccessionReport Error:" + error);
        }
    });

    showWaiting();

}

function readSingleFile(e) {
	  var file = e.target.files[0];
	  if (!file) {
	    return;
	  }
	  var reader = new FileReader();
	  reader.onload = function(e) {
		  view.fileContend = e.target.result;
	  };
	  reader.readAsText(file);

}

function submitjob(){
	$mvar = $('.holdingcls');
	for (var i=0; i<$mvar.length; i++)    {
		var titleIndex = $mvar[i].getAttribute("data-title");
		var holdingIndex = $mvar[i].getAttribute("data-holding");
		if($mvar[i].checked == true){
			if($mvar[i].id.charAt(0) == 'p'){
				reportView.deaccessionTitleView[titleIndex].deaccessionIssueView[holdingIndex].action='p';
			}
			if($mvar[i].id.charAt(0) == 'd'){
				reportView.deaccessionTitleView[titleIndex].deaccessionIssueView[holdingIndex].action='d';
			}
			if($mvar[i].id.charAt(0) == 'n'){
				reportView.deaccessionTitleView[titleIndex].deaccessionIssueView[holdingIndex].action='n';
			}
		}
	}

	showWaiting();

	dojo.rawXhrPost({
        url: "/deaccessionSummary/postDeaccessionJob",
        postData: dojo.toJson(reportView),
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        handleAs: "text",

        load: function(response) {
       	 		hideWaiting();
       	 		window.location="/deaccession_finalized_job";
        	},
        error: function(error) {
        	 hideWaiting();
            alert("ihs_deaccession.js, postDeaccessionJob Error:" + error);
        }
    });

}