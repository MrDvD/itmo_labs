class XML:
   def __init__(self, content):
      """
      A class which works with XML formatting.
      """
      self.content = content

   def parse_opening_tag(self, idx=0): # doesnt work well with: <NoEscapeNeeded name='Pete "Maverick" Mitchell'/>
      """
      Returns the tuple (name, dict_of_metatags, is_closed)
      """
      tags, values, current, is_closed = list(), list(), '', 0
      idx += 1
      while True:
         match self.content[idx]:
            case '=': # assigning value to a metatag 
               tags.append(current) ; current = ''
               values.append(self.parse_quotes(self.content, idx + 1))
               idx += 1
            case ' ': # splitting metatags
               tags.append(current) ; current = ''
            case '>': # closing tag
               tags.append(current)
               if self.content[idx - 1] == '/':
                  is_closed = 1
               break
            case _: # fill current keyword
               current += self.content[idx]
         idx += 1
      return (tags[0], dict(zip(tags[1:], values)), is_closed, idx + 1)

   def add_tag_to_obj(self, obj, key, value):
      """
      Adds a key-value pair to an object in an XML way.
      """
      if key in obj:
         if isinstance(obj[key], list):
            obj[key].append(value)
         else:
            obj[key] = [obj[key], value]
      else:
         obj[key] = value
      return obj

   def parse_closing_key(self, idx=0):
      """
      Returns the index of the next symbol just after the end of closing key.
      """
      while self.content[idx] != '>':
         idx += 1
      return idx + 1

   def scan_until_symbol(self, idx=0):
      """
      Scans the content until the non-space symbol is encountered.
      """
      while self.content[idx] in ' \n\t':
         idx += 1
      return idx

   def parse_tag(self, idx=0):
      """
      Parses tag entirely and returns the tuple (name, python_obj, idx).
      """
      fields = dict()
      idx = self.scan_until_symbol(idx)
      name, meta, is_closed, idx = self.parse_opening_tag(idx)
      for key in meta:
         fields['_' + key] = meta[key]
      if not is_closed:
         idx = self.scan_until_symbol(idx)
         while self.is_key(True, idx):   
            inner_name, obj, idx = self.parse_tag(idx)
            fields = self.add_tag_to_obj(fields, inner_name, obj)
            # fields[inner_name] = obj
            idx = self.scan_until_symbol(idx)
         if fields == dict():
            value = ''
            while not self.is_key(False, idx):
               value += self.content[idx]
               idx += 1
            fields = value
         idx = self.parse_closing_key(idx)
      return (name, fields, idx)

   def parse_quotes(self, idx=0): # can quotes be escaped?
      """
      Reads the content of quoted string 'as is'.
      """
      result, quote = '', self.content[idx]
      idx += 1
      while self.content[idx] != quote:
         result += self.content[idx]
         idx += 1
      return result

   def is_key(self, opening=False, idx=0):
      """
      Checks if the following symbol is tag (opening/closing).
      """
      return self.content[idx] == '<' and self.content[idx + 1] != '/' if opening else self.content[idx:idx+2] == '</'

   def parse_xml(self):
      """
      Returns the Python equivalent dictionary of an input xml file.
      """
      idx = 0
      while idx < len(self.content):
         if self.is_key(self.content, True, idx):
            name, fields, idx = self.parse_tag(self.content, idx)
         idx += 1
      return ''

def to_json(obj):
   """
   Returns the JSON-formatted equivalent of the input Python dictionary.
   """
   pass

# def main():
#    """
#    Main function.
#    """
#    with open('schedule.xml') as f:
#       with open('schedule.out') as g:
#          g.write(to_json(parse_xml(f.read())))

if __name__ == '__main__':
   with open('schedule_test.xml') as f:
      xml = XML(f.read())
      print(xml.parse_tag())
   # main()