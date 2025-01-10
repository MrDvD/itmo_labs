import math

Z = (1 + math.sqrt(5)) / 2

def parse_berg(x):
   if '11' in x:
      raise Exception('Possibly wrong input number by definition.')
   res = 0
   if '.' in x:
      left, right = x.split('.')
      for i in range(len(left) - 1, -1, -1):
         curr = int(left[len(left) - i - 1])
         if curr >= 2:
            raise Exception('Wrong input number by definition.')
         res += curr * Z ** i
      for i in range(len(right)):
         curr = int(right[i])
         if curr >= 2:
            raise Exception('Wrong input number by definition.')
         res += curr * Z ** (-i - 1)
   else:
      for i in range(len(x) - 1, -1, -1):
         curr = int(left[len(left) - i - 1])
         if curr >= 2:
            raise Exception('Wrong input number by definition.')
         res += curr * Z ** i
   return round(res, 8)

assert parse_berg('10000.0001') == 7
assert parse_berg('101000.100001') == 16
assert parse_berg('100.01') == 3

x = input('Type the Bergman number: ')
print('It\'s decimal equivalent is:', parse_berg(x))