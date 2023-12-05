# Implement the auxiliary list as a deque
from collections import deque
from typing import Any


class auxiliaryList():
    def __init__(self, maxSize=0):
        self.auxiliary_list = deque()
        self.maxSize = maxSize

    def add(self, subjectID, resourceID, operation):
        access = (subjectID, resourceID, operation)
        if len(self.auxiliary_list) < self.maxSize:
            self.auxiliary_list.append(access)
        else:
            print("Auxiliary list is full !")

    def remove(self):
        if len(self.auxiliary_list) > 0:
            return self.auxiliary_list.popleft()
        else:
            print("Auxiliary list is empty !")

    def size(self):
        return len(self.auxiliary_list)

    def isEmpty(self):
        return len(self.auxiliary_list) == 0

    def getKeyValue(self, key):
        if type(key) == int:
            return self.auxiliary_list[key]
        print("Invalid key !")
