import random as rand
import numpy as np
from neuralnetwork import NeuralNetwork

file_iris_data = "iris.data"

IRIS_LABLES = {
    'Iris-setosa': 0,
    'Iris-versicolor': 1,
    'Iris-virginica': 2
}

# returns: [..., [sepal length, sepal width, petal length, petal width, class], ...]
def load_iris_data():
    iris_data = []
    with open(file_iris_data, "r") as f:
        iris_data = f.read().strip().split('\n')  # Strip any empty lines

    iris_data = [data.split(',') for data in iris_data if len(data.split(',')) == 5]  # Check each row

    rand.shuffle(iris_data)
    
    split_index = (len(iris_data) * 80) // 100
    iris_training_data = iris_data[:split_index]
    iris_testing_data  = iris_data[split_index:]
    
    iris_training_data_inputs = [list(map(float, inp[:4])) for inp in iris_training_data]
    iris_training_data_labels = [IRIS_LABLES[inp[-1]] for inp in iris_training_data]
    
    iris_testing_data_inputs = [list(map(float, inp[:4])) for inp in iris_testing_data]
    iris_testing_data_labels = [IRIS_LABLES[inp[-1]] for inp in iris_testing_data]
    
    return (np.array(iris_training_data_inputs, dtype=np.float64), 
            iris_training_data_labels, 
            np.array(iris_testing_data_inputs, dtype=np.float64), 
            iris_testing_data_labels)

if __name__ == "__main__":
    iris_training_data_inputs, iris_training_data_labels, iris_testing_data_inputs, iris_testing_data_labels = load_iris_data()
    
    # Initial learning rate, which will be adjusted over time
    initial_learning_rate = 0.01
    nn = NeuralNetwork()
    
    # Training loop with learning rate decay
    for epoch in range(1000):
        for i in range(len(iris_training_data_inputs)):
            learning_rate = initial_learning_rate / (1 + epoch / 50)  # Learning rate schedule
            nn.learn(iris_training_data_inputs[i].reshape(-1, 1), iris_training_data_labels[i], learning_rate)
        
        if epoch % 10 == 0:
            print('Epoch:', epoch)
                
    # Testing phase
    correct_predictions = 0
    for i in range(len(iris_testing_data_inputs)):
        output = nn.forward(iris_testing_data_inputs[i].reshape(-1, 1))[-1]
        predicted_label = np.argmax(output)
        
        if predicted_label == iris_testing_data_labels[i]:
            correct_predictions += 1

    accuracy = correct_predictions / len(iris_testing_data_labels) * 100
    print(f'Test Accuracy: {accuracy:.2f}%')
    # print(nn.weights)
    # print(nn.biases)
