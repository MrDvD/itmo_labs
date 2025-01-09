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

def get_emojis_count(text, emoji_pattern):
    """
    Returns the count of an emoji pattern in the input text.
    """
    emoji_obj = re.compile(emoji_pattern)
    return len(emoji_obj.findall(text))

def decorate_result(count, test_path):
    """
    Decorates the input result for printing purposes.
    """
    return f'File at \'{test_path}\': {count} occurences.'

def main():
    """
    Main function.
    """
    for test_path in get_test_files('tests_1'):
        with open(test_path) as f:
            text = f.read()
            print(decorate_result(get_emojis_count(text, r':<\)'), test_path))

if __name__ == '__main__':
    main()