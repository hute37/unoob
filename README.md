# unoob: Spark 1.6 ScratchPad



CLOG: Summaries
---------------

```$bash

cloc --by-file  --csv . > cloc_all.csv

F=cloc_all.csv; ( echo "pkg,name,$(head -n1 $F)"; cat $F | sed -e '1d' | perl -naF, -e '/(?<dir>\/(\w+\/)*)(?<file>[^\/,]+)/ && print "$+{dir},$+{file},$_"' ) > cloc.csv

csvsql --db 'postgresql://sp:sp@localhost:5432/sp' --insert cloc.csv

psql -h localhost -d sp -Usp -w  -c'select sum(code), pkg from cloc group by pkg order by 1 desc;' | less -SRX
psql -h localhost -d sp -Usp -w  -c'select sum(code), name, max(pkg) from cloc group by name order by 1 desc;' | less -SRX


```

