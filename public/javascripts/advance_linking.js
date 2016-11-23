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
      	getJournalDetail(globalTitleId);
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
      	getJournalDetail(globalTitleId);
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
      	getJournalDetail(globalTitleId);
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
      	getJournalDetail(globalTitleId);
      }
	  });
		showWaiting();
	}

	function drawLinkView( response, ioArgs ){
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



  // toggle_search_home_title_components : new function AJE 2016-09-20 : show/hide parts of page in title searches, results of searches, display of bib info summary/'tools'/timeline/volumes/issues







	function populateSearchList(response, ioArgs) {
  	  console.info('advance_LINKING.js: populateSearchList(response=', response, ' ; ioArgs=', ioArgs); // AJE 2016-09-16 testing
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
	  console.info('advance_LINKING.js, getJournalDetail(', titleid, ')');
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



	function addLink(titleid){
		$(function() {
		  $( "#dialog" ).dialog({title: 'Search a Title', width: 600, modal: true});
		});
	}



