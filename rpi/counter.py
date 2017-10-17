#!/usr/bin/env python
# coding: latin-1
# Ingmar Stapel
# Date: 20170615
# Version Alpha 0.1
# Decision Maker

from time import sleep
from threading import Thread
import os

import RPi.GPIO as GPIO
import requests


TIME = 0.002
MULTI_THREAD = False

MICRO_STEPS = [
    [True, False, False, True],
    [True, False, False, False],
    [True, True, False, False],
    [False, True, False, False],
    [False, True, True, False],
    [False, False, True, False],
    [False, False, True, True],
    [False, False, False, True]
]

STEPS = [52, 51, 51, 51, 51, 52, 51, 51, 51, 51]

#URL = os.getenv('COUNTER_URL', 'https://tbetous-zenicount.herokuapp.com/count')
URL = os.getenv('COUNTER_URL', 'https://zenicount.blaznyoght.space/api/count')
GPIO.setmode(GPIO.BCM)

class Stepper(object):
    """
    Stepper encapsulate a stepper motor pins and move functions
    """
    def __init__(self, pins):
        """
        Construct a new 'Stepper' object.

        :param pins: the stepper motor pins
        :return: returns nothing
        """
        self.pins = pins
    def init(self):
        """
        Init the stepper motor
        """
        for pin in self.pins:
            GPIO.setup(pin, GPIO.OUT)
            GPIO.output(pin, False)
    def step(self, number):
        """
        Micro-stepping the motor

        :param number: micro step to roll
        """
        number = number % 8
        GPIO.output(self.pins[0], MICRO_STEPS[number][0])
        GPIO.output(self.pins[1], MICRO_STEPS[number][1])
        GPIO.output(self.pins[2], MICRO_STEPS[number][2])
        GPIO.output(self.pins[3], MICRO_STEPS[number][3])
        sleep(TIME)
        GPIO.output(self.pins[0], False)
        GPIO.output(self.pins[1], False)
        GPIO.output(self.pins[2], False)
        GPIO.output(self.pins[3], False)
    def move(self, number):
        """
        Stepping the motor

        :param number: Steps to roll (took microsteps by microsteps)
        """
        for i in range(number*8):
            self.step(i)

class CounterWheel(object):
    """
    CounterWheel encapsulate a CounterWheel:
        - state (current number)
        - move functions (number to show)
    """
    def __init__(self, stepper):
        """
        Construct a new 'CounterWheel' object.

        :param stepper: the stepper motor to wrap
        :return: returns nothing
        """
        self.stepper = stepper
        self.current_number = 0
    def set_current_number(self, number):
        """
        Reseting the counter to number

        :param number: number to reset the counter to
        """
        self.current_number = number
    def number(self, number):
        """
        Moves the counter to show specified number

        :param number: number to show
        """
        steps_to_take = 0
	print('Current number '+str(self.current_number))
        if self.current_number == number:
            return None
        if self.current_number > number:
            number += 10
        for i in range(self.current_number, number):
            steps_to_take += STEPS[i % 10]
        self.stepper.move(steps_to_take)
        self.current_number = number % 10

class CounterWheelRunner(Thread):
    """ Thread moving one wheel """
    def __init__(self, counter, number):
        Thread.__init__(self)
        self.number = number
        self.counter = counter
    def run(self):
        self.counter.number(self.number)

class Counter(object):
    """
    CounterWheel encapsulate a full counter:
        - wheels composing the counter
    """
    def __init__(self, wheels):
        """
        Construct a new 'Counter' object.

        :param wheels: wheels composing the counter
        :return: returns nothing
        """
        self.wheels = wheels
    def number(self, number):
        """
        Moves the counter to specified number by moving each wheels
        """
        i = 0
        threads_to_wait = []
        while i < len(self.wheels):
            if MULTI_THREAD:
                sleep(0.25)
                runner = CounterWheelRunner(self.wheels[i], number%10)
                runner.start()
                threads_to_wait.append(runner)
            else:
                self.wheels[i].number(number%10)
            number = int(number / 10)
            i += 1
        for thread in threads_to_wait:
            thread.join()

s1 = Stepper([12, 16, 20, 21])
s1.init()
cw1 = CounterWheel(s1)

s2 = Stepper([26, 13, 19, 6])
s2.init()
cw2 = CounterWheel(s2)

s3 = Stepper([24, 25, 8, 7])
s3.init()
cw3 = CounterWheel(s3)

s4 = Stepper([10, 11, 9, 5])
s4.init()
cw4 = CounterWheel(s4)

s5 = Stepper([27, 22, 4, 17])
s5.init()
cw5 = CounterWheel(s5)

stepper_tab = [cw5, cw4, cw3, cw2, cw1]
#stepper_tab.reverse()
counter = Counter(stepper_tab)
#counter.number(44444)

ip = ""
try:
    import socket
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s.connect(("8.8.8.8", 80))
    ip = s.getsockname()[0]
    s.close()
except Exception:
    pass

while True:
    sleep(2)
    try:
        current_count = requests.get(URL+"?"+ip)
    except requests.exceptions.ConnectionError:
        print "Something wrong happened with the request to get count !"
    else:
        print "Get from network number " + current_count.text + "\n"
        counter.number(int(current_count.text))
#    counter.number(11111)
    #current_count += 147


GPIO.cleanup()

# import code
# code.InteractiveConsole(locals=globals()).interact()
