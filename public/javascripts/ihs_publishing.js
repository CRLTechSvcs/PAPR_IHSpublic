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

	$(function() {
		 $('#startDate').datepicker();
		 $('#endDate').datepicker();
	});
});

function submitPub(){

	var jobName = $('#jobName').val();
	var fileFormat = $('#fileFormat').val();
	var startDate =  $('#startDate').val();
	var endDate =  $('#endDate').val();
	//console.info('submitPub begins with startDate=' +startDate+ '\nendDate='+endDate);

	var errorMessage = '';

	if(jobName == ''){
		errorMessage += 'Enter a  valid Job Name \n' ;
	}

	if(startDate != ''){
		if( !validateDate(startDate) ){ // function validateDate: see ihs_common.js
			errorMessage += 'Enter a  valid Start Date \n' ;
		}
	}

	if( endDate != ''){
		if( !validateDate(endDate) ){
			errorMessage += 'Enter a  valid End Date \n' ;
		}
	}

	if(errorMessage != ''){
		alert(errorMessage);
		return;
	}

// AJE 2016-11-21 reformat the date value so it goes nicely into new DateTime(publishingView.startDate) in Publishing.java
  var startYear = startDate.substr(6);
  var startMonth = startDate.substr(0, 2);
  var startDay = startDate.substr(3, 2);
  startDate =  startYear +"-"+  startMonth +"-"+ startDay;
	//alert('1) submitPub continues with startYear=' +startYear+ '\nstartMonth='+startMonth+ "\nstartDay = " +startDay+ "\n\nstartDate = " +startDate);

  var endYear = endDate.substr(6);
  var endMonth = endDate.substr(0, 2);
  var endDay = endDate.substr(3, 2);
  endDate =  endYear +"-"+  endMonth +"-"+ endDay;
	//alert('2) submitPub continues with endYear=' +endYear+ '\nendMonth='+endMonth+ "\nendDay = " +endDay+ "\n\nendDate = " +endDate);
	console.info('3) submitPub continues with startDate=' +startDate+ '\nendDate='+endDate);
	//alert('3) submitPub continues with startDate=' +startDate+ '\nendDate='+endDate);

// AJE 2016-11-21


	publishingView.jobName = jobName;
	publishingView.fileFormat = fileFormat;
	publishingView.startDate = startDate;
	publishingView.endDate = endDate;
alert('startDate = ' +startDate+ '\n endDate = ' +endDate+ '\npublishingView.startDate = ' +publishingView.startDate+ '\npublishingView.endDate = '+publishingView.endDate);

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