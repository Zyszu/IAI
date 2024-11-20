import pandas as pd
from MachinesSchedule import MachinesSchedule, Task, Tasks

FILE_NAME = "GA_task.xlsx"
MACHINE_COUNT = 10

data_frame = pd.read_excel(FILE_NAME, header=1)

ORDERS = []
for order_id, columns in enumerate(range(0, data_frame.shape[1], 2)):
    tasks = Tasks(order_id)
    for index, row in data_frame.iloc[:, columns:columns+2].iterrows():
        task = Task(machine=row[0], duration=row[1], task_order=index, order_id=order_id)
        tasks.add_task(task)
    ORDERS.append(tasks)

MACHINES_SCHEDULE = MachinesSchedule()
for machine_id in range(1, MACHINE_COUNT + 1):
    MACHINES_SCHEDULE.add_machine(machine_id)