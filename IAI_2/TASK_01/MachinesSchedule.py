from queue import Queue

class Tasks:
    def __init__(self, order_id, task_list=None):
        self.order_id = order_id
        self.task_list = task_list if task_list else []
        self.all_done = False

    def __repr__(self):
        return f"Tasks(order_id={self.order_id}, tasks={self.task_list})"

    def add_task(self, task):
        self.task_list.append(task)

    def reset_tasks(self):
        for task in self.task_list:
            task.done = False
        self.all_done = False

    def do_task(self):
        for task in self.task_list:
            if not task.is_done():
                task.do()
                if task.task_order == len(self.task_list) - 1:
                    self.all_done = True
                return task
        return None

class Task:
    def __init__(self, machine, duration, task_order, order_id):
        self.machine = machine
        self.duration = duration
        self.task_order = task_order
        self.order_id = order_id
        self.done = False

    def __repr__(self):
        return f"Task(machine={self.machine}, duration={self.duration}, task_order={self.task_order}, order_id={self.order_id})"

    def is_done(self):
        return self.done

    def do(self):
        self.done = True

class Event:
    def __init__(self, start_time, task):
        self.task = task
        self.start_time = start_time
        self.end_time = start_time + task.duration

    def __repr__(self):
        return f"Event(task={self.task}, start_time={self.start_time}, end_time={self.end_time})"

class MachinesSchedule:
    def __init__(self):
        self.schedule = {}

    def add_machine(self, machine_name):
        if machine_name not in self.schedule:
            self.schedule[machine_name] = []

    def add_event(self, tasks):
        current_task = tasks.do_task()
        if current_task is None:
            return False

        task_order = current_task.task_order
        previous_task = tasks.task_list[task_order - 1] if task_order > 0 else None

        machine_schedule = self.schedule[current_task.machine]

        prev_task_end_time = (
            max(
                (event.end_time for event in self.schedule[previous_task.machine] if event.task == previous_task),
                default=0,
            )
            if previous_task
            else 0
        )

        start_time = max(machine_schedule[-1].end_time if machine_schedule else 0, prev_task_end_time)
        new_event = Event(start_time, current_task)
        machine_schedule.append(new_event)
        return True

    def get_schedule(self):
        return self.schedule

    def clear_schedule(self):
        for machine in self.schedule:
            self.schedule[machine] = []

    def get_total_time(self):
        return max(
            (event.end_time for machine_events in self.schedule.values() for event in machine_events),
            default=0,
        )

    def get_scheduled_events_count(self):
        return sum(len(events) for events in self.schedule.values())

    def print_schedule(self):
        for machine, events in self.schedule.items():
            print(f"Machine {machine}: {events}")
