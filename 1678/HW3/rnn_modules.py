import torch
from torch._C import device
import torch.nn as nn
import torch.nn.functional as F


class GRUCell(nn.Module):
  """Implementation of GRU cell from https://arxiv.org/pdf/1406.1078.pdf."""

  def __init__(self, input_size, hidden_size, bias=False):
    super().__init__()

    self.input_size = input_size
    self.hidden_size = hidden_size
    self.bias = bias

    # Learnable weights and bias for `update gate`
    self.W_z = nn.Parameter(torch.Tensor(hidden_size, hidden_size + input_size))
    if bias:
      self.b_z = nn.Parameter(torch.Tensor(hidden_size))
    else:
      self.register_parameter('b_z', None)

    # Learnable weights and bias for `reset gate`
    self.W_r = nn.Parameter(torch.Tensor(hidden_size, hidden_size + input_size))
    if bias:
      self.b_r = nn.Parameter(torch.Tensor(hidden_size))
    else:
      self.register_parameter('b_r', None)

    # Learnable weights and bias for `output gate`
    self.W = nn.Parameter(torch.Tensor(hidden_size, hidden_size + input_size))
    if bias:
      self.b = nn.Parameter(torch.Tensor(hidden_size))
    else:
      self.register_parameter('b', None)

    self.reset_parameters()

  def forward(self, x, prev_state):
    if prev_state is None:
      batch = x.shape[0]
      prev_h = torch.zeros((batch, self.hidden_size), device=x.device)
    else:
      prev_h = prev_state

    concat_hx = torch.cat((prev_h, x), dim=1)
    z = torch.sigmoid(F.linear(concat_hx, self.W_z, self.b_z))
    r = torch.sigmoid(F.linear(concat_hx, self.W_r, self.b_r))
    h_tilde = torch.tanh(
        F.linear(torch.cat((r * prev_h, x), dim=1), self.W, self.b))
    next_h = (1 - z) * prev_h + z * h_tilde
    return next_h

  def reset_parameters(self):
    sqrt_k = (1. / self.hidden_size)**0.5
    with torch.no_grad():
      for param in self.parameters():
        param.uniform_(-sqrt_k, sqrt_k)
    return

  def extra_repr(self):
    return 'input_size={}, hidden_size={}, bias={}'.format(
        self.input_size, self.hidden_size, self.bias is not True)

  def count_parameters(self):
    print('Total Parameters: %d' %
          sum(p.numel() for p in self.parameters() if p.requires_grad))
    return


class LSTMCell(nn.Module):

  def __init__(self, input_size, hidden_size, bias=False):
    super().__init__()
    self.input_size = input_size
    self.hidden_size = hidden_size
    self.bias = bias

    self.forget_gate_w = nn.Parameter(torch.Tensor(hidden_size, hidden_size+input_size))
    if bias:
      self.forget_gate_b = nn.Parameter(torch.Tensor(hidden_size))
    else:
      self.register_parameter("forget_gate_b", None)
    
    self.input_gate_w = nn.Parameter(torch.Tensor(hidden_size, hidden_size+input_size))
    if bias:
      self.input_gate_b = nn.Parameter(torch.Tensor(hidden_size))
    else:
      self.register_parameter("input_gate_b", None)
    
    self.candidate_w = nn.Parameter(torch.Tensor(hidden_size, hidden_size+input_size))
    if bias:
      self.candidate_b = nn.Parameter(torch.Tensor(hidden_size))
    else:
      self.register_parameter("candidate_b", None)
    
    self.output_gate_w = nn.Parameter(torch.Tensor(hidden_size, hidden_size+input_size))
    if bias:
      self.output_gate_b = nn.Parameter(torch.Tensor(hidden_size))
    else:
      self.register_parameter("output_gate_b", None)
    
    self.reset_parameters()
    return

  def forward(self, x, prev_state):
    if prev_state is None:
      batch = x.shape[0]
      prev_h = torch.zeros((batch, self.hidden_size), device=x.device)
      inner_state = torch.zeros((batch,self.hidden_size), device=x.device)
    else:
      prev_h = prev_state[0]
      inner_state = prev_state[1]

    inputs = torch.cat((prev_h, x), dim=1)
    #Run forget gate
    inner_state = inner_state * torch.sigmoid(F.linear(inputs, self.forget_gate_w, self.forget_gate_b))
    #Run update gate
    inner_state = inner_state + (torch.sigmoid(F.linear(inputs, self.input_gate_w, self.input_gate_b)) * torch.tanh(F.linear(inputs,self.candidate_w, self.candidate_b)))
    #Run outputs
    out = torch.tanh(inner_state) * torch.sigmoid(F.linear(inputs, self.output_gate_w, self.output_gate_b))
    return (out, inner_state)
    

  def reset_parameters(self):
    sqrt_k = (1. / self.hidden_size)**0.5
    with torch.no_grad():
      for param in self.parameters():
        param.uniform_(-sqrt_k, sqrt_k)
    return

  def extra_repr(self):
    return 'input_size={}, hidden_size={}, bias={}'.format(
        self.input_size, self.hidden_size, self.bias is not True)

  def count_parameters(self):
    print('Total Parameters: %d' %
          sum(p.numel() for p in self.parameters() if p.requires_grad))
    return


class PeepholedLSTMCell(nn.Module):

  def __init__(self, input_size, hidden_size, bias=False):
    super().__init__()

    self.input_size = input_size
    self.hidden_size = hidden_size
    self.bias = bias

    #####################################################################
    # Implement here following the given signature                      #
    raise NotImplementedError
    #####################################################################

    return

  def forward(self, x, prev_state):
    #####################################################################
    # Implement here following the given signature                      #
    raise NotImplementedError
    #####################################################################
    return

  def reset_parameters(self):
    sqrt_k = (1. / self.hidden_size)**0.5
    with torch.no_grad():
      for param in self.parameters():
        param.uniform_(-sqrt_k, sqrt_k)
    return

  def extra_repr(self):
    return 'input_size={}, hidden_size={}, bias={}'.format(
        self.input_size, self.hidden_size, self.bias is not True)

  def count_parameters(self):
    print('Total Parameters: %d' %
          sum(p.numel() for p in self.parameters() if p.requires_grad))
    return


class CoupledLSTMCell(nn.Module):

  def __init__(self, input_size, hidden_size, bias=False):
    super().__init__()

    self.input_size = input_size
    self.hidden_size = hidden_size
    self.bias = bias

    #####################################################################
    # Implement here following the given signature                      #
    raise NotImplementedError
    #####################################################################

    return

  def forward(self, x, prev_state):
    #####################################################################
    # Implement here following the given signature                      #
    raise NotImplementedError
    #####################################################################
    return

  def reset_parameters(self):
    sqrt_k = (1. / self.hidden_size)**0.5
    with torch.no_grad():
      for param in self.parameters():
        param.uniform_(-sqrt_k, sqrt_k)
    return

  def extra_repr(self):
    return 'input_size={}, hidden_size={}, bias={}'.format(
        self.input_size, self.hidden_size, self.bias is not True)

  def count_parameters(self):
    print('Total Parameters: %d' %
          sum(p.numel() for p in self.parameters() if p.requires_grad))
    return
