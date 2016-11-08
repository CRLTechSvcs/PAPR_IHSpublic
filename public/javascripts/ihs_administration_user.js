var memberId = 0;
var userView = {};

var searchResponse  = {}

$( window ).load(function() {

	  dojo.xhrGet({
	        handleAs: 'json',
	        url: "/administration/getAllMember",
	        preventCache: true,
	        error: function(e) {
	            alert("ihs_administration_user.js, getAllMember Error: " + e.message);
	            hideWaiting();
	        },
	        load: populateMemberList
	    });

	  $("#search").hide();
	  $("#detail").hide();
	  showWaiting();

});


function populateMemberList(response, ioArgs){

	searchResponse = response;
	var str = '<select id="memberSelect" onchange="updateMember(event);">';
	str += '<option value="0"> </option>';
	for (var i = 0; i < response.length; i++) {
		str += '<option value="'+ response[i].id +'">' + response[i].name +'</option>';
  }
	str += '</select>';

	$("#memberList").html(str);

	hideWaiting();

}

function updateMember(e){
	memberId = e.target.value;

	$("#userSearch").val('');

	if(memberId != '0'){
// AJE 2016-09-26 massive reformatting of this section
    var dbl_break = '<br /><br />'; // AJE 2016-09-26 new
		var str =
		  '<div id="userAddFormHolder" class="admin_form_alignment">';
			str += 'First Name:&nbsp;<input id ="newFirstName" class="ingestion-form" type="text" />'+ dbl_break;
			str += 'Last Name:&nbsp;<input id ="newLastName" class="ingestion-form" type="text" />'+ dbl_break;
			str += 'User Name:&nbsp;<input id ="newUserName" class="ingestion-form" type="text" />'+ dbl_break;
			str += 'User Password:&nbsp;<input id ="newUserPassword" class="ingestion-form" type="text" />'+ dbl_break;
			/* '<div style="width:150px;float:left;"><input type="radio" name="group" value="user" checked>User</div>'+
			'<div style="width:200px;float:left;"><input type="radio" name="group" value="admin" >Admin</div> <br><br>'+
			*/
      str += '<div id="new_user_role_radio_set" style="float:left;">Choose a role: ';
       str += 'User: <input type="radio" name="group" value="user" checked>';
		  	str += 'Administrator: <input type="radio" name="group" value="admin" >';
			str += '</div>'+ dbl_break;
			str += '<input style="float:left;" type="submit" value="Add User" onclick="addUser()">';
		str += '</div>'+ dbl_break;

		$("#newUserDetail").html(str);

	}else {

		$("#newUserDetail").html('');
	}
	 $("#search").hide();
	 $("#detail").hide();
}

function addUser(){


	userView.memberId = memberId;
	userView.firstName = $("#newFirstName").val();
	userView.lastName = $("#newLastName").val();
	userView.userName = $("#newUserName").val();
	userView.password = $("#newUserPassword").val();
	userView.role = $('input:radio[name=group]:checked').val();


	if(userView.firstName == '' ||
	   userView.lastName == ''	||
	   userView.userName == '' ||
	   userView.password  == '' ){

	  alert('First Name, Lastalert name, Username and Password are required');
	  return;
	}

	$("#newUserDetail").html('');


	dojo.rawXhrPost({
		url: "/administration/addAUser",
		postData: dojo.toJson(userView),
		headers: {
			"Content-Type": "application/json",
			"Accept": "application/json"
		},
		handleAs: "text",

		load: updatAddStatus,
		error: function(error) {
			hideWaiting();
			alert("ihs_administration_user.js, addAUser Error:" + error);
		}
	});

	showWaiting();
	$("#search").hide();
	$("#detail").hide();
  }

function updatAddStatus(response){
	memberId = 0;
	$("#newUserDetail").html("&nbsp; &nbsp;&nbsp;" + response).css('color','green');
	$("#memberSelect").val("0");
	hideWaiting();
}

function searchUserName(){

	 var name = $("#userSearch").val();

	 if(name == ''){
		 name = 'nbsp';
	 }

	 dojo.xhrGet({
	        handleAs: 'json',
	        url: "/administration/searchUser/"+name,
	        preventCache: true,
	        error: function(e) {
	            alert("ihs_administration_user.js, searchUser Error: " + e.message);
	            hideWaiting();
	        },
	        load: populateSearchList
	    });
	  showWaiting();
}

function populateSearchList(response, ioArgs){

	searchResponse = response;


	var str = '<ul>';

	for (var j = 0; j < response.length; j++) {
		str += '<li> <a href="#" onclick="showdetail('  + j + ');">' + response[j].userName+' ( Member: '+ response[j].groupName+') </a></li>';
  }

	str += '</ul>';

	$("#search").html(str);

	$("#search").show();
	hideWaiting();
}
function updateUserName(){
	$("#newUserDetail").html('');
	$("#memberSelect").val("0");
	$("#search").hide();
	$("#detail").hide();
}

function showdetail(j){

	$("#newUserDetail").html('');
	$("#memberSelect").val("0");
	$("#search").hide();


	var str =
		'&nbsp;First Name:&nbsp;<input id ="editFirstName" class="ingestion-form" type="text" value="'+ searchResponse[j].firstName  +'" /><br /><br />'+
		'&nbsp;Last Name:&nbsp;<input id ="editLastName" class="ingestion-form" type="text" value="'+ searchResponse[j].lastName  +'"/><br><br />'+
		'&nbsp;User Name:&nbsp;&nbsp;&nbsp;<input id ="editUserName" class="ingestion-form" type="text" value="'+ searchResponse[j].userName  +'" readonly/><br /><br />'+
		'&nbsp;Password:&nbsp;&nbsp;&nbsp;<input id ="editPassword" class="ingestion-form" type="text" value="'+ searchResponse[j].password  +'"/><br /><br />';

	if(searchResponse[j].role == 'user'){
		str += '<div style="width:150px;float:left;"><input type="radio" name="editgroup" value="user" checked>User</div>'+
			'<div style="width:200px;float:left;"><input type="radio" name="editgroup" value="admin" >Admin</div> <br><br>';
	}else{
		str +='<div style="width:150px;float:left;"><input type="radio" name="editgroup" value="user" >User</div>'+
			'<div style="width:200px;float:left;"><input type="radio" name="editgroup" value="admin" checked> Admin</div> <br><br>';
	}
	str += '<input type="submit" value="Save User" onclick="saveUser(' + j +')"><br />';

   	$("#detail").html(str);
   	$("#detail").show();
}

function saveUser(k){

	userView.userId = searchResponse[k].userId;
	userView.memberId = searchResponse[k].memberId;
	userView.firstName = $("#editFirstName").val();
	userView.lastName = $("#editLastName").val();
	userView.userName = $("#editUserName").val();
	userView.password = $("#editPassword").val();
	userView.role = $('input:radio[name=editgroup]:checked').val();

	if(userView.firstName == '' ||
			   userView.lastName == ''	||
			   userView.userName == '' ||
			   userView.password  == '' ){

			  alert('First Name, Lastalert name, Username and Password are required');
			  return;
	}


	dojo.rawXhrPost({
		url: "/administration/saveAUser",
		postData: dojo.toJson(userView),
		headers: {
			"Content-Type": "application/json",
			"Accept": "application/json"
		},
		handleAs: "text",

		load: updatEditStatus,
		error: function(error) {
			hideWaiting();
			alert("ihs_administration_user.js, saveAUser Error:" + error);
		}
	});

	showWaiting();
	$("#detail").hide();

}

function updatEditStatus(){
	$("#newUserDetail").html('');
	$("#memberSelect").val("0");
	$("#search").html('');
	hideWaiting();
}