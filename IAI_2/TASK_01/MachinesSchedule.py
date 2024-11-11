from queue import Queue

class Tasks(object):
    def __new__(cls, orderID, tasks = None):
        instance = super().__new__(cls)
        return instance
    
    def __init__(self, orderID, tasks = None):
        self.orderID        = orderID
        self.tasks          = tasks if tasks is not None else []
        self.all_done       = False
    
    def __repr__(self):
        return f"Tasks({self.orderID, self.tasks})"
    
    def oids(self):
        return self.orderID
    
    def addTask(self, task):
        self.tasks.append(task)
        
    def are_done(self):
        return self.all_done
        
    def do_task(self):
        for task in self.tasks:
            if not task.is_done():
                task.do()
                
                if(task.taskOrder == 10):
                    self.all_done = True
                return task
    
    def reset_tasks(self):
        for task in self.tasks:
            task.done = False
        self.all_done = False
        
    

class Task(object):
    def __new__(cls, machine, duration, taskOrder, orderID):
        instance = super().__new__(cls)
        return instance
    
    def __init__(self, machine, duration, taskOrder, orderID):
        self.machine        = machine
        self.duration       = duration
        self.taskOrder      = taskOrder
        self.orderID        = orderID
        self.done           = False
        
    def __repr__(self):
        return f"Task({self.machine}, {self.duration}, {self.taskOrder}, {self.orderID})"
    
    def is_done(self):
        return self.done
    
    def do(self):
        self.done = True

class Event:
    def __init__(self, timeEventStart, task):
        self.task           = task
        self.timeEventStart = timeEventStart
        self.timeEventEnd   = timeEventStart + task.duration
    
    def __repr__(self):
        return f"Event(Task({self.task.orderID}, {self.task.taskOrder}), {self.timeEventStart}, {self.timeEventEnd})"

class MachinesSchedule:
    def __init__(self):
        # Initialize an empty dictionary to store the schedule for each machine
        self.schedule = {}

    def add_machine(self, machine_name):
        # Add a new machine with an empty queue if it doesn't already exist
        if machine_name not in self.schedule:
            self.schedule[machine_name] = []
    
    def get_mashines(self):
        return self.schedule.keys()
    
    def get_mashineShedule(self, machine_name):
        return self.schedule[machine_name]

    def add_event(self, tasks):
        currTask = tasks.do_task()
        
        # if currTask == None:
        #     map = {}
        #     for g in gen:
        #         if not g.orderID in map:
        #             map[g.orderID] = 1
        #             continue
        #         map[g.orderID] += 1
            
        #     print(map)
        #     print(sum(map[key] for key in map.keys()))
        #     print(max(map[key] for key in map.keys()))
        
        currTaksOrder = currTask.taskOrder
        prevTask = None if currTaksOrder == 0 else tasks.tasks[currTaksOrder - 1]
        mashineShedule = self.schedule[currTask.machine]
        
        prevTaskTimeEventEnd = 0
        if prevTask != None:
            prevEvents = self.schedule[prevTask.machine]
            for event in prevEvents:
                if event.task == prevTask:
                    prevTaskTimeEventEnd = event.timeEventEnd
        
        if not mashineShedule:
            tStart = prevTaskTimeEventEnd
            newEvent = Event(tStart, currTask)
            mashineShedule.append(newEvent)
            return True
        
        timeEventStart = mashineShedule[-1].timeEventEnd
        tStart = timeEventStart if timeEventStart > prevTaskTimeEventEnd else prevTaskTimeEventEnd
        newEvent = Event(tStart, currTask)
        mashineShedule.append(newEvent)
        return True
        
    def get_last_event(self, machine_name):
        self.schedule[machine_name][-1]

    def get_schedule(self):
        return self.schedule
    
    def get_total_time(self):
        total_time = 0
        
        for key in self.schedule.keys():
            event_list = self.schedule[key]
            last_event = event_list[-1]
            
            if(last_event.timeEventEnd > total_time):
                total_time = last_event.timeEventEnd
        
        return total_time
    
    def get_sheduled_events_count(self):
        events = 0
        for key in self.schedule.keys():
            events += len(self.schedule[key])
        return events
    
    def print_shedule(self):
        for key in self.schedule.keys():
            print(self.schedule[key])
    
    def clear_shedule(self):
        for key in self.schedule.keys():
            self.schedule[key] = []
