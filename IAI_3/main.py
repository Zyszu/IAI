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
    # iris_training_data_indexes = [i for i in range(len(iris_training_data_inputs))]
    
    learning_rate = 1e-3
    nn = NeuralNetwork()
    
    # shuffling data every iteration:
    # d_weights and d_biases do not stabilise at any point
    # the whole process seams pretty random :/

    # d_weights_max, d_biases_max = 1, 1
    # iteration = 0
    # while iteration < 1e4 and (d_weights_max > 1e-2 or d_biases_max > 1e-2):
    #     for i in range(len(iris_training_data_inputs)):
    #         d_weights_max, d_biases_max = nn.backward_propagation(
    #             iris_training_data_inputs[iris_training_data_indexes[i]].reshape(-1, 1),
    #             iris_training_data_labels[iris_training_data_indexes[i]], learning_rate
    #             )
            
    #     if iteration % 10 == 0:
    #         print(f'Iteration: {iteration}')
    #         print(f'd_weights_max: {d_weights_max} | d_biases_max: {d_biases_max}')
    #     iteration += 1

    #     rand.shuffle(iris_training_data_indexes)

    d_weights_max, d_biases_max = 1, 1
    iteration = 0
    while iteration < 2e3 and (d_weights_max > 1e-3 or d_biases_max > 1e-3):
        for i in range(len(iris_training_data_inputs)):
            d_weights_max, d_biases_max = nn.backward_propagation(
                iris_training_data_inputs[i].reshape(-1, 1),
                iris_training_data_labels[i], learning_rate
                )
            
        if iteration % 10 == 0:
            print(f'Iteration: {iteration}')
            print(f'd_weights_max: {d_weights_max} | d_biases_max: {d_biases_max}')
        iteration += 1
                
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
