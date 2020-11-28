from flask_sqlalchemy import SQLAlchemy
db = SQLAlchemy()

class User(db.Model):
	__tablename__ = "users"
	user_id = db.Column(db.Integer, primary_key=True)
	username = db.Column(db.String(24), nullable=False, unique=True, index=True)	
	password_hash = db.Column(db.String(64), nullable=False)

	def __init__(self, username, password_hash):
		self.username = username
		self.password_hash = password_hash

	def __repr__(self):
		return "user {}".format(self.username)

class Category(db.Model):
	__tablename__ = "categories"
	category_id = db.Column(db.Integer, primary_key=True)
	name = db.Column(db.String(24), nullable=False)
	limit = db.Column(db.Float, nullable=False)
	user_id = db.Column(db.Integer, nullable=False)

	def __init__(self, name, limit, user):
		self.name = name
		self.limit = limit
		self.user_id = user.user_id
	
	def __repr__(self):
		return "category {}".format(self.name)
	
class Purchase(db.Model):
	__tablename__ = "purchases"
	purchase_id = db.Column(db.Integer, primary_key=True)
	amount = db.Column(db.Float, nullable=False)
	reason = db.Column(db.Text)
	date = db.Column(db.String(30), nullable=False)
	category_id = db.Column(db.Integer, nullable=False)
	user_id = db.Column(db.Integer, nullable=False)

	def __init__(self, amount, reason, date, category, user):
		self.amount = amount
		self.reason = reason
		self.date = date
		self.category_id = category.category_id
		self.user_id = user.user_id

	def __repr__(self):
		return "purchase {}".format(self.purchase_id)


