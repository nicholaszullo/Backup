for i in {2..100}
do
    java vmsim -n $i -a second swim.trace >> ResultsBelady.txt
done