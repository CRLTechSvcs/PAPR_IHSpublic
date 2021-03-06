var groupId;

var searchResponse;

var addMemberView ={};
var editMemberView ={};
var countryState = {};

$( window ).load(function() {

	  dojo.xhrGet({
	        handleAs: 'json',
	        url: "/administration/getAllGroup",
	        preventCache: true,
	        error: function(e) {
	            alert("ihs_administration_member.js, getAllGroup Error: " + e.message);
	            hideWaiting();
	        },
	        load: populateGroupList
	    });

	  $("#search").hide();
	  showWaiting();

});

$( window ).load(function() {

	  dojo.xhrGet({
	        handleAs: 'json',
	        url: "/administration/getCountryState",
	        preventCache: false,
	        error: function(e) {
	            alert("ihs_administration_member.js, getCountryState Error: " + e.message);
	            hideWaiting();
	        },
	        load: populateCountryState
	    });

	  //$("#search").hide();
	  showWaiting();

});




function populateCountryState(response, ioArgs){

	countryState = response;

}

function populateGroupList(response, ioArgs){

	searchResponse = response;
	var str = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
	str += '<select id="groupSelect" onchange="updateGroup(event);">';
	str += '<option value="0"> </option>';
	for (var i = 0; i < response.length; i++) {
		str += '<option value="'+ response[i].groupId +'">' + response[i].groupName +'</option>';
  }
	str += '</select>';

	$("#groupList").html(str);

	hideWaiting();

}

function updateState(){

	$("#newstate").empty();

	for(var i = 0; i < countryState.length ; i++){
			if($('#newcountry option:selected').text() == countryState[i].name){

				for(var j = 0; j < countryState[i].stateProvinces.length ; j++){
					$("#newstate").append($("<option></option>").text(countryState[i].stateProvinces[j]));
				}

			}

	}

}

function updateGroup(e){
	groupId = e.target.value;

	$("#newMemberDetail").val('');
	$("#search").html('');
	$("#detail").html('');
	$("#memberSearch").val('');
	$("#status").html("");
	editMemberView = {};

	var countryDropDown = '<select id="newcountry" onchange="updateState()" >';
	for(var i = 0; i < countryState.length ; i++){
		countryDropDown += '<option>' + countryState[i].name + '</option>';
	}
	countryDropDown  += '</select>';

	var stateDropDown = '<select id="newstate" >';
	for(var j = 0; j < countryState[0].stateProvinces.length ; j++){
			stateDropDown += '<option>' + countryState[0].stateProvinces[j] + '</option>';
	}
	stateDropDown  += '</select>';

	if(groupId != '0'){

    // AJE 2016-09-26 massive reformatting of this section
    var dbl_break = '<br /><br />'; // AJE 2016-09-26 new
		var str =
		  '<div id="memberSearchFormHolder" class="admin_form_alignment">';
      /* // Travant original
			'&nbsp;Member Name:&nbsp;<input id ="newMemberName" class="ingestion-form" type="text" /><br /><br />'+
			'&nbsp;Member Desc:&nbsp;<input id ="newMemberDesc" class="ingestion-form" type="text" /><br><br />'+
			'&nbsp;Address 1:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input id ="newAddress1" class="ingestion-form" type="text" /><br /><br />'+
			'&nbsp;Address 2:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input id ="newAddress2" class="ingestion-form" type="text" /><br /><br />'+
			'&nbsp;City:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input id ="newCity" class="ingestion-form" type="text" /><br /><br />'+
			'&nbsp;State Or Province:&nbsp;'+ stateDropDown +'<br /><br />'+
			'&nbsp;Postal Code:&nbsp;&nbsp;<input id ="newPostal" class="ingestion-form" type="text" /><br /><br />'+
			'&nbsp;Country:' +
			countryDropDown +
		'<br /><br /> &nbsp; &nbsp;&nbsp; <input type="submit" value="Add Member" onclick="addMember()"><br />';
		*/
		  str += 'Member Name:&nbsp;';
		  str += '<input id ="newMemberName" class="ingestion-form" type="text" />' + dbl_break;
			str += 'Member Desc:&nbsp;&nbsp;';
			str += '<input id ="newMemberDesc" class="ingestion-form" type="text" />' + dbl_break;
			str += 'Address 1:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
			str += '<input id ="newAddress1" class="ingestion-form" type="text" />' + dbl_break;
			str += 'Address 2:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
			str += '<input id ="newAddress2" class="ingestion-form" type="text" />' + dbl_break;
			str += 'City:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
			str += '<input id ="newCity" class="ingestion-form" type="text" />' + dbl_break;
			str += 'State or Province:&nbsp;'+ stateDropDown + '&nbsp;&nbsp;';
			str += 'Postal Code:&nbsp;&nbsp;';
			str += '<input id ="newPostal" class="ingestion-form" type="text" size="20" />' + dbl_break;
			//str += 'Country:' + countryDropDown + dbl_break;
			str += countryDropDown + dbl_break;
		  str += '<input type="submit" value="Add Member" onclick="addMember()">';
		str += '</div>';

		$("#newMemberDetail").html(str);
	}else{
		$("#newMemberDetail").html('');
	}

	$("#editMemberDetail").html('');

}

function addMember(){

	if($("#newMemberName").val()== ''){
		alert("Add Member Name");
		return;
	}

	addMemberView.groupId=groupId;
	addMemberView.name=$("#newMemberName").val().trim();
	addMemberView.description=$("#newMemberDesc").val().trim();
	addMemberView.address1=$("#newAddress1").val().trim();
	addMemberView.address2=$("#newAddress2").val().trim();
	addMemberView.city=$("#newCity").val().trim();
	addMemberView.stateOrProvence=$('#newstate option:selected').text().trim();
	addMemberView.postalCode=$("#newPostal").val().trim();
	addMemberView.country=$('#newcountry option:selected').text().trim();



	dojo.rawXhrPost({
		url: "/administration/addAMember",
		postData: dojo.toJson(addMemberView),
		headers: {
			"Content-Type": "application/json",
			"Accept": "application/json"
		},
		handleAs: "text",

		load: updatAddStatus,
		error: function(error) {
			hideWaiting();
			alert("ihs_administration_member.js, addAMember Error:" + error);
			}
	});

	showWaiting();
}

function updatAddStatus(response){
	$("#status").html("&nbsp; &nbsp;&nbsp;" + response).css('color','green');
	$("#newMemberDetail").html('');
	$("#groupSelect").val("0");
	hideWaiting();
}

function searchMemberByName(){

	  var i = '';
	  $("#groupSelect").val("0");
	  $("#newMemberDetail").val('');
	  $("#detail").html('');

	  var searchParameter = 'nbsp'
		  if(editMemberView.name != null){
			  searchParameter = editMemberView.name;
	  }

	  dojo.xhrGet({
	        handleAs: 'json',
	        url: "/administration/searchMember/"+searchParameter,
	        preventCache: true,
	        error: function(e) {
	            alert("ihs_administration_member.js, searchMember Error: " + e.message);
	            hideWaiting();
	        },
	        load: populateSearchList
	    });
	  showWaiting();
}

function populateSearchList(response, ioArgs){

	searchResponse = response;

	$("#newMemberDetail").html('');

	var str = '<ul>';

	for (var j = 0; j < response.length; j++) { // AJE 2016-11-01 changed Travant labeling 'Group Name' etc. to 'Progam Name' etc.
		str += '<li> <a href="#" onclick="showdetail('  + j + ');">' + response[j].name+'     (Program Name: ' +  response[j].groupName+')</a></li>';
    }

	str += '</ul>';

	$("#search").html(str);
	 $("#search").show();

	hideWaiting();
}

function updateEditState(){

	$("#editstate").empty();

	for(var i = 0; i < countryState.length ; i++){
			if($('#editcountry option:selected').text() == countryState[i].name){

				for(var j = 0; j < countryState[i].stateProvinces.length ; j++){
					$("#editstate").append($("<option></option>").text(countryState[i].stateProvinces[j]));
				}

			}

	}

}

function showdetail(j){

	$("#search").hide();
	$("#newMemberDetail").html('');
	$("#groupSelect").val("0");
	$("#newMemberDetail").val('');
	$("#status").html("");

   var countryDropDown = '<select id="editcountry" onchange="updateEditState()" >';

	for(var i = 0; i < countryState.length ; i++){

		if( searchResponse[j].country == countryState[i].name){
			countryDropDown += '<option selected>' + countryState[i].name + '</option>';
		}else{
			countryDropDown += '<option>' + countryState[i].name + '</option>';
		}
	}

	countryDropDown  += '</select>';

	var stateDropDown = '<select id="editstate" >';

	for(var i = 0; i < countryState.length ; i++){
		if( searchResponse[j].country == countryState[i].name){

			for(var k = 0; k < countryState[i].stateProvinces.length ; k++){

				if( searchResponse[j].stateOrProvence == countryState[i].stateProvinces[k]){
					stateDropDown += '<option selected>' + countryState[i].stateProvinces[k] + '</option>';
				}else{
					stateDropDown += '<option>' + countryState[i].stateProvinces[k] + '</option>';
				}
			}

		}

	}

	stateDropDown  += '</select>';


	var str =
		'&nbsp;Member Name:&nbsp;<input id ="editMemberName" class="ingestion-form" type="text" value="'+ searchResponse[j].name  +'" /><br /><br />'+
	'&nbsp;Member Desc:&nbsp;&nbsp;<input id ="editMemberDesc" class="ingestion-form" type="text" value="'+ searchResponse[j].description  +'"/><br><br />'+
	'&nbsp;Address 1:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input id ="editAddress1" class="ingestion-form" type="text" value="'+ searchResponse[j].address1  +'"/><br /><br />'+
	'&nbsp;Address 2:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input id ="editAddress2" class="ingestion-form" type="text" value="'+ searchResponse[j].address2  +'"/><br /><br />'+
	'&nbsp;City:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input id ="editCity" class="ingestion-form" type="text" value="'+ searchResponse[j].city  +'"/><br /><br />'+
	'&nbsp;State Or Province:&nbsp;' + stateDropDown +'<br /><br />'+
	'&nbsp;Postal Code:&nbsp;&nbsp;<input id ="editPostal" class="ingestion-form" type="text" value="'+ searchResponse[j].postalCode  +'"/><br /><br />'+
	'&nbsp;Country:&nbsp;&nbsp;&nbsp;' + countryDropDown +
	'&nbsp; &nbsp;&nbsp; <input type="submit" value="Save Members" onclick="editMember(' + j +')"><br />';

   	$("#detail").html(str);
}

function editMember(k){


	editMemberView.id= searchResponse[k].id;
	editMemberView.name = $("#editMemberName").val().trim();
	editMemberView.description = $("#editMemberDesc").val().trim();
	editMemberView.address1 = $("#editAddress1").val().trim();
	editMemberView.address2 = $("#editAddress2").val().trim();
	editMemberView.city = $("#editCity").val().trim();
	editMemberView.stateOrProvence = $('#editstate option:selected').text().trim();;
	editMemberView.postalCode = $("#editPostal").val().trim();
	editMemberView.country = $('#editcountry option:selected').text().trim();;


	if(editMemberView.name == ''){
		alert("Add Member Name");
		return;
	}

	dojo.rawXhrPost({
		url: "/administration/editAMember",
		postData: dojo.toJson(editMemberView),
		headers: {
			"Content-Type": "application/json",
			"Accept": "application/json"
		},
		handleAs: "text",

		load: updateEditStatus,
		error: function(error) {
			hideWaiting();
			alert("ihs_administration_member.js, editAMember Error:" + error);
			}
	});

	 showWaiting();



}

function updateEditStatus(){
	$("#status").html("&nbsp; &nbsp;&nbsp;Member Added").css('color','green');
	$("#newMemberDetail").html('');
	$("#detail").html('');
	$("#groupSelect").val("0");
	$("#status").html("");
	hideWaiting();
}

function searchMemberName(obj){

	$("#newMemberDetail").val('');
	$("#search").html('');
	$("#detail").html('');
	$("#newMemberDetail").html('');
	$("#groupSelect").val("0");
	$("#status").html("");

	editMemberView.name = obj.value;
}