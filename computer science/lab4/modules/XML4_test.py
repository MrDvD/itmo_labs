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
      self.Char = ur'[\t\r\n\u0020-\uD7FF\uE000-\uFFFD\u0010000-\u0010FFFF]'
      self.S = r'[ \t\r\n]+'
      self.Eq = self.S + '?=' + self.S + '?'
      self.NameStartChar = ur'[:A-Z_a-z\u00C0-\u00D6\u00D8-\u00F6\u00F8-\u02FF\u0370-\u037D\u037F-\u1FFF\u200C-\u200D\u2070-\u218F\u2C00-\u2FEF\u3001-\uD7FF\uF900-\uFDCF\uFDF0-\uFFFD\u00010000-\u000EFFFF]'
      self.NameChar = r'[-.0-9\u00B7\u0300-\u036F\u203F-\u2040]'
      self.Name = self.NameStartChar + self.NameChar + '*'
      self.Reference = self.EntityRef + '|' + self.CharRef
      self.EntityRef = '&' + self.Name + ';'
      self.CharRef = '&#[0-9]+;|&#x[0-9a-fA-F]+;'
      self.Attribute = '(' + self.Name + ')' + self.Eq + '(' + self.AttValue + ')'
      self.AttValue = '"' + '(' + '[^<&"]|' + self.Reference + ')*' + '"'
      self.EmptyElemTag = re.compile('<' + self.Name + '(' + self.S + self.Attribute + ')*' + self.S + '?/>')
      self.STag = re.compile('<' + f'({self.Name})' + f'((?:{self.S + self.Attribute})*)' + self.S + '?>')
      self.CharData = '[^<&]*'
      self.CDStart = r'<!\[CDATA\['
      self.CData = '(' + self.Char + r'*?(?=]]>))'
      self.CDEnd = ']]>'
      self.CDSect = re.compile(self.CDStart + self.CData + self.CDEnd)
      self.Comment = re.compile(r'<!--' + '((?!-)' + self.Char + '|' + '-(?!-)' + self.Char + ')*' +  '-->')
      # temp_patterns
      self.iterativeAttribute = re.compile(self.S + self.Attribute)
      if autogen:
         self.autogenerate()
   
   def parseContent():

      pass

   def parse_opening_tag(self, idx=0):
      """
      Returns the tuple (name, dict_of_metatags, is_closed)
      """
      attrs, values = list(), list()
      # 
      tag_obj = self.STag.match(self._content, idx)
      if tag_obj.group(2) is not None:
         for attr in self.iterativeAttribute.finditer(tag_obj.group(2)):
            attrs.append(attr.group(1))
            values.append(attr.group(2))
      return (tag_obj.group(1), dict(zip(attrs, values)), self.EmptyElemTag.match(self._content, idx) is not None, tag_obj.end())

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
         # if the following is a tag
         while self.STag.match(self._content, idx):
            inner_name, obj, idx = self.parse_tag(idx)
            fields = self.add_tag_to_obj(fields, inner_name, obj)
         # if the following is the tag's value
         if fields == dict():
            value_obj = self.value_pattern.search(self._content, idx)
            fields = value_obj.group(1)
            idx = value_obj.end()
         else:
            idx = self.closing_tag_pattern.match(self._content, idx).end()
      return (name, fields, idx)