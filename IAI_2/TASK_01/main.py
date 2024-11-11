import random
import copy
import matplotlib.pyplot as plt
import numpy as np

from data_procesing import * # ORDERS, MACHINES_SCHEDULE

POPULATION_SIZE = 100
MUTATION_RATE = 0.90
CROSSOVER_RATE = 0.1
GENERATIONS = 100

scores = []

def load_best_genom():
    f = open("genom_best", "r")
    genom = f.read()
    f.close()
    return [int(i) for i in genom.split('|')[1:]]

def random_genome():
    tasks_array = []
    for i in range(11):
        for _tasks in ORDERS:
            tasks_array.insert(random.randint(0, len(tasks_array)), _tasks)
    
    return tasks_array


def init_population(population_size):
    return [random_genome() for _ in range(population_size)]

def fitness(genom):
    _machines_schedule  = MACHINES_SCHEDULE
    
    for tasks in genom:
        _machines_schedule.add_event(tasks)
    
    val1 = 1.0 / float(_machines_schedule.get_total_time() + 1)
    
    _machines_schedule.clear_shedule()
    for tasks in ORDERS:
        tasks.reset_tasks()
            
    return 1000 * val1

def fitnessInfo(genom):
    _machines_schedule  = MACHINES_SCHEDULE
    
    for tasks in genom:
        _machines_schedule.add_event(tasks)
    
    return _machines_schedule.get_total_time(), _machines_schedule.get_sheduled_events_count()

def select_parent(population, fitnes_values):
    total_fitnes = sum(fitnes_values)
    pick = random.uniform(0, total_fitnes)
    
    current = 0
    for individual, fitnes_value in zip(population, fitnes_values):
        current += fitnes_value
        if current > pick:
            return individual

# [problem] its mixing the tasks so that some of them 
# occure multiple times more than 11 and some of them
# occure lest than 11. This is bad :/
def crossover(parent1, parent2):
    return parent1, parent2
    if random.random() < CROSSOVER_RATE:
        offspring1 = parent1
        offspring2 = parent2
        
        crossover_gen = parent1[random.randint(0, 10)]
        p1_gen_index = parent1.index(crossover_gen)
        p2_gen_index = parent2.index(crossover_gen)
        offspring1[p1_gen_index], offspring1[p2_gen_index] = offspring1[p2_gen_index], offspring1[p1_gen_index]
        offspring2[p2_gen_index], offspring2[p1_gen_index] = offspring2[p1_gen_index], offspring2[p2_gen_index]
        
        return offspring1, offspring2
    else:
        return parent1, parent2
    
def mutate(genome):
    for i in range(len(genome)):
        if not random.random() < MUTATION_RATE:
            return genome
        else:
            for _ in range(random.randint(1, len(genome) // 25)):
                r_pos1 = random.randint(0, len(genome) - 1)
                r_pos2 = random.randint(0, len(genome) - 1)
                temp = genome[r_pos1]
                genome[r_pos1] = genome[r_pos2]
                genome[r_pos2] = temp
            
            return genome
    

def survive(population, fitness_values):
    sorted_population = sorted(zip(population, fitness_values), key=lambda x: x[1], reverse=True)
    
    genomes = [obj for obj, fitness in sorted_population[:2]]
    survivers = []
    
    for genom in genomes:
        newGenome = []
        for _tasks in genom:
            tasks = Tasks(_tasks.orderID)
            for _task in _tasks.tasks:
                tasks.addTask(_task)
            newGenome.append(tasks)
        survivers.append(newGenome)
        
    return survivers

def genetic_algorithm():
    population = init_population(POPULATION_SIZE)
    
    
    for generation in range(GENERATIONS):
        fitness_values = [fitness(genome) for genome in population]
        
        new_population = []
        
        survivers = survive(population, fitness_values)
        new_population.extend(survivers)
        
        for _ in range((POPULATION_SIZE // 2) - 1):
            parent1 = select_parent(population, fitness_values)
            parent2 = select_parent(population, fitness_values)
            
            offspring1, offspring2 = crossover(parent1, parent2)
            new_population.extend([mutate(offspring1), mutate(offspring2)])
        
        population = new_population
        
        fitness_values = [fitness(genome) for genome in population]
        best_fitness = max(fitness_values)
        scores.append(best_fitness)
        print(f"Generation {generation}: Best fitness = {best_fitness} population size: {len(population)}")
        
    best_index = fitness_values.index(max(fitness_values))
    best_solution = population[best_index]
    
    print(f"Best Fitness: {fitness(best_solution)}")
    # print(f"Best Solution: {best_solution}")
    
    time, eventsCount = fitnessInfo(best_solution)
    print(f"there are: {eventsCount}"
          f" completed tasks out of {50*11}. The time it takse to compleet "
          f"all tasks is {time}")
    
    f = open("genom", "w")
    for gen in best_solution:
        f.write("|" + str(gen.oids()))
    f.close()
    

if __name__ == "__main__":
    # genetic_algorithm()
    
    # x = range(len(scores))
    # plt.plot(x, scores)
    # plt.ylabel("score")
    # plt.xlabel("generation")
    # plt.show()
    best_genom = load_best_genom()
    m1, m2 = fitness(best_genom)
    print(m1)