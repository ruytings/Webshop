var xHRObject = new XMLHttpRequest();

function login() {
	var information = "name="
			+ encodeURIComponent(document.getElementById('name').value)
			+ "&password="
			+ encodeURIComponent(document.getElementById('password').value);
	xHRObject.open("POST", "LoginServlet", true);
	xHRObject.onreadystatechange = handleResponse;
	xHRObject.setRequestHeader('Content-Type',
			'application/x-www-form-urlencoded');
	xHRObject.send(information);
	return false;
}

function handleResponse() {
	if (xHRObject.readyState == 4) {
		if (xHRObject.status == 200) {
			//error leegmaken
			var div = document.getElementById("error");
			while(div.firstChild){
			    div.removeChild(div.firstChild);
			}
			var serverResponse = xHRObject.responseXML;
			var error = serverResponse.getElementsByTagName('error')[0];
			if (error != null) {
				var message = error.getElementsByTagName('message')[0].textContent;
				var messageNode = document.createTextNode(message);
				var div = document.getElementById('error');
				var paragraph = document.createElement('p');
				paragraph.appendChild(messageNode);
				div.appendChild(paragraph);
			}
			var user = serverResponse.getElementsByTagName('user')[0];
			var status = user.getElementsByTagName('status')[0].textContent;
			var statusString = new String(status);
			if(1 == statusString) {
				window.location = "adminBestellingen.html";
			} else window.location = "catalogus.html";
		}
	}
}

