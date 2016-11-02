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
            alert("Error: " + e.message);
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

	var errorMessage = '';

	if(jobName == ''){
		errorMessage += 'Enter a  valid Job Name \n' ;
	}


	if(startDate != ''){
		if(  !validateDate(startDate) ){
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


	publishingView.jobName = jobName;
	publishingView.fileFormat = fileFormat;
	publishingView.startDate = startDate;
	publishingView.endDate = endDate;
console.warn('startDate = ', startDate, '; endDate = ', endDate);

	showWaiting();

	dojo.rawXhrPost({
        url: "/publishing/postPublishingView",
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
            alert("Error:" + error);
        }
    });

}