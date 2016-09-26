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



  // toggle_search_home_title_components : new function AJE 2016-09-20 : show/hide parts of page in title searches, results of searches, display of bib info summary/'tools'/timeline/volumes/issues
  function toggle_search_home_title_components(calling_function){
    if (calling_function == 'populateSearchList'){
      // hide these
      document.getElementById('summary').style.display = "none"; // AJE new
      console.info('toggle_...',calling_function,' just set #summary to ', document.getElementById('summary').style.display);
      document.getElementById("tools_for_title_issues").style.display = "none";
      console.info('toggle_...',calling_function,' just set #tools_for_title_issues to ', document.getElementById('tools_for_title_issues').style.display);
      document.getElementById('timeline').style.display = "none"; // AJE new
      console.info('toggle_...',calling_function,' just set #timeline to ', document.getElementById('timeline').style.display);
      document.getElementById('issues').style.display = "none"; // AJE new
      console.info('toggle_...',calling_function,' just set #issues to ', document.getElementById('issues').style.display);
      // show these
      document.getElementById('search_results_header').style.display = "block"; // AJE new
      console.warn('toggle_...',calling_function,' just set #search_results_header to ', document.getElementById('search_results_header').style.display);
      document.getElementById('tools_for_title_issues').style.display = "block"; // AJE new
      console.warn('toggle_...',calling_function,' just set #tools_for_title_issues to ', document.getElementById('tools_for_title_issues').style.display);
      // # results is done in populateSearchList, but sometimes fails to appear, so explicitly do it now
      document.getElementById('results').style.display = "block"; // AJE new
      console.warn('toggle_...',calling_function,' just set #results to ', document.getElementById('tools_for_title_issues').style.display);
    }
    else if (calling_function == 'getJournalDetail'){
	    // hide these
	    document.getElementById("search_results_header").style.display = "none";
      console.info('toggle_...',calling_function,' just set #search_results_header to ', document.getElementById('search_results_header').style.display);
	    document.getElementById("results").style.display = "none"; // list of titles
      console.info('toggle_...',calling_function,' just set #results to ', document.getElementById('results').style.display);
	    // show these
	    document.getElementById("summary").style.display = "block";
      console.warn('toggle_...',calling_function,' just set #summary to ', document.getElementById('summary').style.display);
	    document.getElementById("tools_for_title_issues").style.display = "block";
      console.warn('toggle_...',calling_function,' just set #tools_for_title_issues to ', document.getElementById('tools_for_title_issues').style.display);
	    document.getElementById("timeline").style.display = "block";
      console.warn('toggle_...',calling_function,' just set #timeline to ', document.getElementById('timeline').style.display);
	    document.getElementById("issues").style.display = "block";
      console.warn('toggle_...',calling_function,' just set #issues to ', document.getElementById('issues').style.display);
    }
  } // end toggle_search_home_title_components // end AJE 2016-09-20, resume Travant original


  // AJE 2016-09-21 populateSearchList used to live in search_home.js
	function populateSearchList(response, ioArgs) {

	  //console.info('populateSearchList(response=', response, ' ; ioArgs=', ioArgs); // AJE 2016-09-16 testing

    var results = document.getElementById('results');

    var ul = document.getElementById('search-list');
    if (ul != null) { results.removeChild(ul); }
    ul = document.createElement('ul');
    ul.setAttribute('id', 'search-list');

    for (i = 0; i < response.items.length; i++) {
      var li = document.createElement('li');
      var a = document.createElement('a');

      a.innerHTML = response.items[i].title;
      console.log('response.items[',i,'].title: "', response.items[i].title);// AJE 2016-09-20
      a.setAttribute('href', 'javascript:getJournalDetail(' + response.items[i].titleId + ');');
      a.setAttribute('title', response.items[i].title);
      li.appendChild(a);
      ul.appendChild(li);
    }
    results.appendChild(ul);

    //results.style.visibility = "visible"; // AJE 2016-09-16 change this line from Travant ... to
    results.style.display = "block"; // ... this; now do some more toggling, call new AJE function, in ihs_search.js
    toggle_search_home_title_components('populateSearchList');
    // AJE : resume Travant original

	}// end populateSearchList, moved by AJE from search_home.js 2016-09-21



	function searchJournalByTitle(search) {

    // AJE testing 2016-09-21
    //console.info('searchJournalByTitle(',search,')'); // <input type="text" id="titleid" onkeyup="searchJournalByTitle(this);" class="search">
    console.info('searchJournalByTitle(',search.value,')');

		var results = document.getElementById('results');
		var issn = document.getElementById('issnid');
		issn.value="";

		var value = search.value.replace('\\', ' ').replace('\/', ' ').replace('  ', ' ');

		var value1 = search.value.replace(' ', '');

	    if (value.length < 2 ) {
	        //results.innerHTML = ' '; // Travant
	        console.log = 'searchJournalByTitle, value.length < 2 [no results found for '+value+' yet]'); // AJE 2016-09-21
	    } else {
          /*
            AJE 2016-09-21 DEVNOTE: why is this is condition even here?
            part A: if the remainder of [search term with its spaces replaced] / 3 is 0
            or
            part B: if there is a space at the end of the search term
          */
	        if (value1.length % 3  == 0 || search.value.charAt(search.value.length-1) == ' ') {
	            dojo.xhrGet({
	                handleAs: 'json',
	                url: "/search/searchJournalByTitle/" + value,
	                preventCache: true,
	                error: function(e) {
	                    alert("Error: " + e.message);
	                },
	                load: populateSearchList
	            });
	        } // end questionable IF
	    }
	}

	function searchJournalByISSN(search) {

	  var results = document.getElementById('results');

	  var title = document.getElementById('titleid');
	  title.value="";


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

		  var results = document.getElementById('results');

		  var title = document.getElementById('titleid');
		  title.value="";

		  var issn = document.getElementById('issnid');
			issn.value="";

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