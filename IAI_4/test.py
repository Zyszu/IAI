from coppeliasim_zmqremoteapi_client import RemoteAPIClient
from tank import Tank

import numpy as np
import skfuzzy as fuzz
from skfuzzy import control as ctrl

client = RemoteAPIClient()
sim = client.require('sim')

sim.setStepping(True)

my_tank = Tank()

sensor_names = ['EN', 'ES', 'NE', 'NW', 'SE', 'SW', 'WN', 'WS']
sensor_data = {}
PREF_SIDE = 'East'
OFFSET = -0.258


# Define the fuzzy variables
distance = ctrl.Antecedent(np.arange(-6, 6, .1), 'distance')
velocity = ctrl.Consequent(np.arange(-10, 10, .1), 'velocity')

# Define membership functions for distance
distance['far-front'] = fuzz.trimf(distance.universe,   [0      + OFFSET,  5    + OFFSET, 5     + OFFSET])
distance['very_close'] = fuzz.trimf(distance.universe,  [-0.2   + OFFSET,  0    + OFFSET, 0.2   + OFFSET])
distance['far-back'] = fuzz.trimf(distance.universe,    [-5     + OFFSET, -5    + OFFSET, 0     + OFFSET])

# Define membership functions for velocity
velocity['forward'] = fuzz.trimf(velocity.universe,     [0, 10, 10])
velocity['stop'] = fuzz.trimf(velocity.universe,        [-1, 0, 1])
velocity['backward'] = fuzz.trimf(velocity.universe,    [-10, -10, 0])

# Define the fuzzy rules
rule1 = ctrl.Rule(distance['far-front'], velocity['forward'])
rule2 = ctrl.Rule(distance['very_close'], velocity['stop'])
rule3 = ctrl.Rule(distance['far-back'], velocity['backward'])

# Create a control system and simulation
velocity_ctrl = ctrl.ControlSystem([rule1, rule2, rule3])
velocity_sim = ctrl.ControlSystemSimulation(velocity_ctrl)

'''
    This part is devided into 3 stages
    S1:
        find empty space, during forward drive and position in the middle of it
    S2:
        ~step1:
            rotate the tank 90deg
        ~step2:
            drive to the middle of parking space
        ~step3:
            rotete the tank -90deg
    S3:
        rotate tank, drive int the space, rotate tank again,
        make adjustements
'''

MAX_TIME = 20

sim.startSimulation()

# leting copella sim load all necessary data like: sensors, objects, etc.
# and reseting prev existing tank velocity
my_tank.stop()
while (t := sim.getSimulationTime()) < 5:
    sim.step()

# position tank in the middle of the empty space
curr_time = sim.getSimulationTime()
stop_time = curr_time + 4.8

my_tank.turn_right(2)
while stop_time - curr_time > 0:
    curr_time = sim.getSimulationTime()
    sim.step()

my_tank.stop()

while (t := sim.getSimulationTime()) < MAX_TIME:
    sim.step()


sim.stopSimulation()