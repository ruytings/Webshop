var xHRObject = new XMLHttpRequest();

function shopItem(product) {
	var information = "product=" + encodeURIComponent(product);
	xHRObject.open("POST", "ShopServlet", true);
	xHRObject.onreadystatechange = handleResponse;
	xHRObject.setRequestHeader('Content-Type',
			'application/x-www-form-urlencoded');
	xHRObject.send(information);
}

function getAllData() {
	xHRObject.open("GET", "ShopServlet", true);
	xHRObject.onreadystatechange = handleResponse;
	xHRObject.setRequestHeader('Content-Type',
			'application/x-www-form-urlencoded');
	xHRObject.send(null);
	setTimeout(getAllData, 1000);
}

function handleResponse() {
	if (xHRObject.readyState == 4) {
		if (xHRObject.status == 200) {
			var response = xHRObject.responseXML;
				var stock = response.getElementsByTagName('stock')[0];
				var products = stock.getElementsByTagName('product');

				var parent = document.getElementById("overview");
				while (parent.hasChildNodes()) {
					parent.removeChild(parent.firstChild);
				}

				for (var i = 0; i < products.length; i++) {
					// xml lezen
					var idi = products[i].getElementsByTagName('id')[0].textContent;
					var numberInStockProducti = products[i]
							.getElementsByTagName('numberinstock')[0].textContent;
					var nameProducti = products[i].getElementsByTagName('name')[0].textContent;
					var descriptioni = products[i]
							.getElementsByTagName('description')[0].textContent;
					var pricei = products[i].getElementsByTagName('price')[0].textContent;
					// textnodes maken
					var idiText = document.createTextNode(idi);
					var numberInStockiText = document
							.createTextNode(numberInStockProducti);
					var descriptionProductiText = document
							.createTextNode(descriptioni);
					var nameProductiText = document
							.createTextNode(nameProducti);
					var priceiText = document.createTextNode(pricei);
					// koop-knop maken
					var btn = document.createElement("BUTTON");
					var btnText = document.createTextNode("Toevoegen");
					btn.setAttribute("onclick", "shopItem('" + idi + "')");
					btn.appendChild(btnText);
					// actie wegdoen indien 0 producten in stock
					if (numberInStockProducti == 0) {
						btn.removeEventListener('click', shopItem, false);
					}
					// tabel maken
					var table = document.getElementById("overview");
					var row = table.insertRow(i);
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
				}
				// shopping cart updaten
				var cart = response.getElementsByTagName('cart')[0];
				var totalPrice = cart.getElementsByTagName('totalPrice')[0].textContent;
				var totalQuantity = cart.getElementsByTagName('totalQuantity')[0].textContent;
				// samenvatting maken
				var textNode = document.createTextNode("Aantal: "
						+ totalQuantity + " - Prijs:" + totalPrice);
				var shoppingCart = document.getElementById('winkelmandje');
				var alreadyItemsInShoppingCart = shoppingCart.childNodes[0];
				// samenvatting updaten
				if (alreadyItemsInShoppingCart == null) {
					var shoppingcartparagraph = document.createElement('p');
					shoppingcartparagraph.appendChild(textNode);
					shoppingCart.appendChild(shoppingcartparagraph);
				} else {
					alreadyItemsInShoppingCart
							.removeChild(alreadyItemsInShoppingCart.childNodes[0]);
					alreadyItemsInShoppingCart.appendChild(textNode);
				}
		}
	}
}

function search() {
	var searchBar = document.getElementById("searchBar");
	var string = searchBar.value.toLowerCase();
	if (String != null) {
		var pattern = new RegExp(string);
		var table = document.getElementById("overview");
		for (var i = 0; i < table.rows.length; i++) {
			var row = table.rows[i];
			var cell = row.cells[1];
			if (pattern.test(cell.textContent.toLowerCase()) == false) {
				table.deleteRow(i);
				i--;
			}
		}
	}
}
