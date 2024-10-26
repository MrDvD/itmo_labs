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

def get_surnames_list(text):
    """
    Returns the sorted list of found surnames in the input text.
    """
    surnames_obj = re.compile(r'([A-ZА-ЯЁ]\w+(?:|-[A-ZА-ЯЁ]\w+)) (?:[A-ZА-ЯЁ]\.)+(?:(?:(?:-[A-ZА-ЯЁ]\.)*(?:[A-ZА-ЯЁ]\.)*))*')
    return sorted(surnames_obj.findall(text))

def decorate_result(arr, test_path):
    """
    Decorates the input list for printing purposes.
    """
    return f'File at \'{test_path}\':\n' + '\n'.join(arr)

def main():
    """
    Main function.
    """
    for test_path in get_test_files('tests_2'):
        with open(test_path) as f:
            text = f.read()
            print(decorate_result(get_surnames_list(text), test_path))

if __name__ == '__main__':
    main()