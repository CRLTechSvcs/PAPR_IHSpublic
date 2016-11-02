	require(["dijit/form/ComboBox", "dojo/store/Memory", "dojo/dom", "dojo/domReady!"], function(ComboBox, Memory,  dom) {

				//

				 var stateStore = new Memory({
    			    data: [
            			{name:"", id:""}
        				]
    				});


				//
				var select = ComboBox({
					name: "stateMember",
					id: "stateMember",
					placeHolder: "",
					store: stateStore,
					style: "width: 127px; height: 19px;",
					onChange: function(){
						var val = dijit.byId("stateMember").get("value");
						memberid=0;
						for (var index = 0, len = members.length; index < len; ++index) {
							if(members[index].name == val){
								memberid = members[index].id;
							}
						}
					}
				}, "member");
				select.startup();

				dojo.xhrGet({
	       			 handleAs: 'json',
	       				 url: "/search/getMembers",
	       				 preventCache: false,
	        	   		 error: function(e) {
	            			alert("Error: " + e.message);
	        		},
	        		load: populateMember
	    		});
	});


	function populateMember(response, ioArgs){

		var test= response;

		var store = new dojo.store.Memory(response)
	    dijit.byId("stateMember").store = store;
	    members=response.data;

	}





// AJE 2016-10-27 clearUnusedSearchFields is new: searchJournalByTitle etc. will clear the fields we're not searching on
function clearUnusedSearchFields(calling_function){

  //console.log('clearUnusedSearchFields("',calling_function,'")');

  if (calling_function != 'browseJournalByTitle') {
	  var target_widget = document.getElementById('browse_titleid');
	  target_widget.value="";
  }
  if (calling_function != 'containsJournalByTitle') {
	  var target_widget = document.getElementById('contains_titleid');
		target_widget.value="";
  }
  if (calling_function != 'searchJournalByTitle') {
	  var target_widget = document.getElementById('titleid');
	  target_widget.value="";
  }
  if (calling_function != 'searchJournalByISSN') {
	  var target_widget = document.getElementById('issnid');
		target_widget.value="";
  }
  if (calling_function != 'searchJournalByOCLC') {
	  var target_widget = document.getElementById('oclcid');
		target_widget.value="";
  }
}//end clearUnusedSearchFields




  // toggle_search_home_title_components : new function AJE 2016-09-20 : show/hide parts of page in title searches, results of searches, display of bib info summary/'tools'/timeline/volumes/issues
  function toggle_search_home_title_components(calling_function){
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
      document.getElementById('results').innerHTML = ' ';
    }
    else if (calling_function == 'getJournalDetail'){
	    // hide these
	    document.getElementById('search_results_header').style.display = "none";
	    document.getElementById('results').style.display = "none"; // list of titles
	    // show these
	    document.getElementById('summary').style.display = "block";
	    document.getElementById("timeline").style.display = "block";
	    document.getElementById('issues').style.display = "block";
    }
  } // end toggle_search_home_title_components // end AJE 2016-09-20, resume Travant original


  // AJE 2016-09-21 populateSearchList used to live in search_home.js
	function populateSearchList(response, ioArgs) {

	  console.info('populateSearchList(response=', response, ' ; ioArgs=', ioArgs); // AJE 2016-09-16 testing
    hideWaiting(); // AJE 2016-10-27

    var results = document.getElementById('results');

    // AJE 2016-11-01 to give proper 'no results found' message
    console.warn('ihs_search: populateSearchList: ioArgs.url = "', ioArgs.url, '"; ioArgs.url.indexOf(browseJournalByTitle) == ', ioArgs.url.indexOf('browseJournalByTitle'));
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
    if (ul != null) { results.removeChild(ul); }
    ul = document.createElement('ul');
    ul.setAttribute('id', 'search-list');

    /* AJE 2016-10-26 handle searches where no results were found */
    //if (response.items.length < 1){
    if ( (response.items.length < 1) ||
         (response.items.length == 1 && response.items[0].titleId == 0)) {
      var li = document.createElement('li');
      li.innerHTML = "<h2>No results were found for '"+search_value+"'</h2>";
      ul.appendChild(li);
    } else {
      for (i = 0; i < response.items.length; i++) {
        var li = document.createElement('li');
        var a = document.createElement('a');

        //console.log(i +') response.items[',i,'].title: "', response.items[i].title, ' *.publisher: "', response.items[i].publisher, ' ; response.items[i] = ', response.items[i]);// AJE 2016-09-20
        var display_title = response.items[i].title + " / " + response.items[i].publisher;  // AJE 2016-10-25
        //a.innerHTML = response.items[i].title; // Travant original
        a.innerHTML = display_title; // AJE 2016-10-25
        a.setAttribute('href', 'javascript:getJournalDetail(' + response.items[i].titleId + ');');
        //a.setAttribute('title', response.items[i].title); // Travant original
        a.setAttribute('title', display_title); // AJE 2016-10-25
        li.appendChild(a);
        ul.appendChild(li);
      } // end for
    } // end else

    results.appendChild(ul);

    //results.style.visibility = "visible"; // AJE 2016-09-16 change this line from Travant ... to
    results.style.display = "block"; // ... this; now do some more toggling, call new AJE function, in ihs_search.js
    toggle_search_home_title_components('populateSearchList');
    // AJE : resume Travant original

	}// end populateSearchList, moved by AJE from search_home.js 2016-09-21


  /*
    AJE 2016-10-24 searchJournalByTitle uses app/controllers/SearchJournals.java > searchJournalByTitle
    until 10-26: main.scala.html called searchJournalByTitle from: <input type="text" id="titleid" onkeyup="searchJournalByTitle(this);" class="search">
  */
	function searchJournalByTitle(search) {
    console.info('searchJournalByTitle(',search.value,')');

    clearUnusedSearchFields('searchJournalByTitle');
    showWaiting();

		var results = document.getElementById('results');
		var value = search.value.replace('\\', ' ').replace('\/', ' ').replace('  ', ' ');
		var value1 = search.value.replace(' ', '');

    if (value.length < 2 ) {
        //results.innerHTML = ' '; // Travant
        //console.log('searchJBT, value.length < 2 [no results found for '+value+' yet]'); // AJE 2016-09-21
    } else {
      console.log('searchJBT, value.length = ',value.length,', should search'); // AJE 2016-10-27
        /*
          AJE 2016-09-21 DEVNOTE: why is this is condition even here?
          part A: if the remainder of [search term with its spaces replaced] / 3 is 0
          or
          part B: if there is a space at the end of the search term;
          2016-10-27 removed it
        */
    //if (value1.length % 3  == 0 || search.value.charAt(search.value.length-1) == ' ') {
        dojo.xhrGet({
            handleAs: 'json',
            url: "/search/searchJournalByTitle/" + value,
              //  app/controllers/SearchJournals.java
            preventCache: true,
            error: function(e) {
                alert("Error: " + e.message);
            },
            load: populateSearchList
        });
    //} // end questionable IF 2016-10-27
    }
	} // end Travant's searchJournalByTitle

	/*****************************************************
	AJE 2016-10-24 : browseJournalByTitle is a copy of searchJournalByTitle; uses app/controllers/SearchJournals.java > browseJournalByTitle
	*/
	function browseJournalByTitle(search) {
    console.info('browseJournalByTitle("',search.value,'")');

    clearUnusedSearchFields('browseJournalByTitle');
    showWaiting();

		var results = document.getElementById('results');
		var value = search.value.replace('\\', ' ').replace('\/', ' ').replace('  ', ' ');
		var value1 = search.value.replace(' ', '');

    if (value.length < 2 ) {
        //results.innerHTML = ' '; // Travant
        //console.log('browseJBT, value.length < 2 [no results found for '+value+' yet]'); // AJE 2016-09-21
    } else {
console.log('browseJBT, value.length = ',value.length,', should search'); // AJE 2016-10-27
    // AJE 2016-10-27 remove Travant's IF
    // if (value1.length % 3  == 0 || search.value.charAt(search.value.length-1) == ' ') {
        dojo.xhrGet({
            handleAs: 'json',
            url: "/search/browseJournalByTitle/" + value,
              //  app/controllers/SearchJournals.java
            preventCache: true,
            error: function(e) {
                alert("browseJournalByTitle has Error: " + e.message);
            },
            load: populateSearchList
        });
    // } // end questionable IF 2016-10-27
    }
	} // end AJE 2016-10-24 browseJournalByTitle
	/*****************************************************
	AJE 2016-10-27 : containsJournalByTitle is a copy of searchJournalByTitle; uses app/controllers/SearchJournals.java > containsJournalByTitle
	*/
	function containsJournalByTitle(search) {
    console.info('containsJournalByTitle("',search.value,'")');

    clearUnusedSearchFields('containsJournalByTitle');
    showWaiting();

		var results = document.getElementById('results');
		var value = search.value.replace('\\', ' ').replace('\/', ' ').replace('  ', ' ');
		var value1 = search.value.replace(' ', '');
    if (value.length < 2 ) {
        //console.log('containsJBT, value.length < 2 [no results found for '+value+' yet]'); // AJE 2016-09-21
    } else {
      console.log('containsJBT, value.length = ',value.length,', should search'); // AJE 2016-10-27
      dojo.xhrGet({
          handleAs: 'json',
          url: "/search/containsJournalByTitle/" + value,
          preventCache: true,
          error: function(e) {
              alert("containsJournalByTitle has Error: " + e.message);
          },
          load: populateSearchList
      });
    }
  } // end AJE 2016-10-27 containsJournalByTitle



	function searchJournalByISSN(search) {
	  clearUnusedSearchFields('searchJournalByISSN');
	  showWaiting();

	  var results = document.getElementById('results');

    var st = search.value.replace('-', '');
    if (st.length < 2) {
        results.innerHTML= ' ';
    } else {
        if (st.length == 8) {
            dojo.xhrGet({
                handleAs: 'json',
                url: "/search/searchJournalByISSN/" + st,
                preventCache: true,
                error: function(e) {
                    alert("Error: " + e.message);
                },
                load: populateSearchList
            });
        }
    }
	}

function searchJournalByOCLC(search) {
  clearUnusedSearchFields('searchJournalByOCLC');
  showWaiting();

  var results = document.getElementById('results');

  var st = search.value;
  if (st.length < 2) {
      results.innerHTML= ' ';
  } else {
      if (st.length > 4) {
          dojo.xhrGet({
              handleAs: 'json',
              url: "/search/searchJournalByOCLC/" + st,
              preventCache: true,
              error: function(e) {
                  alert("Error: " + e.message);
              },
              load: populateSearchList
          });
      }
  }
}



/************************************************************************
AJE 2016-10-27 moved this section here from main.scala.html, because advanced_editing_history.scala.html (and others?) need it too
  AJE 2016-10-26 -- capture key events for search boxes, only trigger search when space/enter key found
*/
$(document).ready(function() {
  $("#browse_titleid").keydown(function(e){ // http://stackoverflow.com/questions/3462995/jquery-keydown-keypress-keyup-enterkey-detection#3463044
      var code = e.which || e.keyCode; // recommended to use e.which, it's normalized across browsers; similar to event.keycode
      //if(code==13)e.preventDefault(); // 13 is the enter key; 32 is space
      // 2016-10-27 separate if clauses to check on some weirdness
      if(code==13){
        //console.info('TBR keydown function got key 13: "enter"; pass entire form object, with doc.get.');
        browseJournalByTitle(document.getElementById('browse_titleid'));
      }
      if(code==32){
        //console.info('TBR keydown function got key 32: "space"; pass entire form object, with doc.get.');
        browseJournalByTitle(document.getElementById('browse_titleid'));
      }
  });
  $("#contains_titleid").keydown(function(e){
      var code = e.which || e.keyCode;
      if(code==13){
        //console.info('TCT keydown function got key 13: "enter"; pass entire form object, with doc.get.');
        containsJournalByTitle(document.getElementById('contains_titleid'));
      }
      if(code==32){
        //console.info('TCT keydown function got key 32: "space"; pass entire form object, with doc.get.');
        containsJournalByTitle(document.getElementById('contains_titleid'));
      }
  });
  $("#titleid").keyup(function(e){
      var code = e.which || e.keyCode;
      if(code==13){
        //console.info('KWD keyup function got key 13: "enter"; pass entire form object, with doc.get.');
        searchJournalByTitle(document.getElementById('titleid'));
      }
      if(code==32){
        //console.info('KWD keyup function got key 32: "space"; pass entire form object, with doc.get.');
        searchJournalByTitle(document.getElementById('titleid'));
      }
  });
  $("#issnid").keyup(function(e){
      var code = e.which || e.keyCode;
      if(code==13){
        //console.info('ISSN keyup function got key 13: "enter"; pass entire form object, with doc.get.');
        searchJournalByISSN(document.getElementById('issnid'));
      }
      if(code==32){
       //console.info('ISSN keyup function got key 32: "space"; pass entire form object, with doc.get.');
        searchJournalByISSN(document.getElementById('issnid'));
      }
  });
  $("#oclcid").keyup(function(e){
      var code = e.which || e.keyCode;
      if(code==13){
        //console.info('OCLC keyup function got key 13: "enter"; pass entire form object, with doc.get.');
        searchJournalByOCLC(document.getElementById('oclcid'));
      }
      if(code==32){
        //console.info('OCLC keyup function got key 32: "space"; pass entire form object, with doc.get.');
        searchJournalByOCLC(document.getElementById('oclcid'));
      }
  });
}); // close doc.ready



