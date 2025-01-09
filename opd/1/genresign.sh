# parsing args
while getopts "hn:s:p:a:g:f:t:e:" arg; do
  case $arg in
    h)
      echo $'Usage: genresign [OPTION]...'
      echo $'\t-h\t\t for help'
      echo $'\t-n=NAME\t\t for name input'
      echo $'\t-s=SURNAME\t for surname input'
      echo $'\t-p=NAME\t\t for patronymic input (optional)'
      echo $'\t-g=GROUP\t for group number input'
      echo $'\t-f=FACULTY\t for faculty name input'
      echo $'\t-t=PHONE\t for phone number input'
      echo $'\t-f=FACULTY\t for faculty name input (part. required)'
      echo $'\t-e=EMAIL\t for email address input (part. required)'
      exit 0
      ;;
    n)
      NAME=$OPTARG
      ;;
    s)
      SURNAME=$OPTARG
      ;;
    p)
      PATRONYMIC=$OPTARG
      ;;
    g)
      GROUP=$OPTARG
      ;;
    f)
      FACULTY=$OPTARG
      ;;
    t)
      PHONE=$OPTARG
      ;;
    e)
      EMAIL=$OPTARG
      ;;
  esac
done

# validating input info
test -z "$(grep -iE "^[A-ZА-ЯЁ]{2,}$" <(echo $NAME))" && echo "WARNING: Missing a valid name option (-n)" >&2 && INVALID_FIELD=1
test -z "$(grep -iE "^[A-ZА-ЯЁ]{2,}$" <(echo $SURNAME))" && echo "WARNING: Missing a valid surname option (-s)" >&2 && INVALID_FIELD=1
test -z "$(grep -E "^[A-Z][0-9]{4,4}$" <(echo $GROUP))" && echo "WARNING: Missing a valid group option (-g)" >&2 && INVALID_FIELD=1
test -z "$(grep -iE "^([A-ZА-ЯЁ]+ ?)+$" <(echo $FACULTY))" && echo "WARNING: Missing a valid faculty option (-f)" >&2 && INVALID_FIELD=1
test -z "$(grep -E "^\+[0-9]{11,11}$" <(echo $PHONE))" -a -z "$(grep -E "^[A-Za-z.]+@[a-z]+\.[a-z]+$" <(echo $EMAIL))" && echo "WARNING: Missing either a valid phone number or a valid email options (-t or -e)" >&2 && INVALID_FIELD=1
test -n "$INVALID_FIELD" && echo "ERROR: Not enough valid data for document creation. Exiting" && exit 1

# generating man page
cat pattern.txt | sed -e s/SURNAME/"$SURNAME"/ -e s/NAME/"$NAME"/ -e s/PATRONYMIC/"$PATRONYMIC"/ -e s/GROUP/"$GROUP"/ -e s/FACULTY/"$FACULTY"/ -e s/PHONE/"${PHONE:--}"/ -e s/EMAIL/"${EMAIL:--}"/ -e s/DATE/$(date +"%d.%m.%Y")/ > file.tmp
man ./file.tmp
rm file.tmp
