/**
 * 
 */
 
 var form = document.getElementById('customerInfo'),
 	 facility = form.elements.validation_facilities;
 	 
 	 facility.onChange = function(){
 	 	alert('Stuff changed!');
 	 };