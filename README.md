# Execution instructions
1) cd into build folder
2) run `java -cp .\project1.jar Main`

## if build folder does not exist...
1) run `mkdir build`
2) run `javac -d ./build *.java`
3) cd into build folder
4) run `jar cvf project1.jar *`
5) copy games.txt file into build folder
6) from build folder, run `java -cp .\project1.jar Main`