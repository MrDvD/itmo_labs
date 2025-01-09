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
   
   def is_dict_primitive(self, obj):
      """
      Check if the dict has inner dicts.
      """
      for key in obj.keys():
         if isinstance(obj[key], list):
            return self.is_list_primitive(obj[key])
         return not isinstance(obj[key], dict)
   
   def is_list_primitive(self, obj):
      """
      Checks if the list has inner dicts.
      """
      for key in obj:
         return not isinstance(key, (list, dict))
   
   def generate_tree(self, object, parent=''):
      result = list()
      if isinstance(object, dict):
         stash_items = list()
         object_items = list()
         for key in object.keys():
            if isinstance(object[key], list) and self.is_list_primitive(object[key]):
               stash_items.append((key, object[key]))
            elif isinstance(object[key], (list, dict)):
               object_items += self.generate_tree(object[key], parent + '.' + key if parent else key)
            else:
               stash_items.append((key, object[key]))
         if stash_items:
            result.append((parent, stash_items))
         if object_items:
            result += object_items
      elif isinstance(object, list):
         if self.is_list_primitive(object):
            result.append((parent, object))
         else:
            for item in object:
               result += self.generate_tree(item, parent)
      return result
   
   def get_array_tags(self, tree):
      """
      Returns the array dict tags.
      """
      visited, array = set(), set()
      for item in tree:
         if item[0] in visited:
            array.add(item[0])
         else:
            visited.add(item[0])
      return array
   
   def parse_structure(self, object):
      """
      Parses the tag structure and returns its string equivalent.
      """
      string_result = ''
      tree = self.generate_tree(object)
      array_tags = self.get_array_tags(tree)
      for tag, value in tree:
         if tag in array_tags:
            string_result += f'\n[[{tag}]]\n'
         else:
            string_result += f'\n[{tag}]\n'
         if isinstance(value, list):
            for name, info in value:
               string_result += f'{name} = {self.format_primitives(info)}\n'
         else:
            string_result += f'{tag[0]} = {self.format_primitives(tag[1])}\n'
      return string_result

   def stringify_object(self):
      """
      Fills the str content of TOML object from given object.
      """
      self._content = self.parse_structure(self._object).strip()