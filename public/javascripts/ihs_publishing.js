var publishingView={};

require(["dijit/TitlePane","dojo/dom","dojo/domReady!"], function(dom) {

	dojo.xhrGet({
        handleAs: 'json',
        url: "/publishing/getPublishingView",
        preventCache: true,
        load: function(data) {
        	publishingView = data;
        },
        error: function(e) {
            alert("ihs_publishing.js getPublishingView Error: " + e.message);
        }
    });
  /* $(function() {
    // AJE  2016-12-13 this is jQuery, not Dojo
    //      2016-12-14 replaced jQuery datepicker with Dojo's dijit/form/DateTextBox so hide this entire function
    //  see app/views/publishing_publish_new_data.scala.html for widget location
  		 $('#startDate').datepicker();
  		 $('#endDate').datepicker();
  	}); */
});

function submitPub(){

	var jobName = $('#jobName').val();
	var fileFormat = $('#fileFormat').val();
	var startDate =  $('#startDate').val();
	var endDate =  $('#endDate').val();
	console.log('ihs_publishing : submitPub starts with startDate=' +startDate+ ' ; endDate='+endDate);

// AJE 2016-11-21 reformat the date value so it goes nicely into new DateTime(publishingView.startDate) in Publishing.java
/*
  var startYear = startDate.substr(6);
  var startMonth = startDate.substr(0, 2);
  var startDay = startDate.substr(3, 2);
*/
  var startYear = startDate.substring( startDate.lastIndexOf("/")+1);
  var startMonth = startDate.substring(0, startDate.indexOf("/"));
  console.log('for month, startDate.substring(0)=', startDate.substring(0), ' ; startDate.indexOf("/")-1=', startDate.indexOf("/")-1, ' ; and the character there is ', startDate.charAt(startDate.indexOf("/")-1), '.');
  console.log('for month, expect ', startDate.substring(0, startDate.indexOf("/")), '.');
  console.log('startDate.indexOf("/")+1 = ', startDate.indexOf("/")+1);
  var startDay = startDate.substring(startDate.indexOf("/")+1, startDate.lastIndexOf("/"));
  console.log('for day, expect from ', startDate.indexOf("/")+1, ' to ', startDate.lastIndexOf("/")-1, '.');

  startDate =  startYear +"-"+  startMonth +"-"+ startDay;
	console.log('1) submitPub has startYear=', startYear, ' ; startMonth=', startMonth, " ; startDay = ", startDay, " ; startDate = ", startDate);

  var endYear = endDate.substring( endDate.lastIndexOf("/")+1);
  var endMonth = endDate.substring(0, endDate.indexOf("/"));
  var endDay = endDate.substring(endDate.indexOf("/")+1, endDate.lastIndexOf("/"));
  endDate =  endYear +"-"+  endMonth +"-"+ endDay;
	console.log('2) submitPub has endYear=', endYear, ' ; endMonth=', endMonth, " ; endDay = ", endDay, "; endDate = ", endDate);

	var errorMessage = '';

	if(jobName == ''){
	  console.log('ihs_publishing.js : submitPub has no valid jobName');
		errorMessage += 'Enter a valid Job Name \n' ;
	}

	if(startDate != ''){
	  var dateToValidate = startMonth + "/" + startDay + "/" +startYear; // AJE 2016-12-20 : prepre a version format that function validateDate will like
		//if( !validateDate(startDate) ){ // Travant original
		if( !validateDate(dateToValidate) ){ // function validateDate: see ihs_common.js
			errorMessage += 'Enter a valid Start Date \n' ;
		}
	}

	if( endDate != ''){
	  var dateToValidate = endMonth + "/" + endDay + "/" +endYear; // AJE 2016-12-20 : prepre a version format that function validateDate will like
		// if( !validateDate(endDate) ){ // Travant original
		if( !validateDate(dateToValidate) ){ // function validateDate: see ihs_common.js
			errorMessage += 'Enter a  valid End Date \n' ;
		}
	}

	if(errorMessage != ''){
		alert(errorMessage);
		return;
	}


	publishingView.jobName = jobName;
	publishingView.fileFormat = fileFormat;
	publishingView.startDate = startDate;
	publishingView.endDate = endDate;
  console.log('startDate = ', startDate, ' ; endDate = ', endDate, ' ; publishingView.startDate = ', publishingView.startDate, ' ; publishingView.endDate = ', publishingView.endDate);

	showWaiting();

	dojo.rawXhrPost({
        url: "/publishing/postPublishingView", // see file: conf\routes
        postData: dojo.toJson(publishingView),
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        handleAs: "text",

        load: function(data) {
        	hideWaiting();
   	 		  window.location="/publishing_published_data";
        },
        error: function(error) {
        	 hideWaiting();
           alert("ihs_publishing.js, postPublishingView Error:" + error);
        }
    });
}