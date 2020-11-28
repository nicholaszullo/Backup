import re
from flask import Flask, url_for, render_template, redirect, request, session, g, flash
from models import db, User, Category, Purchase
from werkzeug.security import generate_password_hash, check_password_hash
import os, json

app = Flask(__name__)
app.config["SQLALCHEMY_DATABASE_URI"] = "sqlite:///" + os.path.join(app.root_path, "budget.db")
app.config.from_object(__name__)			
app.config.from_envvar("final_settings", silent=True)
app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = False
db.init_app(app)
SECRET_KEY = "development mode"
app.secret_key = SECRET_KEY
all_user_cats = {}		#track what is viewed on the page. 0 needs rendered, 1 is rendered
all_user_purch = {}

@app.cli.command("initdb")
def initdb_cmd():
	db.create_all()

@app.before_request
def before_request():
	g.user = None
	if "user_id" in session:		#ensure the user if logged in before each request
		g.user = User.query.filter_by(user_id=session["user_id"]).first()


@app.route("/register", methods=["GET","POST"])
def register():
	global all_user_cats
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
			new_user = User(request.form['username'], generate_password_hash(request.form['password']))
			db.session.add(new_user)
			db.session.commit()
			uncat = Category("Uncategorized", 0, new_user)
			db.session.add(uncat)
			db.session.commit()
			all_user_cats[int(uncat.category_id)] = 0
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

@app.route("/")
def homepage():
	global all_user_purch, all_user_cats
	if g.user:
		user_purchs = Purchase.query.filter_by(user_id=g.user.user_id).all()
		user_cats = Category.query.filter_by(user_id=g.user.user_id).all()
		for purch in user_purchs:
			all_user_purch[int(purch.purchase_id)] = 0
		for cat in user_cats:
			all_user_cats[int(cat.category_id)] = 0
	return render_template("home.html")

@app.route("/cats", methods=["POST","GET"])
def cats():
	global all_user_cats
	if not g.user:
		return "Forbidden", 403
	if request.method == 'POST':
		data = request.get_json()
		if data["cat"] != "":
			limit = data["cat-limit"]
			if data["cat-limit"] == "":
				limit = 0
			newCat = Category(data["cat"], limit,g.user)
			db.session.add(newCat)
			db.session.commit()
			all_user_cats[int(newCat.category_id)] = 0
			return "OK!", 200

	elif request.method == 'GET':
		cats = Category.query.filter_by(user_id=g.user.user_id).all()
		dictCat = {}
		for cat in cats:
			if all_user_cats[int(cat.category_id)] == 0:
				dictCat[cat.category_id] = [cat.name, cat.limit]
				all_user_cats[int(cat.category_id)] = 1
		if len(dictCat) == 0:
			return "Not modified", 100
		
		return json.dumps(dictCat), 200

	return "Forbidden", 403
	
@app.route("/cats/<categoryId>", methods=["DELETE"])
def cat_removal(categoryId):
	global all_user_cats, all_user_purch
	if not g.user:
		flash("Must be logged in!")
		return "Forbidden", 403
	if not request.method == 'DELETE':
		flash("Not For You!")
		return "Forbidden", 403
	user_cat_purchases = Purchase.query.filter_by(category_id=categoryId, user_id=g.user.user_id).all()
	uncat_id = Category.query.filter_by(user_id=g.user.user_id, name="Uncategorized").first().category_id
	for purch in user_cat_purchases:
		purch.category_id = uncat_id
		all_user_purch[purch.purchase_id] = 0	
	del all_user_cats[int(categoryId)]
	db.session.delete(Category.query.filter_by(category_id=categoryId).first())
	db.session.commit()
	return "OK!",200

@app.route("/purchases", methods=["POST","GET"])
def purchase():
	global all_user_purch
	if not g.user:
		flash("Must be logged in!")
		return "Forbidden", 403
	if request.method == "POST":
		data = request.get_json()
		date = re.compile('\d{2}\/\d{2}\/\d{4} \d{2}:\d{2}')
		if not date.match(data["date"]):
			return "IMPROPER FORMAT", 400
		if Category.query.filter_by(name=data["cat"], user_id=g.user.user_id).first() == None:
			return "CAT NOT FOUND", 400
		new_purch = Purchase(data["amount"], data["description"],data["date"], Category.query.filter_by(name=data["cat"],user_id=g.user.user_id).first(), g.user)
		db.session.add(new_purch)
		db.session.commit()
		all_user_purch[int(new_purch.purchase_id)] = 0
		return "OK!",200

	elif request.method == 'GET':
		purchs = Purchase.query.filter_by(user_id=g.user.user_id).all()
		dictPurch = {}
		for purch in purchs:
			if all_user_purch[int(purch.purchase_id)] == 0 and purch.date[:2] == "11":	#only return a purchase if it has changed or in the current month, only need to support current month so hard code 11
				dictPurch[purch.purchase_id] = [purch.amount, purch.category_id]
				all_user_purch[int(purch.purchase_id)] = 1
		if len(dictPurch) == 0:
			return "No updates", 100
		return json.dumps(dictPurch), 200
		
	return "Forbidden", 403


if __name__ == "__main__":
	app.run()