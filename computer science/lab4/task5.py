import time

from modules.XML1 import XML as XML1
import xmltodict as XML2
from modules.XML3 import XML as XML3
from modules.XML4 import XML as XML4
from modules.JSON import *
import json as JSON2

def xml1(content):
   xml = XML1(content=content)
   json = JSON(object=xml)

def xml2(content):
   xml = XML2.parse(content)
   JSON2.dumps(xml, ensure_ascii=False, indent=3)

def xml3(content):
   xml = XML3(content=content)
   JSON2.dumps(xml._object, ensure_ascii=False, indent=3)

def xml4(content):
   xml = XML4(content=content)
   JSON2.dumps(xml._object, ensure_ascii=False, indent=3)

def measure_ptime(func, *args, COUNT=100):
   """
   Measures the process time of COUNT executions of an input funcion.
   """
   start = time.process_time()
   for _ in range(COUNT):
      func(*args)
   stop = time.process_time()
   return f'{stop - start:.4f}'

def main():
   """
   Main function.
   """
   with open('tests/schedule.xml') as f:
      content = f.read()
   print('task1.py:', measure_ptime(xml1, content))
   print('task2.py:', measure_ptime(xml2, content))
   print('task3.py:', measure_ptime(xml3, content))
   print('task4.py:', measure_ptime(xml4, content))

if __name__ == '__main__':
   main()