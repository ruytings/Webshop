var xHRObject = new XMLHttpRequest();

function getAllData() {
	xHRObject.open("GET", "ShopServlet", true);
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
			var stock = response.getElementsByTagName('stock')[0];
			var products = stock.getElementsByTagName('product');
			
			var parent = document.getElementById("overview");
			while(parent.hasChildNodes()) {
			   parent.removeChild(parent.firstChild);
			}
			
			for (var i=0; i < products.length; i++) {
				//xml lezen
				var idi = products[i].getElementsByTagName('id')[0].textContent;
				var numberInStockProducti = products[i].getElementsByTagName('numberinstock')[0].textContent;
				var nameProducti = products[i].getElementsByTagName('name')[0].textContent;
				var descriptioni = products[i].getElementsByTagName('description')[0].textContent;
				var pricei = products[i].getElementsByTagName('price')[0].textContent;
				//textnodes maken
				var idiText = document.createTextNode(idi);
				var numberInStockiText = document.createTextNode(numberInStockProducti);
				var descriptionProductiText = document.createTextNode(descriptioni);
				var nameProductiText = document.createTextNode(nameProducti);
				var priceiText = document.createTextNode(pricei);
				//verwijder-knop maken
				var btn = document.createElement("BUTTON");
				var btnText = document.createTextNode("Verwijderen");
				btn.setAttribute("onclick", "deleteItem('" + nameProductiText.textContent + "')");
				btn.appendChild(btnText);
				//bewerk-knop maken
				var btn2 = document.createElement("BUTTON");
				var btnText2 = document.createTextNode("Bewerken");
				btn2.setAttribute("onclick", "editItem('" + idi.textContent + "')");
				btn2.appendChild(btnText2);
				//tabel maken
				var table = document.getElementById("overview");
				var header = table.createTHead();
				var row = header.insertRow(i);
				var cell0 = row.insertCell(0);
				cell0.appendChild(idiText);
				var cell1 = row.insertCell(1);
				cell1.appendChild(nameProductiText);
				var cell2 = row.insertCell(2);
				cell2.appendChild(descriptionProductiText);
				var cell3 = row.insertCell(3);
				cell3.appendChild(priceiText);
				var cell4 = row.insertCell(4);
				cell4.appendChild(numberInStockiText);
				var cell5 = row.insertCell(5);
				cell5.appendChild(btn);
				var cell6 = row.insertCell(6);
				cell6.appendChild(btn2);
			}
		}
	}
}

function deleteItem(nameItem) {
	var information = "action=delete" + "&name="+ encodeURIComponent(nameItem);
	xHRObject.open("POST", "AdminCatalogusServlet", true);
	xHRObject.onreadystatechange = handleResponse;
	xHRObject.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	xHRObject.send(information);
	return false;
}

function addItem(action) {
	var information = "action=" + encodeURIComponent(action) +
					  "&name="+ encodeURIComponent(document.getElementById('name').value) + 
					  "&description="+ encodeURIComponent(document.getElementById('description').value) + 
					  "&price="+ encodeURIComponent(document.getElementById('price').value) + 
					  "&stock="+ encodeURIComponent(document.getElementById('stock').value);
	xHRObject.open("POST", "AdminCatalogusServlet", true);
	xHRObject.onreadystatechange = handleResponse;
	xHRObject.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	xHRObject.send(information);
	return false;
}
