/*

  AJE 2016-07-20 : lots of whitespace changes;
  AJE 2016-07-20 : bit of consolidating or separating concatenation of string vars for readability
  AJE 2016-07-20 : improved bracketing in for, if, and else to clarify

*/

	function populateMember(response, ioArgs){
		var test= response;
		var store = new dojo.store.Memory(response)
	    dijit.byId("stateMember").store = store;
	    members=response.data;
	}


	function populateJournalDetail(response, ioArgs) {// AJE 2016-01-12 modifed how the innerHTML is composed : can find a copy of original function in this dir

    //console.info('search_home.js, populateJournalDetail(response=', response, ', ioArgs=', ioArgs, ')');

		// AJE 2016-06-03: mine was stepped on so reinstated
    document.getElementById("title-col1").innerHTML = '<br>' + response.title;

    var publisherLI = '<li><strong>Publisher:</strong> ' + response.publisher + '</li>';
		var printISSN_LI = '<li><strong>Print ISSN:</strong> ' + response.printISSN + '</li>';
		var eISSN_LI = '<li><strong>Electronic ISSN:</strong>  ' + response.eISSN + '</li>';
    var OCLC_LI =  '<li><strong>OCLC Number:</strong>  ' + response.oclcNumber + '</li>';
    var pub_range_LI = '<li><strong>Publication Range:</strong> ' + response.publicationRange + '</li>';
    var language_LI = '<li><strong>Publication Language:</strong> ' + response.language + '</li>';
    var country_LI = '<li><strong>Publication Country:</strong> ' + response.country + '</li>';
		var volume_flag_LI = '<li><strong>Volume Level:</strong> ' + response.volumeLevelFlag + '</li>';
		var volume_flag_LI = '<li><strong>Volume Level:</strong> ' + response.volumeLevelFlag + '</li>';
		var titlehistory_link_CRL = '<li><a id="titlehistory_link_CRL" href="#" target="_blank">OCLC&#39;s History Visualization for this Journal</a></li>';

    // new button to go back to the search results: AJE 2016-10-27
    var search_results_div = '<div id="show_search_results">';
        search_results_div += '<button id="button_show_search_results" value="0" ';
        search_results_div += 'onClick="toggle_search_home_title_components(\'populateSearchList\');">Show search results</button>';
        search_results_div += '</div>';

    document.getElementById("content-col1").innerHTML = '<ul class="no-decoration">' + publisherLI + printISSN_LI + eISSN_LI + OCLC_LI + pub_range_LI + language_LI + country_LI + volume_flag_LI + titlehistory_link_CRL + '<ul>';
    document.getElementById("content-col1").innerHTML += search_results_div;

    if(response.printISSN){ // AJE 2016-10-18 only show titlehistory_link_CRL when there is an ISSN
  		// AJE new: make the link value right
	  	$("#titlehistory_link_CRL").attr("href", "http://worldcat.org/xissn/titlehistory?issn=" + response.printISSN);
		  //console.warn("search_home.js has set titlehistory_link_CRL.attr.href = ", $("#titlehistory_link_CRL").attr("href") );
    } else if (response.eISSN){
      $("#titlehistory_link_CRL").attr("href", "http://worldcat.org/xissn/titlehistory?issn=" + response.eISSN);
    } else {
      $("#titlehistory_link_CRL").css("display", "none");
    }

    call1 = false; // AJE no idea here ; is original
	}


	function updateCommitment(holdingid, obj) {
    var holdingView = holdings[holdingid];
    for (var index = 0, len = obj.length; index < len; ++index) {
      if (obj.childNodes[index].selected) {
          holdingView.commitmentView[index].checked = '1';
      } else {
          holdingView.commitmentView[index].checked = '0';
      }
    }
	}


	function updateOverallst(holdingid, obj) {
    var holdingView = holdings[holdingid];
    for (var index = 0, len = obj.length; index < len; ++index) {
      if (obj.childNodes[index].selected) {
          holdingView.overalls[index].checked = '1';
      } else {
          holdingView.overalls[index].checked = '0';
      }
    }
	}


	function updateValidation(holdingid, obj) {
    var holdingView = holdings[holdingid];
    for (var index = 0, len = obj.length; index < len; ++index) {
      if (obj.childNodes[index].selected) {
          holdingView.validationLevels[index].checked = '1';
      } else {
          holdingView.validationLevels[index].checked = '0';
      }
    }
	}


	function updateIhsVerified(holdingid, obj) {
    var holdingView = holdings[holdingid];
    for (var index = 0, len = obj.length; index < len; ++index) {
      if (obj.childNodes[index].selected) {
          holdingView.ihsVerified[index].checked = '1';
      } else {
          holdingView.ihsVerified[index].checked = '0';
      }
    }
	}


	function updateMissingPages(holdingid, search) {
    var holdingView = holdings[holdingid];
    holdingView.missingPages = search.value;
	}


	function updateConditions(holdingid, obj) {
    var holdingView = holdings[holdingid];
    for (var index4 = 0, len4 = holdingView.holdingConditionsView.length; index4 < len4; ++index4) {
      if (holdingView.holdingConditionsView[index4].conditionId == obj.value) {
        if (obj.checked) {
            holdingView.holdingConditionsView[index4].checked = '1';
        } else {
            holdingView.holdingConditionsView[index4].checked = '0';
        }
      }
    }
	}


  function saveGlobalCondition(){
    var holdingView = holdings[0];
    var holdingIds = [];

    $mvar = $('.clscbvol');
    for (i=0; i<$mvar.length; i++)    {
       if( $mvar[i].checked == true){
    	  var res = $mvar[i].id.split(":");
    	  holdingIds.push(res[1]);
       }
    }

    holdingView.holdingIds = holdingIds;

    dojo.rawXhrPost({
        url: "/search/postHoldingConditions",
        postData: dojo.toJson(holdingView),
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        handleAs: "text",
        load: function(data) {
        	hideWaiting();
        	call2=false;
        	getJournalDetail(globalTitleId);
        },
        error: function(error) {
        	hideWaiting();
        	call2=false;
          alert("search_home.js, saveGlobalCondition, postHoldingConditions, Error:" + error);
        }
    });

    call2=true;
    showWaiting();
  }


  function drawGlobaledit(){
		 try{
console.info('drawGlobaledit has holdings[globalHoldingIndex=',globalHoldingIndex,'] = ', holdings[globalHoldingIndex], '.');
      holdings[0] = clone(holdings[globalHoldingIndex]);
      var holdingView = holdings[0];
      holdingView.holdingId = 0;
      var image1 = '&emsp;&emsp;&emsp; <img onclick="saveGlobalCondition();" src="/assets/images/save.gif" width="30" height="15">';

      var tmpcommit = clone(holdingView.commitmentView[0]);
      tmpcommit.id =0;
      tmpcommit.name=" ";
      holdingView.commitmentView.unshift(tmpcommit);

      var commitment = '<select onchange="updateCommitment(\'0\',this);">';
      for (var index = 0; index < holdingView.commitmentView.length;  index++) {
        commitment += '<option value="' + holdingView.commitmentView[index].id + '">';
        commitment += holdingView.commitmentView[index].name + '</option>';
        holdingView.commitmentView[index].checked = 0;
      }
      commitment += "</select>";

      var tmpoveralls = clone(holdingView.overalls[0]);
      tmpoveralls.id =0;
      tmpoveralls.name=" ";
      holdingView.overalls.unshift(tmpoveralls);

      var overallst = '<select onchange="updateOverallst(\'0\',this);">';
      for (var index = 0; index < holdingView.overalls.length;  index++) {
        overallst += '<option value="' + holdingView.overalls[index].id + '">';
        overallst += holdingView.overalls[index].name + '</option>';
        holdingView.overalls[index].checked = 0;
      }
      overallst += "</select>";

      var tmpvalidationLevels = clone(holdingView.validationLevels[0]);
      tmpvalidationLevels.id =0;
      tmpvalidationLevels.name=" ";
      holdingView.validationLevels.unshift(tmpvalidationLevels);

      var validation = '<select onchange="updateValidation(\'0\',this);">';
      for (var index =0;  index < holdingView.validationLevels.length; index++) {
        validation += '<option value="' + holdingView.validationLevels[index].id + '">';
        validation += holdingView.validationLevels[index].name + '</option>';
        holdingView.validationLevels[index].checked = 0;
      }
      validation += "</select>";

      var tmpihsVerified = clone(holdingView.ihsVerified[0]);
      tmpihsVerified.id =0;
      tmpihsVerified.name=" ";
      holdingView.ihsVerified.unshift(tmpihsVerified);

      var ihsVerified = '<select  onchange="updateIhsVerified(\'0\',this);">';
      for (var index = 0; index < holdingView.ihsVerified.length; index++) {
        ihsVerified += '<option value="' + holdingView.ihsVerified[index].id + '">';
        ihsVerified += holdingView.ihsVerified[index].name + '</option>';
        holdingView.ihsVerified[index].checked = 0;
      }
      ihsVerified += "</select>";

      var tmpOtherissue = '<br>';
      for (var index = 0; index < holdingView.holdingConditionsView.length; index++) {
        tmpOtherissue += '<input type="checkbox" ';
        tmpOtherissue += 'onclick="updateConditions(\'0\',this);" ';
        tmpOtherissue += 'value="' + holdingView.holdingConditionsView[index].conditionId + '">';
        tmpOtherissue += holdingView.holdingConditionsView[index].name + '</br>';
        holdingView.holdingConditionsView[index].checked=0;
      }

      var st = image1+'<br><strong>Commitment line : </strong> ' + commitment;
        st += '<br><br><strong>Condition:</strong><br>Overall: ' + overallst + '<br>';
        st += 'Validation Level:' + validation + '<br>';
        st += 'Verified in IHS:' + ihsVerified + '<br>';
        st += 'Other Issues:' + tmpOtherissue;

      document.getElementById('glbchildCntd').innerHTML = st;
      document.getElementById("globaledit").style.display  = "block";
		 } catch (e){
			 console.log(e);
		 }
  }


	function editCondition(holdingid) {
    var holdingView = holdings[holdingid];
    var image1 = '&emsp;&emsp;&emsp; <img onclick="saveCondition(' + holdingView.holdingId + ');" src="/assets/images/save.gif" width="30" height="15">';

    document.getElementById(holdingid + 'image1').innerHTML = image1;

    var commitment = '<select onchange="updateCommitment('+ holdingid + ', this);">';
    for (var index = 0; index < holdingView.commitmentView.length;  index++) {
      if (holdingView.commitmentView[index].checked == '1') {
        commitment += '<option value="' + holdingView.commitmentView[index].id + '" selected="selected">' + holdingView.commitmentView[index].name + '</option>';
      } else {
        commitment += '<option value="' + holdingView.commitmentView[index].id + '">' + holdingView.commitmentView[index].name + '</option>';
      }
    }
    commitment += "</select>";

    var overallst = '<select onchange="updateOverallst(' + holdingid + ', this);">';
    for (var index4 = 0, len4 = holdingView.overalls.length; index4 < len4; ++index4) {
      if (holdingView.overalls[index4].checked == '1') {
        overallst += '<option value="' + holdingView.overalls[index4].id + '" selected="selected">' + holdingView.overalls[index4].name + '</option>';
      } else {
        overallst += '<option value="' + holdingView.overalls[index4].id + '">' + holdingView.overalls[index4].name + '</option>';
      }
    }
    overallst += "</select>";

    var validation = '<select  onchange="updateValidation(' + holdingid + ', this);">';
    for (var index4 = 0, len4 = holdingView.validationLevels.length; index4 < len4; ++index4) {
      if (holdingView.validationLevels[index4].checked == '1') {
        validation += '<option value="' + holdingView.validationLevels[index4].id + '" selected="selected">' + holdingView.validationLevels[index4].name + '</option>';
      } else {
        validation += '<option value="' + holdingView.validationLevels[index4].id + '">' + holdingView.validationLevels[index4].name + '</option>';
      }
    }
    validation += "</select>";

    var ihsVerified = '<select  onchange="updateIhsVerified(' + holdingid + ', this);">';
    for (var index4 = 0, len4 = holdingView.ihsVerified.length; index4 < len4; ++index4) {
      if (holdingView.ihsVerified[index4].checked == '1') {
        ihsVerified += '<option value="' + holdingView.ihsVerified[index4].id + '" selected="selected">' + holdingView.ihsVerified[index4].name + '</option>';
      } else {
        ihsVerified += '<option value="' + holdingView.ihsVerified[index4].id + '">' + holdingView.ihsVerified[index4].name + '</option>';
      }
    }
    ihsVerified += "</select>";

    var missingPages = '<input type="text" value ="' + holdingView.missingPages + '" onkeyup="updateMissingPages(' + holdingid + ',this);">';

    var tmpOtherissue = '<br>';
    for (var index4 = 0, len4 = holdingView.holdingConditionsView.length; index4 < len4; ++index4) {
      if (holdingView.holdingConditionsView[index4].checked == '1') {
        tmpOtherissue += '<input type="checkbox" onclick="updateConditions(' + holdingid + ',this);" value="' + holdingView.holdingConditionsView[index4].conditionId + '" checked="checked" >' + holdingView.holdingConditionsView[index4].name + '</br>';
      } else {
        tmpOtherissue += '<input type="checkbox" onclick="updateConditions(' + holdingid + ',this);" value="' + holdingView.holdingConditionsView[index4].conditionId + '">' + holdingView.holdingConditionsView[index4].name + '</br>';
      }
    }

    var st = '<br> <strong> Commitment </strong> ' + commitment + '<br><br>';
      st += '<strong>Condition:</strong><br>Overall: ' + overallst;
      st += '<br>Validation Level:' + validation;
      st += '<br>Verified in IHS:' + ihsVerified;
      st += '<br>Missing Pages:' + missingPages;
      st += '<br>Other Issues:' + tmpOtherissue;

    document.getElementById(holdingid + 'col12').innerHTML = st;
	}


	function saveCondition(holdingid) {
    var holdingView = holdings[holdingid];

    var image1 = '&emsp;&emsp;&emsp; <img onclick="editCondition(' + holdingView.holdingId + ');" src="/assets/images/edit.gif" width="30" height="15">';
    document.getElementById(holdingid + 'image1').innerHTML = image1;
    document.getElementById(holdingid + 'col12').innerHTML = drawCol2Unedit(holdingView);

    dojo.rawXhrPost({
      url: "/search/postHoldingConditions",
      postData: dojo.toJson(holdingView),
      headers: {
      "Content-Type": "application/json",
      "Accept": "application/json"
      },
      handleAs: "text",
      load: function(data) {
        hideWaiting();
      },
      error: function(error) {
        hideWaiting();
        alert("search_home.js, saveCondition, postHoldingConditions, Error:" + error);
      }
    });

    showWaiting();
	}


	function editNote(holdingid) {
	    var holdingView = holdings[holdingid];

	    var image2 = '&emsp;&emsp;&emsp; <img onclick="saveNote(' + holdingView.holdingId + ');" src="/assets/images/save.gif" width="30" height="15">';
	    document.getElementById(holdingid + 'image2').innerHTML = image2;
	    document.getElementById(holdingid + 'notes').readOnly = false;
	}


	function saveNote(holdingid) {
    var holdingView = holdings[holdingid];
    var image2 = '&emsp;&emsp;&emsp; <img onclick="editNote(' + holdingView.holdingId + ');" src="/assets/images/edit.gif" width="30" height="15">';

    document.getElementById(holdingid + 'image2').innerHTML = image2;
    document.getElementById(holdingid + 'notes').readOnly = true;

    holdingView.notes = document.getElementById(holdingid + 'notes').value;

    dojo.rawXhrPost({
      url: "/search/postHoldingNotes",
      postData: dojo.toJson(holdingView),
      headers: {
        "Content-Type": "application/json",
        "Accept": "application/json"
      },
      handleAs: "text",
      load: function(data) {
        // AJE 2016-07-20 11:56:41 : this was always an empty block
      },
      error: function(error) {
        alert("search_home.js, saveNote, postHoldingNotes Error:" + error);
      }
    });
	}

	function drawCol2Unedit(holdingView) {
    var tmpOtherissue = '<br>';
    var overallst = '';
    var validation = '';
    var ihsVerified = '';
    var commitment= '';

    if (holdingView.holdingConditionsView.length > 0) {
      for (var index3 = 0, len3 = holdingView.holdingConditionsView.length; index3 < len3; ++index3){
        if (holdingView.holdingConditionsView[index3].checked == "1") {
          tmpOtherissue += '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' + holdingView.holdingConditionsView[index3].name + '<br>';
        }
      }
    } else {
      tmpOtherissue = 'None';
    }

	 	for (var index4 = 0, len4 = holdingView.commitmentView.length; index4 < len4; ++index4) {
      if (holdingView.commitmentView[index4].checked == '1'){
        commitment = holdingView.commitmentView[index4].name;
      }
    }

    for (var index4 = 0, len4 = holdingView.overalls.length; index4 < len4; ++index4) {
      if (holdingView.overalls[index4].checked == '1'){
        overallst = holdingView.overalls[index4].name;
      }
    }

    for (var index4 = 0, len4 = holdingView.validationLevels.length; index4 < len4; ++index4) {
      if (holdingView.validationLevels[index4].checked == '1'){
        validation = holdingView.validationLevels[index4].name;
      }
    }

    for (var index4 = 0, len4 = holdingView.ihsVerified.length; index4 < len4; ++index4) {
      if (holdingView.ihsVerified[index4].checked == '1'){
        ihsVerified = holdingView.ihsVerified[index4].name;
      }
    }

    var st = '<strong>Commitment: </strong>' + commitment + '<br><br>';
      st += '<strong>Condition:</strong><br>Overall: ' + overallst;
      st += '<br>Validation Level:' + validation;
      st += '<br>Verified in IHS:' + ihsVerified;
      st += '<br>Missing Pages:' + holdingView.missingPages;
      st += '<br>Other Issues:' + tmpOtherissue;

    return st;
	}


	function buildHoldingHTML(holdingView) {
    var image1 = "";
    var image2 = "";

    var image1Id = holdingView.holdingId + "image1";
    var image2Id = holdingView.holdingId + "image2";
    var notesId = holdingView.holdingId + "notes";
    //console.log('buildHoldingHTML: image1Id=', image1Id, ', image2Id=', image2Id, ', notesId=', notesId);

    var col12 = holdingView.holdingId + 'col12';
    var col13 = holdingView.holdingId + 'col13';

    holdings[holdingView.holdingId] = holdingView;


    if (holdingView.editable == '1') {
      image1 = '&emsp;&emsp;&emsp; <img onclick="editCondition(' + holdingView.holdingId + ');" src="/assets/images/edit.gif" width="30" height="15">'
      image2 = '&emsp;&emsp;&emsp; <img onclick="editNote(' + holdingView.holdingId + ');" src="/assets/images/edit.gif" width="30" height="15">'
      globalHoldingIndex=holdingView.holdingId;
    }

    var holdingidtmp = ' <div class="holding" id="holding' + holdingView.holdingId + '"> ' +
      '<div class="holding-col1"> <strong>' + holdingView.member + ' </strong><br /> ' +
        holdingView.location + '<br /> ' + holdingView.location1 + '<br /> ' + holdingView.location2 +
      '</div> ' +

      '<div id="' + image1Id + '">' + image1 + '</div>' +
      '<div class="holding-col2" id="' + col12 + '" > ' +
        drawCol2Unedit(holdingView) +
      '</div> ' +

      '  <div id="' + image2Id + '">' + image2 + '</div>' +
      '    <div class="holding-col3" id="' + col13 + '" > ' +
      '       <strong>Notes:</strong> ' +
      '  	    <textarea class="issue-notes" id="' + notesId + '" readonly> ' + holdingView.notes + '</textarea>' +
      '     </div> ' +
      '   </div> ' +
      '  </div> '; // end building holdingidtmp
	    return holdingidtmp;
	}

  function drawPie(response){
    //console.info("AJE drawPie: response = ", response);

  	var width = 150,
  		height = 150,
  		radius = Math.min(width, height) / 2;

  	var color = d3.scale.ordinal()
  		.range(["#8080FF", "#FFFF80", "#FF8080" ]);

		var arc = d3.svg.arc()
    	.outerRadius(radius - 10)
    	.innerRadius(0);

    	var pie = d3.layout.pie()
    		.sort(null)
    		.value(function(d) { return d.number; });

		//var svg = d3.select("#content-col2").append("svg") // Travant 2016-10-18
		var svg = d3.select("#content-col3").append("svg") // AJE 2016-10-27
    	.attr("width", width)
    	.attr("height", height)
  		.append("g")
    	.attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");

    	var data = response[0].chart;
      //console.info("AJE drawPie: data = ", data);

    	data.forEach(function(d) {
    		d.number = +d.number;
  		});

  		var g = svg.selectAll(".arc")
      		.data(pie(data))
    		.enter().append("g")
      		.attr("class", "arc");

  		g.append("path")
      		.attr("d", arc)
      		.style("fill", function(d) { return color(d.data.status); });

  		g.append("text")
      		.attr("transform", function(d) { return "translate(" + arc.centroid(d) + ")"; })
      		.attr("dy", ".35em")
      		.style("text-anchor", "middle")
      		//.attr("font-size", "10") // Travant original
      		.attr("font-size", "12") // AJE 2016-07-20 modified
      		.attr("font-weight", "bold") // AJE 2016-07-20 added
      		// .attr("fill", "rgb(255,255,255)") // AJE 2016-07-20 added : will produce white text + can be turned on if anyone chooses
      		.text(function(d) { return d.data.status + '(' + d.data.number + ')' ; });

      //console.info("AJE 2016-10-07 end drawPie; force display of piechart, because in populateVolumeDetail we have hidden it");
      //$('#content-col2').css('display', 'block'); // AJE 2016-09-30 for Amy enhancement list 2016-09-27, #7
      $('#content-col3').css('display', 'block'); // AJE 2016-10-27

  }

  function moveCursor(volIndex){
    var volDivs = dojo.query(".dijitTitlePane");

    for (var i=0; i < volDivs.length; i++) {
    	for (var i=0; i<volDivs.length; i++) {
    		var volWidget = dijit.registry.byNode(volDivs[i]);
    		if(volWidget.title.search("Vol") != -1) {
    		  if (!volWidget.open) volWidget.toggle();
    		}
    		if(volWidget.title.search("Issue") != -1) {
    		  if (volWidget.open) volWidget.toggle();
    		}
    	}
    }
    var issuesDiv =  document.getElementById('issues');
    issuesDiv.scrollTop = volIndex;
  }


  function drawTimeBar(response, numberOfIssue){
    var totalsize =800; // Travant original
    /* var totalsize =867;  # AJE 2016-10-13 changed */
    var issuessize = totalsize/numberOfIssue;
    var divindex=0;
    var numberofissue=0;
    var issue_increase = 24.75; // Travant original value: 24.75;  // AJE 2016-10-13 new var, see for loop below

    var html = '<br>';

    for (var index = 0, len = response.length; index < len; ++index) {
      var issueView = response[index].issueView;
      numberofissue = 0;

      for (var index1 = 0, len1 = issueView.length; index1 < len1; ++index1) {
        // AJE 2016-11-04 restructured the internal works of this loop
        if(issueView[index1].issueStatus == "Held"){ html += '<div class="timeline timeline-held" '; }
        if(issueView[index1].issueStatus == "Missing") { html += '<div class="timeline timeline-missing" '; }
        if(issueView[index1].issueStatus == "Wanted") { html += '<div class="timeline timeline-wanted" '; }
        // no matter the issue status: finish the div
        html += 'style="width:'+issuessize+'px;" onclick="moveCursor( ' + divindex +');"> '
          + '<span class="coupontooltip">Vol.' + response[index].volumeNumber +  '<br>'
          + 'Issue. ' + issueView[index1].issueNumber  + '<br>'
          + issueView[index1].issueMonth + ' </span> </div>';

        // numberofissue+=24.75; // Travant original
        numberofissue += issue_increase; // AJE 2016-10-18
        //console.log('drawTimeBar: numberofissue = ', numberofissue);
      }
      //divindex+=24.75+numberofissue; // Travant original
      divindex += issue_increase + numberofissue; // AJE 2016-10-18
      //console.log('drawTimeBar: divindex = ', divindex); // AJE 2016-10-18
    }

    if(response.length > 0) {
      html += '<div id="timeline-left">|' +  response[0].volumeYear + '</div>';
      html += '<div id="timeline-center"> | </div>';
      html +=	'<div id="timeline-right">' + response[response.length-1].volumeYear + '|</div><br>';
    }
    document.getElementById("timeline").innerHTML=html;

    // AJE 2016-10-03 force display of timebar, because in populateVolumeDetail we have hidden it
    $('#timeline').css('display', 'block'); // AJE 2016-09-30 for Amy enhancement list 2016-09-27, #7

  }



	function populateVolumeDetail(response, ioArgs) {
		var numberOfIssue=0;
		var volumeLevelFlag = '0';

    // AJE 2016-10-03 assume there are no issues, so no widgets
    //$('#content-col2').css('display', 'none'); // #content-col2 was where pie chart lived AJE 2016-10-18 until 27th
    $('#content-col3').css('display', 'none'); // #content-col3 is where the pie chart lives AJE 2016-10-27
    $('#timeline').css('display', 'none');
    $('#tools_for_title_issues').css('display', 'none');
    // end AJE 2016-09-30 for Amy enhancement list 2016-09-27, #7

    document.getElementById("summary").style.display = 'block';

    holdings = {};

    for (var index = 0, len = response.length; index < len; ++index) {

	    var table1 =  document.getElementById('table1');
      var row = table1.insertRow(index);

      volumeLevelFlag = response[index].volumeLevelFlag;

      var cb = document.createElement('input');

      cb.type = 'checkbox';
      cb.id = 'cbvol:'+index;
      cb.style.float = "left"; // AJE 2016-10-18 new
    	cb.onclick = function() {
    		var res = this.id.split(":");
    	 	$mvar = $('.clscbvol.'+ res[1]);
    	 	for (i=0; i<$mvar.length; i++)    {
          if ( this.checked ) {
          	$mvar[i].checked = true;
          } else {
          	$mvar[i].checked = false;
          }
    	 	}
    	};
      row.appendChild(cb);

	    var element = document.createElement('div');
			var voldiv = 'vol' + response[index].volumeNumber;
			element.id = voldiv;
			//element.style.float = "right"; // Travant original
			element.style.float = "left"; // AJE 2016-10-18 new
			//element.style.width = "100%"; // AJE 2016-10-18 new: causes volume div to end up under checkbox, so does 99%
			element.style.width = '97%'; // AJE 2016-10-18 new
			row.appendChild(element);

      var title = response[index].volumeYear + " Vol. " + response[index].volumeNumber;

      //var issueid = '<div id = "issueid' + index + '"></div>';
      var issueid = '<table id="issuetable'+ index + '"> </table>';

      var tp = new dijit.TitlePane({
        title: title,
        content: issueid,
        /* style: "width: 750px;", # Travant original */
        style: "width: 100%;", /* AJE 2016-10-18 changed */
        open: true
      });

      document.getElementById(voldiv).appendChild(tp.domNode);

      var issueView = response[index].issueView;

      for (var index1 = 0, len1 = issueView.length; index1 < len1; ++index1) {
        var holdinghtml = ' ';
				numberOfIssue++;
        var holdingViews = issueView[index1].holdingViews;

        for (var index2 = 0, len2 = holdingViews.length; index2 < len2; ++index2) {
          holdinghtml = holdinghtml + buildHoldingHTML(holdingViews[index2]);
        }

        var issueTitle =  '';
        if (volumeLevelFlag == '1'){
        	issueTitle = 'Volume Details&emsp;|&emsp;' + issueView[index1].issueStatus;
        	issueTitle += '&emsp;|&emsp;&emsp;|&emsp;Best Holding Condition: ' + issueView[index1].issueCondition;
        } else {
          issueTitle = 'Issue: ' +issueView[index1].issueNumber;
          issueTitle += '&emsp;|&emsp;' + issueView[index1].issueMonth;
          issueTitle += '&emsp;|&emsp;' + issueView[index1].issueStatus;
          issueTitle += '&emsp;|&emsp;' + issueView[index1].issueCount;
          issueTitle += '&emsp;|&emsp;Best Holding Condition: ' + issueView[index1].issueCondition;
        }

        //console.warn('search_home.js, populateVolumeDetail: issueView[index1=',index1,'].issueMonth = ', issueView[index1].issueMonth, ' ; issueStatus: ', issueView[index1].issueStatus,' ; ' );

        var clr = "";

        if (issueView[index1].issueStatus == "Held") { clr = "issue held" }
        if (issueView[index1].issueStatus == "Missing") { clr = "issue missing" }
        if (issueView[index1].issueStatus == "Wanted") { clr = "issue wanted" }

        var issuetp = new dijit.TitlePane({
          title: issueTitle,
          content: holdinghtml,
          open: false,
          style: "width: 700px;'",
          class: clr
        });

        var issuetable =  document.getElementById('issuetable'+index);
        var issuerow = issuetable.insertRow(index1);

        for (var index3 = 0; index3 <holdingViews.length; index3++) {
          if(holdingViews[index3].editable == '1'){
            var cb1 = document.createElement('input');
            cb1.type = 'checkbox';
            cb1.style.float = 'Left'
            cb1.className = 'clscbvol '+index
            cb1.id = 'holdingid:' + holdingViews[index3].holdingId;
            issuerow.appendChild(cb1);
            break;
          }
        }

	    	var issueelement = document.createElement('div');
	    	issueelement.style.float= 'Right';

	    	issueelement.appendChild(issuetp.domNode);


	    	issuerow.appendChild(issueelement);

	    	hideWaiting();
      } // end for index1
	  } // end for just plain index

//console.info('AJE 2016-09-15/10-03 : response: ', response, '.');

	    if(response.length > 0) {
	      //document.getElementById("content-col2").innerHTML = ' '; // AJE 2016-10-18
	      document.getElementById("content-col3").innerHTML = ' '; // AJE 2016-10-27
			  //drawPie(response); // AJE 2016-10-03 see comment above: I think this is in the wrong place
			  /* AJE 2016-10-03
	      drawTimeBar(response, numberOfIssue);
        // AJE 2016-09-30 for Amy enhancement list 2016-09-27, #7
        console.warn('Amy enhancement list 2016-09-27, #7: show tools_for_title_issues if numberOfIssue > 0 [now: ', numberOfIssue, ']');
        $('#tools_for_title_issues').css('display', 'block');
			  // 2016-10-03 resume Travant original */
		  } else {
			  //document.getElementById("content-col2").innerHTML ='<img src="/assets/images/empty.gif" height="42" width="42" />' ;// AJE 2016-10-18
			  document.getElementById("content-col3").innerHTML ='<img src="/assets/images/empty.gif" height="42" width="42" />' ;// AJE 2016-10-27
		  }

	    /* AJE 2016-09-15 : Travant original, all on 1 line
	    if( volumeLevelFlag == 0){ drawTimeBar(response, numberOfIssue); }

	    // but don't we always want to see the timebar? if there are issues?
	    // 2016-10-03 moved this above

	    console.info('AJE 2016-09-15/10-03 : near end populateVolumeDetail: if numberOfIssue > 0 [now: ', numberOfIssue, '] then call drawTimeBar');
	    if( numberOfIssue > 0){
	      drawTimeBar(response, numberOfIssue);
	    }
	    or
	    console.info('AJE 2016-09-15/10-03 : near end populateVolumeDetail: if numberOfIssue > 0 [now: ', numberOfIssue, '] then call drawTimeBar');
	    if( numberOfIssue > 0){
	      drawTimeBar(response, numberOfIssue);
        // AJE 2016-09-30 for Amy enhancement list 2016-09-27, #7
        console.warn('Amy enhancement list 2016-09-27, #7: show tools_for_title_issues if numberOfIssue > 0 [now: ', numberOfIssue, ']');
        $('#tools_for_title_issues').css('display', 'block');
	    }
	    */

      /*
         AJE 2016-10-03 The timeline shows up for titles with no issues, where it has no business being.
          Moved out here with a check on the length of *issueView* instead of *response* or *numberOfIssue*
      */
      if (issueView && issueView.length > 0){
        //console.warn("AJE 2016-10-03 issueView.length=", issueView.length, " ; call drawPie and drawTimeBar because in populateVolumeDetail we have hidden it");
        drawPie(response);
	      drawTimeBar(response, numberOfIssue);
        $('#tools_for_title_issues').css('display', 'block'); // AJE 2016-09-30 for Amy enhancement list 2016-09-27, #7
      } // 2016-10-03 resume Travant original


		drawGlobaledit();

	    call2 = false;
	    hideWaiting();
	} // end populateVolumeDetail



	function getJournalDetail(id) {

      //console.info('search_home.js, getJournalDetail("', id, '")');

	    // AJE 2016-09-15 if search results are in main whitespace area (as per Amy req.) then they need to be hidden first of all
	    // document.getElementById("results").style.visibility = "hidden"; // still takes up space
	    toggle_search_home_title_components('getJournalDetail'); // in ihs_search.js
	    // end AJE 2016-09-15

      //console.info('search_home.js, getJournalDetail has call1 = ', call1, ' ; call2 = ', call2);

	    if (call1 || call2) { return; }
	    call1 = true;
	    call2 = true;

	    globalTitleId=id;
	    showWaiting();

	    document.getElementById("title-col1").innerHTML = ' ';

	    var glbchildNode = document.getElementById("childiedit");
	    glbchildNode.parentNode.removeChild(glbchildNode);
	    var element = document.createElement('div');
	    element.id = "childiedit";
	    document.getElementById("globaledit").appendChild(element);
	    document.getElementById("globaledit").style.marginLeft  = "55px";
	    document.getElementById("globaledit").style.display  = "none";

	    var glbchildCntd = '<div id="glbchildCntd"> test</div>'
      var tpgbedt = new dijit.TitlePane({
        title: "Multi-Record Edit (Choose Volumes and Issues Below)",
        content: glbchildCntd,
        style: "width: 750px;",
        open: false
      });
	    document.getElementById('childiedit').appendChild(tpgbedt.domNode);

	    var childNode = document.getElementById("childissues");
	    childNode.parentNode.removeChild(childNode);
	    var element = document.createElement('div');
	    element.id = "childissues";
	    document.getElementById("issues").appendChild(element);

	    //Add table
	    var tbl = document.createElement("table");
	    tbl.id ="table1"
	    element.appendChild(tbl);

	    // document.getElementById("summary").innerHTML=' ';

      //console.info('search_home.js, dojo.xhrGet "/search/getJournalDetail/', id, '".');
	    dojo.xhrGet({
        handleAs: 'json',
        url: "/search/getJournalDetail/" + id,
        preventCache: true,
        error: function(e) { alert("search_home.js, getJournalDetail Error: " + e.message); },
        load: populateJournalDetail
	    });

      //console.info('search_home.js, dojo.xhrGet "/search/getJournalVolumeDetail/', id, '/',memberid, '".');
      dojo.xhrGet({
        handleAs: 'json',
        url: "/search/getJournalVolumeDetail/" + id + "/" + memberid,
        preventCache: true,
        error: function(e) { alert("search_home.js, getJournalVolumeDetail Error: " + e.message); },
        load: populateVolumeDetail
      });

      //console.info('search_home.js, dojo.xhrGet "/search/getJournalWantStatus/', id, '".');
      dojo.xhrGet({
        handleAs: 'json',
        url: "/search/getJournalWantStatus/" + id ,
        preventCache: true,
        error: function(e) { alert("search_home.js, getJournalWantStatus Error: " + e.message); },
        load:  function(response) {
          if(response.status == 0){
            document.getElementById('want-status').innerHTML = " Title Status Wanted ";
            document.getElementById('want-status').value = 0;
          }else {
            document.getElementById('want-status').innerHTML = " Title Status Not Wanted ";
            document.getElementById('want-status').value = 1;
          }
        }
      });
	}


	function setJournalWantStatus(){
		var status =  document.getElementById('want-status').value;

    dojo.xhrGet({
      handleAs: 'json',
      url: "/search/setJournalWantStatus/" + globalTitleId + "/" + status,
      preventCache: true,
      error: function(e) { alert("search_home.js, setJournalWantStatus, Error: " + e.message); },
      load:  function(response) {
        if (response.status == 0){
          document.getElementById('want-status').innerHTML = " Title Status Wanted "; // Travant original
          document.getElementById('want-status').value = 0;
        } else {
          document.getElementById('want-status').innerHTML = " Title Status Not Wanted "; // Travant original
          document.getElementById('want-status').value = 1;
        }
      }
    });
	}

// AJE 2016-09-21 moved populateSearchList to ihs_search.js, where it can live with searchJournalByTitle, etc.
// AJE 2016-09-21 Travant misspelled function name seachJournalByTitle; appears to be unused: see ihs_search.js : function searchJournalByOCLC
// AJE 2016-09-30 removed seachJournalByTitle
// AJE 2016-09-21 Travant misspelled function name seachJournalByISSN; appears to be unused: see ihs_search.js : function searchJournalByOCLC
// AJE 2016-09-30 removed seachJournalByISSN
// AJE 2016-09-21 Travant misspelled function name seachJournalByOCLC; appears to be unused: see ihs_search.js : function searchJournalByOCLC
// AJE 2016-09-30 removed seachJournalByOCLC

	function collapseAllVolumes() {
		var volDivs = dojo.query(".dijitTitlePane");
		for (var i=0; i < volDivs.length; i++) {
			for (var i=0; i<volDivs.length; i++) {
				var volWidget = dijit.registry.byNode(volDivs[i]);
				if(volWidget.title.search("Vol") != -1) {
  			  if (volWidget.open) { volWidget.toggle(); }
				}
			}
		}
	}


	function expandAllVolumes() {
    var volDivs = dojo.query(".dijitTitlePane");
    for (var i=0; i < volDivs.length; i++) {
      for (var i=0; i<volDivs.length; i++) {
        var volWidget = dijit.registry.byNode(volDivs[i]);
        if(volWidget.title.search("Vol") != -1) {
          if(volWidget.title == "Multi-Record Edit (Choose Volumes and Issues Below)"){
            // AJE 2016-07-20 11:56:41 : this was always an empty block
          } else {
            if (!volWidget.open) volWidget.toggle();
          }
        }
      }
    }
	}


