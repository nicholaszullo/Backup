/**\
 * 	TODO:
 * 	Remove card from dom when game ends?
 * 	CSS Improvements
 */
var bingo = false;
var storage = window.localStorage;
let wins = document.getElementById("changeWins");
if (storage.getItem("wins")){
	wins.innerText = (storage.getItem("wins"));
}
let losses = document.getElementById("changeLosses");
if (storage.getItem("losses")){
	losses.innerText = (storage.getItem("losses"));
}

function createBoard(){
	
	const board = document.getElementById("board");
	let b = document.createElement("div");
	b.innerText = ("B");
	board.appendChild(b).className = "cell";
	let i = document.createElement("div");
	i.innerText = ("I");
	board.appendChild(i).className = "cell";
	let n = document.createElement("div");
	n.innerText = ("N");
	board.appendChild(n).className = "cell";
	let g = document.createElement("div");
	g.innerText = ("G");
	board.appendChild(g).className = "cell";
	let o = document.createElement("div");
	o.innerText = ("O");
	board.appendChild(o).className = "cell";

	for (i = 1; i <= 25; i++){
		let cell = document.createElement("div");
		if (i != 13){
			cell.innerText = (i);
			cell.addEventListener("click", function(){
				clicked(cell);
			}, false);
		} else {
			cell.innerText = ("Free!");
			cell.style.backgroundColor = "red"
		}
		board.appendChild(cell).className = "cell";
	}
}

function clicked(cell){
	if (cell.style.backgroundColor == "red"){
		cell.style.backgroundColor = "azure"
	} else {
		cell.style.backgroundColor = "red";
	}
	
	bingo = false;
	const cells = document.getElementsByClassName("cell");
	//Test rows
	for (i = 1; i < 6; i++){
		if (cells[(i*5)+0].style.backgroundColor=="red" &&cells[(i*5)+1].style.backgroundColor=="red" &&cells[(i*5)+2].style.backgroundColor=="red" &&cells[(i*5)+3].style.backgroundColor=="red" &&cells[(i*5)+4].style.backgroundColor=="red"){
			bingo = true;
			break;
		}
	}
	//Test Cols
	for (i = 0; i < 5; i++){
		if (cells[(1*5)+i].style.backgroundColor == "red" && cells[(2*5)+i].style.backgroundColor == "red" && cells[(3*5)+i].style.backgroundColor == "red" && cells[(4*5)+i].style.backgroundColor == "red" && cells[(5*5)+i].style.backgroundColor == "red"){
			bingo = true;
			break;
		}
	}
	//Test diags
	
	if (cells[(1*5)+0].style.backgroundColor == "red" &&cells[(2*5)+1].style.backgroundColor == "red" &&cells[(3*5)+2].style.backgroundColor == "red" &&cells[(4*5)+3].style.backgroundColor == "red" && cells[(5*5)+4].style.backgroundColor == "red"){
		bingo = true;
	} else if (cells[(1*5)+4].style.backgroundColor == "red" &&cells[(2*5)+3].style.backgroundColor == "red" && cells[(3*5)+2].style.backgroundColor == "red" &&cells[(4*5)+1].style.backgroundColor == "red" &&cells[(5*5)+0].style.backgroundColor == "red"){
		bingo = true;
	} 
	
	if (bingo){
		alert("BINGO!!!");
	}
}

function newNums(){
	const board = document.getElementById("board");
	if (document.getElementsByClassName("cell").length == 0){
		createBoard();
	}
	for (i = 0; i < 5; i++){				//1st row is letters so skip
		var prev = new Array(5);
		
		for (k = 0; k < 5; k++){
			prev[k] = 0;
		}
		
		for (j = 1; j < 6; j++){
			if (j == 3 && i == 2)			//Skip changing freespace
				continue;
			var rand = Math.floor(Math.random()*15) + 15*(i)+1;
			while (prev.includes(rand)){
				rand =  Math.floor(Math.random()*15) + 15*(i)+1;

			}
			prev[j-1] = rand;
			var cells = board.getElementsByClassName("cell");
			cells[(5*(j)+i)].style.backgroundColor = "azure";
			cells[(5*(j)+i)].innerText = (rand);

		}

	}

}
var dupes = new Set();
function caller(){
	//Remove duplicate calls. store in array
	var rand = Math.floor(Math.random() * 75)+1;			//[0,1) returned by Math.random so wont go over 75 but dont want 0 so +1
	while (dupes.has(rand)){
		rand = Math.floor(Math.random() * 75)+1;
	}
	dupes.add(rand);
	var num = document.getElementById("calledNum");
	num.innerText = ("Last called number: "+rand);
	called = setTimeout(function(){
		caller();
	}, 2000);
	
}


let newGame = document.getElementById("newGameRandom");
let newGameSpecified = document.getElementById("newGameSpecified");
let iWon = document.getElementById("iWon");
let iLost = document.getElementById("iLost");

newGame.addEventListener("click", function(){
	newNums();
	dupes = new Set();
	caller();
	newGame.style.display = "none";
	newGameSpecified.style.display = "none";
	iWon.style.display = "inherit";
	iLost.style.display = "inherit";
}, false);

//B(5,3,2,1,11)I(16,23,30,19,22)N(34,35,f,40,45)G(46,47,50,55,60)O(61,66,68,72,75) copy and paste holder
newGameSpecified.addEventListener("click", function(){
	var userBoard = prompt("Enter a board string: No  duplicate numbers allowed! Numbers in B should be from 1-15, I:16-30, N:31-45, G: 46-60, O: 61-75", 
	"B(x,x,x,x,x)I(x,x,x,x,x)N(x,x,f,x,x)G(x,x,x,x,x)O(x,x,x,x,x)");
	var regex = /[Bb]\((?:1[0-5]|\d),(?:1[0-5]|\d),(?:1[0-5]|\d),(?:1[0-5]|\d),(?:1[0-5]|\d)\)[Ii]\((?:1[6-9]|2\d|30),(?:1[6-9]|2\d|30),(?:1[6-9]|2\d|30),(?:1[6-9]|2\d|30),(?:1[6-9]|2\d|30)\)[Nn]\((?:3[1-9]|4[0-5]),(?:3[1-9]|4[0-5]),[Ff],(?:3[1-9]|4[0-5]),(?:3[1-9]|4[0-5])\)[Gg]\((?:4[6-9]|5\d|60),(?:4[6-9]|5\d|60),(?:4[6-9]|5\d|60),(?:4[6-9]|5\d|60),(?:4[6-9]|5\d|60)\)[Oo]\((?:6[1-9]|7[0-5]),(?:6[1-9]|7[0-5]),(?:6[1-9]|7[0-5]),(?:6[1-9]|7[0-5]),(?:6[1-9]|7[0-5])\)/
	if (!regex.test(userBoard)){
		alert("Incorrectly formatted board! Follow the format and only change the x's");
		return ;
	}
	if (document.getElementsByClassName("cell").length == 0){
		createBoard();
	}
	dupes = new Set();
	
	for (i = 0; i < 5; i++){				
		for (j = 1; j < 6; j++){
			if (j == 3 && i == 2)			//Skip changing freespace
				continue;
			var newSquare = "";
			if (i == 0){
				let index = userBoard.search(/(?:\d,|\d\))/);
				let twoDigit = userBoard.search(/\d\d/);
				if (index < twoDigit){
					var userVal = userBoard.charAt(index);
					userBoard = userBoard.slice(index+1);
				} else {
					var userVal = userBoard.substring(twoDigit,twoDigit+2);
					userBoard = userBoard.slice(twoDigit+2);
				}
				if (dupes.has(parseInt(userVal))){		
					alert("You've entered invalid numbers for your board. Check the range specifications again. Generating new board");
					newNums();
					caller();
					return;
				}
				dupes.add(parseInt(userVal));
				newSquare = userVal;
				
			} else {
				let index = userBoard.search(/\d\d/);
				let userVal = userBoard.substring(index,index+2);
				if (dupes.has(parseInt(userVal))){			
					alert("You've entered invalid numbers for your board. Check the range specifications again. Generating new board");
					newNums();
					caller();
					return;
				}
				dupes.add(parseInt(userVal));
				newSquare = userVal;
				userBoard = userBoard.slice(index+2);
			}
			var cells = board.getElementsByClassName("cell");
			cells[(5*(j)+i)].style.backgroundColor = "azure";
			cells[(5*(j)+i)].innerText = newSquare;
			
		}

	}
	newGame.style.display = "none";
	newGameSpecified.style.display = "none";
	iWon.style.display = "inherit";
	iLost.style.display = "inherit";
	caller();
},false);

iWon.addEventListener("click", function(){
	clearTimeout(called);
	if (bingo){
		alert("Correctly called a bingo!");
		let val = document.getElementById("changeWins");
		storage.setItem("wins", parseInt(val.innerText)+1);
		val.innerText = parseInt(val.innerText)+1;

		newGame.style.display = "inherit";
		newGameSpecified.style.display = "inherit";
		iWon.style.display = "none";
		iLost.style.display = "none";
	} else{
		alert("You do not have bingo... Game will continue");
		caller();
	}

},false);

iLost.addEventListener("click", function(){
	alert("clicked lost");
	clearTimeout(called);
	let losses = document.getElementById("changeLosses");
	storage.setItem("losses", parseInt(losses.innerText) +1);
	losses.innerText = parseInt(losses.innerText) +1;
	newGame.style.display = "inherit";
	newGameSpecified.style.display = "inherit";
	iWon.style.display = "none";
	iLost.style.display = "none";
}, false);

