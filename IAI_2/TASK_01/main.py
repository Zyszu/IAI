import random
import copy
import matplotlib.pyplot as plt
from collections import Counter
from data_procesing import ORDERS, MACHINES_SCHEDULE, load_stored_genome, save_genome

# Constants
POPULATION_SIZE = 100
MUTATION_RATE = 0.3
CROSSOVER_RATE = 0.9
GENERATIONS = 100

scores = []

BASE_GENOME = []
for i in range(11):
    for j in range(50):
        BASE_GENOME.append(j)


def random_genome():
    genome = BASE_GENOME[:]
    random.shuffle(genome)
    return genome

def init_population(size):
    return [random_genome() for _ in range(size)]

def fitness(genome):
    MACHINES_SCHEDULE.clear_schedule()
    for index in genome:
        MACHINES_SCHEDULE.add_event(ORDERS[index])
    total_time = MACHINES_SCHEDULE.get_total_time()
    MACHINES_SCHEDULE.clear_schedule()
    for task in ORDERS:
        task.reset_tasks()
    return 1000 / (total_time + 1)

def get_total_time(genome):
    MACHINES_SCHEDULE.clear_schedule()
    for index in genome:
        MACHINES_SCHEDULE.add_event(ORDERS[index])
    total_time = MACHINES_SCHEDULE.get_total_time()
    MACHINES_SCHEDULE.clear_schedule()
    for task in ORDERS:
        task.reset_tasks()
    return total_time

def crossover(parent1, parent2):
    if random.random() < CROSSOVER_RATE:
        # Randomly select a crossover range
        start, end = sorted(random.sample(range(len(parent1)), 2))
        
        # Extract the subsequence from parent1
        subsequence = parent1[start:end+1]
        subsequence_counts = Counter(subsequence)
        
        # Build child1: Start with the subsequence, and then add genes from parent2
        child1 = subsequence[:]
        for gene in parent2:
            if subsequence_counts[gene] > 0:
                subsequence_counts[gene] -= 1
            else:
                child1.append(gene)
        
        # Build child2: Start with the subsequence, and then add genes from parent1
        subsequence_counts = Counter(subsequence)  # Reset the counter
        child2 = subsequence[:]
        for gene in parent1:
            if subsequence_counts[gene] > 0:
                subsequence_counts[gene] -= 1
            else:
                child2.append(gene)
        
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
        if generation % 100 == 0:
            print(f"Generation {generation}: Best fitness = {best_fitness}")

    best_genome = population[fitness_values.index(max(fitness_values))]
    print(f"Best score: {fitness(best_genome)}")
    return best_genome

if __name__ == "__main__":
    best_genome = genetic_algorithm()
    saved_genome = load_stored_genome()
    saved_genome = saved_genome if saved_genome else BASE_GENOME

    t1, t2 = get_total_time(best_genome), get_total_time(saved_genome)

    print(f"New calculated genome total time: {t1}")
    print(f"Stored genome total time: {t2}")

    if t1 < t2:
        print(f"New calcuated genome is beter. saving...")
        save_genome(best_genome)

    # plt.plot(scores)
    # plt.xlabel("Generation")
    # plt.ylabel("Fitness")
    # plt.show()