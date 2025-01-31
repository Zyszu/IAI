from coppeliasim_zmqremoteapi_client import RemoteAPIClient

client = RemoteAPIClient()
sim = client.require('sim')

sim.setStepping(True)

sensor_names = ['EN', 'ES', 'NE', 'NW', 'SE', 'SW', 'WN', 'WS']

left_front_handle   = sim.getObject('/left_front')
left_back_handle    = sim.getObject('/left_back')
right_back_handle   = sim.getObject('/right_back')
right_front_handle  = sim.getObject('/right_front')


FS = 100
SP = 5

sim.setJointForce(left_back_handle, FS)
sim.setJointForce(left_front_handle, FS)

sim.setJointForce(right_back_handle, FS)
sim.setJointForce(right_front_handle, FS)

sim.setJointTargetVelocity(left_back_handle, SP)
sim.setJointTargetVelocity(left_front_handle, SP)

sim.setJointTargetVelocity(right_back_handle, -SP)
sim.setJointTargetVelocity(right_front_handle, SP)

sim.startSimulation()
while (t := sim.getSimulationTime()) < 10:

    sim.step()

sim.stopSimulation()