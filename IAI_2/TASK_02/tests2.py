import numpy as np
import matplotlib.pyplot as plt
import matplotlib.animation as animation

x, y = np.meshgrid(np.arange(0, 10, 0.1), np.arange(0, 10, 0.1))
z = np.max(np.dstack([abs(x), abs(y)]), 2)

plt.figure(figsize=(6, 6))
cs = plt.contour(x, y, z)
pts = np.array([(1.5, 3.0), (1.5, 4.4), (1.5, 6.0), (1.5, 7.5)])
plt.clabel(cs, manual=pts)

plt.show()