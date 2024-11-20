from modules.XML3 import *
import json as JSON

def main():
   """
   Main function.
   """
   with open('tests/schedule.xml') as f:
      with open('schedule.out', 'w') as g:
         xml = XML(content=f.read())
         JSON.dump(xml._object, g, ensure_ascii=False, indent=3)

if __name__ == '__main__':
   main()