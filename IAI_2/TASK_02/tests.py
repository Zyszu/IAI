import numpy as np
import matplotlib.pyplot as plt
from matplotlib.colors import LogNorm


def objective_function(position):
    x, y = position
    return (1.5 - x - x * y) ** 2 + (2.25 - x + x * y ** 2) ** 2 + (2.625 - x + x * y ** 3) ** 2

pos = [np.linspace(-4.5, 4.5, 100) for p in range(2)]
X, Y = np.meshgrid(pos[0], pos[1])
Z = objective_function([X, Y])

levs = [1e-3, 1e-2, 1e-1, 1e0, 1e1, 1e2, 1e3, 1e4, 1e5, 1e6]
contours = plt.contourf(X, Y, Z, levels=levs, cmap='Blues_r', norm = LogNorm())

plt.xlim([-4.5, 4.5])
plt.ylim([-4.5, 4.5])
plt.colorbar()
plt.show()