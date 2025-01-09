import math

# helper function to map 10> digits to their corresponding letters
def map_number(x):
   return str(x) if x < 10 else chr(ord('A') + x - 10)

# Decimal-to-Factorial
def to_factorial(x):
   if not x:
      return '0'
   result, curr_div = '', 2
   while x > 0:
      result += map_number(x % curr_div)
      x //= curr_div
      curr_div += 1
   return result[::-1]

# Factorial-to-Decimal
def parse_factorial(x):
   result = 0
   for i in range(len(str(x)), 0, -1):
      if (x % 10 ** i) // 10 ** (i - 1) > i:
         raise Exception('Wrong input number by definition.')
      result += (x % 10 ** i) // 10 ** (i - 1) * math.factorial(i)
   return result

assert to_factorial(20) == '310'
assert to_factorial(106) == '4120'
assert parse_factorial(4120) == 106
assert parse_factorial(310) == 20

# x = int(input('Type the decimal number: '))
# print('It\'s factorial equivalent is:', to_factorial(x))
# y = int(input('Type the factorial number: '))
# print('It\'s decimal equivalent is:', parse_factorial(y))