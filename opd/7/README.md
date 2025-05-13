# Fun Fact

### ST #5 ~ LD #5

Рассмотрим цикл исполнения микрокоманд в БЭВМ:

```
--- ST #5 ------- 0xEF05 -- 1110 1111 0000 0101

01 00A0009004	    INFETCH		IP → BR, AR
02 0104009420			          BR + 1 → IP; MEM(AR) → DR
03 0002009001			          DR → CR
04 8109804002			          if CR(15) = 1 then GOTO CHKBR @ 09
09 800C404002	    CHKBR		  if CR(14) = 0 then GOTO CHKABS @ 0C
0A 800C204002			          if CR(13) = 0 then GOTO CHKABS @ 0C
0B 8157104002			          if CR(12) = 1 then GOTO BRANCHES @ 57

0C 8024084002	    CHKABS		if CR(11) = 0 then GOTO OPFETCH @ 24
0D 0020011002	    ADFETCH		extend sign CR(0..7) → BR
0E 811C044002			          if CR(10) = 1 then GOTO T11XX @ 1C
1C 8120024002	    T11XX		  if CR(9) = 1 then GOTO T111X @ 20
20 8023014002	    T111X		  if CR(8) = 0 then GOTO T1110 @ 23
21 0001009020	    T1111		  BR → DR
22 8028101040			          GOTO EXEC @ 28

28 813C804002	    EXEC		  if CR(15) = 1 then GOTO CMD1XXX @ 3C
3C 8143204002	    CMD1XXX		if CR(13) = 1 then GOTO CMD101X @ 43
43 8146104002	    CMD101X		if CR(12) = 1 then GOTO SWAM @ 46
44 0010C09001	    LD		    DR → AC, N, Z, V
45 80C4101040			          GOTO INT @ C4
```

Таким образом, `ST #5` равносильно `LD #5`.