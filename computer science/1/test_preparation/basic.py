def to_base(x, base, prec=8):
   if x <= 0:
      raise Exception('Input should be positive.')
   res = ''
   whole, frac = int(x), x - int(x)
   while whole > 0:
      res += str(whole % base)
      whole //= base
   if frac:
      res = res[::-1] + '.'
      for _ in range(prec):
         frac *= base
         res += str(int(frac))
         frac -= int(frac)
      return res
   return res[::-1]

def parse_base(x, base, prec=8):
   res = 0
   if '.' in x:
      left, right = x.split('.')
      for i in range(len(left) - 1, -1, -1):
         curr = int(left[len(left) - i - 1])
         if curr >= base:
            raise Exception('Wrong input number by definition.')
         res += curr * base ** i
      for i in range(len(right)):
         curr = int(right[i])
         if curr >= base:
            raise Exception('Wrong input number by definition.')
         res += curr * base ** (-i - 1)
   else:
      for i in range(len(x) - 1, -1, -1):
         curr = int(left[len(left) - i - 1])
         if curr >= base:
            raise Exception('Wrong input number by definition.')
         res += curr * base ** i
   return round(res, prec)

assert parse_base('11100111.1101', 2) == 231.8125
assert to_base(231.8125, 2, prec=4) == '11100111.1101'