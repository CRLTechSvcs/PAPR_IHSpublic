	var flipsarch = true;
	// AJE 2016-11-22 yes, I absolutely am not kidding, Travant delivered this product with variables 'globaltitleid' and 'globalTitleId', both set to zero
	var globaltitleid = 0; // Travant original
	var globalTitleId = 0; // Travant original


	function addParent(partitleid){
		dojo.xhrGet({
      handleAs: 'json',
      url: "/advancedEditing/addParent/" + globaltitleid +"/"+ partitleid,
      preventCache: true,
      error: function(e) {
      	hideWaiting();
          alert("advance_linking.js, addParent Error: " + e.message);
      },
      load: function (response, ioArgs){
      	hideWaiting();
      	getJournalDetail_adv_link(globalTitleId);
      }
	  });
		$( "#dialog" ).dialog('close');
		showWaiting();
	}

	function addChild(chititleid){
		dojo.xhrGet({
      handleAs: 'json',
      url: "/advancedEditing/addChild/" + globaltitleid +"/"+ chititleid,
      preventCache: true,
      error: function(e) {
      	hideWaiting();
          alert("advance_linking.js, addChild Error: " + e.message);
      },
      load: function (response, ioArgs){
      	hideWaiting();
      	getJournalDetail_adv_link(globalTitleId);
      }
    });
		$( "#dialog" ).dialog('close');
		showWaiting();
	}



	function removeParent(parentTitleid){
		var id = 'parent'+parentTitleid;
		document.getElementById(id).innerHTML = 'Removed';
		dojo.xhrGet({
      handleAs: 'json',
      url: "/advancedEditing/removeParent/" + globaltitleid + "/" + parentTitleid,
      preventCache: true,
      error: function(e) {
      	hideWaiting();
          alert("advance_linking.js, removeParent Error: " + e.message);
      },
      load: function (response, ioArgs){
      	hideWaiting();
      	getJournalDetail_adv_link(globalTitleId);
      }
	  });
		showWaiting();
	}


	function removeChild(childTitleid){
		var id = 'child'+childTitleid;
		document.getElementById(id).innerHTML = 'Removed';
		dojo.xhrGet({
      handleAs: 'json',
      url: "/advancedEditing/removeChild/" +  globaltitleid + "/" + childTitleid,
      preventCache: true,
      error: function(e) {
      	hideWaiting();
          alert("advance_linking.js, removeChild Error: " + e.message);
      },
      load: function (response, ioArgs){
      	hideWaiting();
      	getJournalDetail_adv_link(globalTitleId);
      }
	  });
		showWaiting();
	}



	function drawLinkView( response, ioArgs ){

    console.info('advance_linking.js, drawLinkView( response=', response, ' ; ioArgs=', ioArgs, ')');

    var html  = '<br /><br /><br /><br /><br /> '+
      '<h2>'+ response.title +'</h2>' +
      '<br /><br />'+ response.publicationRange + '<br /><br /> ' +
      '<a href="javascript:addLink(' + response.titleId+ ');" id="add-new-link"><img src="/assets/images/addlink.gif" title="Add a New Link to this Title"/></a>' ;

    var htmlparent = '<h2>Parents / Preceeding Titles</h2><div class="link-list">';

    for( var i=0 ; i < response.parents.length; i++){
      htmlparent +=
      '<div class="link-item">'+
        '<strong>'+ response.parents[i].title + '</strong>' +
        '<br />' + response.parents[i].publicationRange + '<br />' +
        '<div id="parent'+ response.parents[i].titleId +'">'+
          '<a href="javascript:removeParent('+ response.parents[i].titleId +');">' +
          '<img src="/assets/images/remove.gif">'+
          '</a>' +
        '</div>' +
      '</div> ';
    }
    htmlparent += '</div>';

    var htmlchild = '<h2>Children / Succeeding Titles</h2>' +
    '<div class="link-list">' ;

	  for( var i = 0 ; i < response.childs.length; i++){
	   	htmlchild +=
	   	'<div class="link-item" >'+
        '<strong>'+ response.childs[i].title + '</strong>' +
        '<br />' + response.childs[i].publicationRange + '<br />'+
          '<div id="child'+ response.childs[i].titleId +'">'+
            '<a href="javascript:removeChild('+ response.childs[i].titleId +');">' +
              '<img src="/assets/images/remove.gif">' +
            '</a>' +
          '</div>'+
        '</div> ';
	  }

	  htmlchild+='</div> ';

	  document.getElementById('links-current').innerHTML = html;
	  document.getElementById('links-parents').innerHTML = htmlparent;
	  document.getElementById('links-children').innerHTML = htmlchild;

    document.getElementById('search').style.display = 'none';
 	  document.getElementById('link').style.display = 'block';

 	  hideWaiting();

	}




	function populateSearchList_adv_link(response, ioArgs) { // AJE 2016-11-23 may not actually get called

	  console.info('populateSearchList_adv_link(response=', response, ' ; ioArgs=', ioArgs, '): flipsarch = ', flipsarch, '.'); // AJE 2016-09-16 testing
    hideWaiting(); // AJE 2016-11-23

	  if(flipsarch){
  	  console.info('populateSearchList_adv_link, flipsarch block with flipsarch = ', flipsarch, '.'); // AJE 2016-09-16 testing

	    var results  = document.getElementById('results');
	    results.style.visibility = "visible";

	    var ul = document.getElementById('search-list');
	    if (ul != null) {
	        results.removeChild(ul);
	    }
	    ul = document.createElement('ul');
	    ul.setAttribute('id', 'search-list');

	    for (i = 0; i < response.items.length; i++) {
console.info('populateSearchList_adv_link, for response.items ', i,' of ', response.items.length,'.'); // AJE 2016-09-16 testing
        var li = document.createElement('li');
        var a = document.createElement('a');

        //a.innerHTML = response.items[i].title;// Travant original
        var display_title = response.items[i].title + " / " + response.items[i].publisher;  // AJE 2016-11-29
        a.innerHTML = display_title; // AJE 2016-11-29
        a.setAttribute('href', 'javascript:getJournalDetail_adv_link(' + response.items[i].titleId + ');');
        a.setAttribute('title', response.items[i].title);
        li.appendChild(a);
        ul.appendChild(li);
	    }

	    results.appendChild(ul);

	  } else { // not flipsarch
	  	 var html = "";
	  	 for (i = 0; i < response.items.length; i++) {
	  	 	if(response.items[i].titleId != globaltitleid){
	  	 		html +=
	  	 		'<a href="javascript:addParent('+ response.items[i].titleId +');">' +
	  	 		  '<img src="/assets/images/parent.gif">&nbsp;</a>' +
	  	 		'<a href="javascript:addChild('+ response.items[i].titleId +');">' +
	  	 		  '<img src="/assets/images/child.gif"></a>&nbsp; &nbsp;' +
	  	 		  response.items[i].title +'<br/>';
	  	 	}
	  	 }

	     document.getElementById('dialogue-results').innerHTML = html;
	  }
	} // end populateSearchList_adv_link





	function populateJournalDetail_adv_link(response, ioArgs){
    console.info('populateJournalDetail_adv_link(response=', response, ', ioArgs=', ioArgs, ')');

		var html ="";

		for (var i=0; i < response.length; i++) {
			if( response[i].currentVersionFlag == "Y"){
				currentTitle = response[i];
				currentTitleChanged = clone(currentTitle);

        // AJE 2016-11-28 : next is fine for advance_history, not here
				//document.getElementById('currentTitle').innerHTML =  drawCurrentTitle(); // ihs_search.js
        //console.info('populateJournalDetail_adv_link is not filling #currentTitle.innerHTML because no such element.');

        document.getElementById('links').innerHTML =  drawLinkView(response, ioArgs); // advance_linking.js
        console.info('populateJournalDetail_adv_link is filling #currentTitle.innerHTML because no such element.');

			} else {
				var preTitle = response[i];
				html += drawPreviousTitle(preTitle);
			} // end if/else
		} // end for

		document.getElementById('search').style.display = 'none';
		/* AJE 2016-11-28
		document.getElementById('currentTitle').style.display = 'block';
		document.getElementById('previousTitle').innerHTML =  html;
		document.getElementById('previousTitle').style.display = 'block'; */
		document.getElementById('links').innerHTML =  html;
		document.getElementById('links').style.display = 'block';
		document.getElementById('links').style.display = 'block';
    // end AJE 2016-11-28

		hideWaiting();

	}



	function getJournalDetail_adv_link(titleid){ // AJE 2016-11-23 renamed this function to reflect its location
    console.info('getJournalDetail_adv_link(', titleid, ')');
	  flipsarch = false;
	  globalTitleId=titleid;
	  globaltitleid = titleid;

  	dojo.xhrGet({
        handleAs: 'json',
        url: "/advancedEditing/getLinkView/" + titleid,
        preventCache: true,
        error: function(e) {
            alert("advance_linking.js, getLinkView Error: " + e.message);
        },
        load: drawLinkView
    });

    showWaiting();
	}


	function addLink(titleid){
		$(function() {
		  $( "#dialog" ).dialog({title: 'Search a Title', width: 600, modal: true});
		});
	}



