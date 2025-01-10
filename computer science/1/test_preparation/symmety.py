# helper function to map 10> digits to their corresponding letters
def map_number(x):
   return str(x) if x < 10 else chr(ord('A') + x - 10)

def parse_number(x):
   if x[0] == '-':
      sign = -1
      x = x[1:]
   else:
      sign = 1
   return sign * (int(x) if x in '0123456789' else ord(x) - ord('A') + 10)

def format_sym(x):
   return str(x) if parse_number(x) >= 0 else f'{{^{x[1:]}}}'

def inverse_str_int(x):
   return x[1:] if x[0] == '-' else x if x == '0' else '-' + x

def to_base(x, base, reverse=False):
   if not x:
      return '0'
   res, sign, x = '', x < 0, abs(x)
   while x > 0:
      res += map_number(x % base)
      x //= base
   return res if reverse else res[::-1], sign

def to_sym(num, base):
   if not base % 2:
      raise Exception('Wrong base by definition!')
   x, sign = to_base(num, base, reverse=True)
   x, res = list(map(parse_number, list(x))), list()
   i, l = 0, len(x)
   while i < l:
      if x[i] > base // 2:
         if x[i] == base:
            res.append('0')
         else:
            res.append('-' + map_number(base - x[i]))
         if i + 1 == len(x):
            x.append(1)
            l += 1
         else:
            x[i + 1] += 1
      else:
         res.append(str(x[i]))
      i += 1
   if sign:
      res = list(map(inverse_str_int, res))
   return ''.join(map(format_sym, res[::-1]))

def tokenize(x):
   tokens, i = list(), 0
   while i < len(x):
      curr = ''
      if x[i] == '{':
         curr += '-'
         i += 2
         while x[i] != '}':
            curr += x[i]
            i += 1
      else:
         curr += x[i]
      tokens.append(curr)
      i += 1
   return tokens

def parse_sym(x, base):
   x, res = tokenize(x), 0
   for i in range(len(x) - 1, -1, -1):
      res += parse_number(x[len(x) - i - 1]) * base ** i
   return res

assert to_sym(1205, 5) == '20{^2}10'
assert to_sym(-1205, 5) == '{^2}02{^1}0'
assert parse_sym('20{^2}10', 5) == 1205
assert parse_sym('{^2}02{^1}0', 5) == -1205

# x = int(input('Type the origin number: '))
# base = int(input('Type the base: '))
# print('It\'s sym equivalent is:', to_sym(x, base))
# y = int(input('Type the sym number: '))
# base = int(input('Type the base: '))
# print('It\'s decimal equivalent is:', parse_sym(y, base))