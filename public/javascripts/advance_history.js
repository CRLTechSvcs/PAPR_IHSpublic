var currentTitle = {};
	var currentTitleChanged = {} ;
	var title="title";
	var prevTitleId, prevTitleVersionId;
	var strLen=25;
	var globalTitleId=0;

	// AJE 2016-09-29 next is Travant original: try dojo/parser instead of dojo[dot]parser
	// require(["dojo/dom", "dojo/domReady!", "dojo.parser", "dijit.form.DateTextBox"]);
	// AJE 2016-09-29 next solved the first error, now it looks like the same again with DateTextBox
	// require(["dojo/dom", "dojo/domReady!", "dojo/parser", "dijit.form.DateTextBox"]);
	require(["dojo/dom", "dojo/domReady!", "dojo/parser", "dijit/form/DateTextBox"]);



	function finalSavePrevTitle(){
		dojo.xhrGet({
	        handleAs: 'json',
	        url: "/advancedEditing/restorePreviousTitle/" + prevTitleId + "/" + prevTitleVersionId,
	        preventCache: true,
	        load: function(data) {

				hideWaiting();
				getJournalDetail(globalTitleId);

	        },
	        error: function(e) {
	        	hideWaiting();
	            alert("advance_history.js, finalSavePrevTitle, Error: " + e.message);
	        }
	    });
	 	$( "#dialog-pre-confirm" ).dialog('close');
	 	showWaiting();

	}

	function cancelSavePrevTitle(){
		try {
			$( "#dialog-pre-confirm" ).dialog('close');
		}catch(err) {

    	}
	}


	function restorePreviousTitle(titleId, titleVersionId){
		prevTitleId = titleId;
		prevTitleVersionId = titleVersionId;
		$(function() {
    			 $( "#dialog-pre-confirm" ).dialog({title:'Confirm Save of Changes to Previous Version', width: 500,  height: 100, modal: true});
  		});
	}

	function drawPreviousTitle(previousTitle){
		var tmptitle = "";
		if(previousTitle.title.length > strLen +1 ){ // AJE 2016-11-22 strLen is global, set at top of this file
			tmptitle = previousTitle.title.substring(0,strLen);
		} else {
			tmptitle = previousTitle.title;
		}

		var innerhtml =
				 		'<div id="current">'+
							'<div class="history-title1">'+
							'<span id="previus-title" title="' + previousTitle.title +'">'+ tmptitle + '</span>'+
						    '</div>'+
							'<div class="history-title2" id="edit-button">'+
								'<a href="javascript:restorePreviousTitle('+ previousTitle.titleId +','+ previousTitle.titleVersionId +');" title="Edit the Current Version"><img src="/assets/images/restore.gif" /></a>'+
							'</div>'+

						'<div class="history-col1">'+
							'<ul class="no-decoration">'+
								'<li class="edit-only"><strong>Title:</strong> <span id="edit-title"></span></li>'+
								'<li class="edit-only"><strong>Alpha Title:</strong> <span id="edit-alphatitle">Africa Today.</span></li>'+

								'<li><strong>Publisher:</strong> <span id="edit-publisher">' + previousTitle.publisherName +'</span></li>'+

								'<li><strong>Print ISSN:</strong> <span id="edit-issn">'+previousTitle.printISSN +'</span></li>'+
								'<li><strong>Electronic ISSN:</strong> <span id="edit-eissn">'+previousTitle.eISSN +'</span></li>'+
								'<li><strong>OCLC Number:</strong> <span id="edit-oclc">'+previousTitle.oclcNumber +'</span></li>'+
								'<li><strong>LCCN:</strong> <span id="edit-lccn">'+previousTitle.lccn +'</span></li>'+
								'<li><strong>Note:</strong> <span id="edit-note">'+previousTitle.note +'</span></li>'+
								'<li><strong>Image Page Ratio:</strong> <span id="edit-note">'+previousTitle.imagePageRatio +'</span></li>'+
								'<li><strong>Publication Range:</strong> '+previousTitle.publicationRange +'</li>'+
								'<li><strong>Publication Language:</strong> <span id="edit-note">'+previousTitle.language +'</span></li>'+
								'<li><strong>Publication Country:</strong> '+previousTitle.country +'</li>'+
							'</ul>'+

						'</div>'+

						'<div class="history-col2" style="padding:0px 10px;">'+
								'<strong>Frequency Ranges:</strong><br /><br />'+
								'<div class="freq_pub_range">'+
								drawPubRange(previousTitle) +
								'</div>'+
							'</div>'+

				'</div>' +
				'<br/><br/><br/><br/><br/><hr />'+
					'&emsp;<strong>Date Changed:'+ previousTitle.changeDate + '</strong>'+
					'&emsp;&emsp;<strong>Changed by User:'+ previousTitle.changeUser + ' </strong>'+
					'&emsp;&emsp;<strong>Changed by Member:'+ previousTitle.changeMember + ' </strong>'+
					'<hr />';

			 return innerhtml;

	}

	function drawCurrentTitle(){
		var tmptitle = "";
		if(currentTitle.title.length > strLen + 1){
			tmptitle = currentTitle.title.substring(0,strLen);
		} else {
			tmptitle = currentTitle.title;
		}

		var publisherName;

		for(var i = 0; i< publisher.length; i++){
			if (publisher[i].id == currentTitle.publisher ){
				publisherName=publisher[i].name;
			}
		}

		var innerhtml =
		  '<div id="current" >'+
      '<div class="history-title1">'+
        '<span id="current-title" title="'+ currentTitle.title +'">'+ tmptitle + '</span>'+
        '&emsp;&emsp;(Current Version)'+
      '</div>';

    innerhtml +=
    '<div class="history-title2" id="edit-button">'+
      '<a href="javascript:drawEditCurrentTitle();" id="edit-current" title="Edit the Current Version">'+
      '<img src="/assets/images/edit.gif" /></a>'+
    '</div>';

    innerhtml +=
    '<div class="history-col1" >'+
      '<ul class="no-decoration">'+
        '<li class="edit-only"><strong>Title:</strong> <span ></span></li>'+
        '<li class="edit-only"><strong>Alpha Title:</strong> <span >Africa Today.</span></li>'+
        '<li><strong>Publisher:</strong> <span >' + publisherName +'</span></li>'+
        '<li><strong>Print ISSN:</strong> <span >'+currentTitle.printISSN +'</span></li>'+
        '<li><strong>Electronic ISSN:</strong> <span >'+currentTitle.eISSN +'</span></li>'+
        '<li><strong>OCLC Number:</strong> <span >'+currentTitle.oclcNumber +'</span></li>'+
        '<li><strong>LCCN:</strong> <span>'+currentTitle.lccn +'</span></li>'+
        '<li><strong>Note:</strong> <span >'+currentTitle.note +'</span></li>'+
        '<li><strong>Image Page Ratio:</strong> <span >'+currentTitle.imagePageRatio +'</span></li>'+
        '<li><strong>Publication Range:</strong> '+currentTitle.publicationRange +'</li>'+
        '<li><strong>Publication Language:</strong> '+currentTitle.language +'</li>'+
        '<li><strong>Publication Country:</strong> '+currentTitle.country +'</li>'+
      '</ul>'+
    '</div>';

      innerhtml +=
        '<div class="history-col2" style="padding:0px 10px;">'+
          '<strong>Frequency Ranges:</strong><br /><br />'+
          '<div class="freq_pub_range">'+ drawPubRange(currentTitle) +'</div>'+
        '</div>'+
				'</div>' + // closes id="current" ?
				'<br/><br/><br/><br/><br/><br/>'+
				'<div>' +
					'<hr/>&emsp;<strong>Date Changed:'+ currentTitle.changeDate + '</strong>'+
					'&emsp;&emsp;<strong>Changed by User:'+ currentTitle.changeUser + ' </strong>'+
					'&emsp;&emsp;<strong>Changed by Member:'+ currentTitle.changeMember + ' </strong>'+
					'<hr />'+
				'</div>';

    return innerhtml;

	}



	function finalSaveCurrentTitle(){
		dojo.rawXhrPost({
      url: "/advancedEditing/postTitle",
      postData: dojo.toJson(currentTitleChanged),
      headers: {
        "Content-Type": "application/json",
        "Accept": "application/json"
      },
      handleAs: "text",

      load: function(data) {
      	hideWaiting();
  	    getJournalDetail(globalTitleId);
      },
      error: function(error) {
        hideWaiting();
        alert("advance_history.js, advancedEditing/postTitle Error:" + error);
      }
    });
    $( "#dialog-cur-confirm" ).dialog('close');
    showWaiting();
	}



	function cancelSaveCurrentTitle(){
		try {
			$( "#dialog-cur-confirm" ).dialog('close');
		} catch(err) {
      alert("advance_history.js, cancelSaveCurrentTitle Error:" + error);
    }
	}

	function saveCurrentTitle(){
    try {
      $( "#dialog" ).dialog('close');
      $( "#dialog-cur-confirm" ).dialog('close');
    } catch(err) {
      alert("advance_history.js, saveCurrentTitle Error:" + error);
    }

		$(function() {
      $( "#dialog-cur-confirm" ).dialog({title:'Confirm Save of Changes to Current Version', width: 500,  height: 100, modal: true});
    });
	}

	function cancelCurrentTitle(){
		try {
			$( "#dialog" ).dialog('close');
			$( "#dialog-cur-confirm" ).dialog('close');
		} catch(err) {
      alert("advance_history.js, cancelCurrentTitle Error:" + error);
    }
		currentTitleChanged = clone(currentTitle);
		document.getElementById('currentTitle').innerHTML =  drawCurrentTitle();
	}

	function updateTitle(field, txt){
		if(field == 'title'){
			currentTitleChanged.title = txt.value;
		}
		if(field == 'publisher'){
			currentTitleChanged.publisher = txt.value;
		}
		if(field == 'alphaTitle'){
			currentTitleChanged.alphaTitle = txt.value;
		}
		if(field == 'printISSN'){
			currentTitleChanged.printISSN = txt.value;
		}
		if(field == 'eISSN'){
			currentTitleChanged.eISSN = txt.value;
		}
		if(field == 'oclcNumber'){
			currentTitleChanged.oclcNumber = txt.value;
		}
		if(field == 'lccn'){
			currentTitleChanged.lccn = txt.value;
		}
		if(field == 'note'){
			currentTitleChanged.note = txt.value;
		}
		if(field == 'ImagePageRatio'){
			currentTitleChanged.ImagePageRatio = txt.value;
		}
		if(field == 'language'){
			currentTitleChanged.language = txt.value;
		}
		if(field == 'country'){
			currentTitleChanged.country = txt.value;
		}
	} // end updateTitle



	function saveChangePubRange(i){
		var pubRange = currentTitle.publicationRangeViews[i];
		var startDate =  $('#startDate1').val();
		var endDate =  $('#endDate1').val();
		if( !validateDate(startDate) ){
			alert( 'Enter a  valid Start Date' );
			return;
		}
		if(endDate != '' ) {
			if( !validateDate(endDate) ){
				alert( 'Enter a valid End Date' );
				return;
			}
		}

		var prdrng = document.getElementById('dialog-select-drop1');
		currentTitleChanged.publicationRangeViews[i].startDate = startDate;
		currentTitleChanged.publicationRangeViews[i].endDate = endDate;
		currentTitleChanged.publicationRangeViews[i].pubRangeId = prdrng.options[prdrng.selectedIndex].value;
		currentTitleChanged.publicationRangeViews[i].status = "change";

		document.getElementById('pubRange').innerHTML = drawEditPubRange(currentTitleChanged);

		$( "#dialog1" ).dialog('close');
	}



	function savePubRange(){
		var startDate =  $('#startDate').val();
		var endDate =  $('#endDate').val();

		if( !validateDate(startDate) ){
			alert( 'Enter a  valid Start Date' );
			return;
		}

		if(endDate != '' ) {
			if( !validateDate(endDate) ){
				alert( 'Enter a valid End Date' );
				return;
			}
		}

		var prdrng  = document.getElementById('dialog-select-drop');

		var obj = { "status": "new", "startDate" : startDate, "endDate" : endDate,  "pubRangeId" : prdrng.options[prdrng.selectedIndex].value }

		currentTitleChanged.publicationRangeViews.push(obj);

		document.getElementById('pubRange').innerHTML = drawEditPubRange(currentTitleChanged);

		$( "#dialog" ).dialog('close');
	}


	function addPubRange(){
		var html = '<select id="dialog-select-drop">';
		for(var i = 0; i <periodicityType.length; i++){
      html += '<option value="'+ periodicityType[i].id  +'" >' + periodicityType[i].name +'</option>';
		}
		html += "</select>";

		document.getElementById('dialog-select').innerHTML =html;

		$(function() {
      $('#startDate').datepicker();
      $('#startDate').datepicker('setDate' , '');
      $('#endDate').datepicker();
      $('#endDate').datepicker('setDate' , '');
      $('#dialog').dialog({title:'New Frequency Ranges', modal: true});
		});

	}

	function changePubRange(index){

		var pubRange = currentTitle.publicationRangeViews[index];

		var html = '<select id="dialog-select-drop1">';
		for(var i = 0; i <periodicityType.length; i++){
			if(pubRange.pubRangeId == periodicityType[i].id ){
				html += '<option value="'+ periodicityType[i].id  +'" selected>' + periodicityType[i].name +'</option>';
			}else{
				html += '<option value="'+ periodicityType[i].id  +'" >' + periodicityType[i].name +'</option>';
			}
		}
		html += "</select>";

		document.getElementById('dialog-select1').innerHTML =html;

		document.getElementById('dialog1-save').innerHTML = '<a href="javascript:saveChangePubRange('+ index + ');" ><img src="/assets/images/add.gif" /></a> ';
		$(function() {
      $('#startDate1').datepicker();
      $('#startDate1').datepicker('setDate' , pubRange.startDate);
      $('#endDate1').datepicker();
      $('#endDate1').datepicker('setDate' , pubRange.endDate);
      $('#dialog1').dialog({title:'Change Frequency Ranges', modal: true});
      });
	}



	function drawEditCurrentTitle(){
			var publisherDrop = '<select onchange="updateTitle(\'publisher\', this.options[this.selectedIndex])">';
			for(var i = 0; i< publisher.length ; i++){
				if(	currentTitle.publisher == publisher[i].id){
					publisherDrop += '<option selected="selected" value="'+ publisher[i].id  +'"> ' + publisher[i].name +'</option>';
				}else{
					publisherDrop += '<option value="'+ publisher[i].id  +'" >' + publisher[i].name +'</option>';
				}
			}
			publisherDrop += "</select>";

			var tmptitle = "";
			if(currentTitle.title.length > strLen+1){
				tmptitle = currentTitle.title.substring(0,strLen);
			}else {
				tmptitle = currentTitle.title;
			}

			var innerhtml =
        '<div id="current">'+
            '<div class="history-title1">'+
              '<span id="current-title">'+ tmptitle + '</span>&emsp;&emsp;(Current Version)'+
            '</div>'+
          '<div class="history-title2" id="edit-button">'+
            '<a href="javascript:saveCurrentTitle();" id="save-current" title="Save the Current Version">'+
              '<img src="/assets/images/save.gif" /></a> &nbsp; &nbsp;'+
            '<a href="javascript:cancelCurrentTitle();" id="cancel-current" title="Cancel the Current Version">'+
              '<img src="/assets/images/cancel.gif" /></a>'+
          '</div>' +

          '<div class="history-col1">'+
          '<ul class="no-decoration">'+
            '<li><strong>Title:</strong> <input type="text" size="15" value ="' + currentTitle.title + '" onkeyup="updateTitle(\'title\', this);"> </li>' +
            '<li><strong>Alpha Title:</strong> <input type="text" size="18" value ="' + currentTitle.alphaTitle + '" onkeyup="updateTitle(\'alphaTitle\', this);"> </li>' +
            '<li><strong>Publisher:</strong> <span id="edit-publisher">' + publisherDrop +'</span></li>'+
            '<li><strong>Print ISSN:</strong> <input type="text" size="18" value ="' + currentTitle.printISSN + '" onkeyup="updateTitle(\'printISSN\', this);"> </li>' +
            '<li><strong>Electronic ISSN:</strong> <input type="text" size="14" value ="' + currentTitle.eISSN + '" onkeyup="updateTitle(\'eISSN\', this);"> </li>' +
            '<li><strong>OCLC Number:</strong> <input type="text" size="15.5" value ="' + currentTitle.oclcNumber + '" onkeyup="updateTitle(\'oclcNumber\', this);"></li>' +
            '<li><strong>LCCN:</strong> <input type="text" size="23" value ="' + currentTitle.lccn + '" onkeyup="updateTitle(\'lccn\', this);"></li>' +
            '<li><strong>Note:</strong> <input type="text" size="23" value ="' + currentTitle.note + '" onkeyup="updateTitle(\'note\', this);"></li>' +
            '<li><strong>Image Page Ratio:</strong> <input type="text" size="10" value ="' + currentTitle.imagePageRatio + '" onkeyup=" updateField(\'imagePageRatio\', this);"></li>' +
            '<li><strong>Publication Language:</strong> <input type="text" size="10" value ="' + currentTitle.language + '" onkeyup="updateTitle(\'language\', this);"></li>' +
            '<li><strong>Publication Country:</strong> <input type="text" size="10" value ="' + currentTitle.country + '" onkeyup="updateTitle(\'country\', this);"></li>' +
            //'<li><strong>Publication Range:</strong> '+currentTitle.publicationRange +'</li>'+
          '</ul>'+
          '</div>'+

  				'<div class="history-col2" style="padding:0px 10px;">'+
    				'<strong>Frequency Ranges:</strong>&emsp;'+
    				'<a href="javascript:addPubRange();"><img src="/assets/images/add.gif" height="15" width="30"></a>'+
            '<br /><br />' +
    				'<div id="pubRange" class="freq_pub_range">'+ drawEditPubRange(currentTitleChanged) + '</div>'+
          '</div>'+
				'</div><br/><br/><br/><br/><br/><br/><br/><br/>'+
				'<hr /> &emsp;<strong>Date Changed: </strong>'+
				'&emsp;&emsp;<strong>Changed by User: </strong>'+
				'&emsp;&emsp;<strong>Changed by Member: </strong>'+
				'<hr />';

			document.getElementById('currentTitle').innerHTML = innerhtml;
	}



	function drawPubRange(currentTitle){
		var pubRange ="";
		var timerange = ' ' ;
		for(var i = 0; i < currentTitle.publicationRangeViews.length; i++) {
			for(var j = 0; j < periodicityType.length; j++){
				if(periodicityType[j].id == currentTitle.publicationRangeViews[i].pubRangeId){
					pubRange = periodicityType[j].name;
				}
			}
      timerange +=
      '<div style="float:left;">' +
        currentTitle.publicationRangeViews[i].startDate +' to ' +
        currentTitle.publicationRangeViews[i].endDate +
      '</div><br/>' +
      '<div style="padding-left:20px;"> ' + pubRange + '</div>' +
      '<hr/>';
    } // end for i
    return timerange;
	}


	function drawEditPubRange(currentTitle){
		var pubRange = "";
		var timerange = ' ' ;
		for(var i = 0; i < currentTitle.publicationRangeViews.length; i++) {
			for(var j = 0; j < periodicityType.length; j++){
				if(periodicityType[j].id == currentTitle.publicationRangeViews[i].pubRangeId){
					pubRange = periodicityType[j].name;
				}
			}
      timerange +=
        '<div style="float:left;">' +
          currentTitle.publicationRangeViews[i].startDate + ' to ' +
          currentTitle.publicationRangeViews[i].endDate +
        '</div> '+
        '<div style="width:100%;text-align:right;">'+
          '<a href="javascript:changePubRange('+i+');"><img src="/assets/images/edit.gif" height="15" width="30"></a><br /><br />' +
        '</div>'+
        '<div style="padding-left:20px;">' + pubRange + '</div>' +
        '<hr/>';
    } // end for i

    return timerange;
	}






  // toggle_search_home_title_components : new function AJE 2016-09-20 : show/hide parts of page in title searches, results of searches, display of bib info summary/'tools'/timeline/volumes/issues
  function toggle_search_home_title_components(calling_function){

    console.log('advance_history.js, toggle_search_home_title_components("',calling_function,'")');

    if (calling_function == 'populateSearchList'){
      // hide these
      document.getElementById('summary').style.display = "none"; // AJE new
      document.getElementById('timeline').style.display = "none"; // AJE new
      document.getElementById('issues').style.display = "none"; // AJE new
      // show these
      document.getElementById('search_results_header').style.display = "block"; // AJE new
      // # results is done in populateSearchList, but sometimes fails to appear, so explicitly do it now
      document.getElementById('results').style.display = "block"; // AJE new
    }
    else if (calling_function == 'clear_search'){ // new block 2016-10-26 : see main.scala.html
      // clear the search boxes and wipe out any search results
      document.getElementById('search_boxes_form').reset();
      document.getElementById('search_results_header').style.display = "none";
      document.getElementById('results').innerHTML = ' ';
    }
    else if (calling_function == 'getJournalDetail') {
    /* else if (calling_function == 'getJournalDetail' ||
      calling_function == 'populateJournalDetail' ||
      calling_function == 'populateVolumeDetail') { */
	    // hide these
	    document.getElementById('search_results_header').style.display = "none";
	    document.getElementById('results').style.display = "none"; // list of titles
	    // show these
	    document.getElementById('summary').style.display = "block";
	    document.getElementById("timeline").style.display = "block";
	    document.getElementById('issues').style.display = "block";
    }
  } // end toggle_search_home_title_components // end AJE 2016-09-20, resume Travant original






	function populateSearchList(response, ioArgs) {
  	  console.info('advance_HISTORY.js: populateSearchList(response=', response, ' ; ioArgs=', ioArgs); // AJE 2016-09-16 testing
      hideWaiting(); // AJE 2016-11-01

	    var results = document.getElementById('results');

    // AJE 2016-11-01 clear unused search boxes
    //console.warn('advance history: populateSearchList: ioArgs.url = "', ioArgs.url, '"; ioArgs.url.indexOf(browseJournalByTitle) == ', ioArgs.url.indexOf('browseJournalByTitle'));
      var search_box = '';
      if(ioArgs.url.indexOf('browseJournalByTitle') != -1 ){
        search_box = document.getElementById('browse_titleid');
      } else if(ioArgs.url.indexOf('containsJournalByTitle') != -1 ){
        search_box = document.getElementById('contains_titleid');
      } else if(ioArgs.url.indexOf('searchJournalByTitle') != -1 ){
        search_box = document.getElementById('titleid');
      } else if(ioArgs.url.indexOf('searchJournalByISSN') != -1 ){
        search_box = document.getElementById('issnid');
      } else if(ioArgs.url.indexOf('searchJournalByOCLC') != -1 ){
        search_box = document.getElementById('oclcid');
      }
      var search_value = search_box.value;
      // end AJE 2016-11-01

	    var ul = document.getElementById('search-list');
	    if (ul != null) {
	        results.removeChild(ul);
	    }
	    ul = document.createElement('ul');
	    ul.setAttribute('id', 'search-list');

      /* AJE 2016-11-01 handle searches where no results were found */
      if ( (response.items.length < 1) ||
           (response.items.length == 1 && response.items[0].titleId == 0)) {
        var li = document.createElement('li');
        li.innerHTML = "<h2>No results were found for '"+search_value+"'</h2>";
        ul.appendChild(li);
      } else {
  	    for (i = 0; i < response.items.length; i++) {
          var li = document.createElement('li');
          var a = document.createElement('a');

          var display_title = response.items[i].title + " / " + response.items[i].publisher;  // AJE 2016-11-01
          a.innerHTML = display_title; // AJE 2016-11-01
          a.setAttribute('href', 'javascript:getJournalDetail(' + response.items[i].titleId + ');');
          a.setAttribute('title', response.items[i].title);
          li.appendChild(a);
          ul.appendChild(li);
        } // end for
      } // end else
	    results.appendChild(ul);
      results.style.display = "block"; // ... this; now do some more toggling, call new AJE function, in ihs_search.js
      toggle_search_home_title_components('populateSearchList');
      // AJE : resume Travant original
	}





	function getJournalDetail(titleid){
	  console.info('advance_HISTORY.js, getJournalDetail(', titleid, ')');
    dojo.xhrGet({
      handleAs: 'json',
      url: "/advancedEditing/GetTitles/" + titleid,
      preventCache: true,
      error: function(e) {
        alert("advance_history.js, GetTitles Error: " + e.message);
      },
      load: populateJournalDetail
    });
    globalTitleId = titleid;
    showWaiting();
	}



	function updateField(field, obj){
		var value= obj.value;

		if(!$.isNumeric(obj.value)){
			obj.value = '';
			value=0;
		}

		if(field == 'imagePageRatio'){
    	if(value>100){
  		  obj.value='100';
  		  value=100;
    	} else if(value<0){
    		obj.value='0';
    		value=0;
    	}
    	currentTitleChanged.imagePageRatio = value;
		}
	}