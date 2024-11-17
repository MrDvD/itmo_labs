from modules.Parser import *

class TOML(Parser):
   def __init__(self, content=None, object=None, autogen=True):
      """
      A class which works with TOML formatting.
      """
      super().__init__(content, object, False)
      self._emptyContent = '<emptyTOML>'
      if autogen:
         self.autogenerate()

   def format_primitives(self, item):
      """
      Formats primitives in a TOML-like manner.
      """
      if not isinstance(item, str):
         return item
      return '"' + item.replace("\"", "\\\"") + '"'
   
   # def add_indentations(self, depth):
   #    return '\n' + self._tabSymbol * self._tabCount * depth

   def has_inner_dict(self, obj):
      """
      Checks if the dict has inner dicts.
      """
      for key in obj:
         return isinstance(obj[key], dict)
   
   def is_list_primitive(self, obj):
      """
      Checks if the list has inner dicts.
      """
      for key in obj:
         return not isinstance(key, (list, dict))
   
   def gen_tree(self, object, prev_key='', parent=''):
      """
      Generates tree from an object.
      """
      result = list()
      if isinstance(object, dict):
         for key in object.keys():
            result.append(self.gen_tree(object[key], key, parent + '.' + prev_key if parent else prev_key))
      elif isinstance(object, list):
         if self.is_list_primitive(object):
            return (parent, (prev_key, object))
         else:
            for item in object:
               result.append(self.gen_tree(item, prev_key, parent))
      else:
         return (parent, (prev_key, object))
      return result if len(result) > 1 else result[0]
   
   def parse_structure(self, tree):
      """
      Parses the tree and returns its string equivalent.
      """
      string_result, flag = '', False
      for item in tree:
         if isinstance(item, tuple):
            if not flag:
               string_result += f'\n[[{item[0]}]]\n'
               flag = True
            string_result += f'{item[1][0]} = {self.format_primitives(item[1][1])}\n'
         else:
            string_result += self.parse_structure(item)
      return string_result

   def stringify_object(self):
      """
      Fills the str content of TOML object from given object.
      """
      self._content = self.parse_structure(self.gen_tree(self._object)).strip()