from functools import cache

@cache
def fib(x):
   if x == 1:
      return 1
   if x == 2:
      return 2
   return fib(x - 1) + fib(x - 2)

def to_fib(x):
   if x == 0:
      return '0'
   res, M = '1', 1
   while fib(M + 1) <= x:
      M += 1
   x -= fib(M)
   while M > 1:
      M -= 1
      if x >= fib(M):
         x -= fib(M)
         res += '1'
      else:
         res += '0'
   return res

def parse_fib(x):
   if '11' in x:
      raise Exception('Wrong fib number by definition.')
   res = 0
   for i in range(len(x), 0, -1):
      if x[len(x) - i] == '1':
         res += fib(i)
   return res

assert to_fib(16) == '100100'
assert parse_fib('100100') == 16