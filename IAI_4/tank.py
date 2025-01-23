from coppeliasim_zmqremoteapi_client import RemoteAPIClient

class Tank:
    def __init__(self):
        # Connect to the remote API client
        client = RemoteAPIClient()
        self.sim = client.getObject('sim')

        # Get handles to robot drivers
        self.left_front_handle = self.sim.getObject('/left_front')
        self.left_back_handle = self.sim.getObject('/left_back')
        self.right_back_handle = self.sim.getObject('/right_back')
        self.right_front_handle = self.sim.getObject('/right_front')

        self.side_handles = []
        for l in 'rl':
            for i in range(1, 7):
                handle = self.sim.getObject(f'/sj_{l}_{i}')
                self.side_handles.append(handle)

        # Initial velocity
        self.leftvelocity = 0
        self.rightvelocity = 0
        self.MaxVel = 10
        self.dVel = 1

    def stop(self):
        # Set drivers to stop mode
        force = 0
        self.sim.setJointForce(self.left_front_handle, force)
        self.sim.setJointForce(self.left_back_handle, force)
        self.sim.setJointForce(self.right_back_handle, force)
        self.sim.setJointForce(self.right_front_handle, force)

        force = 10
        for h in self.side_handles:
            self.sim.setJointForce(h, force)

        # Brake
        self.leftvelocity = 10
        self.rightvelocity = 10
        self.sim.setJointTargetVelocity(self.left_front_handle, self.leftvelocity)
        self.sim.setJointTargetVelocity(self.left_back_handle, self.leftvelocity)
        self.sim.setJointTargetVelocity(self.right_back_handle, self.rightvelocity)
        self.sim.setJointTargetVelocity(self.right_front_handle, self.rightvelocity)

    def go(self):
        # Set drivers to go mode
        force = 10
        self.sim.setJointForce(self.left_front_handle, force)
        self.sim.setJointForce(self.left_back_handle, force)
        self.sim.setJointForce(self.right_back_handle, force)
        self.sim.setJointForce(self.right_front_handle, force)

        force = 0
        for h in self.side_handles:
            self.sim.setJointForce(h, force)

    def setVelocity(self):
        # Verify if the velocity is in the correct range
        self.leftvelocity = max(min(self.leftvelocity, self.MaxVel), -self.MaxVel)
        self.rightvelocity = max(min(self.rightvelocity, self.MaxVel), -self.MaxVel)

        # Send the velocity values to the drivers
        self.sim.setJointTargetVelocity(self.left_back_handle, self.leftvelocity)
        self.sim.setJointTargetVelocity(self.right_back_handle, self.rightvelocity)

    def forward(self, velocity=None):
        self.go()
        if velocity is not None:
            self.leftvelocity = velocity
            self.rightvelocity = velocity
        else:
            self.rightvelocity = self.leftvelocity = (self.leftvelocity + self.rightvelocity) / 2
            self.leftvelocity += self.dVel
            self.rightvelocity += self.dVel
        self.setVelocity()

    def backward(self, velocity=None):
        self.go()
        if velocity is not None:
            self.leftvelocity = -velocity
            self.rightvelocity = -velocity
        else:
            self.rightvelocity = self.leftvelocity = (self.leftvelocity + self.rightvelocity) / 2
            self.leftvelocity -= self.dVel
            self.rightvelocity -= self.dVel
        self.setVelocity()

    def turn_left(self, velocity=None):
        self.go()
        if velocity is not None:
            self.leftvelocity = -velocity
            self.rightvelocity = velocity
        else:
            self.leftvelocity -= self.dVel
            self.rightvelocity += self.dVel
        self.setVelocity()

    def turn_right(self, velocity=None):
        self.go()
        if velocity is not None:
            self.leftvelocity = velocity
            self.rightvelocity = -velocity
        else:
            self.leftvelocity += self.dVel
            self.rightvelocity -= self.dVel
        self.setVelocity()