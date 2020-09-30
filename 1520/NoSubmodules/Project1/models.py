from flask_sqlalchemy import SQLAlchemy
db = SQLAlchemy()

attending_events = db.Table("attending_events", 
	db.Column("user_id", db.Integer, db.ForeignKey("users.user_id")),
	db.Column("event_id", db.Integer, db.ForeignKey("events.event_id")))	#association table for users attending events

class User(db.Model):
	__tablename__ = "users"
	user_id = db.Column(db.Integer, primary_key=True)
	username = db.Column(db.String(24), nullable=False, unique=True, index=True)	# what are the arguments to String? max length?
	password_hash = db.Column(db.String(64), nullable=False)
	host_events = db.relationship("Event", backref="user", lazy="dynamic")	#one host to many events
	attends = db.relationship("Event", secondary=attending_events, backref=db.backref("users",lazy="dynamic"), lazy="dynamic") 		#can attend many events

	def __init__(self, username, password_hash):
		self.username = username
		self.password_hash = password_hash

	def __repr__(self):
		return "user {}".format(self.username)

class Event(db.Model):
	__tablename__ = "events"
	event_id = db.Column(db.Integer, primary_key=True)
	title = db.Column(db.String(90), nullable=False)
	description = db.Column(db.Text)
	time_start = db.Column(db.DateTime, nullable=False)
	time_end = db.Column(db.DateTime, nullable=False)
	host = db.Column(db.Integer, db.ForeignKey("users.user_id"))	#add host column to this event
	attendees = db.relationship("User", secondary=attending_events, backref=db.backref("events",lazy="dynamic"), lazy="dynamic")
	
	def __init__(self, title,description,time_start,time_end):
		self.title = title
		self.description = description
		self.time_start = time_start
		self.time_end = time_end

	def __repr__(self):
		return "Event {}".format(self.title)
