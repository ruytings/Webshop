var xHRObject = new XMLHttpRequest();

function register() {
	var information = "userName="
			+ encodeURIComponent(document.getElementById('userName').value)
			+ "&password="
			+ encodeURIComponent(document.getElementById('password').value)
			+ "&email="
			+ encodeURIComponent(document.getElementById('email').value)
			+ "&confirmation="
			+ encodeURIComponent(document.getElementById('confirmation').value);
	xHRObject.open("POST", "RegisterServlet", true);
	xHRObject.onreadystatechange = handleResponse;
	xHRObject.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	xHRObject.send(information);
	return false;
}

function handleResponse() {
	if (xHRObject.readyState == 4) {
		if (xHRObject.status == 200) {
			var serverResponse = xHRObject.responseXML;
			//error leegmaken
			var div = document.getElementById("error");
			while(div.firstChild){
			    div.removeChild(div.firstChild);
			}
			if (serverResponse != null) {
				var error = serverResponse.getElementsByTagName('error')[0];
				var message = error.getElementsByTagName('message')[0].textContent;
				var messageNode = document.createTextNode(message);
				var div = document.getElementById('error');
				var paragraph = document.createElement('p');
				paragraph.appendChild(messageNode);
				div.appendChild(paragraph);
			} else {
				window.location = "login.html";
			}
		}
	}
}

function getProduct() {
	$.ajax({
			type: "GET",
	   		url: "LoginServlet",
	   		dataType: "xml",
	   		success: function(xml){
	   			//XML lezen
	   			$(xml).find('product').each(function(){
	   				var id = $(this).find('id').text();
	   				var name = $(this).find('name').text();
	   				var numberinstock = $(this).find('numberinstock').text();
	   				var description = $(this).find('description').text();
	   				var price = $(this).find('price').text();
	   			// create table
	   				$("#reclame").empty();
	   				var $table = $('<table>');
	   			// caption
	   				$table.append('<caption></caption>')
	   				// thead
	   				.append('<thead>').children('thead')
	   				.append('<tr />').children('tr').append('<th>Naam</th><th>Omschrijving</th><th>Prijs</th><th>Stock</th>');

	   				//tbody
	   				var $tbody = $table.append('<tbody />').children('tbody');

	   				// add row
	   				$tbody.append('<tr />').children('tr:last')
	   				.append("<td>" + name + "</td>")
	   				.append("<td>" + description + "</td>")
	   				.append("<td>" + numberinstock + "</td>")
	   				.append("<td>" + price +"</td>");

	   				// add table to dom
	   				$table.appendTo('#reclame');
	   			});
	   			setTimeout(getProduct, 5000);
	   		},
	   		error: function() {
	   			alert("An error occurred while processing XML file.");
	   		}
	 	});
};
