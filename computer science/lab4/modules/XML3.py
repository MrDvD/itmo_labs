from modules.Parser import *
import re

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
      self.opening_tag_pattern = re.compile(r'\s*<\w')
      self.closing_tag_pattern = re.compile(r'\s*<\/[\w-]+>')
      self.value_pattern = re.compile(r'\s*(?:(.*)(?=<\/[\w-]+>)|(?(1)|(.+)))')
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
      if key in obj:
         if isinstance(obj[key], list):
            obj[key].append(value)
         else:
            obj[key] = [obj[key], value]
      else:
         obj[key] = value
      return obj

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
            # if the following is the tag's value
            elif self.value_pattern.match(self._content, idx):
               value_obj = self.value_pattern.search(self._content, idx)
               value = value_obj.group(1) if value_obj.group(1) else value_obj.group(2)
               if isinstance(fields, dict):
                  if fields == dict():
                     fields = value
                     idx = value_obj.end()
                  else:
                     if '__text' in fields:
                        fields['__text'] += '\\n' + value
                     else:
                        fields['__text'] = value
               else:
                  fields += value
               idx = value_obj.end()
            if idx >= len(self._content):
               break
         else:
            idx = self.closing_tag_pattern.match(self._content, idx).end()
      return (name, fields, idx)
   
   def parse_string(self):
      """
      Parses the content of input XML string into object.
      """
      name, obj, _ = self.parse_tag()
      self._object = {name: obj}