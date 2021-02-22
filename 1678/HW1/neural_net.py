import numpy as np
import matplotlib.pyplot as plt
from tqdm import tqdm

def forward(x,w1,w2):
	z = np.tanh(x@w1.T)		#tanh activation
	y_pred = z@w2.T			#identity activation
	return (z, y_pred)	#NxM , Nx1

def backprop(x, y, m, iters, eta):
	w1 = np.random.normal(0,1,size=(m,x.shape[1])) * ((1/x.shape[1]) ** 0.5)	#random weight * tanh optimization
	w2 = np.random.normal(0,1,size=(1,m)) * ((2/m) ** 0.5)		#random weight * optimization
	error_over_time = np.zeros(shape=(iters))		#create the errors matrix
	print("Training...")
	for i in tqdm(range(iters)):#range(iters): #	#tqdm for a cool progress bar
		err = 0
		for j in range(x.shape[0]):
			#sample = np.random.randint(0,x.shape[0])	#chose a random sample to use for backprop
			x_i = x[j].reshape(x.shape[1],1).T		#move from (D,) to (1,D) for multiplication
			z,y_pred = forward(x_i,w1,w2)	
			y_err = (y_pred - y[j]) 	#calculate errors
			z_err = (1 - z**2)* sum(y_err*w2)
			err += y_err ** 2
			w2 -= eta*(y_err*z)			#update weights with learning rate eta
			w1 -= eta*(z_err.T*x_i)	
		error_over_time[i] = err
	return w1,w2, error_over_time

data = np.genfromtxt("winequality/winequality-red.csv", dtype=float, delimiter=',', skip_header=1)		#process csv data, skipping the first line
holder = np.array_split(data,2)
train = holder[0].T
test = holder[1].T
for i in range(train.shape[0]-1):		#standardize all columns of data
	test[i] = (test[i] - np.mean(train[i], axis=0)) / np.std(train[i], axis=0)			#test before train so train data isnt stdized for test calculations
	train[i] = (train[i] - np.mean(train[i], axis=0)) / np.std(train[i], axis=0)

x = train[:-1]		#remove truth values from input 
y = train[train.shape[0]-1]		#truth values
eta = .0001
w1,w2,err = backprop(x.T,y.T,30,1000,eta)	#train
x = test[:-1]
y = test[test.shape[0]-1]
z,y_pred = forward(x.T, w1,w2)	#test
rmse = np.mean((np.squeeze(y_pred) - y) ** 2) ** 0.5	#calculate Root Mean Square Error
x = np.arange(0,1000,1)
y = err
plt.title(f"{eta} eta")
plt.figtext(.5, .5, f"RMSE is: {rmse}", ha='center')
plt.plot(x,y)
plt.show()
#plt.savefig(f"{eta}_lr.png")