def to_nine_inv(x):
    res, x = list(), int(x)
    if not x:
      return [0]
    while x > 0:
        res.append(x % 9)
        x //= 9
    return res

def to_nine_sym(num):
    x, res = to_nine_inv(num), list()
    i, l = 0, len(x)
    while i < l:
        if x[i] > 4:
            if x[i] == 9:
                res.append('0')
            else:
                res.append("{" + f'^{9 - x[i]}' + "}")
            if i + 1 == len(x):
                x.append(1)
                l += 1
            else:
                x[i + 1] += 1
        else:
            res.append(str(x[i]))
        i += 1
    return ''.join(res[::-1])

print(to_nine_sym(input('Введите десятичное число для перевода в 9С: '))) 
