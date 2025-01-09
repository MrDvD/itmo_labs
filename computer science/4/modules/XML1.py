from modules.Parser import *

class XML(Parser):
   def __init__(self, content=None, object=None, autogen=True):
      """
      A class which works with XML formatting.
      """
      super().__init__(content, object, False)
      self._emptyContent = '<emptyXML>'
      self.S = ' \n\r\t'
      self.screened = {'lt': '<', 'gt': '>', 'amp': '&', 'apos': '\'', 'quot': '\"'}
      if autogen:
         self.autogenerate()

   def parse_opening_tag(self, idx=0):
      """
      Returns the tuple (name, dict_of_metatags, is_closed)
      """
      tags, values, current, is_closed = list(), list(), '', 0
      idx += 1
      while True:
         match self._content[idx]:
            case '=': # assigning value to a metatag
               tags.append(current) ; current = ''
               values.append(self.parse_quotes(idx + 1))
               idx += 1
            case ' ': # splitting metatags
               tags.append(current) ; current = ''
            case '>': # closing tag
               if self._content[idx - 1] == '/':
                  tags.append(current[:-1])
                  is_closed = 1
               else:
                  tags.append(current)
               break
            case _: # fill current keyword
               current += self._content[idx]
         idx += 1
      return (tags[0], dict(zip(tags[1:], values)), is_closed, idx + 1)

   def add_tag_to_obj(self, obj, key, value):
      """
      Adds a key-value pair to an object in an XML way.
      """
      if isinstance(value, (dict, list)) and not len(value):
         value = None
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
      while self._content[idx] != '>':
         idx += 1
      return idx + 1

   def scan_until_symbol(self, idx=0):
      """
      Scans the content until the non-space symbol is encountered.
      """
      while self._content[idx] in ' \r\n\t':
         idx += 1
      return idx
   
   def parse_screened(self, idx=0):
      """
      Parses the screened version of some chars.
      """
      code = ''
      while self._content[idx] != ';':
         code += self._content[idx]
         idx += 1
      return self.screened[code[1:]], idx
   
   def parse_char(self, idx=0):
      """
      Parses the following char.
      """
      if self._content[idx] == '&':
         return self.parse_screened(idx)
      return self._content[idx], idx

   def parse_tag(self, idx=0):
      """
      Parses the content of XML tag entirely and returns the tuple (tag_name, python_obj, idx).
      """
      fields = dict()
      while not self.is_key(True, idx):
         idx = self.scan_until_symbol(idx)
         if self.is_comment(idx):
            idx = self.parse_comment(idx)
      name, meta, is_closed, idx = self.parse_opening_tag(idx)
      for key in meta:
         fields['_' + key] = meta[key]
      if not is_closed:
         idx = self.scan_until_symbol(idx)
         while not self.is_key(False, idx):
            if self.is_key(True, idx): # if the following is an opening tag
               if isinstance(fields, str):
                  fields = {'__text': fields}
               inner_name, obj, idx = self.parse_tag(idx)
               fields = self.add_tag_to_obj(fields, inner_name, obj)
            else: # if the following is the tag's value (or a comment)
               if self.is_comment(idx):
                  idx = self.parse_comment(idx)
                  continue
               char, idx = self.parse_char(idx)
               if isinstance(fields, dict):
                  if fields == dict() and char not in self.S:
                     fields = char
                  else:
                     if '__text' in fields:
                        fields['__text'] += char
                     elif self._content[idx] not in self.S:
                        fields['__text'] = char
               else:
                  fields += char
               idx += 1
         idx = self.parse_closing_key(idx)
      if isinstance(fields, dict) and '__text' in fields:
         fields['__text'] = repr(fields['__text'].rstrip())[1:-1]
      elif isinstance(fields, str):
         fields = repr(fields.rstrip())[1:-1]
      return (name, fields, idx)

   def parse_quotes(self, idx=0):
      """
      Reads the content of quoted string 'as is'.
      """
      result, quote = '', self._content[idx]
      idx += 1
      while self._content[idx] != quote:
         result += self._content[idx]
         idx += 1
      return result
   
   def is_comment(self, idx=0):
      """
      Checks if the string is a comment.
      """
      return self._content[idx:idx+2] == '<!'
   
   def parse_comment(self, idx=0):
      """
      Omits the whole comment.
      """
      while self._content[idx-2:idx+1] != '-->':
         idx += 1
      return idx + 1

   def is_key(self, opening=False, idx=0):
      """
      Checks if the following symbol is tag (opening/closing).
      """
      if self._content[idx:idx+2] == '<!':
         return False
      return self._content[idx] == '<' and self._content[idx + 1] != '/' if opening else self._content[idx:idx+2] == '</'
   
   def parse_string(self):
      """
      Parses the content of input XML string into object.
      """
      name, obj, _ = self.parse_tag()
      self._object = {name: obj}