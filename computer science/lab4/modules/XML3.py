import re

from modules.Parser import *

class XML(Parser):
   def __init__(self, content=None, object=None, autogen=True):
      """
      A class which works with XML formatting.
      """
      super().__init__(content, object, False)
      self._emptyContent = '<emptyXML>'
      # patterns
      self.tag_pattern = re.compile(r'<([\w-]+)(?:(.*)(\/))?>')
      self.attr_pattern = re.compile(r'([\w-]+)=(\'|")(.*)\2')
      self.opening_tag_pattern = re.compile(r'<\w')
      self.closing_tag_pattern = re.compile(r'<\/[\w-]+>')
      self.value_pattern = re.compile(r'[ \n\r\t]*[^<&]+')
      self.screened_pattern = re.compile(r'&(\w+);')
      self.comment_pattern = re.compile(r'<!--.*?-->')
      self.S = re.compile(r'[ \r\n\t]+')
      self.screened = {'lt': '<', 'gt': '>', 'amp': '&', 'apos': '\'', 'quot': '\"'}
      if autogen:
         self.autogenerate()

   def parse_opening_tag(self, idx=0):
      """
      Returns the tuple (name, dict_of_metatags, is_closed)
      """
      attrs, values = list(), list()
      tag_obj = self.tag_pattern.search(self._content, idx)
      if tag_obj.group(2) is not None:
         for attr in self.attr_pattern.finditer(tag_obj.group(2)):
            attrs.append(attr.group(1))
            values.append(attr.group(3))
      return (tag_obj.group(1), dict(zip(attrs, values)), tag_obj.group(3) is not None, tag_obj.end())

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
   
   def add_string_to_obj(self, obj, string):
      if isinstance(obj, dict):
         if obj == dict() and not self.S.fullmatch(string):
            obj = string.lstrip()
         else:
            if '__text' in obj:
               obj['__text'] += string
            elif not self.S.fullmatch(string):
               obj['__text'] = string.lstrip()
      else:
         obj += string
      return obj
   
   def parse_screened(self, code):
      """
      Parses the screened version of some chars.
      """
      return self.screened[code]

   def parse_tag(self, idx=0):
      """
      Parses the content of XML tag entirely and returns the tuple (tag_name, python_obj, idx).
      """
      fields = dict()
      name, meta, is_closed, idx = self.parse_opening_tag(idx)
      for key in meta:
         fields['_' + key] = meta[key]
      if not is_closed:
         while not self.closing_tag_pattern.match(self._content, idx):
            # if the following is a tag
            if self.opening_tag_pattern.match(self._content, idx):
               if isinstance(fields, str):
                  fields = {'__text': fields}
               inner_name, obj, idx = self.parse_tag(idx)
               fields = self.add_tag_to_obj(fields, inner_name, obj)
            # if the following is a comment
            elif self.comment_pattern.match(self._content, idx):
               idx = self.comment_pattern.match(self._content, idx).end()
            # if the following is the tag's value
            elif self.value_pattern.match(self._content, idx):
               value_obj = self.value_pattern.search(self._content, idx)
               fields = self.add_string_to_obj(fields, value_obj.group(0))
               idx = value_obj.end()
            # if the following is a screened symbol
            elif self.screened_pattern.match(self._content, idx):
               screened_obj = self.screened_pattern.search(self._content, idx)
               fields = self.add_string_to_obj(fields, self.parse_screened(screened_obj.group(1)))
               idx = screened_obj.end()
            if idx >= len(self._content):
               break
         else:
            idx = self.closing_tag_pattern.match(self._content, idx).end()
      if isinstance(fields, dict) and '__text' in fields:
         fields['__text'] = fields['__text'].rstrip()
      elif isinstance(fields, str):
         fields = fields.rstrip()
      return (name, fields, idx)
   
   def parse_string(self):
      """
      Parses the content of input XML string into object.
      """
      name, obj, _ = self.parse_tag()
      self._object = {name: obj}