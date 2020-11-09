function setup(){
	document.getElementById("submitChat").addEventListener("click", sendPost)	
	updateChats()
}

function sendPost(){
	var httpRequest = new XMLHttpRequest();
	if (!httpRequest){
		alert("Unable to create request!");
	}
	var temp = { "chat": document.getElementById("newChatContent").value }
	document.getElementById("newChatContent").value = ""
	var data = JSON.stringify(temp)
	httpRequest.open("POST", "/new_chat");
	httpRequest.setRequestHeader('Content-Type', 'application/json');
	httpRequest.send(data)
}

function updateChats(){
	var httpRequest = new XMLHttpRequest();
	var chatWindow = document.getElementById("allChats")

	httpRequest.open("POST", "/get_chats")
	httpRequest.setRequestHeader("Content-Type", "application/json")
	var data = JSON.stringify({"numberchats" : chatWindow.children.length-1})
	httpRequest.onreadystatechange = function() {
		if (httpRequest.readyState === XMLHttpRequest.DONE){
			console.log(httpRequest.status)
			if (httpRequest.status === 200){
				var chats = JSON.parse(httpRequest.responseText);
				for (let i = 0; i < chats.length; i++){
					let chat = document.createElement("p");
					chat.innerText = chats[i]
					chatWindow.appendChild(chat)
				}
				window.setTimeout(updateChats,1000);
			} else {
				chatWindow.remove();
				document.getElementById("newChatWindow").remove();
				bottom = document.getElementById("bottom");
				notice = document.createElement("h1");
				notice.innerText = "ROOM CLOSED!";
				bottom.append(notice);
				home = document.createElement("a");
				home.innerText = "HOME";
				home.href = "/";
				bottom.append(home);
			}
		} 
	};
	httpRequest.send(data);

}

window.addEventListener("load", setup, true);