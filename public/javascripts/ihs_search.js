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
	            			alert("ihs_search.js, getMembers Error: " + e.message);
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


  // function drawPubRange() was moved 2016-11-28 by AJE here from advance_history.js, since advance_linking.js seems to need it too
	function drawPubRange(currentTitle){
	  console.warn('in ihs_search.js, drawPubRange(',currentTitle,')');
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



  // function drawCurrentTitle() was moved 2016-11-23 by AJE here from advance_history.js, since advance_linking.js seems to need it too
	function drawCurrentTitle(){
    var strLen=25;
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

	}  // end drawCurrentTitle()



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

    console.log('ihs_search.js, toggle_search_home_title_components("',calling_function,'")');

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

    /* AJE 2016-11-23 shunt over to similar functions in other files ; the names are new but the content is more or less Travant's: may never have worked       advanced_editing_history */
    var page = location.href;
    var id = response.items[0].titleId;
    console.info('ihs_search.js, populateSearchList(',response, ',', ioArgs,'), \n page=', page);
    //console.info('(page.indexOf("advanced_editing_history") > -1) is ', (page.indexOf("advanced_editing_history") > -1));
    //console.info('(page.indexOf("advanced_editing_history") > -1) is ', (page.indexOf("advanced_editing_linking") > -1))

    if (page.indexOf("advanced_editing_history") > -1) {
      console.info('ihs_search.js, populateSearchList, exiting with populateJournalDetail_adv_hist(',response, ',', ioArgs,')');
      populateSearchList_adv_hist(response, ioArgs); // advance_history.js
      return;
    } else if (page.indexOf("advanced_editing_linking") > -1) {
      console.info('ihs_search.js, populateSearchList, exiting with populateJournalDetail_adv_link(',response, ',', ioArgs,')');
      populateSearchList_adv_link(response, ioArgs); // advance_linking.js
      return;
    } /* AJE 2016-11-23 end shunt */

  	  //console.warn('ihs_search.js, enter populateSearchList(response=', response, ' ; ioArgs=', ioArgs); // AJE 2016-09-16 testing
      hideWaiting(); // AJE 2016-10-27

      var results = document.getElementById('results');

      // AJE 2016-11-01 clear unused search boxes
      //console.warn('ihs_search.js populateSearchList clears search boxes: ioArgs.url = "', ioArgs.url, '"; ioArgs.url.indexOf(browseJournalByTitle) == ', ioArgs.url.indexOf('browseJournalByTitle'));
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
      //console.log("response.items.length = " +response.items.length+ " items in populateSearchList ; r.i[0].titleID = " +response.items[0].titleID+ ".");
      if ( (response.items.length < 1) ||
           ((response.items[0].titleId == 0) || (response.items[0].titleId == undefined) || (response.items[0].titleId == ""))
         ) {
        //console.log("populateSearchList hit the IF");
        var li = document.createElement('li');
        li.innerHTML = "<h2>No results were found for <span class='search_term'>&nbsp;'"+search_value+"'&nbsp;</span></h2>";
        ul.appendChild(li);
      } else {
        //console.log("populateSearchList hit the ELSE");
        for (i = 0; i < response.items.length; i++) {
          var li = document.createElement('li');
          var a = document.createElement('a');

          //console.log(i +') response.items[',i,'].title: "', response.items[i].title, ' *.publisher: "', response.items[i].publisher, ' ; response.items[i] = ', response.items[i]);// AJE 2016-09-20

          var display_title = response.items[i].title + " / " + response.items[i].publisher;  // AJE 2016-10-25
          //a.innerHTML = response.items[i].title; // Travant original
          a.innerHTML = display_title; // AJE 2016-10-25

          /* AJE 2016-11-23 shunt over to similar functions in other files ; the names are new but the content is more or less Travant's: may never have worked       advanced_editing_history */
          if (page.indexOf("advanced_editing_history") > -1) {
            console.info('ihs_search.js, populateSearchList, will do javascript:getJournalDetail_adv_hist(' + response.items[i].titleId + ');');
            a.setAttribute('href', 'javascript:getJournalDetail_adv_hist(' + response.items[i].titleId + ');');
          } else if (page.indexOf("advanced_editing_linking") > -1) {
            console.info('ihs_search.js, populateSearchList, will do javascript:getJournalDetail_adv_link(' + response.items[i].titleId + ');');
            a.setAttribute('href', 'javascript:getJournalDetail_adv_link(' + response.items[i].titleId + ');');
          } else { // default case
            //a.setAttribute('title', response.items[i].title); // Travant original
            a.setAttribute('href', 'javascript:getJournalDetail(' + response.items[i].titleId + ');');
          } /* AJE 2016-11-23 end shunt */

          a.setAttribute('title', display_title); // AJE 2016-10-25
          a.setAttribute('id', response.items[i].titleId); // AJE 2016-11-04

          li.appendChild(a);
          ul.appendChild(li);
        } // end for
      } // end else

      results.appendChild(ul);

      //results.style.visibility = "visible"; // AJE 2016-09-16 change this line from Travant ... to
      results.style.display = "block"; // ... this; now do some more toggling, call new AJE function, in ihs_search.js
      toggle_search_home_title_components('populateSearchList');
      // AJE : resume Travant original

      // AJE 2016-11-04 : enhancements list #6: if only 1 title returned, display its detail screen immediately (no clicking on the title)
      //console.warn("populateSearchList response.items.length == ", response.items.length);
      if((response.items.length == 1)
        && ((response.items[0].titleId != 0) && (response.items[0].titleId != ""))
      ){
        var thisTitleID = new String(response.items[0].titleId);
        thisTitleID = thisTitleID.replace(" ", "");
        //console.warn("enhancements list #6, response.items[",i,"].titleId = '", response.items[i].titleId, "' ; thisTitleID = '", thisTitleID, "' after js.replace.");
        //console.warn("ihs_search.js, populateSearchList will call getJournalDetail("+thisTitleID+").");
        getJournalDetail(thisTitleID); // search_home.js
      } //end enhancements list #6: if(response.items.length == 1) AJE 2016-11-04
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
      //console.log('searchJBT, value.length = ',value.length,', should search'); // AJE 2016-10-27
        dojo.xhrGet({
            handleAs: 'json',
            url: "/search/searchJournalByTitle/" + value,
              //  app/controllers/SearchJournals.java
            preventCache: true,
            error: function(e) {
                alert("ihs_search.js, searchJournalByTitle Error: " + e.message);
            },
            load: populateSearchList
        });
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
    //console.log('browseJBT, value.length = ',value.length,', should search'); // AJE 2016-10-27
      dojo.xhrGet({
          handleAs: 'json',
          url: "/search/browseJournalByTitle/" + value,
            //  app/controllers/SearchJournals.java
          preventCache: true,
          error: function(e) {
              alert("ihs_search.js, browseJournalByTitle Error: " + e.message);
          },
          load: populateSearchList
      });
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
      //console.log('containsJBT, value.length = ',value.length,', should search'); // AJE 2016-10-27
      dojo.xhrGet({
          handleAs: 'json',
          url: "/search/containsJournalByTitle/" + value,
          preventCache: true,
          error: function(e) {
              alert("ihs_searcg.js, containsJournalByTitle Error: " + e.message);
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
                    alert("ihs_search.js, searchJournalByISSN Error: " + e.message);
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
                  alert("ihs_search.js, searchJournalByOCLC Error: " + e.message);
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



