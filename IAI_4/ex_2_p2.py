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


# Define the fuzzy variables
distance = ctrl.Antecedent(np.arange(0, 10, .01), 'distance')
velocity = ctrl.Consequent(np.arange(0, 10, .1), 'velocity')

# Define membership functions for distance
distance['very_close'] = fuzz.trimf(distance.universe, [0, 0, 0.1])
distance['close'] = fuzz.trimf(distance.universe, [0.1, 5, 10])

# Define membership functions for velocity
velocity['stop'] = fuzz.trimf(velocity.universe, [0, 0, 0])
velocity['slow'] = fuzz.trimf(velocity.universe, [0, 2, 5])

# Define the fuzzy rules
rule1 = ctrl.Rule(distance['very_close'], velocity['stop'])
rule2 = ctrl.Rule(distance['close'], velocity['slow'])

# Create a control system and simulation
velocity_ctrl = ctrl.ControlSystem([rule1, rule2])
velocity_sim = ctrl.ControlSystemSimulation(velocity_ctrl)

'''
    This part is devided into 3 stages
    S1:
        find empty space, during forward drive
    S2:
        position the tank in the middle of empty space
    S3:
        rotate tank, drive int the space, rotate tank again,
        make adjustements
'''

MAX_TIME = 120

sim.startSimulation()
my_tank.stop()
while (t := sim.getSimulationTime()) < 5:
    sim.step()

# STAGE 1
while (t := sim.getSimulationTime()) < 20:
    sensores_raw = my_tank.read_proximity_sensors()
    for sn in sensor_names:
        sensor_data[sn] = sensores_raw[sn]

    val = np.abs(sensor_data['WN']['detectedPoint'] - sensor_data['WS']['detectedPoint'])
    

    velocity_sim.input['distance'] = val
    velocity_sim.compute()
    vel = np.round(velocity_sim.output['velocity'], 1)

    print(sensores_raw['WS'])

    # print(f'val: {val}')
    # print(f'speed: {vel}')

    if sensor_data['SE']['detectedPoint'] > 1.55:
        my_tank.forward(vel)
    else:
        my_tank.forward(4)
    
    sim.step()


# STAGE 2
# ~STEP 1
# t_curr = sim.getSimulationTime()
# t_target = t_curr + 5
# while t_target - t_curr > 0:
#     sensores_raw = my_tank.read_proximity_sensors()
#     for sn in sensor_names:
#         sensor_data[sn] = sensores_raw[sn]

#     val = np.abs(3.05 - (sensor_data['NE']['detectedPoint'] + sensor_data['NW']['detectedPoint']))
    

#     velocity_sim.input['distance'] = val
#     velocity_sim.compute()

#     vel = np.round(velocity_sim.output['velocity'], 1)
#     print(f'val: {val}')
#     print(f'speed: {vel}')

#     my_tank.turn_right(vel)
    
#     sim.step()
#     t_curr = sim.getSimulationTime()

t_curr = sim.getSimulationTime()
t_target = t_curr + 1.8
while t_target - t_curr > 0:
    my_tank.turn_right(5)
    sim.step()
    t_curr = sim.getSimulationTime()



# ~STEP 2
t_curr = sim.getSimulationTime()
t_target = t_curr + 20
while t_target - t_curr > 0:
    sensores_raw = my_tank.read_proximity_sensors()
    for sn in sensor_names:
        sensor_data[sn] = sensores_raw[sn]

    val = np.abs( (sensor_data['EN']['detectedPoint'] + sensor_data['WN']['detectedPoint'])
                 - (sensor_data['ES']['detectedPoint'] + sensor_data['WS']['detectedPoint']))
    

    velocity_sim.input['distance'] = val
    velocity_sim.compute()

    vel = np.round(velocity_sim.output['velocity'], 1)
    print(f'val: {val}')
    print(f'speed: {vel}')

    my_tank.forward(vel)
    
    sim.step()
    t_curr = sim.getSimulationTime()


t_curr = sim.getSimulationTime()
t_target = t_curr + 1.8
while t_target - t_curr > 0:
    my_tank.turn_left(5)
    sim.step()
    t_curr = sim.getSimulationTime()

my_tank.stop()

t_curr = sim.getSimulationTime()
t_target = t_curr + 5
while t_target - t_curr > 0:
    sim.step()
    t_curr = sim.getSimulationTime()

sim.stopSimulation()