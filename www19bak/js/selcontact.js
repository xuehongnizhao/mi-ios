function selectContact(){
		window.plugins.ContactPicker.chooseContact(function(contactInfo) {
			//to get all phones numbers
			    document.getElementById('contact').value = contactInfo.displayName;
			   
				document.getElementById('phone').value = contactInfo.phones[0].replace(/-/g,"");
			});
	}
