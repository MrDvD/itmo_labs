from factorial import to_factorial
import math

def get_permutation(init_set, k):
   if k > math.factorial(len(init_set)):
      raise Exception('Not enough elements for input permutation number.')
   k = to_factorial(k)
   k = '0' * (len(init_set) - 1 - len(k)) + k
   res = [''] * len(init_set)
   for i in range(len(k)):
      count = 0
      for j in range(len(init_set) - 1, -1, -1):
         if res[j] == '':
            if count == int(k[i]):
               res[j] = str(init_set[len(k) - i])
               break
            count += 1
   for i in range(len(init_set)):
      if res[i] == '':
         res[i] = str(init_set[0])
   return ' '.join(res)

assert get_permutation([1, 2, 3, 4, 5], 93) == '4 5 2 3 1'
assert get_permutation([1, 2, 3, 4, 5], 3) == '2 3 1 4 5'
assert get_permutation([1, 2, 3, 4, 5], 119) == '5 4 3 2 1'
assert get_permutation([1, 2, 3, 4, 5], 0) == '1 2 3 4 5'

# init_set = input('Type the initial set (0-permutations) with spaces: ').split()
# k = int(input('Type the wanted permutation\'s number: '))
# print('Result is:', get_permutation(init_set, k))