class Parser:
   def __init__(self, content=None, object=None, autogen=True):
      """
      An abstract parser class for markup languages.
      """
      self._content, self._object = content, object._object if isinstance(object, Parser) else object
      self._emptyContent = '<emptyContent>'
      self._tabSymbol = ' '
      self._tabCount = 3
      if autogen:
         self.autogenerate()

   def __str__(self):
      """
      Returns the string equivalent of the input Python dictionary.
      """
      return self._emptyContent if self._content is None else self._content
   
   def autogenerate(self):
      """
      Generates the omitting object/string of a parsed file.
      """
      if self._content is None:
         self.stringify_object()
      else:
         self.parse_string()