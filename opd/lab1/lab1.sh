### 1.1. Creating tree structure
mkdir /home/studs/s466449/lab0 # making a root directory  
cd /home/studs/s466449/lab0

# ~/lab0
mkdir charmander4 crustle6 unfezant9
touch ferroseed6 pignite4 tentacruel4
# ~/lab0/charmander4
cd charmander4
mkdir sudowoodo fearow
touch snorlax nidoqueen togekiss mudkip
# ~/lab0/crustle6
cd ../crustle6
touch sandile blitzle
mkdir manectric hoppip
# ~/lab0/unfezant9
cd ../unfezant9
mkdir delcatty leafeon
touch serperior

### 1.2. Filling files with content
# ~/lab0/unfezant9
echo $'satk=8 sdef=10 spd=11' > serperior
# ~/lab0
cd ..
echo $'Способности  Supersonic Constrict Acid Toxic\nSpikes Bubblebeam Wrap Barrier Water Pulse Poison Jab Screech Hex\nHydro Pump Sludge Wave Wring Out' > tentacruel4
echo $'Возможности\nOverland=7 Surface=5 Jump=4 Power=5 Intelligence=4 Firestarter=0\nHeater=0' > pignite4
echo $'Возможности  Overland=4 Jump=1\nPower=2 Intelligence=2 Sprouter=0 Sinker=0' > ferroseed6
# ~/lab0/crustle6
cd crustle6
echo $'Тип\nпокемона  ELECTRIC NONE' > blitzle
echo $'weight=33.5 height=28.0 atk=7 def=4' > sandile
# ~/lab0/charmander4
cd ../charmander4
echo $'Развитые способности  Gluttony' > snorlax
echo $'Возможности\nOverland=7 Surface=2 Jump=2 Power=4 Intelligence=4 Groundshaper=0 Pack\nMon=0' > nidoqueen
echo $'Живет  Forest Rainforest' > togekiss
echo $'Тип диеты\nHerbivore' > mudkip

### 2. Setting permissions
# ~/lab0/charmander4
chmod a=rwx,g-r sudowoodo
chmod 753 fearow
chmod 006 snorlax
chmod 620 nidoqueen
chmod 062 togekiss
chmod 600 mudkip
# ~/lab0
cd ..
chmod ugo=x,g+w,o+r charmander4
chmod a=rwx crustle6
chmod 624 ferroseed6
chmod u=rw,g=w,o= pignite4
chmod 400 tentacruel4
chmod 537 unfezant9
# ~/lab0/crustle6
cd crustle6
chmod 006 sandile
chmod 307 manectric
chmod 046 blitzle
chmod 550 hoppip
# ~/lab0/unfezant9
cd ../unfezant9
chmod 751 delcatty
chmod 335 leafeon
chmod 062 serperior

### 3. Creating links
# ~/lab0
cd ..
ln pignite4 unfezant9/serperiorpignite
cp -r charmander4 unfezant9/leafeon
ln -s charmander4 Copy_64 # FIX: relative path
ln -s ../ferroseed6 charmander4/nidoqueenferroseed # FIX: relative path
cp ferroseed6 crustle6/sandileferroseed # FIX: cat -> cp
cp pignite4 charmander4/sudowoodo
cat unfezant9/serperior unfezant9/serperior > ferroseed6_65

### 4. Working with files' content
# ~/lab0
wc -m charmander4/snorlax charmander4/nidoqueen charmander4/togekiss charmander4/mudkip crustle6/sandile crustle6/blitzle 2>/tmp/std.err | sort
ls -latpR unfezant9 2>&1 | grep -E "^-|:$|^$" # FIX: using -R option
grep "Не" ferroseed6 2>/tmp/std.err
ls -paR crustle6 | grep -v "/$" # FIX: using -R option
cat -n charmander4/snorlax charmander4/nidoqueen charmander4/togekiss | sort -k 2 -r
ls -latuR 2>&1 | grep "^-" | head -n 2 # FIX: using -R option

### 5. Removing files
# ~/lab0
rm pignite4
rm -f crustle6/sandile
rm Copy_*
rm unfezant9/serperiorpigni*
rmdir unfezant9
rmdir unfezant9/leafeon
