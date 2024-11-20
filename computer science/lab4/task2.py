import xmltodict as XML
import json as JSON

def main():
   """
   Main function.
   """
   with open('tests/test2.xml') as f:
      with open('schedule.out', 'w') as g:
         xml = XML.parse(f.read())
         JSON.dump(xml, g, ensure_ascii=False, indent=3)

if __name__ == '__main__':
   main()