import re

from modules.Parser import *

def sequence(*funcs):
   """
   Yields tuple if all of funcs are matched sequentially.
   """
   if len(funcs) == 0:
      def result(idx=0):
         yield (), idx
      return result
   def result(idx=0):
      for arg1, idx in funcs[0](idx):
         for others, idx in sequence(*funcs[1:])(idx):
            yield (arg1,) + others, idx
   return result

def lor(*funcs):
   """
   Yields the first non-None match.
   """
   def result(idx=0):
      for func in funcs:
         for val, idx in func(idx):
            yield val, idx
            return
   return result

def question(func):
   """
   Yields the match once if possible.
   """
   def result(idx=0):
      for val, idx in func(idx):
         yield val, idx
         return
      yield None, idx
   return result

def asterisk(func):
   """
   Yields the list of func as much as possible.
   """
   def result(idx=0):
      for (val1, others), idx in sequence(func,
                                          asterisk(func))(idx):
         yield [val1] + others, idx
         return
      yield [], idx
   return result

class XML(Parser):
   class Pattern:
      Char = re.compile('[\t\r\n\u0020-\uD7FF\uE000-\uFFFD\U00010000-\U0010FFFF]')
      S = re.compile('[ \t\r\n]+')
      Eq = re.compile('[ \t\r\n]*=[ \t\r\n]*')
      NameStartChar = '[:A-Z_a-z\u00C0-\u00D6\u00D8-\u00F6\u00F8-\u02FF\u0370-\u037D\u037F-\u1FFF\u200C-\u200D\u2070-\u218F\u2C00-\u2FEF\u3001-\uD7FF\uF900-\uFDCF\uFDF0-\uFFFD\U00010000-\U000EFFFF]'
      NameChar = '(?:' + NameStartChar + '|[-.0-9\u00B7\u0300-\u036F\u203F-\u2040])'
      Name = re.compile(NameStartChar + NameChar + '*')
      AttValue = re.compile('\"([^<&\"]*)\"')
      CharData = re.compile('[^<&]+')
      EntityRef = {'lt': '<', 'gt': '>', 'amp': '&', 'apos': '\'', 'quot': '\"'}
   
   def __init__(self, content=None, object=None, autogen=True):
      """
      A class which works with XML formatting.
      """
      super().__init__(content, object, False)
      self._emptyContent = '<emptyXML>'
      if autogen:
         self.autogenerate()
   
   def minus(self, pattern, chars):
      """
      Substracts 'chars' from matched pattern symbol.
      """
      def result(idx=0):
         obj = pattern.match(self._content[idx:])
         if obj is not None:
            if obj.group(0) not in chars:
               yield obj.group(0), idx + obj.end()
      return result
   
   def parseCharData(self):
      def result(idx=0):
         obj = self.Pattern.CharData.match(self._content[idx:])
         if obj is not None:
            yield (obj.group(0), idx + obj.end())
      return result
   
   def parseWord(self, word, value=None):
      def result(idx=0):
         if self._content[idx:].startswith(word):
            yield value, idx + len(word)
      return result
   
   def parseS(self):
      def result(idx=0):
         obj = self.Pattern.S.match(self._content[idx:])
         if obj is not None:
            yield (None, idx + obj.end())
      return result
   
   def parseName(self):
      def result(idx=0):
         obj = self.Pattern.Name.match(self._content[idx:])
         if obj is not None:
            yield (obj.group(0), idx + obj.end())
      return result
   
   def parseEq(self):
      def result(idx=0):
         obj = self.Pattern.Eq.match(self._content[idx:])
         if obj is not None:
            yield (None, idx + obj.end())
      return result
   
   def parseAttValue(self):
      def result(idx=0):
         obj = self.Pattern.AttValue.match(self._content[idx:])
         if obj is not None:
            yield (obj.group(1), idx + obj.end())
      return result

   def parseAttribute(self):
      def result(idx=0):
         for (name, _, attValue), idx in sequence(self.parseName(),
                                                  self.parseEq(),
                                                  self.parseAttValue())(idx):
            yield {name: attValue}, idx
      return result

   def parseEmptyElemTag(self):
      def result(idx=0):
         for (_, name, keyvalues, _, _), idx in sequence(self.parseWord('<'),
                                                         self.parseName(),
                                                         asterisk(sequence(self.parseS(),
                                                                           self.parseAttribute())),
                                                         question(self.parseS()),
                                                         self.parseWord('/>'))(idx):
            tag = {name: dict()}
            for kv in keyvalues:
               if kv is not None:
                  _, kv = kv
                  for k in kv.keys():
                     tag[name]['_' + k] = kv[k]
            if not len(tag[name]):
               tag[name] = None
            yield tag, idx
      return result
   
   def parseSTag(self):
      def result(idx=0):
         for (_, name, keyvalues, _, _), idx in sequence(self.parseWord('<'),
                                                         self.parseName(),
                                                         asterisk(sequence(self.parseS(),
                                                                           self.parseAttribute())),
                                                         question(self.parseS()),
                                                         self.parseWord('>'))(idx):
            tag = {name: dict()}
            for kv in keyvalues:
               if kv is not None:
                  _, kv = kv
                  for k in kv.keys():
                     tag[name]['_' + k] = kv[k]
            yield tag, idx
      return result
   
   def parseETag(self):
      def result(idx=0):
         for (_, _, _, _), idx in sequence(self.parseWord('</'),
                                           self.parseName(),
                                           question(self.parseS()),
                                           self.parseWord('>'))(idx):
            yield None, idx
      return result
   
   def parseComment(self):
      def result(idx=0):
         for _, idx in sequence(self.parseWord('<!--'),
                                asterisk(lor(self.minus(self.Pattern.Char, '-'),
                                             sequence(self.parseWord('-', '-'),
                                                      self.minus(self.Pattern.Char, '-')))),
                                self.parseWord('-->'))(idx):
            yield None, idx
      return result

   def parseElemTag(self):
      def result(idx=0):
         for (fields, content, _), idx in sequence(self.parseSTag(),
                                                   self.parseContent(),
                                                   self.parseETag())(idx):
            key = next(iter(fields))
            if isinstance(content, str):
               if len(fields[key]) == 0:
                  fields[key] = content
               else:
                  fields[key]['__text'] = content
            else:
               for subkey in content.keys():
                  self.add_tag_to_obj(fields[key], subkey, content[subkey])
               if not len(content):
                  fields[key] = None
            yield fields, idx
      return result

   def parseElement(self):
      def result(idx=0):
         for value, idx in lor(self.parseEmptyElemTag(),
                               self.parseElemTag())(idx):
            yield value, idx
      return result
   
   def parseEntityRef(self):
      def result(idx=0):
         for (_, name, _), idx in sequence(self.parseWord('&'),
                                           self.parseName(),
                                           self.parseWord(';'))(idx):
            yield self.Pattern.EntityRef[name], idx
      return result

   def parseContent(self):
      def result(idx=0):
         fields = dict()
         for (value, other), idx in sequence(question(self.parseCharData()),
                                                asterisk(sequence(lor(self.parseElement(),
                                                                      self.parseEntityRef(),
                                                                      self.parseComment()),
                                                                  question(self.parseCharData()))))(idx):
            if value is not None and value.lstrip():
               fields = value
            for var, data in other:
               if var is not None:
                  if isinstance(var, str):
                     if isinstance(fields, dict):
                        fields = var
                     else:
                        fields += var
                  else:
                     if isinstance(fields, str):
                        fields = {'__text': fields}
                     for key in var:
                        self.add_tag_to_obj(fields, key, var[key])
               if data is not None and data.lstrip():
                  if isinstance(fields, dict):
                     fields['__text'] = fields.get('__text', '') + data
                  else:
                     fields += data
            yield fields, idx
      return result
   
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
   
   def parse_string(self):
      """
      Parses the content of input XML string into object.
      """
      for value, _ in self.parseElement()():
         self._object = value