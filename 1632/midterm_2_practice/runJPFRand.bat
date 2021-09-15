javac -cp jpf-core/build/jpf-annotations.jar src/Rand.java

java -jar jpf-core/build/RunJPF.jar +site=./jpf-core/site.properties Rand.jpf
