
import numpy as np
import torch
import time
from tqdm import tqdm

"""Let's first simulate some data and have a loop. 

Here for easier visualization, our feature contains only 1 variable so it can be easily shown on a 2-d figure. But the same will apply to higher dimensional data.
"""

w, b = 12, -4

data = torch.rand(200, 1)
print(data)
label = w * data + b + 0.5 * torch.randn(200, 1)

weight = torch.rand(1, requires_grad=True)   # Assuming y = x * w + b
bias = torch.rand(1, requires_grad=True)

print(weight, bias)

# Hyperparameter
learning_rate = 0.1

total_steps = range(10000)

for step in total_steps:
  prediction = weight * data + bias
  loss = ((prediction - label) ** 2).mean()   # Mean squared error
  if (step + 1) % 100 == 0:
    total_steps.set_description("Loss: %.4f" % loss.detach().item())

  loss.backward()
  # plt.plot(x, weight * x + bias)

  with torch.no_grad():   # Stops tracking variable
    weight -= learning_rate * weight.grad
    bias -= learning_rate * bias.grad

    weight.grad.zero_()   # Avoid gradients being accumulated
    bias.grad.zero_()

w_hat = weight.detach().item()    # Stops tracking variable
b_hat = bias.detach().item()

print("Learned weight=%.4f, bias=%.4f" % (w_hat, b_hat))
