from modules.Parser import *
import re

class XML(Parser):
   def __init__(self, content=None, object=None, autogen=True):
      """
      A class which works with XML formatting.
      """
      super().__init__(content, object, False)
      self._emptyContent = '<emptyXML>'
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
               tags.append(current)
               if self._content[idx - 1] == '/':
                  is_closed = 1
               break
            case _: # fill current keyword
               current += self._content[idx]
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
      while self._content[idx] != '>':
         idx += 1
      return idx + 1

   def scan_until_symbol(self, idx=0):
      """
      Scans the content until the non-space symbol is encountered.
      """
      while self._content[idx] in ' \n\t':
         idx += 1
      return idx

   def parse_tag(self, idx=0):
      """
      Parses the content of XML tag entirely and returns the tuple (tag_name, python_obj, idx).
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
               value += self._content[idx]
               idx += 1
            fields = value
         idx = self.parse_closing_key(idx)
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

   def is_key(self, opening=False, idx=0):
      """
      Checks if the following symbol is tag (opening/closing).
      """
      return self._content[idx] == '<' and self._content[idx + 1] != '/' if opening else self._content[idx:idx+2] == '</'
   
   def parse_string(self):
      """
      Parses the content of input XML string into object.
      """
      self._object = self.parse_tag()[1]