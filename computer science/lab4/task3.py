from modules.XML3 import *
from modules.JSON3 import *

def main():
   """
   Main function.
   """
   with open('schedule.xml') as f:
      with open('schedule.out', 'w') as g:
         xml = XML(content=f.read())
         json = JSON(object=xml)
         g.write(str(json))

if __name__ == '__main__':
   main()