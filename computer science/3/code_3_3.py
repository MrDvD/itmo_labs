import re, os

def get_test_files(child_dir):
    """
    Returns sorted paths to test files from children directory.
    """
    test_files = list()
    for file in sorted(os.listdir(child_dir)):
        relative_path = os.path.join(child_dir, file)
        if (os.path.isfile(relative_path)):
            test_files.append(relative_path)
    return test_files

def get_words_list(text):
    """
    Returns the sorted list of found words with exactly one vowel in the input text.
    """
    words_obj = re.compile(r'\b(?:(?:[б-джзй-нп-тф-ъь])*([аеёиоуыэюя])\1*)?(?:(?:[б-джзй-нп-тф-ъь\-])*(?:\1)+)*(?(1)[б-джзй-нп-тф-ъь]*)\b', re.I)
    nonempty_obj = re.compile(r'\b\w+\b')
    word_list = list()
    for match in words_obj.finditer(text):
        if nonempty_obj.match(match.group(0)):
            word_list.append(match.group(0))
    return sorted(word_list, key=lambda x: (len(x), x.lower()))

def decorate_result(arr, test_path):
    """
    Decorates the input list for printing purposes.
    """
    return f'File at \'{test_path}\':\n' + '\n'.join(arr)

def main():
    """
    Main function.
    """
    for test_path in get_test_files('tests_3'):
        with open(test_path) as f:
            text = f.read()
            print(decorate_result(get_words_list(text), test_path))

if __name__ == '__main__':
    main()