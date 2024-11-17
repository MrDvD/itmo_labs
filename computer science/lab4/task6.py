from modules.XML1 import *
from modules.TOML import *

def main():
   """
   Main function.
   """
   with open('schedule_test copy.xml') as f:
      with open('schedule.out', 'w') as g:
         xml = XML(content=f.read())
         toml = TOML(object=xml)
         g.write(str(toml))

if __name__ == '__main__':
   main()