function setup(){
	if (document.getElementById("logout") != null){
		document.getElementById("logout").addEventListener("click", sendLogout)
	}
	if (document.getElementById("leaveRoom") != null){
		document.getElementById("leaveRoom").addEventListener("click", sendLeaveRoom)
	}
}

function sendLogout(){
	var httpRequest = new XMLHttpRequest();
	if (!httpRequest){
		alert("Unable to create request!");
	}
	httpRequest.onreadystatechange = function() { if (httpRequest.readyState === XMLHttpRequest.DONE){location.reload()}};
	httpRequest.open("POST", "/logout");
	httpRequest.send();

}

function sendLeaveRoom(){
	var httpRequest = new XMLHttpRequest();
	if (!httpRequest){
		alert("Unable to create request!");
	}
	httpRequest.onreadystatechange = function() { if (httpRequest.readyState === XMLHttpRequest.DONE){location.reload()}};
	httpRequest.open("POST", "/leave");
	httpRequest.send();
}

window.addEventListener("load",setup, true)