import pandas as pd
import numpy as np

from MachinesSchedule import MachinesSchedule, Task, Tasks

file_name = "GA_task.xlsx"
dataFrame = pd.read_excel(file_name, header = 1,  skiprows = 0)

ordersList = []

numberOfColumns = int(dataFrame.shape[1] / 2)
for i in range(numberOfColumns):
    index = i*2
    order = dataFrame.iloc[:, index:index+2].to_numpy()
    ordersList.append(order)
        

orders = []

for i, order in enumerate(ordersList):
    tasks = Tasks(i)
    for j, orderTask in enumerate(order):
        task = Task(orderTask[0], orderTask[1], j, i)
        tasks.addTask(task)
    
    orders.append(tasks)

# outcome of this file
MACHINES_SCHEDULE = MachinesSchedule()
ORDERS = orders

for i in range(10):
    MACHINES_SCHEDULE.add_machine(i + 1)
    