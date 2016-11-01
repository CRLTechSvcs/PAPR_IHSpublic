var groupView={}
groupView.groupName='';

var searchGroupView={};
var searchResponse;

$( window ).load(function() {

	$("#search").hide();
});

function addGroupName(obj){
	groupView.groupName=obj.value;
	$("#status").html("");
	$("#search").html("");
	$("#detail").html("");
	$("#groupSearch").val('');
	searchGroupView={};
}

function addGroupDesc(obj){
	groupView.groupDesc=obj.value;
	$("#status").html("")
	$("#search").html("");
	$("#detail").html("");
	$("#groupSearch").val('');
	searchGroupView={};
}

function addGroup(){

	if(groupView.groupName == ''){
		//alert("Enter a Group Name "); // Travant original
		alert("Enter a Program Name"); // AJE 2016-11-01
		return;
	}

	$("#status").html("")


	dojo.rawXhrPost({
		url: "/administration/addAGroup",
		postData: dojo.toJson(groupView),
		headers: {
			"Content-Type": "application/json",
			"Accept": "application/json"
		},
		handleAs: "text",

		load: updateStatus,
		error: function(error) {
			hideWaiting();
			alert("Error:" + error);
			}
	});

	showWaiting();
}


function updateStatus(response){
	$("#status").html("&nbsp; &nbsp;&nbsp;"+ response).css('color','green');
	$("#groupSearch").val('');
	hideWaiting();
}


function searchGroupName(obj){
	searchGroupView.groupName=obj.value;
	$("#status").html("");
	$("#search").html('');
	$("#detail").html('');
	$("#addName").val('');
	$("#addDesc").val('');
	groupView={}
}


function searchGroup(){

	  var i = '';
	  var searchParameter = 'nbsp'
	  if(searchGroupView.groupName != null){
		  searchParameter = searchGroupView.groupName;
	  }
	  dojo.xhrGet({
	        handleAs: 'json',
	        url: "/administration/serchGroup/"+searchParameter,
	        preventCache: true,
	        error: function(e) {
	            alert("Error: " + e.message);
	            hideWaiting();
	        },
	        load: populateSearchList
	    });
	  showWaiting();
}




function submiteGroupChange(i){
	searchGroupView.groupId= searchResponse[i].groupId;
	searchGroupView.groupName = $("#editgroupName").val();
	searchGroupView.groupDesc = $("#editgroupDesc").val();

	if(searchGroupView.groupName == ''){
		//alert("Enter a Group Name "); // Travant original
		alert("Enter a Program Name"); // AJE 2016-11-01
		return;
	}

	dojo.rawXhrPost({
		url: "/administration/editGroup",
		postData: dojo.toJson(searchGroupView),
		headers: {
			"Content-Type": "application/json",
			"Accept": "application/json"
		},
		handleAs: "text",

		load: updateEditStatus,
		error: function(error) {
			hideWaiting();
			alert("Error:" + error);
			}
	});

	 showWaiting();
}

function updateEditStatus(response){
	$("#search").html('');
	$("#detail").html("Group updated");

	hideWaiting();
}

function showdetail(i){

	$("#search").hide();

  // AJE 2016-11-01 changed Travant labeling 'Group Name' etc. to 'Progam Name' etc.
	var str = '&nbsp;Program Name:&nbsp;<input class="ingestion-form" id="editgroupName" type="text" value="'+ searchResponse[i].groupName  +'" /><br /><br />'
   	str += '&nbsp;Program Description:&nbsp;<input class="ingestion-form"  id="editgroupDesc" type="text" value="'+ searchResponse[i].groupDesc  +'" /><br /><br />'
   	str += '&nbsp; &nbsp;&nbsp; <input type="submit" value="Submit Group Change" onclick="submiteGroupChange('+  i  +')"><br /><br />'

   	$("#detail").html(str);
	$("#detail").show();

}
function populateSearchList(response, ioArgs){

	searchResponse = response;
	var str = '<ul >';

	for (i = 0; i < response.length; i++) {
		str += '<li> <a href="#" onclick="showdetail('  + i + ');">' + response[i].groupName+' </a></li>';
    }

	str += '</ul>';

	$("#search").html(str);
	$("#search").show();
	$("#detail").hide();

	hideWaiting();
}
