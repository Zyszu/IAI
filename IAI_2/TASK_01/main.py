import random
import copy
import matplotlib.pyplot as plt
from data_procesing import ORDERS, MACHINES_SCHEDULE

# Constants
POPULATION_SIZE = 100
MUTATION_RATE = 0.7
CROSSOVER_RATE = 0.2
GENERATIONS = 10

scores = []

BASE_GENOME = []
for i in range(11):
    for j in range(50):
        BASE_GENOME.append(i)


def random_genome():
    genome = ORDERS[:]
    random.shuffle(genome)
    return genome

def rand_gen():
    genome = []
    for i in range(11):
        for j in range(50):
            genome.append(i)

def init_population(size):
    return [random_genome() for _ in range(size)]

def fitness(genome):
    MACHINES_SCHEDULE.clear_schedule()
    for task in genome:
        MACHINES_SCHEDULE.add_event(task)
    time = MACHINES_SCHEDULE.get_total_time()
    MACHINES_SCHEDULE.clear_schedule()
    for task in ORDERS:
        task.reset_tasks()
    return 1000 / (time + 1)

def crossover(parent1, parent2):
    if random.random() < CROSSOVER_RATE:
        # Randomly select a crossover range
        start, end = sorted(random.sample(range(len(parent1)), 2))
        
        # Extract the subsequence from parent1
        subsequence = parent1[start:end+1] 
        
        # Build offspring by maintaining the relative order from parent2
        child1 = subsequence + [gene for gene in parent2 if gene not in subsequence]
        child2 = subsequence + [gene for gene in parent1 if gene not in subsequence]
        
        return child1, child2
    return parent1, parent2


def mutate(genome):
    if random.random() < MUTATION_RATE:
        idx1, idx2 = random.sample(range(len(genome)), 2)
        genome[idx1], genome[idx2] = genome[idx2], genome[idx1]
    return genome

def select_parent(population, fitness_values):
    total_fitness = sum(fitness_values)
    pick = random.uniform(0, total_fitness)
    current = 0
    for individual, fitness_value in zip(population, fitness_values):
        current += fitness_value
        if current > pick:
            return individual

def genetic_algorithm():
    population = init_population(POPULATION_SIZE)

    for generation in range(GENERATIONS):
        fitness_values = [fitness(genome) for genome in population]
        new_population = []

        # Preserve the best solutions
        top_individuals = sorted(zip(population, fitness_values), key=lambda x: x[1], reverse=True)[:2]
        new_population.extend([copy.deepcopy(ind) for ind, _ in top_individuals])

        while len(new_population) < POPULATION_SIZE:
            parent1 = select_parent(population, fitness_values)
            parent2 = select_parent(population, fitness_values)
            offspring1, offspring2 = crossover(parent1, parent2)
            new_population.extend([mutate(offspring1), mutate(offspring2)])

        population = new_population
        best_fitness = max(fitness_values)
        scores.append(best_fitness)
        print(f"Generation {generation}: Best fitness = {best_fitness}")

    best_genome = population[fitness_values.index(max(fitness_values))]
    print(f"Best solution time: {fitness(best_genome)}")
    return best_genome

if __name__ == "__main__":
    best_genome = genetic_algorithm()
    # plt.plot(scores)
    # plt.xlabel("Generation")
    # plt.ylabel("Fitness")
    # plt.show()
