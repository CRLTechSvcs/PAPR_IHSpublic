function signOut(signout) {
	var loginButton = document.getElementById('login-button');
	var cancelButton = document.getElementById('cancel-button');
	var loginError = document.getElementById('login-error');
	loginError.innerHTML = "";
	if (signout) {
		loginButton.innerHTML = "Log In";
		cancelButton.style.display = "none";
	} else {
		loginButton.innerHTML = "Switch User";
		cancelButton.style.display = "inline";
	}
	var credentials = document.getElementById('credentials');
	var login = document.getElementById('login-div');
	login.style.display = "inline";
	credentials.style.display = "none";
}

function cancel() {
	var credentials = document.getElementById('credentials');
	var login = document.getElementById('login-div');
	login.style.display = "none";
	credentials.style.display = "inline";
	resetForm();
}

function login() {
	var validUserNames = ["bfrisbie", "awood", "jdoe", "jsmith", "jpublic"];
	var validUsers = ["Brandon Frisbie", "Amy Wood", "Jane Doe", "John Smith", "John Public"];
	var validOrgs = ["Travant Solutions", "CRL", "Indiana University", "Northwestern University", "University of Chicago"];
	var userInput = document.getElementById('login-form_user');
	var i = validUserNames.indexOf(userInput.value)
	if (i>=0) {
		var userName = document.getElementById('curr_user_name');
		var org = document.getElementById('curr_user_org');
		userName.innerHTML = validUsers[i];
		org.innerHTML = validOrgs[i];
		cancel();
		
		var iu = false;
		var nw = false;
		var crl = false;
		switch(i) {
			case 1:
				crl = true;
				nw = true;
			case 2:
				iu = true;
				break;
			case 3:
				nw = true;
				break;			
		}
		require(["dojo/_base/array", "dojo/query"], function (array, query) {
			array.forEach(
					query("a.edit-iu"),
					function(editLink) {
						if (iu)
							editLink.style.display = "inline";
						else
							editLink.style.display = "none";
					}
				);
			array.forEach(
					query("a.edit-nw"),
					function(editLink) {
						if (nw)
							editLink.style.display = "inline";
						else
							editLink.style.display = "none";
					}
				);
			array.forEach(
					query("a.edit-crl"),
					function(editLink) {
						if (crl)
							editLink.style.visibility = "visible";
						else
							editLink.style.visibility = "hidden";
					}
				);
			
		});
		
	} else {
		var loginError = document.getElementById('login-error');
		loginError.innerHTML = "Invalid User Name and Password";
		resetForm();
	}
}

function resetForm() {
	var userInput = document.getElementById('login-form_user');
	var passInput = document.getElementById('login-form_pass');
	userInput.className = "pre-input";
	userInput.value = "User Name";
	userInput.blur();
	passInput.className = "pre-input";
	passInput.value = "Password";
	passInput.type = "text";
	passInput.blur();
}

function savingsEstimatorFocus(e) {
	e.target.value = e.target.dataset.typedvalue;
}

function savingsEstimatorBlur(e) {
	var savingsSpan = document.getElementById('annual-savings');
	var linearFeet = document.getElementById('linear-feet').innerHTML;
	var cost = Number(e.target.value);
	if (isNaN(cost)) {
		e.target.value = 'NaN!';
		return;
	}
	e.target.value = cost.toLocaleString('en-US', {style: 'currency', currency: 'USD', minimumFractionDigits: '2'});
	e.target.dataset.typedvalue = cost;
	savingsSpan.innerHTML = (cost*linearFeet).toLocaleString('en-US', {style: 'currency', currency: 'USD', minimumFractionDigits: '2'});
}

function toggleIssueTimeline(num) {
	var zoom = document.getElementById('issueTimelineZoom'+num);
	var image = document.getElementById('issueTimeline'+num);
	if (zoom.innerHTML==='+') {
		zoom.innerHTML = '-';
		zoom.title = 'Zoom Out';
		image.src = 'images/IssueTimeline2.jpg';
	} else {
		zoom.innerHTML = '+';
		zoom.title = 'Zoom In';
		image.src = 'images/IssueTimeline1.jpg';
	}
}

function fileSourceClick(e) {
	var httpForm = document.getElementById('http-upload-form');
	var ftpForm = document.getElementById('ftp-upload-form');
	
	if (document.getElementById('file-source-http').checked) {
		httpForm.style.display = "inline";
		ftpForm.style.display = "none";
	} else {
		httpForm.style.display = "none";
		ftpForm.style.display = "inline";
	}
}

function publishTypeClick(e) {
	var memberForm = document.getElementById('member-selection-form');
	
	if (document.getElementById('publish-type-member').checked) {
		memberForm.style.display = "inline";
	} else {
		memberForm.style.display = "none";
	}
}


function reportTypeSelect(e) {
	var parametersTitle = document.getElementById('parameters-title');
	var report1Form = document.getElementById('report1-form');
	var report2Form = document.getElementById('report2-form');
	var report3Form = document.getElementById('report3-form');
	var searchPanel = document.getElementById('search-panel');

	report1Form.style.display = "none";
	report2Form.style.display = "none";
	report3Form.style.display = "none";
	searchPanel.style.display = "none";
	
	var selectedValue = document.getElementById('report-type-select').value;
	
	switch (selectedValue) {
	case 'report1':
		report1Form.style.display = "block";
		break;
	case 'report2':
		report2Form.style.display = "block";
		searchPanel.style.display = "block";
		break;
	case 'report3':
		report3Form.style.display = "block";
		searchPanel.style.display = "block";
		break;
	}
	
	var submit = document.getElementById('report-form-submit');
	if (selectedValue==="") {
		submit.disabled = true;
		parametersTitle.style.display = "none";
	} else {
		submit.disabled = false;
		parametersTitle.style.display = "inline";
	}
}

function updateISSN(issn) {
	var issn1 = document.getElementById('issn1');
	var issn2 = document.getElementById('issn2');
	
	issn1.value = issn;
	issn2.value = issn;
}




function showResults(search) {
	var results = document.getElementById('results');
	if (search.value=="")
		results.style.visibility = "hidden";
	else
		results.style.visibility = "visible";					
}
function timelineClick(e) {
	var timeline = document.getElementById('timeline');
	var x;
	if (e.pageX)
		x = e.pageX;
	else
		x = e.clientX + document.body.scrollLeft + document.documentElement.scrollLeft;
	x -= timeline.offsetLeft;
	var width = timeline.clientWidth;
	var percent;
	if (x<width*.03)
		percent=0;
	else if (x>width*.97)
		percent=1;
	else {
		x -= width*.03;
		width*=.94;
		percent = x/width
	}
	var addYears = Math.round(percent*53);
	var year = addYears + 1954;
	var yearDiv =  document.getElementById('vol3');
	var offset = yearDiv.offsetTop - yearDiv.parentNode.offsetTop;
	var issuesDiv =  document.getElementById('issues');
	issuesDiv.scrollTop = offset;
	
}
