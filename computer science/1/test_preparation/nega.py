import math

# helper function to get the positive remainder of division x % m
def mod(x, m):
   return x % m if x % m >= 0 else x % m - m

def div(x, y):
   return math.ceil(x / y)

# helper function to map 10> digits to their corresponding letters
def map_number(x):
   return str(x) if x < 10 else chr(ord('A') + x - 10)

def parse_number(x):
   return int(x) if x in '0123456789' else ord(x) - ord('A') + 10

# Base-to-Nega
def to_nega(x, base):
   x = int(x, base)
   if not x:
      return '0'
   result = ''
   while abs(x) > 1:
      result += map_number(mod(x, -base))
      x = div(x, -base)
   if x == 1:
      result += '1'
   return result[::-1]

# Nega-to-Base
def parse_nega(x, base):
   result = 0
   for i in range(len(str(x)) - 1, -1, -1):
      if parse_number(x[len(x) - i - 1]) >= base:
         raise Exception('Wrong input number by definition.')
      result += parse_number(x[len(x) - i - 1]) * (-base) ** i
   return result

assert to_nega('1937', 10) == '18077'
assert parse_nega('18077', 10) == 1937

# x = int(input('Type the base number: '))
# print('It\'s nega equivalent is:', to_nega(x))
# y = int(input('Type the nega number: '))
# print('It\'s base equivalent is:', parse_nega(y))