var budget = {};
var limit = {};
var uncat_id = -1;		//Initially -1 so error if used before defined

function setup(){
	if (document.getElementById("welcome") == null){
		login = document.createElement("a");
		login.href = "/login";
		login.textContent = "Login Here";
		reg = document.createElement("a");
		reg.href = "/register";
		reg.textContent = "Register Here";
		document.getElementById("welcome-banner").appendChild(login);
		document.getElementById("welcome-banner").appendChild(reg);
		return ;		//Disable functionality if not logged in
	}
	document.getElementById("cat-send").addEventListener("click", sendCat);
	document.getElementById("update").addEventListener("click", update);		//Everything calls update so not needed anymore but I'll keep for fun
	document.getElementById("purchase-send").addEventListener("click", sendPurchase);
	let date_obj = new Date();
	let month = date_obj.getMonth();
	const monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
	month_header = document.createElement("h2")
	month_header.textContent = monthNames[month];
	month_header.id = "month";
	document.getElementById("cat-list").appendChild(month_header);
	update();
}

function sendCat(){
	var httpRequest = new XMLHttpRequest();
	if (!httpRequest){
		alert("Unable to create request!");
	}
	var temp = { "cat" : document.getElementById("cat-new").value,
		"cat-limit" : document.getElementById("cat-limit").value }
	document.getElementById("cat-new").value = "";
	document.getElementById("cat-limit").value = "";
	var data = JSON.stringify(temp);
	httpRequest.open("POST", "/cats");
	httpRequest.onreadystatechange = function () {
		if (httpRequest.readyState === XMLHttpRequest.DONE){
			console.log("Sent category " +data + " to server");
			update()
		}
	}
	httpRequest.setRequestHeader('Content-Type', 'application/json');
	httpRequest.send(data)
}

function update(){
	var cat_window = document.getElementById("cat-list");
	var httpRequest = new XMLHttpRequest();
	if (!httpRequest){
		alert("Unable to create request!");
	}
	httpRequest.onreadystatechange = function () {
		if (httpRequest.readyState === XMLHttpRequest.DONE){
			console.log("Updated Categories: "+httpRequest.responseText );		//cats[key] is the category with id key. cats[key][0] is the string name, cats[key][1] is the limit
			if (httpRequest.status === 200){
				var cats = JSON.parse(httpRequest.responseText);
				for (var key in cats){
					let cat = document.createElement("div");
					cat.className = "cat";
					cat.id = "cat"+key;
					cat.textContent = "Name: " + cats[key][0];
					if (cats[key][0] != "Uncategorized"){
						let del_button = document.createElement("button");
						del_button.id = "delete-cat"+key;
						del_button.textContent = "Delete!";
						del_button.className = "btn";
						(function (catid) {		//Closure to use key in for loop
							del_button.addEventListener("click", function(){
								removeCat(catid);
							})
						} (key));
					cat.appendChild(del_button)
					} else {
						(function (key) {
							uncat_id = key;
						})(key);
					}

					let limitt = document.createElement("div");
					limitt.id = "limit-cat"+key;
					if (cats[key][1] == 0){
						limitt.textContent = "Limit: Unlimited";
					} else {
						limitt.textContent = "Limit: " +cats[key][1];
					}
					budget[key] = [cats[key][1]];
					limit[key] = cats[key][1];
					cat.appendChild(limitt);
					cat_window.appendChild(cat);
				}
			}
			updatePurchs();			//Ensure categories are finished updating, now update purchases
		}
	}
	httpRequest.open("GET", "/cats");
	httpRequest.send();
}

function removeCat(id){
	var httpRequest = new XMLHttpRequest();
	if (!httpRequest){
		alert("Unable to create request!");
	}
	httpRequest.onreadystatechange = function () {
		if (httpRequest.readyState === XMLHttpRequest.DONE){
			console.log("Sent delete category " +id+ " to server");
			delete budget[id];
			update();
		}
	}
	httpRequest.open("DELETE", "/cats/"+id)
	httpRequest.send()
	var curr = document.getElementById("cat"+id);
	curr.parentNode.removeChild(curr);
}

function sendPurchase(){
	var httpRequest = new XMLHttpRequest();
	if (!httpRequest){
		alert("Unable to create request!");
	}
	var temp = {"amount" : document.getElementById("purchase-amount").value,
		"description" : document.getElementById("purchase-description").value,
		"date" : document.getElementById("purchase-date").value,
		"cat" : document.getElementById("purchase-cat").value }

	document.getElementById("purchase-amount").value = "";
	document.getElementById("purchase-description").value = "";
	document.getElementById("purchase-date").value = "";
	document.getElementById("purchase-cat").value = "";
	var data = JSON.stringify(temp);
	httpRequest.open("POST", "/purchases");
	httpRequest.onreadystatechange = function () {
		if (httpRequest.readyState === XMLHttpRequest.DONE){
			console.log("Sent new purchase " +data + " to server");
			update()
		}
	}
	httpRequest.setRequestHeader('Content-Type', 'application/json');
	httpRequest.send(data)

}

function updatePurchs(){
	var httpRequest = new XMLHttpRequest();
	if (!httpRequest){
		alert("Unable to create request!");
	}
	httpRequest.onreadystatechange = function () {
		if (httpRequest.readyState === XMLHttpRequest.DONE){
			console.log("Updated purchases: " + httpRequest.responseText);	//purchs[key] is purchase with id key. purchs[key][0] is the amount, purchs[key][1] is the category id
			if (httpRequest.status === 200){
				var purchs = JSON.parse(httpRequest.responseText);
				for (var key in purchs){
					budget[purchs[key][1]].push(purchs[key][0]);
			//		var holder = document.createElement("p");		//Uncomment to display purchases in each category
			//		holder.textContent = purchs[key][0];
			//		document.getElementById("cat"+purchs[key][1]).appendChild(holder);
				}
			}
			calculate();			//Ensure purchases are done updating, now calculate
		}
	}
	httpRequest.open("GET", "/purchases");
	httpRequest.send()

}

function calculate(){
	var asArray = [];		//Convert dictionary to array for map and reduce functions
	let i = 0;
	for (var key in budget){
		if (parseInt(key) == uncat_id){
			var uncatPurchs = budget[key];
		} else {
			asArray[i++] = budget[key];
		}
	}
	asArray = asArray.map(function(subarray){
		return subarray.reduce(sub);
	})
	uncatPurchs = uncatPurchs.reduce(add);
	i = 0;
	for (var key in budget){
		if (parseInt(key) != uncat_id){
			display(key, asArray[i++]);
		}
	}
	let cat = document.getElementById("cat"+uncat_id);
	if (document.getElementById("uncat-total") == null){
		var uncat_total = document.createElement("p");
		uncat_total.id = "uncat-total";
	} else {
		var uncat_total = document.getElementById("uncat-total");
	}
	uncat_total.textContent = "Total spent on uncategorized is: " + uncatPurchs;
	cat.appendChild(uncat_total);
}

function sub(remain,val){
	return remain-val;
}

function add(sum, val){
	return sum+val;
}

function display(id, val){
	let cat = document.getElementById("cat"+id);
	if (document.getElementById("remain-cat"+id) == null){
		var remain = document.createElement("p");
	} else {
		var remain = document.getElementById("remain-cat"+id);
	}
	remain.id = "remain-cat"+id;

	if (limit[id] == 0){
		remain.textContent = "Unlimited Budget";
	} else if (val < 0){
		remain.textContent = "Remaining budget: Overspent!";
	} else {
		remain.textContent = "Remaining budget: " + val;
	}
	cat.appendChild(remain);
}

window.addEventListener("load", setup, true);