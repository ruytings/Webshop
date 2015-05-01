var xHRObject = new XMLHttpRequest();

function getAllData() {
	xHRObject.open("GET", "AdminOrdersServlet", true);
	xHRObject.onreadystatechange = handleResponse;
	xHRObject.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	xHRObject.send(null);
	setTimeout(getAllData, 1000);
}

function handleResponse() {
	if (xHRObject.readyState == 4) {
		if (xHRObject.status == 200) {
			//error leegmaken
			var div = document.getElementById("error");
			while(div.firstChild){
			    div.removeChild(div.firstChild);
			}
			var response = xHRObject.responseXML;
			var error = response.getElementsByTagName('error')[0];

			if(error != null) {
				var message = error.getElementsByTagName('message')[0].textContent;
				var messageNode = document.createTextNode(message);
				var div = document.getElementById('error');
				var paragraph = document.createElement('p');
				paragraph.appendChild(messageNode);
				div.appendChild(paragraph);
			}
			var ordersList = response.getElementsByTagName('orders')[0];
			var orders = ordersList.getElementsByTagName('order');
			
			var parent = document.getElementById("overview");
			while(parent.hasChildNodes()) {
			   parent.removeChild(parent.firstChild);
			}
			
			for (var i=0; i < orders.length; i++) {
				//xml lezen
				var idi = orders[i].getElementsByTagName('id')[0].textContent;
				var producti = orders[i].getElementsByTagName('productId')[0].textContent;
				var useri = orders[i].getElementsByTagName('userId')[0].textContent;
				var quantityi = orders[i].getElementsByTagName('quantity')[0].textContent;
				var statusi = orders[i].getElementsByTagName('status')[0].textContent;
				//textnodes maken
				var idiText = document.createTextNode(idi);
				var productiText = document.createTextNode(producti);
				var useriText = document.createTextNode(useri);
				var quantityiText = document.createTextNode(quantityi);
				var statusiText = document.createTextNode(statusi);
				//verwijder-knop maken
				var btn = document.createElement("BUTTON");
				var btnText = document.createTextNode("Verwijderen");
				btn.setAttribute("onclick", "deleteItem('" + idi + "')");
				btn.appendChild(btnText);
				//koop-knop maken
				var btn2 = document.createElement("BUTTON");
				var btn2Text = document.createTextNode("Afrekenen");
				btn2.setAttribute("onclick", "buyItem('" + idi + "')");
				btn2.appendChild(btn2Text);
				//tabel maken
				var table = document.getElementById("overview");
				var header = table.createTHead();
				var row = header.insertRow(i);
				var cell0 = row.insertCell(0);
				cell0.appendChild(idiText);
				var cell1 = row.insertCell(1);
				cell1.appendChild(productiText);
				var cell2 = row.insertCell(2);
				cell2.appendChild(useriText);
				var cell3 = row.insertCell(3);
				cell3.appendChild(quantityiText);
				var cell4 = row.insertCell(4);
				cell4.appendChild(statusiText);
				var cell5 = row.insertCell(5);
				cell5.appendChild(btn);
				var cell6 = row.insertCell(6);
				cell6.appendChild(btn2);
			}
		}
	}
}

function deleteItem(id) {
	var information = "action=delete" + "&id="+ encodeURIComponent(id);
	xHRObject.open("POST", "AdminOrdersServlet", true);
	xHRObject.onreadystatechange = handleResponse;
	xHRObject.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	xHRObject.send(information);
	return false;
}

function buyItem(id) {
	var information = "action=buy" + "&id="+ encodeURIComponent(id);
	xHRObject.open("POST", "AdminOrdersServlet", true);
	xHRObject.onreadystatechange = handleResponse;
	xHRObject.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	xHRObject.send(information);
	return false;
}

function addItem(action) {
	var information = "action=" + encodeURIComponent(action) + 
					  "&user="+ encodeURIComponent(document.getElementById('user').value) + 
					  "&product="+ encodeURIComponent(document.getElementById('product').value) + 
					  "&quantity="+ encodeURIComponent(document.getElementById('quantity').value);
	xHRObject.open("POST", "AdminOrdersServlet", true);
	xHRObject.onreadystatechange = handleResponse;
	xHRObject.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	xHRObject.send(information);
	return false;
}