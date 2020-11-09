from flask import Flask, url_for, render_template, redirect, request, session, g, flash
import copy, json

app = Flask(__name__)
SECRET_KEY = "dev mode"
app.secret_key = SECRET_KEY

users = {}
chatrooms = {}
numRooms = 0

class User:
	def __init__(self, username, password,room_num):
		self.username = username
		self.password = password
		self.room_num = copy.deepcopy(room_num)
class RoomData:
	def __init__(self, host, number):
		self.data = []
		self.host = host
		self.number = number

@app.before_request
def before_request():
	g.user = None
	if "username" in session:		#ensure the user if logged in before each request
		g.user = users[session['username']]

@app.route('/', methods=["GET"])
def homepage():
	rooms = {}
	if chatrooms:
		rooms = chatrooms

	return render_template('home.html', rooms = rooms,users=users)

@app.route("/room")
def room():
	if not g.user:
		flash("Not logged in!")
		return redirect(url_for('login'))
	if int(g.user.room_num) not in chatrooms:
		flash("Room is closed. Please leave room")
		return redirect(url_for('homepage'))
	if g.user.room_num != -1:
		return render_template('chat.html', room = chatrooms[int(g.user.room_num)])
	flash("Must be in a room!")
	return redirect(url_for('homepage'))


@app.route("/register", methods=["GET","POST"])
def register():
	if g.user:
		flash("Already Logged in!")
		return redirect(url_for('homepage'))
	error = None
	if request.method == "POST":
		if not request.form['username']:	#if username is empty
			error = "Blank username given!"
		elif not request.form['password']:
			error = "Blank password given!"
		elif not request.form['password2']:
			error = "Matching password blank!"
		elif request.form['password'] != request.form['password2']:
			error = "Passwords do not match!"
		else:
			if request.form['username'] in users:
				error = "Username taken!"
			if not error:
				users[request.form['username']] = User(request.form['username'], request.form['password'], -1)
				flash("Successful register! Login please")
				return redirect(url_for('login'))

	return render_template("userReg.html", error=error)

@app.route("/login", methods=["GET","POST"])
def login():
	if g.user:
		flash("Already logged in!")
		return redirect(url_for('homepage'))
	error = None
	if request.method == "POST":
		if not request.form['username']:
			error = "blank username!"
		elif not request.form['password']:
			error = "Blank password!"
		else:
			if request.form['username'] in users and users[request.form['username']].password == request.form['password']:
				g.user = users[request.form['username']]
				session['username'] = request.form['username']
				flash("Successful Login!")
				return redirect(url_for('homepage'))

			error = "Username or password incorrect!"

	return render_template("userLogin.html", error=error)
				
@app.route("/logout", methods=["POST"])
def logout():
	if g.user:
		session.pop("username", None)		# Remove user from session
		g.user = None
		flash("Logout success!")
		return "OK!"
	else:
		flash("Login first!")
		return "Forbidden"

@app.route('/leave', methods=["POST"])
def leaveRoom():
	if g.user:
		if g.user.room_num != -1:
			flash("Left room " + g.user.room_num)
			g.user.room_num = -1
			return "OK!"

	flash("Unable to leave room")
	return "Forbidden"

@app.route('/join_room', methods=["POST"])
def joinRoom():
	if not g.user:
		flash("Not logged in!")
		return "Forbidden"
	if g.user.room_num == request.get_json()["num"]:
		flash("Already in this room!")
		return "Forbidden"
	if g.user.room_num != -1:
		flash("Leave your current room before joining a new one")
		return "Forbidden"
	
	g.user.room_num = request.get_json()["num"]
	flash("Joined room " +g.user.room_num)
	return "OK!"

@app.route('/new_room', methods=["POST"])
def newRoom():
	if not g.user:
		flash("Must be logged in!")
		return "Forbidden"
	global numRooms
	chatrooms[numRooms] = RoomData(g.user.username, numRooms)
	numRooms += 1
	return "OK!"

@app.route('/remove_room', methods=["POST"])
def removeRoom():
	if not g.user:
		flash("Must be logged in!")
		return "Forbidden", 403
	data = request.get_json()
	num = data["num"]
	del chatrooms[int(num)]
	return "OK!"

@app.route('/new_chat', methods=["POST"])
def newChat():
	if int(g.user.room_num) not in chatrooms:
		return "Not Found", 404
	
	data = request.get_json()
	chatrooms[int(g.user.room_num)].data.append(g.user.username + " says: " + data["chat"])
	return "OK!"

@app.route('/get_chats', methods=["POST"])
def returnChats():
	if not g.user:
		flash("Must be logged in!")
		return "Forbidden", 403
	if g.user.room_num == -1:
		flash("Not in room anymore")
		return "Forbidden", 403
	if int(g.user.room_num) not in chatrooms:
		flash("ROOM CLOSED BY HOST :(")
		return "Forbidden", 403
	
	return json.dumps(chatrooms[int(g.user.room_num)].data[request.get_json()["numberchats"]:])     #Slice the chat history and only return the new chats

if __name__ == "__main__":
	app.run()