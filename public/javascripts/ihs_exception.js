 function populatedialog1(response, ioArgs){          
	    		var dataExceptionStore = new dojo.data.ItemFileWriteStore({
       			 	data: {
            			items: response
        			}
    			});
    		
    			var dataExceptionGrid = dijit.byId('dialog1-list');
				
				dataExceptionGrid.setStore(dataExceptionStore); 		
	   
	    }
		
		function getDataErrorDetail(i){
		  
		  document.getElementById('resolve-data-error-div').style.display = 'block';
		  
		  dojo.xhrGet({
        			handleAs: 'json',
        			url: "/ingestion/getExceptionDetail/"+i,
        			preventCache: true,
        			error: function (e) {
            			alert("Error: " + e.message);
        			},
        			load: populatedialog1
    	});
    			
		  recordId = i;
		  
		
		}
		
		function reprocessDataErrorRecord(){
		    
		    document.getElementById('resolve'+recordId).disabled = true; 
		    document.getElementById('resolve-data-error-div').style.display = 'none';
		    
		      
			var dataExceptionGrid = dijit.byId('dialog1-list');
			
			var item = dataExceptionGrid.getItem(0);//data store item

			var cell = dataExceptionGrid.getCell(2);
			var printSsen = dataExceptionGrid.store.getValue(item, cell.field);
			
			
			item = dataExceptionGrid.getItem(1);//data store item
			var oclcNumber = dataExceptionGrid.store.getValue(item, cell.field);
			
			item = dataExceptionGrid.getItem(2);//data store item
			var title = dataExceptionGrid.store.getValue(item, cell.field);
			
			
			item = dataExceptionGrid.getItem(3);//data store item
			var holdings = dataExceptionGrid.store.getValue(item, cell.field);
			
			item = dataExceptionGrid.getItem(4);//data store item
			var years = dataExceptionGrid.store.getValue(item, cell.field);
			
			var text =  '{ "firstName":"John" , "lastName":"Doe" } '; 
				
			var obj = {};
			obj.recordId=recordId;
			obj.printISSN = printSsen;
			obj.oclcNumber = oclcNumber;
			obj.title = title;
			obj.holding= holdings;
			obj.years = years;
						
		    dojo.rawXhrPost ({
					url: "/ingestion/postExceptionResolve",
					postData: dojo.toJson(obj),
					headers: { "Content-Type": "application/json", "Accept": "application/json" },
        			handleAs: "text", 
        			load: function(data){
        				
      				},
					error: function(error) {
						alert("Error:"+ error);
					}
				});
		}
		
		function ignore(){
		    document.getElementById('resolve'+recordId).disabled = true; 
		    document.getElementById('resolve-data-error-div').style.display = 'none';
			
			dojo.rawXhrPost ({
					url: "/ingestion/postExceptionIgnore/"+recordId,
        			load: function(data){
        				
      				},
					error: function(error) {
						alert("Error:"+ error);
					}
				});
		}
		
		function onhold(){
		
		    document.getElementById('resolve'+recordId).disabled = true; 
		    document.getElementById('resolve-data-error-div').style.display = 'none';
		    
		    dojo.rawXhrPost ({
					url: "/ingestion/postExceptionOnhold/"+recordId,
        			load: function(data){
        				
      				},
					error: function(error) {
						alert("Error:"+ error);
					}
				});
				
		} 
		
		
		
		function populateIngestionExceptionSummary(response, ioArgs){    
		          
		         
		        maxrecord = parseInt(response.maxrecord);
		        size = parseInt(response.size);
		        offset = parseInt(response.offset);
		        
	    		var dataExceptionStore = new dojo.data.ItemFileReadStore({
       			 	data: {
            			items: response.items
        			}
    			});
    		
    			var dataExceptionGrid = dijit.byId('ingestion-job-summary');
				
				dataExceptionGrid.setStore(dataExceptionStore); 		
				
				if(first == 1){
					if(offset == 0){ 
						dijit.registry.byId('previous').set('disabled', true);	
					} else { 
						dijit.registry.byId('previous').set('disabled', false);	
					} 
	    	
					if( size < maxrecord ){ 
						dijit.registry.byId('next').set('disabled', true);			
					} else  { 
						dijit.registry.byId('next').set('disabled', false);
					} 
				}
	   
	    }
	    
	    function getException (){
			 document.getElementById('resolve-data-error-div').style.display = 'none';
	    	 dojo.xhrGet({
        			handleAs: 'json',
        			url: "/ingestion/" + exceptionUrl + offset,
        			preventCache: true,
        			error: function (e) {
            			alert("Error: " + e.message);
        			},
        			load: populateIngestionExceptionSummary
	    	});
	    
	    	
		}    