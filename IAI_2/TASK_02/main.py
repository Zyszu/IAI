import numpy as np
import copy

# Define the function to minimize
def objective_function(position):
    x, y = position
    return (1.5 - x - x * y) ** 2 + (2.25 - x + x * y ** 2) ** 2 + (2.625 - x + x * y ** 3) ** 2

# PSO parameters
num_particles = 100         # Number of particles in the swarm
num_dimensions = 2         # Number of dimensions (x, y)
num_iterations = 1000        # Number of iterations
w = 0.1                    # Inertia weight
c1 = 1.5                   # Cognitive coefficient (personal best influence)
c2 = 1.5                   # Social coefficient (global best influence)
position_min = -4.5        # Minimum position value
position_max = 4.5         # Maximum position value
velocity_min = -.01          # Minimum velocity
velocity_max = .01           # Maximum velocity

iterations_positions = []

# Initialize particle positions and velocities
positions = np.random.uniform(position_min, position_max, (num_particles, num_dimensions))
velocities = np.random.uniform(velocity_min, velocity_max, (num_particles, num_dimensions))

# Initialize personal best positions and their corresponding values
personal_best_positions = positions.copy()
personal_best_scores = np.array([objective_function(pos) for pos in personal_best_positions])

# Initialize the global best position and its corresponding value
global_best_position = personal_best_positions[np.argmin(personal_best_scores)]
global_best_score = np.min(personal_best_scores)

# PSO algorithm
for iteration in range(num_iterations):
    for i in range(num_particles):
        # Update velocity
        inertia = w * velocities[i]
        cognitive_component = c1 * np.random.rand() * (personal_best_positions[i] - positions[i])
        social_component = c2 * np.random.rand() * (global_best_position - positions[i])
        velocities[i] = inertia + cognitive_component + social_component

        # Clamp velocities to the specified range
        velocities[i] = np.clip(velocities[i], velocity_min, velocity_max)

        # Update position
        positions[i] += velocities[i]
        positions[i] = np.clip(positions[i], position_min, position_max)  # Ensure positions stay within bounds

        # Evaluate fitness
        current_score = objective_function(positions[i])

        # Update personal best if the current position is better
        if current_score < personal_best_scores[i]:
            personal_best_positions[i] = positions[i]
            personal_best_scores[i] = current_score

    # Update global best if a better solution is found
    min_personal_best_index = np.argmin(personal_best_scores)
    if personal_best_scores[min_personal_best_index] < global_best_score:
        global_best_position = personal_best_positions[min_personal_best_index]
        global_best_score = personal_best_scores[min_personal_best_index]

    iterations_positions.append([copy.deepcopy(positions), global_best_score])
    
    # Print the best score for each iteration (optional)
    print(f"Iteration {iteration + 1}/{num_iterations}, Best Score: {global_best_score}")
    

# Output the best found solution
print(f"\nOptimal Solution: x = {global_best_position[0]}, y = {global_best_position[1]}")
print(f"Minimum Value: {global_best_score}")

import matplotlib.animation as animation
import matplotlib.pyplot as plt
from matplotlib.colors import LogNorm

fig, ax = plt.subplots()

pos = [np.linspace(-4.5, 4.5, 100) for p in range(2)]
X, Y = np.meshgrid(pos[0], pos[1])
Z = objective_function([X, Y])

levs = [1e-3, 1e-2, 1e-1, 1e0, 1e1, 1e2, 1e3, 1e4, 1e5, 1e6]



pos, score_best = iterations_positions[0]

x, y = np.transpose(pos)

contours = plt.contourf(X, Y, Z, levels=levs, cmap='Blues_r', norm = LogNorm())
scat = ax.scatter(x, y, s=5, color='red')
plt.colorbar()

plt.xlabel("x")
plt.ylabel("y")
ax.set_xlim([-4.5, 4.5])
ax.set_ylim([-4.5, 4.5])

def animate(i):
    pos, score_best = iterations_positions[i]
    x, y = np.transpose(pos)
    data = list(zip(x, y))
    scat.set_offsets(data)
    
    plt.title(f"iteration: {i+1}\nbest min = {score_best}")
    return scat,

ani = animation.FuncAnimation(fig, animate, repeat=False,
                                    frames=len(iterations_positions), interval=10)

# To save the animation using Pillow as a gif
# writer = animation.PillowWriter(fps=15,
#                                 metadata=dict(artist='Me'),
#                                 bitrate=1800)
# ani.save('scatter.gif', writer=writer)

plt.show()

score_best = [1/i[1] for i in iterations_positions]

x = np.arange(len(score_best))

plt.title(f"itetations best scores")

plt.xlabel("iteration")
plt.ylabel("iteration best score")
plt.plot(x, score_best)
plt.show()