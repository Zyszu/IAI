import numpy as np
import random as rand

class NeuralNetwork:
    def __init__(self, input_size=4, hidden_layers=[2, 5], output_size=3):
        self.input_size = input_size
        self.hidden_layers = hidden_layers
        self.output_size = output_size
        self.weights = []
        self.biases = []
        
        SCALE_FACTOR = .1
        
        # It seams like using np.random.standard_normal and np.random.rand in this case has
        # no significant impact on how fast the neural network learns

        # input to hidden layer network weights and biases
        self.weights.append(SCALE_FACTOR * np.random.standard_normal(size=(hidden_layers[1], input_size)))
        self.biases.append(np.zeros((hidden_layers[1], 1)))

        # hidden layer network weights and biases
        for i in range(hidden_layers[0] - 1):
            self.weights.append(SCALE_FACTOR * np.random.standard_normal(size=(hidden_layers[1], hidden_layers[1])))
            self.biases.append(np.zeros((hidden_layers[1], 1)))
        
        # hidden layer network to output weights and biases
        self.weights.append(SCALE_FACTOR * np.random.standard_normal(size=(output_size, hidden_layers[1])))
        self.biases.append(np.zeros((output_size, 1)))
    
    def activation_ReLU(self, inputs):
        return np.maximum(0, inputs)
    
    def activation_deriv_ReLU(self, inputs):
        return inputs > 0
    
    def activation_Softmax(self, inputs):
        exp_values = np.exp(inputs - np.max(inputs))
        return exp_values / np.sum(exp_values)
    
    def forward(self, inputs):
        layers = [inputs]
        
        for i in range(len(self.weights) - 1):
            hidden_output = self.activation_ReLU(np.dot(self.weights[i], layers[-1]) + self.biases[i])
            layers.append(hidden_output)
        
        soft_max_output = self.activation_Softmax(np.dot(self.weights[-1], layers[-1]) + self.biases[-1])
        layers.append(soft_max_output)
        
        return layers
    
    def loss(self, output, correct_output_label):
        # Cross-entropy loss in this case is better than R^2 loss function
        # Because it allows to find out the minimum possible loss faster and using least steps
        return -np.log(output[correct_output_label])
    
    # https://www.youtube.com/watch?v=IN2XmBhILt4&list=PLjUC8HjyxGTSrn4cZEw9Uw8R0STaRcbYY&index=2
    # https://www.youtube.com/watch?v=6ArSys5qHAU&list=PLjUC8HjyxGTSrn4cZEw9Uw8R0STaRcbYY&index=6
    def backward_propagation(self, inputs, correct_output_label, learning_rate):
        # Step 1: Forward pass to get all layers' activations
        layers = self.forward(inputs)
        
        # Step 2: Initialize lists to hold gradients for weights and biases
        d_weights = [np.zeros(w.shape) for w in self.weights]
        d_biases = [np.zeros(b.shape) for b in self.biases]
        
        # Step 3: Calculate gradient for the output layer
        delta = layers[-1]
        delta[correct_output_label] -= 1  # Gradient of softmax + cross-entropy loss
        
        d_weights[-1] = np.dot(delta, layers[-2].T)
        d_biases[-1] = delta
        
        # Step 4: Backpropagate through hidden layers
        for i in reversed(range(len(self.weights) - 1)):
            delta = np.dot(self.weights[i+1].T, delta) * self.activation_deriv_ReLU(layers[i+1])
            d_weights[i] = np.dot(delta, layers[i].T)
            d_biases[i] = delta
        
        # Step 5: Update parameters using the gradients and learning rate (learning_rate)
        self.update_params(d_weights, d_biases, learning_rate)

        # Step 6: Returning max derivatives values in order to stop learning
        #         when they are low enough
        d_weights_max = np.max([np.max(dwm) for dwm in d_weights])
        d_biases_max = np.max([np.max(dbm) for dbm in d_biases])
        return d_weights_max, d_biases_max
    
    def update_params(self, d_weights, d_biases, learning_rate):
        self.weights = [w - learning_rate * dw for w, dw in zip(self.weights, d_weights)]
        self.biases = [b - learning_rate * db for b, db in zip(self.biases, d_biases)]
