function setup(){
	document.getElementById("createChatroom").addEventListener("click", sendNewRoom)
	if (document.getElementsByClassName("joinRoom") != null){
		var buttons = document.getElementsByClassName("joinRoom");
		for (var i = 0; i < buttons.length; i++){	//Could use let to avoid closure, but I wanted to do it the old fashioned way
			(function (i,buttons) {					//Closure on i. Take i as input to wrapper function to ensure each listener on each button works as expected
				buttons[i].addEventListener("click", function(){
					sendJoinRoom(buttons[i].value);
				},false);
			}(i,buttons));
		}
	}	
	if (document.getElementsByClassName("removeRoom") != null){
		var buttons = document.getElementsByClassName("removeRoom");
		for (var i = 0; i < buttons.length; i++){	
			(function (i,buttons) {					
				buttons[i].addEventListener("click", function(){
					sendRemoveRoom(buttons[i].value);
				},false);
			}(i,buttons));
		}
	}
}

function sendJoinRoom(num){
	var httpRequest = new XMLHttpRequest();
	if (!httpRequest){
		alert("Unable to create request!");
	}
	httpRequest.onreadystatechange = function() { if (httpRequest.readyState === XMLHttpRequest.DONE){location.reload()}};
	var data = JSON.stringify({ "num": num });
	httpRequest.open("POST", "/join_room");
	httpRequest.setRequestHeader('Content-Type', 'application/json');
	httpRequest.send(data);
}

function sendRemoveRoom(num){
	var httpRequest = new XMLHttpRequest();
	if (!httpRequest){
		alert("Unable to create request!");
	}
	httpRequest.onreadystatechange = function() { if (httpRequest.readyState === XMLHttpRequest.DONE){location.reload()}};
	var data = JSON.stringify({ "num": num });
	httpRequest.open("POST", "/remove_room");
	httpRequest.setRequestHeader('Content-Type', 'application/json');
	httpRequest.send(data);

}

function sendNewRoom(){
	var httpRequest = new XMLHttpRequest();
	if (!httpRequest){
		alert("Unable to create request!");
	}
	httpRequest.onreadystatechange = function() { if (httpRequest.readyState === XMLHttpRequest.DONE){location.reload()}};
	httpRequest.open("POST", "/new_room");
	httpRequest.send();

}

window.addEventListener("load", setup, true);