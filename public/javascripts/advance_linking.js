	var flipsarch=true;
	var globaltitleid=0;
	var globalTitleId=0;


	function addParent(partitleid){

		dojo.xhrGet({

	        handleAs: 'json',
	        url: "/advancedEditing/addParent/" + globaltitleid +"/"+ partitleid,
	        preventCache: true,
	        error: function(e) {
	        	hideWaiting();
	            alert("Error: " + e.message);
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
	            alert("Error: " + e.message);
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
	            alert("Error: " + e.message);
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
	            alert("Error: " + e.message);
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


		   var htmlparent = '<h2>Parents / Preceeding Titles</h2>' +
		   					 '<div class="link-list">';


		   for( var i =0 ; i < response.parents.length; i++){

		   	htmlparent += '<div class="link-item" > '+
								 ' <strong> '+ response.parents[i].title + '</strong><br /> '+
								   response.parents[i].publicationRange + '<br />'+
								   '<div id="parent'+ response.parents[i].titleId +'">'+
								 		'<a href="javascript:removeParent('+ response.parents[i].titleId +');"><img src="/assets/images/remove.gif"> </a>' +
								   '</div> ' +
							'</div> ';
			}
		   htmlparent+='</div> ';

		  var htmlchild='<h2> Children / Succeeding Titles </h2>' +
		  				'<div class="link-list" > ' ;

		  for( var i =0 ; i < response.childs.length; i++){

		   	htmlchild +=  '<div class="link-item" > '+
								 ' <strong> '+ response.childs[i].title + '</strong><br /> '+
								   response.childs[i].publicationRange + '<br />'+
								  '<div id="child'+ response.childs[i].titleId +'">'+
								     '<a href="javascript:removeChild('+ response.childs[i].titleId +');"><img src="/assets/images/remove.gif"> </a>' +
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

	function getJournalDetail(titleid){

	  flipsarch = false;

	  globalTitleId=titleid;

	  globaltitleid = titleid;

	  	dojo.xhrGet({
	        handleAs: 'json',
	        url: "/advancedEditing/getLinkView/" + titleid,
	        preventCache: true,
	        error: function(e) {
	            alert("Error: " + e.message);
	        },
	        load: drawLinkView
	    });

	  	showWaiting();
	}

	function populateSearchList(response, ioArgs) {

	  if(flipsarch){
	    var results  = document.getElementById('results');

	    results.style.visibility = "visible";


	    var ul = document.getElementById('search-list');

	    if (ul != null) {
	        results.removeChild(ul);
	    }

	    ul = document.createElement('ul');
	    ul.setAttribute('id', 'search-list');

	    for (i = 0; i < response.items.length; i++) {
	        var li = document.createElement('li');
	        var a = document.createElement('a');

	        a.innerHTML = response.items[i].title;
	        a.setAttribute('href', 'javascript:getJournalDetail(' + response.items[i].titleId + ');');
	        a.setAttribute('title', response.items[i].title);
	        li.appendChild(a);
	        ul.appendChild(li);
	    }

	    results.appendChild(ul);
	  }else{
	  	 var html = "";

	  	 for (i = 0; i < response.items.length; i++) {

	  	 	if(response.items[i].titleId != globaltitleid){
	  	 		html+='<a href="javascript:addParent('+ response.items[i].titleId +');"> <img src="/assets/images/parent.gif"> &nbsp; </a>' +
	  	 			  '<a href="javascript:addChild('+ response.items[i].titleId +');"> <img src="/assets/images/child.gif"> </a>&nbsp; &nbsp;' +
	  	 			  response.items[i].title +
	  	 		 '<br>';
	  	 	}
	  	 }

	  	  document.getElementById('dialogue-results').innerHTML = html;
	  }
	}

	function addLink(titleid){
		$(function() {
				  $( "#dialog" ).dialog({title: 'Search a Title', width: 600, modal: true});

  			});
	}