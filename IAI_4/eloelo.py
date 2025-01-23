from coppeliasim_zmqremoteapi_client import RemoteAPIClient
from tank import Tank


client = RemoteAPIClient()
sim = client.require('sim')

sim.setStepping(True)

# left_front_handle = None
# left_back_handle = None
# right_front_handle = None
# right_back_handle = None

# left_front_handle = sim.getObject('/left_front')
# left_back_handle  = sim.getObject('/left_back')
# right_back_handle = sim.getObject('/right_back')
# right_front_handle= sim.getObject('/right_front')

# handles = [left_front_handle, left_back_handle, right_back_handle, right_front_handle]

# for handle in handles:
#     sim.setJointForce(handle, 10)

my_tank = Tank()

sim.startSimulation()
while (t := sim.getSimulationTime()) < 10:
    print(f'Simulation time: {t:.2f} [s]')
    my_tank.forward(10.0 - sim.getSimulationTime())
    sim.step()
sim.stopSimulation()
