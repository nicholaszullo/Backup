from flask import Flask, url_for, render_template, redirect, request, session, g, flash		#flask processes
import os				#get the file path
from models import db, User, Event		#the database objects
from werkzeug.security import generate_password_hash, check_password_hash		#secure passwords
import re		#regular expressions
import datetime
"""
	TODO:
		Continue CSS improvements
"""
app = Flask(__name__);
app.config["SQLALCHEMY_DATABASE_URI"] = "sqlite:///" + os.path.join(app.root_path, "events.db")
app.config.from_object(__name__)			#why are these 2 needed?
app.config.from_envvar("proj1_settings", silent=True)
app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = False
db.init_app(app)
SECRET_KEY = "development mode"
app.secret_key = SECRET_KEY

@app.cli.command("initdb")
def initdb_cmd():
	db.create_all()

@app.before_request
def before_request():
	g.user = None
	if "user_id" in session:		#ensure the user if logged in before each request
		g.user = User.query.filter_by(user_id=session["user_id"]).first()

@app.route("/")
def homepage():
	eventList = Event.query.all()
	userEvents = []
	if g.user:
		userEvents = User.query.filter_by(user_id=g.user.user_id).first().host_events.filter_by(host=g.user.user_id).all()

	return render_template("home.html", eventList=eventList, User=User, Event=Event, userEvents=userEvents);

@app.route("/register", methods=["GET","POST"])
def register():
	if g.user:	# if the user is already logged in
		return redirect(url_for("homepage"))
	error = None	#Display nothing on first load, only show if changed
	if request.method == "POST":	#if a valid form submission
		if not request.form['username']:	#if username is empty
			error = "Blank username given!"
		elif User.query.filter_by(username=request.form['username']).first() and User.query.filter_by(username=request.form['username']).first().username == request.form['username']:
			error = "Sorry, username taken!"
		elif not request.form['password']:
			error = "Blank password given!"
		elif not request.form['password2']:
			error = "Matching password blank!"
		elif request.form['password'] != request.form['password2']:
			error = "Passwords do not match!"
		else:	#valid register
			db.session.add(User(request.form['username'], generate_password_hash(request.form['password'])))
			db.session.commit()
			flash("Successful register! Login please")
			return redirect(url_for('login'))

	return render_template("userReg.html", error=error)

@app.route("/login", methods=["GET", "POST"])
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
			if not User.query.filter_by(username=request.form['username']).first():
				error = "Username or password incorrect!"
			else:
				user = User.query.filter_by(username=request.form['username']).first()
				if not check_password_hash(user.password_hash, request.form['password']):
					error = "Username or password incorrect!"
				else:
					g.user = user
					flash("Successful Login!")
					session['user_id'] = user.user_id
					return redirect(url_for('homepage'))

	return render_template("userLogin.html", error=error)

@app.route("/logout")
def logout():
	if g.user:
		session.pop("user_id", None)		# Remove user from session
		g.user = None
		return render_template("userLogout.html")
	else:
		flash("Login first!")
		return redirect(url_for("login"))

@app.route('/create', methods=["GET", "POST"])
def createEvent():
	if not g.user:
		flash("You must be logged in to create an event!")
		return redirect(url_for("login"))
	error = None
	if request.method == "POST":
		date = re.compile('\d{2}\/\d{2}\/\d{4} \d{2}:\d{2}')
		if not request.form["title"]:
			error = "Blank title!"
		elif not request.form["start"]:
			error = "Blank start!"
		elif not request.form["end"]:
			error = "Blank end!"
		elif not date.match(request.form["start"]):
			error = "Start date formatted incorrectly! Ensure proper spacing, case sensitivity, and a 0 before a single digit number"
		elif not date.match(request.form["end"]):
			error = "End date formatted incorrectly! Ensure proper spacing, case sensitivity, and a 0 before a single digit number"
		else:
			time_start = datetime.datetime(int(request.form["start"][6:10]), int(request.form["start"][3:5]), int(request.form["start"][:2]), int(request.form["start"][11:13]), int(request.form["start"][14:16]))
			time_end = datetime.datetime(int(request.form["end"][6:10]), int(request.form["end"][3:5]), int(request.form["end"][:2]), int(request.form["end"][11:13]), int(request.form["end"][14:16]))
			newEvent = Event(title=request.form["title"], description=request.form["description"], time_start=time_start, time_end=time_end)
			db.session.add(newEvent)
			g.user.host_events.append(newEvent)
			db.session.commit()
			return redirect(url_for('homepage'))

	return render_template("eventCreate.html", error = error)

@app.route('/attend/<event_id>')
def attend(event_id):
	if not g.user:
		flash("Must be logged in to attend an event!")
		return redirect(url_for('login'))

	if g.user.user_id == Event.query.filter_by(event_id=event_id).first().host:
		flash("Host cannot join event!")
		return redirect(url_for('homepage'))

	event = Event.query.filter_by(event_id=event_id).first()
	g.user.attends.append(event)
	db.session.commit()
	return render_template("eventAttend.html")

@app.route('/cancel/<event_id>', methods=["POST","GET"])
def cancel(event_id):
	if not g.user:
		flash("Must be logged in to cancel an event!")
		return redirect(url_for('login'))
	event = Event.query.filter_by(event_id=event_id).first()
	if g.user.user_id != event.host:
		flash("Only host can cancel an event!")
		return redirect(url_for('homepage'))
	if request.method == "POST":
		if request.form['confirm'] == "CONFIRM":
			db.session.delete(event)
			db.session.commit()
			flash("Confirmed cancel!")
			return redirect(url_for('homepage'))
		else:
			flash("Unable to confirm!")

	return render_template("eventCancel.html")

if __name__ == "__main__":
	app.run();