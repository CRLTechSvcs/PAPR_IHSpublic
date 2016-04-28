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
	
	
	
	function searchJournalByTitle(search) {

		var results = document.getElementById('results');
		var issn = document.getElementById('issnid');
		issn.value="";
		
		var value = search.value.replace('\\', ' ').replace('\/', ' ').replace('  ', ' ');
		
		var value1 = search.value.replace(' ', '');
		
	    if (value.length < 2 ) {
	        results.innerHTML = ' ';
	    } else {
	    
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
	        }
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