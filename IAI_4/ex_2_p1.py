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
velocity['forward'] = fuzz.trimf(velocity.universe,     [0,     10,     10])
velocity['stop'] = fuzz.trimf(velocity.universe,        [-1,    0,      1] )
velocity['backward'] = fuzz.trimf(velocity.universe,    [-10,   -10,    0] )

# Define the fuzzy rules
rule1 = ctrl.Rule(distance['far-front'], velocity['forward'])
rule2 = ctrl.Rule(distance['very_close'], velocity['stop'])
rule3 = ctrl.Rule(distance['far-back'], velocity['backward'])

# Create a control system and simulation
velocity_ctrl = ctrl.ControlSystem([rule1, rule2, rule3])
velocity_sim = ctrl.ControlSystemSimulation(velocity_ctrl)


MAX_TIME = 120
sim.startSimulation()

# leting copella sim load all necessary data like: sensors, objects, etc.
# and reseting prev existing tank velocity
my_tank.stop()
while (t := sim.getSimulationTime()) < 5:
    sim.step()

# STAGE 1

# drive forward untill empty space is found
while (t := sim.getSimulationTime()) < MAX_TIME:
    sensores_raw = my_tank.read_proximity_sensors()
    for sn in sensor_names:
        sensor_data[sn] = sensores_raw[sn]

    dist1 = sensor_data['NE']['detectedObjectHandle'][2]
    dist2 = sensor_data['SE']['detectedObjectHandle'][2]

    if dist1 + dist2 > 3 and dist1 < 2 and dist2 < 2:
        my_tank.stop()
        break

    my_tank.forward(10)
    sim.step()


# position tank in the middle near the empty space
while (t := sim.getSimulationTime()) < MAX_TIME:
    sensor_data = my_tank.read_proximity_sensors()

    distane_difference = sensor_data['NE']['detectedPoint'] - sensor_data['SE']['detectedPoint']
    velocity_sim.input['distance'] = distane_difference
    velocity_sim.compute()
    vel = velocity_sim.output['velocity']

    if np.abs(vel) < 0.05:
        my_tank.stop()
        break

    my_tank.forward(vel)
    sim.step()

# rotate tank 90 deg
curr_time = sim.getSimulationTime()
stop_time = curr_time + 4.8

my_tank.turn_right(2)
while stop_time - curr_time > 0:
    curr_time = sim.getSimulationTime()
    sim.step()

my_tank.stop()

# forward
curr_time = sim.getSimulationTime()
stop_time = curr_time + 5.5

my_tank.forward(3)
while stop_time - curr_time > 0:
    curr_time = sim.getSimulationTime()
    sim.step()

my_tank.stop()


# rotate tank -90 deg
curr_time = sim.getSimulationTime()
stop_time = curr_time + 4.8

my_tank.turn_left(2)
while stop_time - curr_time > 0:
    curr_time = sim.getSimulationTime()
    sim.step()

my_tank.stop()

# just wait
curr_time = sim.getSimulationTime()
stop_time = curr_time + 10
while stop_time - curr_time > 0:
    curr_time = sim.getSimulationTime()
    sim.step()

sim.stopSimulation()