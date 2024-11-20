from modules.Parser import *

class JSON(Parser):
   def __init__(self, content=None, object=None, autogen=True):
      """
      A class which works with JSON formatting.
      """
      super().__init__(content, object, False)
      self._emptyContent = '<emptyJSON>'
      if autogen:
         self.autogenerate()

   def format_primitives(self, item):
      """
      Formats primitives in a JSON-like manner.
      """
      if item is None:
         return 'null'
      if not isinstance(item, str):
         return item
      return '"' + item.replace("\"", "\\\"") + '"'
   
   def add_indentations(self, depth):
      return '\n' + self._tabSymbol * self._tabCount * depth

   def parse_structure(self, object, depth=0):
      """
      Parses the tag structure and returns its string equivalent.
      """
      if not isinstance(object, (dict, list)):
         return self.format_primitives(object)
      else:
         if isinstance(object, dict):
            string_result, keys = '{' + self.add_indentations(depth + 1), list(object.keys())
            N = len(keys)
            for i in range(N):
               string_result += self.format_primitives(str(keys[i]))
               string_result += ': '
               string_result += self.parse_structure(object[keys[i]], depth + 1)
               if i != N - 1:
                  string_result += ',' + self.add_indentations(depth + 1)
            string_result += self.add_indentations(depth) + '}'
         else:
            string_result, N = '[' + self.add_indentations(depth + 1), len(object)
            for i in range(N):
               string_result += self.parse_structure(object[i], depth + 1)
               if i !=  N - 1:
                  string_result += ',' + self.add_indentations(depth + 1)
            string_result += self.add_indentations(depth) + ']'
      return string_result

   def stringify_object(self):
      """
      Fills the str content of JSON object from given object.
      """
      self._content = self.parse_structure(self._object)