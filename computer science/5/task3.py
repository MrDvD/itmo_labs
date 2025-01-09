import matplotlib.pyplot as plt
import csv

september, october, november, december = [[], [], [], []], [[], [], [], []], [[], [], [], []], [[], [], [], []]

with open('task2.csv') as f:
   reader = csv.reader(f, delimiter=',')
   for row in reader:
      if '09' in row[2]:
         september
         september[0].append(int(row[4]))
         september[1].append(int(row[5]))
         september[2].append(int(row[6]))
         september[3].append(int(row[7]))
      elif '10' in row[2]:
         october[0].append(int(row[4]))
         october[1].append(int(row[5]))
         october[2].append(int(row[6]))
         october[3].append(int(row[7]))
      elif '11' in row[2]:
         november[0].append(int(row[4]))
         november[1].append(int(row[5]))
         november[2].append(int(row[6]))
         november[3].append(int(row[7]))
      elif '12' in row[2]:
         december[0].append(int(row[4]))
         december[1].append(int(row[5]))
         december[2].append(int(row[6]))
         december[3].append(int(row[7]))

fig, (sept, octo, nove, dece) = plt.subplots(1, 4, sharey=True)
sept.grid(axis='y')
octo.grid(axis='y')
nove.grid(axis='y')
dece.grid(axis='y')

labs = ["<OPEN>", "<HIGH>", '<LOW>', '<CLOSE>']
colors = ['skyblue', 'tomato', 'peachpuff', 'orange']

s = sept.boxplot(september, labels=labs, patch_artist=True)
sept.title.set_text('25.09.18')
o = octo.boxplot(october, labels=labs, patch_artist=True)
octo.title.set_text('23.10.18')
n = nove.boxplot(november, labels=labs, patch_artist=True)
nove.title.set_text('23.11.18')
d = dece.boxplot(december, labels=labs, patch_artist=True)
dece.title.set_text('03.12.18')
lst = [s, o, n, d]
for i in lst:
   for patch, color in zip(i['boxes'], colors):
      patch.set_facecolor(color)

plt.show()