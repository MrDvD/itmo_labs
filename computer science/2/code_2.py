import math

def areaSizeBit(hammingCodeSize):
    """
    Yields the size of informational bit's matching area.
    """
    i, r = 0, math.ceil(math.log2(hammingCodeSize))
    while i < r:
        yield 2 ** i
        i += 1

def detectError(hammingCode):
    """
    Returns the index of symbol which contains an error.
    Zero means 'no errors found'.
    """
    syndrom = ''
    for r in areaSizeBit(len(hammingCode)):
        currentBit = 0
        for i in range(r - 1, len(hammingCode), 2 * r):
            for j in range(r):
                currentBit ^= hammingCode[i + j]
        syndrom += str(currentBit)
    return int(syndrom[::-1], 2)

def matchSymbol(idx):
    """
    Matches the index with the mathematical notation of symbol.
    """
    r = math.log2(idx)
    if r == int(r):
        return f'r{1 + int(r)}'
    return f'i{idx - math.ceil(r)}'

def parseMessage(hammingCode):
    """
    Parses the informational bits from the Hamming code sequence.
    """
    message = ''
    for i in range(1, len(hammingCode) + 1):
        r = math.log2(i)
        if r != int(r):
            message += str(hammingCode[i - 1])
    return message

hammingCode = list(map(int, list(input('Type the Hamming code sequence:\n'))))
returnCode = detectError(hammingCode)
match returnCode:
    case 0:
        print('No errors found!')
    case _:
        keys = [0, 'r1', 'r2', 'i1', 'r3', 'i2', 'i3', 'i4']
        print(f'Error found at symbol {matchSymbol(returnCode)}!')
        hammingCode[returnCode - 1] ^= 1
print('The found message is:')
print(parseMessage(hammingCode))