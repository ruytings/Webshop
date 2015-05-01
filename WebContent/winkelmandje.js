var xHRObject = new XMLHttpRequest();

function getAllData() {
	xHRObject.open("GET", "CartServlet", true);
	xHRObject.onreadystatechange = handleResponse;
	xHRObject.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	xHRObject.send(null);
	setTimeout(getAllData, 1000);
}

function buyItems() {
	var information = "action=buy";
	xHRObject.open("POST", "CartServlet", true);
	xHRObject.onreadystatechange = handleResponse;
	xHRObject.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	xHRObject.send(information);
}

function cancelItem(id) {
	var information = "action=cancel" + "&id="+ encodeURIComponent(id);
	xHRObject.open("POST", "CartServlet", true);
	xHRObject.onreadystatechange = handleResponse;
	xHRObject.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	xHRObject.send(information);
	return false;
}

function handleResponse() {
	if (xHRObject.readyState == 4) {
		if (xHRObject.status == 200) {
			var response = xHRObject.responseXML;
			//errors afhandelen
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
				var quantityi = orders[i].getElementsByTagName('quantity')[0].textContent;
				//textnodes maken
				var idiText = document.createTextNode(idi);
				var productiText = document.createTextNode(producti);
				var quantityiText = document.createTextNode(quantityi);
				//verwijder-knop maken
				var btn = document.createElement("BUTTON");
				var btnText = document.createTextNode("Bestelling annuleren");
				btn.setAttribute("onclick", "cancelItem('" + idi + "')");
				btn.appendChild(btnText);
				//tabel maken
				var table = document.getElementById("overview");
				var header = table.createTHead();
				var row = header.insertRow(i);
				var cell0 = row.insertCell(0);
				cell0.appendChild(idiText);
				var cell1 = row.insertCell(1);
				cell1.appendChild(productiText);
				var cell2 = row.insertCell(2);
				cell2.appendChild(quantityiText);
				var cell3 = row.insertCell(3);
				cell3.appendChild(btn);
			}
			
			//history maken
			var historyList = response.getElementsByTagName('history')[0];
			var historyOrders = historyList.getElementsByTagName('horder');
			
			var parent = document.getElementById("history");
			while(parent.hasChildNodes()) {
			   parent.removeChild(parent.firstChild);
			}
			
			for (var i=0; i < historyOrders.length; i++) {
				//xml lezen
				var idi = historyOrders[i].getElementsByTagName('hid')[0].textContent;
				var producti = historyOrders[i].getElementsByTagName('hproductId')[0].textContent;
				var quantityi = historyOrders[i].getElementsByTagName('hquantity')[0].textContent;
				//textnodes maken
				var idiText = document.createTextNode(idi);
				var productiText = document.createTextNode(producti);
				var quantityiText = document.createTextNode(quantityi);
				//tabel maken
				var table = document.getElementById("history");
				var header = table.createTHead();
				var row = header.insertRow(i);
				var cell0 = row.insertCell(0);
				cell0.appendChild(idiText);
				var cell1 = row.insertCell(1);
				cell1.appendChild(productiText);
				var cell2 = row.insertCell(2);
				cell2.appendChild(quantityiText);
			}
		}
		
	}
}